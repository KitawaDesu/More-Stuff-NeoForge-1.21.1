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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;

public class OmniBlockEntity extends BlockEntity {
    private static final int MAX_SETUP_PER_TICK = 5;

    private int charge = 0;
    private int tickCounter = 0;
    private int nextTorchSoundTick = 0;
    private boolean initialized = false;

    private final List<Vec3> activeBeam = new ArrayList<>();
    private final Queue<Runnable> setupQueue = new ArrayDeque<>();

    public OmniBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OMNI.get(), pos, state);
        scheduleNextTorchSound();
    }

    public int getStoredCharge() { return charge; }

    public void setStoredCharge(int value) {
        int clamped = Mth.clamp(value, 0, 30);
        if (clamped != charge) {
            charge = clamped;
            setChanged();
            if (level != null) {
                level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
                level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
            }
        }
    }

    public int getRedstonePower() { return Mth.clamp(charge / 2, 0, 15); }

    private void scheduleNextTorchSound() {
        this.nextTorchSoundTick = tickCounter + 12 + getLevelRandom().nextInt(25);
    }

    private RandomSource getLevelRandom() {
        return level != null ? level.getRandom() : RandomSource.create();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!initialized && level != null) {
            initialized = true;
            BlockState state = getBlockState();
            boolean hasAnchor = state.getValue(OmniBlock.ANCHOR_UP)
                    || state.getValue(OmniBlock.ANCHOR_DOWN)
                    || state.getValue(OmniBlock.ANCHOR_NORTH)
                    || state.getValue(OmniBlock.ANCHOR_SOUTH)
                    || state.getValue(OmniBlock.ANCHOR_EAST)
                    || state.getValue(OmniBlock.ANCHOR_WEST);
            if (hasAnchor) {
                setupQueue.add(() -> {
                    level.updateNeighborsAt(worldPosition, state.getBlock());
                    level.updateNeighbourForOutputSignal(worldPosition, state.getBlock());
                });
            }
            setChanged();
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, OmniBlockEntity be) {
        if (level.isClientSide) return;
        be.tickCounter++;

        // --- Process setup tasks ---
        int setupCount = 0;
        while (!be.setupQueue.isEmpty() && setupCount < MAX_SETUP_PER_TICK) {
            Runnable setup = be.setupQueue.poll();
            if (setup != null) setup.run();
            setupCount++;
        }

        // --- Sounds ---
        if (state.getValue(OmniBlock.END_POINT) && be.tickCounter >= be.nextTorchSoundTick) {
            level.playSound(null, pos, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.1f, 0.73f);
            be.scheduleNextTorchSound();
        }
        if (be.tickCounter % 80 == 0) {
            level.playSound(null, pos, SoundEvents.BEACON_AMBIENT, SoundSource.BLOCKS, 0.05f, 1.0f);
        }

        int interval = Mth.clamp(Objects.requireNonNull(be.getLevel()).random.nextInt(41) + 20, 20, 150);
        if (be.tickCounter % (interval * 2.5) != 0 || !state.getValue(OmniBlock.END_POINT)) return;

        int charge = be.getStoredCharge();
        if (charge <= 0 || !(level instanceof ServerLevel server)) return;

        be.activeBeam.clear();
        Vec3 start = Vec3.atCenterOf(pos);
        int maxAllowed = Math.max(2, (charge / 10) * 3);

        // --- Lightning Rod Target ---
        BlockPos rodTarget = null;
        Vec3 rodEnd = null;
        for (BlockPos checkPos : BlockPos.betweenClosed(pos.offset(-charge, -charge, -charge),
                pos.offset(charge, charge, charge))) {
            BlockState checkState = level.getBlockState(checkPos);
            if (checkState.is(Blocks.LIGHTNING_ROD)) {
                Vec3 end = Vec3.atCenterOf(checkPos);
                Vec3 hit = BeamUtils.findBeamEnd(level, start, end, maxAllowed);
                if (hit != null && hit.distanceToSqr(end) < 0.5) {
                    rodTarget = checkPos;
                    rodEnd = BeamUtils.adjustRodEnd(checkState, end);
                    break;
                }
            }
        }

        if (rodTarget != null && rodEnd != null) {
            be.activeBeam.addAll(BeamUtils.buildPath(server.random, start, rodEnd, charge));
            level.playSound(null, pos, SoundEvents.SHULKER_SHOOT, SoundSource.BLOCKS, 1.0f, 2.0f);
        } else {
            // --- Weighted Entity Selection (1 entity max) ---
            AABB area = new AABB(pos).inflate(charge);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, area);
            if (!entities.isEmpty()) {
                RandomSource random = server.getRandom();
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

                // Pick one entity
                float totalWeight = entityWeights.values().stream().reduce(0f, Float::sum);
                float pick = random.nextFloat() * totalWeight;
                LivingEntity target = null;
                for (Map.Entry<LivingEntity, Float> entry : entityWeights.entrySet()) {
                    pick -= entry.getValue();
                    if (pick <= 0) { target = entry.getKey(); break; }
                }
                if (target == null) target = entities.get(0);

                Vec3 end = target.position().add(0, target.getBbHeight() * 0.5, 0);
                Vec3 hit = BeamUtils.findBeamEnd(level, start, end, maxAllowed);
                if (hit != null) {
                    be.activeBeam.addAll(BeamUtils.buildPath(server.random, start, hit, charge));

                    if (hit.distanceToSqr(end) < 0.5) {
                        float damage = Math.max(0, charge - (float) start.distanceTo(end)) * 0.1f;

                        // --- Shield blocking logic ---
                        if (target.isBlocking() && target.getUseItem().getItem() instanceof ShieldItem) {
                            // Check if the target is facing the OmniBlock
                            Vec3 lookVec = target.getLookAngle().normalize();
                            Vec3 toBlock = start.subtract(target.position().add(0, target.getBbHeight() * 0.5, 0)).normalize();
                            double dot = lookVec.dot(toBlock); // 1.0 = fully facing, -1.0 = opposite

                            if (dot > 0.5) { // Adjust threshold as needed (0.5 = roughly 60° cone)
                                ItemStack shieldStack = target.getUseItem();
                                EquipmentSlot shieldSlot = target.getOffhandItem() == shieldStack ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;

                                shieldStack.hurtAndBreak((int) (15 + (15 * ((charge / 2.5) * 0.37361))), target, shieldSlot);

                                // Reduce damage to zero
                                target.hurt(level.damageSources().source(
                                                Objects.requireNonNull(ModDamageSources.ELECTRICITY.getDelegate().getKey())),
                                        damage * 0.0f
                                );
                                level.playSound(null, pos, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1.0f, 1.0f);
                            } else {
                                // Normal damage if not facing
                                target.hurt(level.damageSources().source(
                                                Objects.requireNonNull(ModDamageSources.ELECTRICITY.getDelegate().getKey())),
                                        damage
                                );
                            }
                        } else {
                            // Normal damage if not blocking
                            target.hurt(level.damageSources().source(
                                            Objects.requireNonNull(ModDamageSources.ELECTRICITY.getDelegate().getKey())),
                                    damage
                            );
                        }
                    }

                    level.playSound(null, pos, SoundEvents.SHULKER_SHOOT, SoundSource.BLOCKS, 1.0f, 2.0f);
                }
            }
        }

        if (!be.activeBeam.isEmpty()) BeamUtils.spawnParticlesAlongPath(server, be.activeBeam);
    }

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