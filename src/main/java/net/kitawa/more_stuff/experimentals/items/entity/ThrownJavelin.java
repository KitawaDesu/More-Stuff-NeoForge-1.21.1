package net.kitawa.more_stuff.experimentals.items.entity;

import net.kitawa.more_stuff.experimentals.entities.ExperimentalCombatEntities;
import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownJavelin extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> ID_TEXTURE = SynchedEntityData.defineId(ThrownJavelin.class, EntityDataSerializers.STRING);

    private boolean dealtDamage;
    public int clientSideReturnTridentTickCount;

    public ThrownJavelin(EntityType<? extends ThrownJavelin> type, Level level) {
        super(type, level);
    }

    public ThrownJavelin(Level level, LivingEntity shooter, ItemStack stack) {
        super(ExperimentalCombatEntities.JAVELIN.get(), shooter, level, stack, stack);
        setItemStack(stack);
    }

    public ThrownJavelin(Level level, double x, double y, double z, ItemStack stack) {
        super(ExperimentalCombatEntities.JAVELIN.get(), x, y, z, level, stack, stack);
        setItemStack(stack);
    }

    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverLevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, stack, this), 0, 127)
                : 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
        builder.define(ID_TEXTURE, "more_stuff:textures/entity/javelin/wood.png"); // default fallback
    }

    public void setItemStack(ItemStack stack) {
        this.entityData.set(ID_FOIL, stack.hasFoil());
        this.entityData.set(ID_LOYALTY, getLoyaltyFromItem(stack));

        if (stack.getItem() instanceof JavelinItem spear) {
            this.entityData.set(ID_TEXTURE, spear.getTexture().toString());
        }
    }

    @Override
    protected EntityHitResult findHitEntity(Vec3 startVec, Vec3 endVec) {
        return dealtDamage ? null : super.findHitEntity(startVec, endVec);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        ItemStack stack = getWeaponItem();
        float damage = 8.0F;

        if (stack.getItem() instanceof JavelinItem spear) {
            damage = spear.getTier().getAttackDamageBonus() + 6.0F;
        }

        Entity owner = getOwner();
        DamageSource source = this.damageSources().trident(this, owner != null ? owner : this);

        if (this.level() instanceof ServerLevel serverLevel) {
            damage = EnchantmentHelper.modifyDamage(serverLevel, stack, entity, source, damage);
        }

        dealtDamage = true;

        if (entity.hurt(source, damage)) {
            if (entity.getType() != EntityType.ENDERMAN) {
                if (this.level() instanceof ServerLevel serverLevel1) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel1, entity, source, stack);
                }
                if (entity instanceof LivingEntity livingEntity) {
                    doKnockback(livingEntity, source);
                    doPostHurtEffects(livingEntity);
                }
            }
        }

        setDeltaMovement(getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        playSound(SoundEvents.TRIDENT_HIT, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) dealtDamage = true;

        Entity owner = getOwner();
        int loyalty = this.entityData.get(ID_LOYALTY);

        if (loyalty > 0 && (dealtDamage || isNoPhysics()) && owner != null) {
            if (!isAcceptibleReturnOwner()) {
                if (!level().isClientSide && pickup == AbstractArrow.Pickup.ALLOWED) {
                    spawnAtLocation(getPickupItem(), 0.1F);
                }
                discard();
            } else {
                setNoPhysics(true);
                Vec3 vec = owner.getEyePosition().subtract(position());
                setPosRaw(getX(), getY() + vec.y * 0.015 * loyalty, getZ());
                if (level().isClientSide) yOld = getY();

                double speed = 0.05 * loyalty;
                setDeltaMovement(getDeltaMovement().scale(0.95).add(vec.normalize().scale(speed)));

                if (clientSideReturnTridentTickCount == 0) {
                    playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }
                clientSideReturnTridentTickCount++;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayer) || !entity.isSpectator());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        dealtDamage = tag.getBoolean("DealtDamage");
        String texture = tag.getString("Texture");
        if (!texture.isEmpty()) {
            entityData.set(ID_TEXTURE, texture);
        }
        entityData.set(ID_LOYALTY, getLoyaltyFromItem(getPickupItemStackOrigin()));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("DealtDamage", dealtDamage);
        tag.putString("Texture", entityData.get(ID_TEXTURE));
    }

    @Override
    public ItemStack getWeaponItem() {
        return getPickupItemStackOrigin();
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.TRIDENT);
    }

    @Override
    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean isFoil() {
        return entityData.get(ID_FOIL);
    }

    public ResourceLocation getTexture() {
        String path = entityData.get(ID_TEXTURE);
        return path.isEmpty()
                ? ResourceLocation.fromNamespaceAndPath("more_stuff", "textures/entity/spear/wood.png")
                : ResourceLocation.parse(path);
    }
}
