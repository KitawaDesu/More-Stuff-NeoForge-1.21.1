package net.kitawa.more_stuff.entities.projectiles;

import net.kitawa.more_stuff.entities.ModEntities;
import net.kitawa.more_stuff.entities.monster.VeilStalkerEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.*;

public class VeilProjectile extends Projectile {

    private static final int DAMAGE = 4;
    private static final int EFFECT_DURATION = 60;

    private static final EntityDataAccessor<Float> DATA_VISIBILITY =
            SynchedEntityData.defineId(VeilProjectile.class, EntityDataSerializers.FLOAT);

    public VeilProjectile(EntityType<? extends VeilProjectile> type, Level level) {
        super(type, level);
    }

    public VeilProjectile(Level level, VeilStalkerEntity owner) {
        super(ModEntities.VEIL_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_VISIBILITY, 0.05f);
    }

    public float getVisibility() {
        return this.entityData.get(DATA_VISIBILITY);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public float getPickRadius() {
        return 0.3f;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHULKER_BULLET_HURT, SoundSource.HOSTILE, 1.0F, 1.0F);
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.CRIT,
                    this.getX(), this.getY(), this.getZ(), 15, 0.2, 0.2, 0.2, 0.0);
            this.discard();
        }
        return true;
    }

    public void shoot(double dx, double dy, double dz, float speed, float inaccuracy) {
        Vec3 vec = new Vec3(dx, dy, dz).normalize()
                .add(this.random.triangle(0, 0.0172275 * inaccuracy),
                        this.random.triangle(0, 0.0172275 * inaccuracy),
                        this.random.triangle(0, 0.0172275 * inaccuracy))
                .scale(speed);
        this.setDeltaMovement(vec);
        double horizontal = vec.horizontalDistance();
        this.setYRot((float)(Mth.atan2(vec.x, vec.z) * (180 / Math.PI)));
        this.setXRot((float)(Mth.atan2(vec.y, horizontal) * (180 / Math.PI)));
        this.yRotO = this.getYRot();
        this.xRotO = this.getXRot();
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (target instanceof VeilStalkerEntity) return false;
        return super.canHitEntity(target);
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            int lightLevel = level().getBrightness(LightLayer.BLOCK, blockPosition());
            float targetVisibility = lightLevel / 15.0f;
            float current = getVisibility();
            this.entityData.set(DATA_VISIBILITY, current + (targetVisibility - current) * 0.15f);

            Vec3 pos = this.position();
            Vec3 nextPos = pos.add(this.getDeltaMovement());

            BlockHitResult blockHit = this.level().clip(new ClipContext(
                    pos, nextPos,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE, this));

            if (blockHit.getType() != HitResult.Type.MISS) {
                this.onHit(blockHit);
                return;
            }

            AABB searchBox = this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0);
            for (Entity entity : this.level().getEntities(this, searchBox)) {
                if (entity == this.getOwner()) continue;
                if (!entity.isPickable()) continue;
                EntityHitResult entityHit = new EntityHitResult(entity);
                this.onHit(entityHit);
                return;
            }

            this.setPos(nextPos);
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (this.tickCount > 100) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.level().isClientSide()) return;

        Entity target = result.getEntity();

        if (target instanceof LivingEntity living) {
            DamageSource source = this.level().damageSources().thrown(this,
                    this.getOwner() instanceof LivingEntity owner ? owner : this);
            living.hurt(source, DAMAGE);
            living.addEffect(new MobEffectInstance(MobEffects.GLOWING, EFFECT_DURATION, 0));
        }

        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.level().isClientSide()) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.SHULKER_BULLET_HIT, SoundSource.HOSTILE, 1.0F, 1.0F);
        }
        this.discard();
    }
}