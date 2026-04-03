package net.kitawa.more_stuff.entities.monster;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.kitawa.more_stuff.entities.ModEntities;
import net.kitawa.more_stuff.entities.monster.goals.ZombieWolfAttackGoal;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.*;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static net.minecraft.tags.ItemTags.DYEABLE;

public class ZombieWolf extends Monster {
    private static final ResourceLocation SPEED_MODIFIER_BABY_ID = ResourceLocation.fromNamespaceAndPath("more_stuff","baby");
    private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(
            SPEED_MODIFIER_BABY_ID, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
    );
    private static final EntityDataAccessor<Boolean> DATA_ANGRY_ID =
            SynchedEntityData.defineId(ZombieWolf.class, EntityDataSerializers.BOOLEAN);
    private static final ResourceLocation REINFORCEMENT_CALLER_CHARGE_ID = ResourceLocation.fromNamespaceAndPath("more_stuff","reinforcement_caller_charge");
    private static final AttributeModifier ZOMBIE_REINFORCEMENT_CALLEE_CHARGE = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath("more_stuff","reinforcement_callee_charge"), -0.05F, AttributeModifier.Operation.ADD_VALUE
    );

    private static final ResourceLocation LEADER_ZOMBIE_BONUS_ID = ResourceLocation.fromNamespaceAndPath("more_stuff", "leader_zombie_bonus");
    private static final ResourceLocation ZOMBIE_RANDOM_SPAWN_BONUS_ID = ResourceLocation.fromNamespaceAndPath("more_stuff", "zombie_random_spawn_bonus");

    private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(ZombieWolf.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(ZombieWolf.class, EntityDataSerializers.INT);

    public static final float ZOMBIE_LEADER_CHANCE = 0.05F;
    public static final int REINFORCEMENT_ATTEMPTS = 50;
    public static final int REINFORCEMENT_RANGE_MAX = 40;
    public static final int REINFORCEMENT_RANGE_MIN = 7;

    private static final EntityDimensions BABY_DIMENSIONS = ModEntities.ZOMBIE_WOLF.get().getDimensions().scale(0.5F).withEyeHeight(0.93F);
    private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = p -> p == Difficulty.HARD;

    private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal(this, DOOR_BREAKING_PREDICATE);
    private boolean canBreakDoors; // we can leave the field if used elsewhere, but the goal is removed

    public ZombieWolf(EntityType<? extends ZombieWolf> entityType, Level level) {
        super(entityType, level);
    }

    // ---------------------------
    // Attributes
    // ---------------------------
    public static AttributeSupplier.Builder createAttributes() {
        return Wolf.createAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.ARMOR, 2.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.1D);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        // Always keep max air — cannot drown
        this.setAirSupply(this.getMaxAirSupply());
    }

    @Override
    public int getMaxAirSupply() {
        return 300; // vanilla default, but it's always reset anyway
    }
    // ---------------------------
    // Synced data
    // ---------------------------
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_BABY_ID, false);
        builder.define(DATA_SPECIAL_TYPE_ID, 0);
        builder.define(DATA_ANGRY_ID, false); // <-- add this
    }
    // ---------------------------
    // Goals
    // ---------------------------
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new ZombieAttackTurtleEggGoal(this, 1.0, 3));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new LeapAtTargetGoal(this, 0.4F));
        this.goalSelector.addGoal(2, new ZombieWolfAttackGoal(this, 1.0, false));
        // Removed MoveThroughVillageGoal with door-breaking
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(ZombieWolf.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Wolf.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    // ---------------------------
    // Baby & size
    // ---------------------------
    @Override
    public boolean isBaby() {
        return this.getEntityData().get(DATA_BABY_ID);
    }

    @Override
    public void setBaby(boolean childZombie) {
        this.getEntityData().set(DATA_BABY_ID, childZombie);
        if (this.level() != null && !this.level().isClientSide) {
            AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_BABY_ID);
            if (childZombie) {
                attributeinstance.addTransientModifier(SPEED_MODIFIER_BABY);
            }
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }

    // ---------------------------
    // Tick & sun burning (same as zombie)
    // ---------------------------
    @Override
    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.isSunSensitive() && this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        Item item = itemstack.getItem();
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.onEquippedItemBroken(item, EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.igniteForSeconds(8.0F);
                }
            }
        }

        super.aiStep();
    }

    protected boolean isSunSensitive() {
        return true;
    }

    // ---------------------------
    // Reinforcement logic (spawns ZombieWolves)
    // ---------------------------
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!super.hurt(source, amount)) {
            return false;
        } else if (!(this.level() instanceof ServerLevel)) {
            return false;
        } else {
            ServerLevel serverlevel = (ServerLevel)this.level();
            LivingEntity livingentity = this.getTarget();
            if (livingentity == null && source.getEntity() instanceof LivingEntity) {
                livingentity = (LivingEntity)source.getEntity();
            }

            if (livingentity != null
                    && this.level().getDifficulty() == Difficulty.HARD
                    && (double)this.random.nextFloat() < this.getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE)
                    && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {

                int i = Mth.floor(this.getX());
                int j = Mth.floor(this.getY());
                int k = Mth.floor(this.getZ());

                for (int l = 0; l < REINFORCEMENT_ATTEMPTS; l++) {
                    int i1 = i + Mth.nextInt(this.random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(this.random, -1, 1);
                    int j1 = j + Mth.nextInt(this.random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(this.random, -1, 1);
                    int k1 = k + Mth.nextInt(this.random, REINFORCEMENT_RANGE_MIN, REINFORCEMENT_RANGE_MAX) * Mth.nextInt(this.random, -1, 1);
                    BlockPos blockpos = new BlockPos(i1, j1, k1);

                    EntityType<ZombieWolf> zType = ModEntities.ZOMBIE_WOLF.get();
                    if (SpawnPlacements.isSpawnPositionOk(zType, this.level(), blockpos)
                            && SpawnPlacements.checkSpawnRules(zType, serverlevel, MobSpawnType.REINFORCEMENT, blockpos, this.level().random)) {

                        ZombieWolf reinforcement = zType.create(this.level());
                        if (reinforcement == null) continue;

                        reinforcement.setPos((double)i1, (double)j1, (double)k1);
                        if (!this.level().hasNearbyAlivePlayer((double)i1, (double)j1, (double)k1, 7.0)
                                && this.level().isUnobstructed(reinforcement)
                                && this.level().noCollision(reinforcement)
                                && !this.level().containsAnyLiquid(reinforcement.getBoundingBox())) {

                            reinforcement.setTarget(livingentity);
                            reinforcement.finalizeSpawn(serverlevel, this.level().getCurrentDifficultyAt(reinforcement.blockPosition()), MobSpawnType.REINFORCEMENT, null);
                            serverlevel.addFreshEntityWithPassengers(reinforcement);

                            AttributeInstance attributeinstance = this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
                            AttributeModifier attributemodifier = attributeinstance.getModifier(REINFORCEMENT_CALLER_CHARGE_ID);
                            double d0 = attributemodifier != null ? attributemodifier.amount() : 0.0;
                            attributeinstance.removeModifier(REINFORCEMENT_CALLER_CHARGE_ID);
                            attributeinstance.addPermanentModifier(
                                    new AttributeModifier(REINFORCEMENT_CALLER_CHARGE_ID, d0 - 0.05, AttributeModifier.Operation.ADD_VALUE)
                            );
                            reinforcement.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(ZOMBIE_REINFORCEMENT_CALLEE_CHARGE);
                            break;
                        }
                    }
                }
            }

            return true;
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag) {
            // Mark the wolf as angry when attacking
            this.setAngry(true);

            float f = this.level().getCurrentDifficultyAt(this.blockPosition()).getEffectiveDifficulty();
            if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < f * 0.3F) {
                entity.igniteForSeconds((float)(2 * (int)f));
            }
        }
        return flag;
    }

    // ---------------------------
    // Sounds
    // ---------------------------
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.WOLF_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WOLF_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WOLF_DEATH;
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.ZOMBIE_STEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState block) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    // ---------------------------
    // Equipment: only MAINHAND and BODY allowed
    // ---------------------------
    // ---------------------------
    // Equipment: only BODY allowed
    // ---------------------------
    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        if (slot == EquipmentSlot.BODY) {
            super.setItemSlot(slot, stack);
            return;
        }
        // clear any other slot attempts (prevents hand items, head, feet, legs)
        super.setItemSlot(slot, ItemStack.EMPTY);
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        float dayMultiplier = 1.0F;

        // Add logarithmic day scaling if enabled in config
        if (MoreStuffGeneralConfig.allowLogarithmicArmor) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
        }

        // Final spawn chance with all multipliers
        float baseChance = 0.6F * MoreStuffGeneralConfig.naturalArmorMultiplier;
        float finalChance = baseChance * difficulty.getSpecialMultiplier() * dayMultiplier;

        if (random.nextFloat() < finalChance) {
            int i = random.nextInt(2);
            float f = this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;

            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;

            boolean flag = true;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(slot);
                    if (!flag && random.nextFloat() < f) break;

                    flag = false;

                    if (itemstack.isEmpty()) {
                        Item item;

                        // Insert custom armor selection logic for BODY slot
                        if (slot == EquipmentSlot.BODY) {
                            item = switch (i) {
                                case 0 -> {
                                    int roll = random.nextInt(3);
                                    yield switch (roll) {
                                        case 0 -> Items.WOLF_ARMOR;
                                        case 1 -> ModItems.LEATHER_WOLF_ARMOR.get();
                                        case 2 -> ModItems.WOOD_PLATE_WOLF_ARMOR.get();
                                        default -> ModItems.TURTLE_SCUTE_WOLF_ARMOR.get();
                                    };
                                }
                                case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_WOLF_ARMOR.get() : ModItems.GOLDEN_WOLF_ARMOR.get();
                                case 2 -> ModItems.CHAINMAIL_WOLF_ARMOR.get();
                                case 3 -> {
                                    if (ModList.get().isLoaded("create")) {
                                        int roll = random.nextInt(4); // 0–3
                                        yield switch (roll) {
                                            case 0 -> ModItems.IRON_WOLF_ARMOR.get();
                                            case 1 -> ModItems.COPPER_WOLF_ARMOR.get();
                                            case 2 -> CreateCompatItems.ZINC_WOLF_ARMOR.get();
                                            default -> CreateCompatItems.BRASS_WOLF_ARMOR.get();
                                        };
                                    } else {
                                        yield random.nextBoolean() ? ModItems.IRON_WOLF_ARMOR.get() : ModItems.COPPER_WOLF_ARMOR.get();
                                    }
                                }
                                case 4 -> random.nextBoolean() ? ModItems.EMERALD_WOLF_ARMOR.get() : ModItems.DIAMOND_WOLF_ARMOR.get();
                                default -> null;
                            };
                        } else {
                            item = getEquipmentForSlot(slot, i);
                        }

                        if (item != null) {
                            this.setItemSlot(slot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }


    protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        this.enchantSpawnedWeapon(level, random, difficulty);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                this.enchantSpawnedArmor(level, random, slot, difficulty);
                this.dyeSpawnedArmor(level, random, slot, difficulty);
            }
        }
    }
    @Unique
    protected void dyeSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
        float threshold = random.nextFloat(); // Random threshold between 0.0 and 1.0
        float chance = random.nextFloat();    // Actual chance roll

        if (chance >= threshold * MoreStuffGeneralConfig.applyArmorDyeingMultiplier) {
            return; // Exit early if the chance does not meet the random threshold
        }

        ItemStack equippedItem = this.getItemBySlot(slot);

        // Only attempt to dye if the item exists and is dyeable
        if (!equippedItem.isEmpty() && isDyeable(equippedItem.getItem())) {
            int r = random.nextInt(MoreStuffGeneralConfig.r);
            int g = random.nextInt(MoreStuffGeneralConfig.g);
            int b = random.nextInt(MoreStuffGeneralConfig.b);
            int color = (r << 16) + (g << 8) + b;

            DyedItemColor dyedColor = new DyedItemColor(color, true);
            equippedItem.set(DataComponents.DYED_COLOR, dyedColor);

            // Re-apply the dyed item to the slot
            this.setItemSlot(slot, equippedItem);
        }
    }

    @Unique
    private boolean isDyeable(Item item) {
        // Get the ResourceLocation for the item
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);

        if (itemId == null) {
            return false; // If the item doesn't have a valid registry key, it's not dyeable
        }

        // Retrieve the Holder<Item> for the item
        Holder<Item> itemHolder = BuiltInRegistries.ITEM.getHolder(itemId).orElse(null);

        if (itemHolder == null) {
            return false; // No holder found, return false
        }

        // Get the tag for "dyeable" items and check if the item is in the tag
        return BuiltInRegistries.ITEM.getTag(DYEABLE)
                .map(tag -> tag.contains(itemHolder))
                .orElse(false);
    }

    // ---------------------------
    // Save/load (no underwater conversion fields)
    // ---------------------------
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("IsBaby", this.isBaby());
        compound.putBoolean("CanBreakDoors", this.canBreakDoors());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBaby(compound.getBoolean("IsBaby"));
        this.setCanBreakDoors(compound.getBoolean("CanBreakDoors"));
    }

    @Override
    public boolean killedEntity(ServerLevel level, LivingEntity entity) {
        boolean flag = super.killedEntity(level, entity);

        // Villager -> ZombieVillager conversion (existing logic)
        if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD)
                && entity instanceof Villager villager
                && net.neoforged.neoforge.event.EventHooks.canLivingConvert(entity, EntityType.ZOMBIE_VILLAGER, (timer) -> {})) {
            if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean()) {
                return flag;
            }

            ZombieVillager zombievillager = villager.convertTo(EntityType.ZOMBIE_VILLAGER, false);
            if (zombievillager != null) {
                zombievillager.finalizeSpawn(
                        level,
                        level.getCurrentDifficultyAt(zombievillager.blockPosition()),
                        MobSpawnType.CONVERSION,
                        new Zombie.ZombieGroupData(false, true)
                );
                zombievillager.setVillagerData(villager.getVillagerData());
                zombievillager.setGossips(villager.getGossips().store(net.minecraft.nbt.NbtOps.INSTANCE));
                zombievillager.setTradeOffers(villager.getOffers().copy());
                zombievillager.setVillagerXp(villager.getVillagerXp());
                net.neoforged.neoforge.event.EventHooks.onLivingConvert(entity, zombievillager);
                if (!this.isSilent()) {
                    level.levelEvent(null, 1026, this.blockPosition(), 0);
                }

                flag = false;
            }
        }
        return flag;
    }

    protected void randomizeReinforcementsChance() {
        this.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * 0.1F);
    }

    public boolean hasArmor() {
        return this.getBodyArmorItem().is(ModItemTags.WOLF_ARMOR);
    }


    @Override
    public boolean isBodyArmorItem(ItemStack stack) {
        return stack.is(ModItemTags.WOLF_ARMOR);
    }

    public boolean isAngry() {
        return this.getEntityData().get(DATA_ANGRY_ID);
    }

    public void setAngry(boolean angry) {
        this.getEntityData().set(DATA_ANGRY_ID, angry);
    }

    // break turtle egg goal inner class copied from zombie
    static class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
        ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
            super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
        }

        @Override
        public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public void playBreakSound(Level level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
        }

        @Override
        public double acceptedDistance() {
            return 1.14;
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    public boolean canBreakDoors() {
        return this.canBreakDoors;
    }

    public void setCanBreakDoors(boolean canBreakDoors) {
        if (this.supportsBreakDoorGoal() && GoalUtils.hasGroundPathNavigation(this)) {
            if (this.canBreakDoors != canBreakDoors) {
                this.canBreakDoors = canBreakDoors;
                ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(canBreakDoors);
                if (canBreakDoors) {
                    this.goalSelector.addGoal(1, this.breakDoorGoal);
                } else {
                    this.goalSelector.removeGoal(this.breakDoorGoal);
                }
            }
        } else if (this.canBreakDoors) {
            this.goalSelector.removeGoal(this.breakDoorGoal);
            this.canBreakDoors = false;
        }
    }

    protected boolean supportsBreakDoorGoal() {
        return true;
    }

    // spawn rules similar to zombies (adjust tag if you want exact wolf-spawn surfaces)
    public static boolean checkZombieWolfSpawnRules(
            EntityType<ZombieWolf> type,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random
    ) {
        // -------------------------------
        // 0. Spawner bypass (vanilla behavior)
        // -------------------------------
        if (MobSpawnType.isSpawner(spawnType)) {
            return Monster.checkMobSpawnRules(type, level, spawnType, pos, random);
        } else {
            if (level.getDifficulty() != Difficulty.PEACEFUL) {
                if (!MobSpawnType.ignoresLightRequirements(spawnType)
                        && !Monster.isDarkEnoughToSpawn(level, pos, random)) {
                    return false;
                }

                if (spawnType == MobSpawnType.SPAWNER) {
                    if (level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON)) {
                        return checkMobSpawnRules(type, level, spawnType, pos, random);
                    }
                }

                if (!Monster.checkMobSpawnRules(type, level, spawnType, pos, random)) {
                    return false;
                }

                // -------------------------------
                // 2. Wolf rules (merged)
                // -------------------------------
                if (!level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON)) {
                    return false;
                }

                if (!ZombieWolf.isDarkEnoughToSpawn(level, pos, random)) {
                    return false;
                }

                // -------------------------------
                // 3. Custom zombie-wolf rule
                // -------------------------------
                if (level.getBlockState(pos).getFluidState().isSource()) {
                    return false;
                }
            }
            return true;
        }
    }

    @Unique
    public boolean canArmorAbsorb(DamageSource damageSource) {
        ItemStack bodyArmor = this.getBodyArmorItem();

        boolean hasDivineAbsorption = this.level().registryAccess()
                .lookup(Registries.ENCHANTMENT)
                .flatMap(reg -> reg.get(ModEnchantments.DIVINE_ABSORPTION))
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, bodyArmor) > 0)
                .orElse(false);

        return (bodyArmor.is(ModItemTags.ABSORBS_DAMAGE) && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR))
                || hasDivineAbsorption;
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.canArmorAbsorb(damageSource)) {
            super.actuallyHurt(damageSource, damageAmount);
        } else {
            ItemStack itemstack = this.getBodyArmorItem();
            int i = itemstack.getDamageValue();
            int j = itemstack.getMaxDamage();
            itemstack.hurtAndBreak(Mth.ceil(damageAmount), this, EquipmentSlot.BODY);
            if (this.getBodyArmorItem().is(ModItems.LEATHER_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.LEATHER_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(Items.WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, Items.WOLF_ARMOR.getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.WOOD_PLATE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.WOOD_PLATE_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.IRON_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.IRON_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.CHAINMAIL_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.CHAINMAIL_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.GOLDEN_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.GOLDEN_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.COPPER_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.COPPER_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.ROSE_GOLDEN_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.ROSE_GOLDEN_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.EMERALD_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.EMERALD_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.DIAMOND_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.DIAMOND_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.NETHERITE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.NETHERITE_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.ROSARITE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.ROSARITE_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (this.getBodyArmorItem().is(ModItems.TURTLE_SCUTE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.TURTLE_SCUTE_WOLF_ARMOR.get().getDefaultInstance()),
                                this.getX(),
                                this.getY() + 1.0,
                                this.getZ(),
                                20,
                                0.2,
                                0.1,
                                0.2,
                                0.1
                        );
                    }
                }
            } else if (ModList.get().isLoaded("create")) {
                if (this.getBodyArmorItem().is(CreateCompatItems.ZINC_WOLF_ARMOR)) {
                    if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                        this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                        if (this.level() instanceof ServerLevel serverlevel) {
                            serverlevel.sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, CreateCompatItems.ZINC_WOLF_ARMOR.get().getDefaultInstance()),
                                    this.getX(),
                                    this.getY() + 1.0,
                                    this.getZ(),
                                    20,
                                    0.2,
                                    0.1,
                                    0.2,
                                    0.1
                            );
                        }
                    }
                } else if (this.getBodyArmorItem().is(CreateCompatItems.BRASS_WOLF_ARMOR)) {
                    if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                        this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                        if (this.level() instanceof ServerLevel serverlevel) {
                            serverlevel.sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, CreateCompatItems.BRASS_WOLF_ARMOR.get().getDefaultInstance()),
                                    this.getX(),
                                    this.getY() + 1.0,
                                    this.getZ(),
                                    20,
                                    0.2,
                                    0.1,
                                    0.2,
                                    0.1
                            );
                        }
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource random = level.getRandom();
        populateDefaultEquipmentSlots(random, difficulty);
        populateDefaultEquipmentEnchantments(level, random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }
}