package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import net.kitawa.more_stuff.experimentals.entities.ExperimentalCombatEntities;
import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Arrow that reads tuning from the ItemStack's DataComponents.
 */
public class DynamicArrowEntity extends AbstractArrow {

    private static final int EXPOSED_POTION_DECAY_TIME = 600;
    private static final int NO_EFFECT_COLOR = -1;
    private static final byte EVENT_POTION_PUFF = 0;

    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_GRAVITY = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_DAMAGE_MULT = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_BASE_DAMAGE = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_CRIT = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_NO_PHYSICS = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> DATA_WATER_INERTIA = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Byte> PIERCE_LEVEL = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_HAS_POTION = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ON_FIRE = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_EXPLOSIVE = SynchedEntityData.defineId(DynamicArrowEntity.class, EntityDataSerializers.BOOLEAN);


    // Dynamic behavior fields

    private static final Method SET_PIERCE_METHOD = findSetPierceMethod();

    // --- Constructors ---
    public DynamicArrowEntity(EntityType<? extends DynamicArrowEntity> type, Level level) {
        super(type, level);
    }

    public DynamicArrowEntity(EntityType<? extends DynamicArrowEntity> type, double x, double y, double z, Level level, ItemStack pickup, ItemStack firedFromWeapon) {
        super(type, x, y, z, level, pickup, firedFromWeapon);
        setItemStack(pickup);
        updateColor();
    }

    public DynamicArrowEntity(EntityType<? extends DynamicArrowEntity> type, LivingEntity owner, Level level, ItemStack pickup, ItemStack firedFromWeapon) {
        super(type, owner, level, pickup, firedFromWeapon);
        setItemStack(pickup);
        updateColor();
    }

    // --- Synced Data ---
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_EFFECT_COLOR, NO_EFFECT_COLOR);
        builder.define(DATA_GRAVITY, 0.05f);
        builder.define(DATA_DAMAGE_MULT, 1.0f);
        builder.define(DATA_BASE_DAMAGE, 2.0f);
        builder.define(DATA_CRIT, 0.25f);
        builder.define(DATA_NO_PHYSICS, false);
        builder.define(DATA_WATER_INERTIA, 0.6f);
        builder.define(PIERCE_LEVEL, (byte) 0);
        builder.define(DATA_HAS_POTION, false);
        builder.define(DATA_ON_FIRE, false);
        builder.define(DATA_EXPLOSIVE, false);
    }

    // --- Potion Handling ---
    private PotionContents getPotionContents() {
        return this.getPickupItemStackOrigin().getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);

        // ─── Server-side only logic ───
        if (this.level().isClientSide()) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        // Explosive arrow logic
        if (this.entityData.get(DATA_EXPLOSIVE)) {
            explode();
            return;
        }

        if (!(hitResult.getEntity() instanceof LivingEntity target)) return;

        // Fire effect
        if (this.entityData.get(DATA_ON_FIRE)) {
            target.setRemainingFireTicks(100);
        }

        float critChance = this.entityData.get(DATA_CRIT);

        if (critChance > 0.0f && this.random.nextFloat() < critChance) {
            double newDamage = this.getBaseDamage() * 1.5; // 50% more on crit
            target.hurt(this.damageSources().arrow(this, this.getOwner()), (float)newDamage);

            // Particle + sound feedback (server side so all clients see it)
            serverLevel.playSound(
                    null,
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT,
                    SoundSource.PLAYERS,
                    1.0F,
                    1.2F
            );
            serverLevel.sendParticles(
                    ParticleTypes.CRIT,
                    this.getX(), this.getY(0.0625), this.getZ(),
                    8, 0.3, 0.3, 0.3, 0.1
            );
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);

        if (!this.level().isClientSide && this.entityData.get(DATA_EXPLOSIVE)) {
            explode();
        }
    }

    // --- Explosion logic ---
    private void explode() {
        if (this.level().isClientSide) return;

        float radius = Math.max(this.entityData.get(DATA_BASE_DAMAGE) / 2.0f, 4.0f);
        boolean fire = this.entityData.get(DATA_ON_FIRE);
        Level.ExplosionInteraction interaction = Level.ExplosionInteraction.MOB;

        ExplosionDamageCalculator damageCalculator = new ExplosionDamageCalculator() {
            @Override
            public boolean shouldBlockExplode(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, float resistance) {
                return state.is(Blocks.NETHER_PORTAL) ? false : super.shouldBlockExplode(explosion, world, pos, state, resistance);
            }

            @Override
            public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter world, BlockPos pos, BlockState state, FluidState fluid) {
                return state.is(Blocks.NETHER_PORTAL) ? Optional.empty() : super.getBlockExplosionResistance(explosion, world, pos, state, fluid);
            }
        };

        this.level().explode(
                this,
                Explosion.getDefaultDamageSource(this.level(), this),
                damageCalculator,
                this.getX(),
                this.getY(0.0625),
                this.getZ(),
                radius,
                fire,
                interaction
        );

        this.level().playSound(
                null,
                this.getX(), this.getY(), this.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.BLOCKS,
                4.0F,
                (1.0F + (this.level().random.nextFloat() - this.level().random.nextFloat()) * 0.2F) * 0.7F
        );

        ((ServerLevel)this.level()).sendParticles(
                ParticleTypes.EXPLOSION_EMITTER,
                this.getX(), this.getY(0.0625), this.getZ(),
                1, 0, 0, 0, 0.0
        );
        ((ServerLevel)this.level()).sendParticles(
                ParticleTypes.EXPLOSION,
                this.getX(), this.getY(0.0625), this.getZ(),
                10, radius / 2, radius / 2, radius / 2, 0.1
        );

        // Only discard arrow if it's explosive
        if (this.entityData.get(DATA_EXPLOSIVE)) {
            this.discard();
        }
    }

    private void updateColor() {
        PotionContents potion = getPotionContents();
        this.entityData.set(DATA_EFFECT_COLOR, potion.equals(PotionContents.EMPTY) ? NO_EFFECT_COLOR : potion.getColor());
    }

    public int getColor() {
        return this.entityData.get(DATA_EFFECT_COLOR);
    }

    // --- Apply components from ItemStack ---
    private void applyComponentsFromStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        Boolean hasPotion = stack.get(ArrowDataComponents.HAS_POTION.get());
        this.entityData.set(DATA_HAS_POTION, hasPotion != null && hasPotion);

        if (this.entityData.get(DATA_HAS_POTION)) {
            updateColor();
        } else {
            this.entityData.set(DATA_EFFECT_COLOR, NO_EFFECT_COLOR);
        }

        Double gm = stack.get(ArrowDataComponents.GRAVITY.get());
        if (gm != null) this.entityData.set(DATA_GRAVITY, gm.floatValue());

        Double dm = stack.get(ArrowDataComponents.DAMAGE_MULTIPLIER.get());
        if (dm != null) this.entityData.set(DATA_DAMAGE_MULT, dm.floatValue());

        Double bd = stack.get(ArrowDataComponents.BASE_DAMAGE.get());
        if (bd != null) this.entityData.set(DATA_BASE_DAMAGE, bd.floatValue());

        Float wi = stack.get(ArrowDataComponents.WATER_INERTIA.get());
        if (wi != null) this.entityData.set(DATA_WATER_INERTIA, wi);

        Float critChance = stack.get(ArrowDataComponents.CRIT.get());
        if (critChance != null) {
            this.entityData.set(DATA_CRIT, Mth.clamp(critChance, 0.0f, 1.0f));
        }

        Boolean np = stack.get(ArrowDataComponents.NO_PHYSICS.get());
        if (np != null && np) this.entityData.set(DATA_NO_PHYSICS, true);

        Integer pierce = stack.get(ArrowDataComponents.PIERCE_LEVEL.get());
        if (pierce != null) invokeSetPierceLevel((byte)Math.max(0, Math.min(127, pierce)));

        Boolean explosive = stack.get(ArrowDataComponents.EXPLOSIVE.get());
        if (explosive != null && explosive) this.entityData.set(DATA_EXPLOSIVE, true);

        Boolean onFire = stack.get(ArrowDataComponents.ON_FIRE.get());
        if (onFire != null && onFire) {
            this.entityData.set(DATA_ON_FIRE, true);
            this.setSharedFlagOnFire(true); // keep the arrow ignited constantly
        } else {
            this.entityData.set(DATA_ON_FIRE, false);
        }
    }

    public void setItemStack(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        boolean hasPotion = stack.has(DataComponents.POTION_CONTENTS);
        stack.set(ArrowDataComponents.HAS_POTION, hasPotion);

        applyComponentsFromStack(stack);
        updateColor();
    }

    // --- Physics overrides ---
    @Override
    protected double getDefaultGravity() {
        return this.entityData.get(DATA_GRAVITY);
    }

    @Override
    public double getBaseDamage() {
        return this.entityData.get(DATA_BASE_DAMAGE) * this.entityData.get(DATA_DAMAGE_MULT);
    }

    @Override
    protected float getWaterInertia() {
        return this.entityData.get(DATA_WATER_INERTIA);
    }

    // --- Effects on hit ---
    @Override
    protected void doPostHurtEffects(LivingEntity target) {
        Entity source = this.getEffectSource();

        // --- Apply potion effects as before ---
        if (this.entityData.get(DATA_HAS_POTION)) {
            PotionContents potion = this.getPotionContents();
            potion.potion().ifPresent(holder -> {
                for (MobEffectInstance effect : holder.value().getEffects()) {
                    target.addEffect(
                            new MobEffectInstance(
                                    effect.getEffect(),
                                    Math.max(effect.mapDuration(d -> d / 8), 1),
                                    effect.getAmplifier(),
                                    effect.isAmbient(),
                                    effect.isVisible()
                            ),
                            source
                    );
                }
            });

            for (MobEffectInstance custom : potion.customEffects()) {
                target.addEffect(custom, source);
            }
        }

        // --- Apply fire effect if arrow is on fire ---
        if (this.entityData.get(DATA_ON_FIRE)) {
            // 5 seconds is default Minecraft arrow-on-fire duration
            target.setRemainingFireTicks(100);
        }

        super.doPostHurtEffects(target);
    }

    // --- Tick logic ---
    @Override
    public void tick() {
        super.tick();

        if (this.entityData.get(DATA_ON_FIRE)) {
            this.setSharedFlagOnFire(true);
        }

        if (this.level().isClientSide) {
            // Client-side potion particles
            if (this.entityData.get(DATA_HAS_POTION)) {
                if (this.inGround) {
                    if (this.inGroundTime % 5 == 0)
                        makeParticle(1);
                } else {
                    makeParticle(2);
                }
            }

        } else { // === SERVER SIDE ===

            // Skip if no potion data is defined
            if (!this.entityData.get(DATA_HAS_POTION))
                return;

            PotionContents contents = this.getPotionContents();
            if (contents == null || contents.equals(PotionContents.EMPTY))
                return;

            // Potion decay logic
            if (this.inGround && this.inGroundTime >= EXPOSED_POTION_DECAY_TIME) {
                this.level().broadcastEntityEvent(this, EVENT_POTION_PUFF);

                // Convert to plain dynamic arrow
                ItemStack newStack = new ItemStack(ExperimentalCombatItems.DYNAMIC_ARROW.get());
                newStack.set(ArrowDataComponents.HAS_POTION, false);
                this.setPickupItemStack(newStack);
            }
        }
    }

    private void makeParticle(int amount) {
        int color = this.getColor();
        if (color == NO_EFFECT_COLOR || amount <= 0) return;

        for (int i = 0; i < amount; i++) {
            this.level().addParticle(
                    ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, color),
                    this.getRandomX(0.5),
                    this.getRandomY(),
                    this.getRandomZ(0.5),
                    0.0, 0.0, 0.0
            );
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_POTION_PUFF) {
            int color = getColor();
            if (color != NO_EFFECT_COLOR) {
                float r = (float)(color >> 16 & 255) / 255.0F;
                float g = (float)(color >> 8 & 255) / 255.0F;
                float b = (float)(color & 255) / 255.0F;
                for (int j = 0; j < 20; j++) {
                    this.level().addParticle(
                            ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, r, g, b),
                            this.getRandomX(0.5),
                            this.getRandomY(),
                            this.getRandomZ(0.5),
                            0.0, 0.0, 0.0
                    );
                }
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    // --- Reflection ---
    private static Method findSetPierceMethod() {
        try {
            Method m = AbstractArrow.class.getDeclaredMethod("setPierceLevel", byte.class);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void invokeSetPierceLevel(byte level) {
        if (SET_PIERCE_METHOD == null) return;
        try {
            SET_PIERCE_METHOD.invoke(this, level);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ExperimentalCombatItems.DYNAMIC_ARROW.get());
    }

    @Override
    protected void setPickupItemStack(ItemStack stack) {
        super.setPickupItemStack(stack);
        applyComponentsFromStack(stack);
        updateColor();
    }

    // Convenience constructors
    public DynamicArrowEntity(Level level, LivingEntity shooter, ItemStack pickup, @Nullable ItemStack weapon) {
        this(ExperimentalCombatEntities.DYNAMIC_ARROW.get(), shooter, level, pickup, weapon);
    }

    public DynamicArrowEntity(Level level, double x, double y, double z, ItemStack pickup, @Nullable ItemStack weapon) {
        this(ExperimentalCombatEntities.DYNAMIC_ARROW.get(), x, y, z, level, pickup, weapon);
    }
}
