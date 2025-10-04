package net.kitawa.more_stuff.blocks.custom.electricity;

import net.kitawa.more_stuff.ModDamageSources;
import net.kitawa.more_stuff.blocks.ModBlockEntities;
import net.kitawa.more_stuff.util.configs.ShockPriorityConfig;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.common.Mod;

import javax.swing.text.html.parser.Entity;
import java.util.*;

public class TeslaCoilBlockEntity extends BlockEntity {
    private static final int MAX_BEAMS_PER_TICK = 5;

    private int charge = 0;
    private int tickCounter = 0;
    private int nextTorchSoundTick = 0;
    private boolean initialized = false;

    private final List<List<Vec3>> activeBeams = new ArrayList<>();
    private final Queue<Runnable> setupQueue = new ArrayDeque<>();

    public TeslaCoilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TESLA_COIL.get(), pos, state);
        scheduleNextTorchSound();
    }

    public int getStoredCharge() { return charge; }

    public void setStoredCharge(int value) {
        int clamped = Mth.clamp(value, 0, 180);
        if (clamped != this.charge) {
            this.charge = clamped;
            setChanged();

            if (level != null) {
                BlockState state = level.getBlockState(worldPosition);
                if (state.getBlock() instanceof TeslaCoilBlock) {
                    boolean newFlag = this.charge > 0;
                    if (state.getValue(TeslaCoilBlock.HAS_CHARGE) != newFlag) {
                        level.setBlock(worldPosition, state.setValue(TeslaCoilBlock.HAS_CHARGE, newFlag), 3);

                        BlockPos otherPos = state.getValue(TeslaCoilBlock.TOP) ? worldPosition.below() : worldPosition.above();
                        BlockState otherState = level.getBlockState(otherPos);
                        if (otherState.getBlock() instanceof TeslaCoilBlock) {
                            level.setBlock(otherPos, otherState.setValue(TeslaCoilBlock.HAS_CHARGE, newFlag), 3);
                        }
                    }
                }
            }
        }
    }

    public int getRedstonePower() { return Mth.clamp(charge / 12, 0, 15); }

    private void scheduleNextTorchSound() { this.nextTorchSoundTick = tickCounter + 12 + getLevelRandom().nextInt(25); }
    private RandomSource getLevelRandom() { return level != null ? level.getRandom() : RandomSource.create(); }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!initialized && level != null) {
            initialized = true;
            setupQueue.add(() -> {
                BlockState state = getBlockState();
                level.updateNeighborsAt(worldPosition, state.getBlock());
                level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
            });
        }
    }

    // === Tick ===
    public static void tick(Level level, BlockPos pos, BlockState state, TeslaCoilBlockEntity be) {
        if (level.isClientSide) return;
        be.tickCounter++;

        // === Run setup tasks ===
        int setupCount = 0;
        while (!be.setupQueue.isEmpty() && setupCount < MAX_BEAMS_PER_TICK) {
            Runnable setup = be.setupQueue.poll();
            if (setup != null) setup.run();
            setupCount++;
        }

        // === Sounds ===
        if (state.getValue(TeslaCoilBlock.TOP) && be.tickCounter >= be.nextTorchSoundTick) {
            level.playSound(null, pos, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.1f, 0.73f);
            be.scheduleNextTorchSound();
        }
        if (be.tickCounter % 80 == 0) {
            level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.05f, 1.0f);
        }

        // Only fire every 40 ticks on the TOP block
        if (be.tickCounter % 40 != 0 || !state.getValue(TeslaCoilBlock.TOP)) return;

        be.activeBeams.clear();
        int charge = be.getStoredCharge();
        if (charge <= 0 || !(level instanceof ServerLevel server)) return;

        Vec3 start = Vec3.atCenterOf(pos);

        // === Step 1: Lightning rods eat charge ===
        int rodsRequired = charge / 30; // full 30-charge chunks
        int leftover = charge % 30;     // immediate remainder

        List<BlockPos> rods = new ArrayList<>();
        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-20, -20, -20), pos.offset(20, 20, 20))) {
            if (level.getBlockState(checkPos).is(Blocks.LIGHTNING_ROD)) {
                rods.add(checkPos.immutable());
            }
        }

        int rodsToUse = Math.min(rods.size(), rodsRequired);
        int rodConsumed = rodsToUse * 30;
        leftover += (charge - rodConsumed);

        for (int i = 0; i < rodsToUse; i++) {
            BlockPos rodPos = rods.get(i);
            Vec3 rodEnd = BeamUtils.adjustRodEnd(level.getBlockState(rodPos), Vec3.atCenterOf(rodPos));
            Vec3 hit = BeamUtils.findBeamEnd(level, start, rodEnd, 2);
            be.activeBeams.add(BeamUtils.buildPath(server.random, start, hit, 30));
        }

        // === Step 2: Distribute leftover charge to entities (in 30-charge chunks) ===
        if (leftover > 0) {
            double radius = Math.min(charge / 1.75, 40);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(radius));
            if (!entities.isEmpty()) {
                RandomSource random = server.getRandom();

                // assign weights
                Map<LivingEntity, Float> entityWeights = new HashMap<>();
                for (LivingEntity entity : entities) {
                    String key = entity.getType().builtInRegistryHolder().getRegisteredName();
                    float weight = ShockPriorityConfig.CONFIG.entityTypePriority().getOrDefault(key, 1.0f);

                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;
                        ItemStack stack = entity.getItemBySlot(slot);
                        if (!stack.isEmpty() && stack.is(ModItemTags.METALLIC_ARMOR)) {
                            weight += 0.25f;
                        }
                    }
                    entityWeights.put(entity, weight);
                }

                // process leftover in full 30-charge chunks
                int chunks = leftover / 30;
                for (int i = 0; i < chunks && !entityWeights.isEmpty(); i++) {
                    List<LivingEntity> chosen = pickWeightedEntities(entityWeights, random, 1);
                    LivingEntity target = chosen.get(0);

                    int spend = 30;
                    leftover -= spend;

                    Vec3 end = target.position().add(0, target.getBbHeight() * 0.5, 0);
                    Vec3 hit = BeamUtils.findBeamEnd(level, start, end, 2);
                    be.activeBeams.add(BeamUtils.buildPath(server.random, start, hit, spend));

                    // Only deal damage if beam actually reaches entity
                    if (hit.distanceToSqr(end) < 0.5) {
                        float damage = Math.max(0, spend - (float) start.distanceTo(end)) * 0.05f;
                        if (damage > 0) {
                            target.hurt(level.damageSources().source(
                                            Objects.requireNonNull(ModDamageSources.ELECTRICITY.getDelegate().getKey())),
                                    damage
                            );
                        }
                    }

                    // Remove chosen from pool so it won't get hit twice this cycle
                    entityWeights.remove(target);
                }

                level.playSound(null, pos, SoundEvents.SHULKER_SHOOT, SoundSource.BLOCKS, 1.0f, 2.0f);
            }
        }

        // === Step 3: Particle spawning ===
        for (List<Vec3> path : be.activeBeams) {
            BeamUtils.spawnParticlesAlongPath(server, path);
        }
    }

    // === Weighted helper ===
    private static List<LivingEntity> pickWeightedEntities(Map<LivingEntity, Float> entityWeights, RandomSource random, int count) {
        List<LivingEntity> chosen = new ArrayList<>();
        Map<LivingEntity, Float> pool = new HashMap<>(entityWeights);

        for (int i = 0; i < count && !pool.isEmpty(); i++) {
            float totalWeight = pool.values().stream().reduce(0f, Float::sum);
            float pick = random.nextFloat() * totalWeight;
            LivingEntity selected = null;
            for (Map.Entry<LivingEntity, Float> entry : pool.entrySet()) {
                pick -= entry.getValue();
                if (pick <= 0) {
                    selected = entry.getKey();
                    break;
                }
            }
            if (selected == null) selected = pool.keySet().iterator().next();
            chosen.add(selected);
            pool.remove(selected);
        }
        return chosen;
    }

    // === Save/Load ===
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        tag.putInt("Charge", charge);
        tag.putInt("TickCounter", tickCounter);
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider lookup) {
        super.loadAdditional(tag, lookup);
        this.charge = tag.getInt("Charge");
        this.tickCounter = tag.getInt("TickCounter");
    }
}