package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.blocks.custom.frostbitten_caverns.IceSheetBlock;
import net.kitawa.more_stuff.util.tags.ModEnchantmentTags;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "actuallyHurt", at = @At("HEAD"), cancellable = true)
    private void injectActuallyHurt(DamageSource damageSource, float damageAmount, CallbackInfo ci) {
        if (damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR)) {
            return; // skip all logic
        }

        List<EquipmentSlot> armorSlots = List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET,
                EquipmentSlot.BODY
        );

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = this.getItemBySlot(slot);

            if (tryAbsorb(this.getControllingPassenger(), stack, slot, damageAmount)) {
                ci.cancel(); // damage absorbed successfully, cancel the hit
                return;
            }
        }
    }

@Shadow
public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    private boolean tryAbsorb(LivingEntity entity, ItemStack stack, EquipmentSlot slot, float damageAmount) {
        if (stack.isEmpty()) return false;

        boolean canAbsorb = stack.is(ModItemTags.ABSORBS_DAMAGE) ||
                EnchantmentHelper.hasTag(stack, ModEnchantmentTags.ALLOWS_ARMOR_ABSORPTION);

        if (!canAbsorb) return false;

        int maxDamage = stack.getMaxDamage();
        int currentDamage = stack.getDamageValue();
        int durabilityLeft = maxDamage - currentDamage;

        // Calculate 10% durability threshold (minimum 1)
        int minDurabilityLeft = Math.max(1, Math.round(maxDamage * 0.10f));
        int minDamageValue = maxDamage - minDurabilityLeft;

        // Skip absorption if below 10% durability
        if (durabilityLeft <= minDurabilityLeft) {
            return false;
        }

        int oldDamage = currentDamage;

        stack.hurtAndBreak(Mth.ceil(damageAmount), entity, slot);

        // Clamp post-damage durability to 10% minimum
        if (stack.getDamageValue() > minDamageValue) {
            stack.setDamageValue(minDamageValue);
        }

        // Play crackiness effect if state changed
        if (Crackiness.WOLF_ARMOR.byDamage(oldDamage, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(stack)) {
            entity.playSound(SoundEvents.WOLF_ARMOR_CRACK);

            if (entity.level() instanceof ServerLevel serverLevel && !stack.isEmpty()) {
                serverLevel.sendParticles(
                        new ItemParticleOption(ParticleTypes.ITEM, stack.copy()),
                        entity.getX(), entity.getY() + 1.0, entity.getZ(),
                        20, 0.2, 0.1, 0.2, 0.1
                );
            }
        }

        return true; // Absorbed successfully
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void injectArmorAbsorptionTag(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            return;
        }

        LivingEntity entity = (LivingEntity)(Object)this;

        // Store the tagged item and a way to remove it
        ItemStack taggedItem = null;
        Runnable removeItem = null;

        // Check armor slots
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (!stack.isEmpty() && EnchantmentHelper.hasTag(stack, EnchantmentTags.CURSE)) {
                    taggedItem = stack;
                    removeItem = () -> entity.setItemSlot(slot, ItemStack.EMPTY);
                    break;
                }
            }
        }

        // Check hand slots (if nothing found in armor)
        if (taggedItem == null) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = entity.getItemInHand(hand);
                if (!stack.isEmpty() && EnchantmentHelper.hasTag(stack, EnchantmentTags.CURSE)) {
                    taggedItem = stack;
                    removeItem = () -> entity.setItemInHand(hand, ItemStack.EMPTY);
                    break;
                }
            }
        }

        // If found, apply totem effect and delete item
        if (taggedItem != null && removeItem != null) {
            if (entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.awardStat(Stats.ITEM_USED.get(taggedItem.getItem()));
                CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, taggedItem);
                entity.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }

            entity.setHealth(1.0F);
            entity.removeEffectsCuredBy(net.neoforged.neoforge.common.EffectCures.PROTECTED_BY_TOTEM);
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            entity.level().broadcastEntityEvent(entity, (byte) 35);

            // Delete the actual item
            removeItem.run();

            cir.setReturnValue(true);
        }
    }

    private ItemStack findTaggedItem(LivingEntity entity) {
        // Check armor slots
        for (ItemStack armorStack : entity.getArmorSlots()) {
            if (EnchantmentHelper.hasTag(armorStack, ModEnchantmentTags.ALLOWS_ITEM_TO_BE_SACRIFICED)) {
                return armorStack.copy();
            }
        }

        // Check hand slots
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack handStack = entity.getItemInHand(hand);
            if (EnchantmentHelper.hasTag(handStack, ModEnchantmentTags.ALLOWS_ITEM_TO_BE_SACRIFICED)) {
                return handStack.copy();
            }
        }

        return null;
    }

    @ModifyVariable(
            method = "travel",
            at = @At(value = "STORE"), // right after f2 is stored
            ordinal = 0
    )
    private float ms$checkUnderAndAbove(float original) {
        float friction = original;

        BlockPos basePos = this.getBlockPosBelowThatAffectsMyMovement();

        boolean foundOverride = false;

        // check upwards from that block, up to ~0.6 blocks
        for (int i = 0; i <= 1; i++) {
            BlockPos checkPos = basePos.above(i);
            double relativeY = this.getY() - checkPos.getY();

            // only consider blocks that overlap the entity’s feet up to 0.6 above ground
            if (relativeY >= -0.01 && relativeY <= 0.6) {
                BlockState state = this.level().getBlockState(checkPos);

                if (state.getBlock() instanceof IceSheetBlock ice) {
                    // If we find an IceSheetBlock, override completely and ignore the block below
                    friction = ice.getCustomFriction();
                    foundOverride = true;
                    break; // no need to keep checking further
                }
            }
        }

        // If we didn’t find an override block, fall back to vanilla (block below)
        return foundOverride ? friction : original;
    }
}
