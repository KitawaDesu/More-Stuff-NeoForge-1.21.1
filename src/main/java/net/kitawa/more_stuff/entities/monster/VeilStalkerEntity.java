package net.kitawa.more_stuff.entities.monster;

import net.kitawa.more_stuff.blocks.custom.end.phantasmic.VeilOrchidBlock;
import net.kitawa.more_stuff.entities.projectiles.VeilProjectile;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class VeilStalkerEntity extends Monster {

    private float targetVisibility = 0.05f;

    private static final EntityDataAccessor<Float> DATA_VISIBILITY =
            SynchedEntityData.defineId(VeilStalkerEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> DATA_PASTE_APPLIED =
            SynchedEntityData.defineId(VeilStalkerEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int MELEE_COOLDOWN = 20;
    private static final int RANGED_COOLDOWN = 60;
    private static final int TELEPORT_COOLDOWN = 100;

    private int meleeCooldown = 0;
    private int rangedCooldown = 0;
    private int teleportCooldown = 0;

    public VeilStalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.FLYING_SPEED, 0.4);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_VISIBILITY, 0.05f);
        builder.define(DATA_PASTE_APPLIED, false);
    }

    public float getVisibility() {
        return this.entityData.get(DATA_VISIBILITY);
    }

    private void setVisibility(float v) {
        this.entityData.set(DATA_VISIBILITY, v);
    }

    public boolean isPasteApplied() {
        return this.entityData.get(DATA_PASTE_APPLIED);
    }

    public void setPasteApplied(boolean applied) {
        this.entityData.set(DATA_PASTE_APPLIED, applied);
    }

    public boolean isNearlyInvisible() {
        return getVisibility() < 0.1f;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new VeilStalkerEscapeWaterGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new VeilStalkerAttackGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0, 0.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                // Never retaliate against other VeilStalkers
                LivingEntity attacker = mob.getLastHurtByMob();
                if (attacker instanceof VeilStalkerEntity) return false;
                return super.canUse();
            }
        });
        // Prioritize glowing entities over normal targets
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false,
                entity -> !(entity instanceof VeilStalkerEntity) && entity.hasEffect(MobEffects.GLOWING)));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);

        if (item.is(ModItems.VEIL_PASTE.get())) {
            if (!level().isClientSide()) {
                setVisibility(1.0f);
                targetVisibility = 1.0f;
                setPasteApplied(true);
                item.shrink(1);
            }
            return InteractionResult.sidedSuccess(level().isClientSide());
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void aiStep() {
        // Hover slightly above ground — counteract gravity when near a target
        if (!this.onGround() && this.getDeltaMovement().y < 0.0) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
        }

        LivingEntity target = this.getTarget();
        if (target != null) {
            // Try to stay at eye level with target, float upward if needed
            if (target.getEyeY() > this.getEyeY() + 0.5 && this.getDeltaMovement().y < 0.3) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.2, 0.0));
                this.hasImpulse = true;
            }
        }

        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // Never take damage from other VeilStalkers or VeilProjectiles owned by VeilStalkers
        if (source.getDirectEntity() instanceof VeilStalkerEntity) return false;
        if (source.getDirectEntity() instanceof VeilProjectile projectile
                && projectile.getOwner() instanceof VeilStalkerEntity) return false;
        return super.hurt(source, amount);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }


    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            updateVisibility();
            tickCooldowns();
        }
    }

    private void updateVisibility() {
        // Paste applied — always fully visible, always aggressive
        if (isPasteApplied()) {
            setVisibility(1.0f);
            return;
        }

        float current = getVisibility();
        BlockPos pos = this.blockPosition();
        int lightLevel = level().getBrightness(LightLayer.BLOCK, pos);
        boolean inRevealRadius = isInRevealRadius(pos);

        if (inRevealRadius) {
            targetVisibility = 1.0f;
        } else {
            targetVisibility = lightLevel / 15.0f;
        }

        if (this.getTarget() == null) {
            targetVisibility = Math.min(targetVisibility, 0.3f);
        }

        float newVisibility = current + (targetVisibility - current) * 0.15f;
        setVisibility(newVisibility);
    }

    private boolean isInRevealRadius(BlockPos pos) {
        int checkRadius = 8;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int x = -checkRadius; x <= checkRadius; x++) {
            for (int y = -checkRadius; y <= checkRadius; y++) {
                for (int z = -checkRadius; z <= checkRadius; z++) {
                    mutable.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
                    BlockState state = level().getBlockState(mutable);
                    if (state.getBlock() instanceof VeilOrchidBlock
                            && state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
                        int radius = state.getValue(VeilOrchidBlock.REVEAL_RADIUS);
                        if (pos.distSqr(mutable) <= (double)(radius * radius)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private void tickCooldowns() {
        if (meleeCooldown > 0) meleeCooldown--;
        if (rangedCooldown > 0) rangedCooldown--;
        if (teleportCooldown > 0) teleportCooldown--;
    }

    public void tryAttackTarget(LivingEntity target) {
        double dist = this.distanceTo(target);
        if (dist > 24) return;

        if (!this.getSensing().hasLineOfSight(target)) return;

        boolean targetIsGlowing = target.hasEffect(MobEffects.GLOWING);

        // Glowing targets bypass shadow mode entirely — full attack behavior
        if (isNearlyInvisible() && !targetIsGlowing) {
            if (dist <= 2.5 && random.nextFloat() < 0.05f) {
                doMeleeAttack(target);
            } else if (random.nextFloat() < 0.01f && rangedCooldown <= 0) {
                doRangedAttack(target);
            } else if (teleportCooldown <= 0) {
                teleportAwayFrom(target);
            }
            return;
        }

        if (dist <= 2.5 && meleeCooldown <= 0) {
            doMeleeAttack(target);
        } else if (dist <= 16 && rangedCooldown <= 0) {
            doRangedAttack(target);
        }
    }



    private void doMeleeAttack(LivingEntity target) {
        this.doHurtTarget(target);
        meleeCooldown = MELEE_COOLDOWN;
    }

    private void doRangedAttack(LivingEntity target) {
        Vec3 targetPos = target.getEyePosition();
        Vec3 origin = this.getEyePosition();
        Vec3 direction = targetPos.subtract(origin).normalize();

        VeilProjectile projectile = new VeilProjectile(level(), this);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.shoot(direction.x, direction.y, direction.z, 0.5f, 0.5f);
        level().addFreshEntity(projectile);

        rangedCooldown = RANGED_COOLDOWN;
    }

    private void teleportAwayFrom(LivingEntity target) {
        Vec3 away = this.position().subtract(target.position()).normalize().scale(12);

        for (int attempt = 0; attempt < 16; attempt++) {
            double tx = this.getX() + away.x + (random.nextDouble() - 0.5) * 6;
            double ty = this.getY() + (random.nextDouble() - 0.5) * 4;
            double tz = this.getZ() + away.z + (random.nextDouble() - 0.5) * 6;

            BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos(tx, ty, tz);
            while (check.getY() > level().getMinBuildHeight()
                    && !level().getBlockState(check).blocksMotion()) {
                check.move(Direction.DOWN);
            }

            BlockState ground = level().getBlockState(check);
            BlockState above = level().getBlockState(check.above());
            BlockState above2 = level().getBlockState(check.above(2));

            if (ground.blocksMotion()
                    && ground.getFluidState().isEmpty()
                    && above.isAir()
                    && above2.isAir()) {
                this.teleportTo(tx, check.getY() + 1, tz);
                teleportCooldown = TELEPORT_COOLDOWN;
                return;
            }
        }
        teleportCooldown = TELEPORT_COOLDOWN / 2;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {}

    static class VeilStalkerAttackGoal extends Goal {
        private final VeilStalkerEntity mob;
        private LivingEntity target;

        VeilStalkerAttackGoal(VeilStalkerEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            target = mob.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public void tick() {
            mob.getLookControl().setLookAt(target, 30f, 30f);

            if (mob.isNearlyInvisible()) {
                double dist = mob.distanceTo(target);
                if (dist > 20) {
                    mob.getMoveControl().setWantedPosition(
                            target.getX(), target.getY(), target.getZ(), 0.5);
                }
            } else {
                mob.getMoveControl().setWantedPosition(
                        target.getX(), target.getY(), target.getZ(), 1.0);
            }

            mob.tryAttackTarget(target);
        }
    }


    static class VeilStalkerEscapeWaterGoal extends Goal {
        private final VeilStalkerEntity mob;
        private int cooldown = 0;

        VeilStalkerEscapeWaterGoal(VeilStalkerEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return mob.isInWater() || mob.isInLava();
        }

        @Override
        public boolean canContinueToUse() {
            return mob.isInWater() || mob.isInLava();
        }

        @Override
        public void tick() {
            if (--cooldown > 0) return;
            cooldown = 20; // try every second

            RandomSource random = mob.getRandom();

            for (int attempt = 0; attempt < 16; attempt++) {
                double tx = mob.getX() + (random.nextDouble() - 0.5) * 16;
                double ty = mob.getY() + random.nextDouble() * 8;
                double tz = mob.getZ() + (random.nextDouble() - 0.5) * 16;

                BlockPos.MutableBlockPos check = new BlockPos.MutableBlockPos(tx, ty, tz);

                // Walk down to find solid ground
                while (check.getY() > mob.level().getMinBuildHeight()
                        && !mob.level().getBlockState(check).blocksMotion()) {
                    check.move(Direction.DOWN);
                }

                BlockState ground = mob.level().getBlockState(check);
                BlockState above = mob.level().getBlockState(check.above());
                BlockState above2 = mob.level().getBlockState(check.above(2));

                if (ground.blocksMotion()
                        && ground.getFluidState().isEmpty()
                        && above.getFluidState().isEmpty()
                        && above2.getFluidState().isEmpty()
                        && above.isAir()
                        && above2.isAir()) {
                    mob.teleportTo(tx, check.getY() + 1, tz);
                    return;
                }
            }
        }
    }@Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.BLAZE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.BLAZE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.BLAZE_DEATH;
    }

    @Override
    public void playSound(SoundEvent sound, float volume, float pitch) {
        // Half pitch gives a deep, ghostly effect
        super.playSound(sound, volume, pitch * 0.5f);
    }

    @Override
    public float getVoicePitch() {
        // Base pitch before random variation — lower = more ghostly
        return 0.5f;
    }

}