package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.blocks.custom.overworld.frostbitten_caverns.IceSheetBlock;
import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
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
        if (damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR)) return;

        LivingEntity self = (LivingEntity)(Object)this;

        for (EquipmentSlot slot : List.of(
                EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.BODY)) {
            ItemStack stack = self.getItemBySlot(slot);
            if (tryAbsorb(self, stack, slot, damageAmount)) {
                ci.cancel();
                return;
            }
        }
    }

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    private boolean tryAbsorb(LivingEntity entity, ItemStack stack, EquipmentSlot slot, float damageAmount) {
        if (stack.isEmpty()) return false;

        boolean hasItemTag = stack.is(ModItemTags.ABSORBS_DAMAGE);
        boolean hasEnchantTag = hasEnchantment(stack, entity, ModEnchantments.DIVINE_ABSORPTION);

        if (!hasItemTag && !hasEnchantTag) return false;

        int maxDamage = stack.getMaxDamage();
        int currentDamage = stack.getDamageValue();
        int durabilityLeft = maxDamage - currentDamage;
        int minDurabilityLeft = Math.max(1, Math.round(maxDamage * 0.10f));
        int minDamageValue = maxDamage - minDurabilityLeft;

        if (durabilityLeft <= minDurabilityLeft) return false;

        int oldDamage = currentDamage;

        stack.hurtAndBreak(Mth.ceil(damageAmount), entity, slot);

        if (stack.getDamageValue() > minDamageValue) {
            stack.setDamageValue(minDamageValue);
        }

        int newDamage = stack.getDamageValue();
        if (Crackiness.WOLF_ARMOR.byDamage(oldDamage, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(newDamage, maxDamage)) {
            entity.playSound(SoundEvents.WOLF_ARMOR_CRACK);

            if (entity.level() instanceof ServerLevel serverLevel && !stack.isEmpty()) {
                serverLevel.sendParticles(
                        new ItemParticleOption(ParticleTypes.ITEM, stack.copy()),
                        entity.getX(), entity.getY() + 1.0, entity.getZ(),
                        20, 0.2, 0.1, 0.2, 0.1
                );
            }
        }

        return true;
    }

    @Inject(method = "checkTotemDeathProtection", at = @At("HEAD"), cancellable = true)
    private void injectArmorAbsorptionTag(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return;

        LivingEntity entity = (LivingEntity)(Object)this;

        ItemStack taggedItem = null;
        Runnable removeItem = null;

        // Check armor slots
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = entity.getItemBySlot(slot);
                if (!stack.isEmpty() && hasCurseOfSacrification(stack, entity)) {
                    taggedItem = stack;
                    final EquipmentSlot finalSlot = slot;
                    removeItem = () -> entity.setItemSlot(finalSlot, ItemStack.EMPTY);
                    break;
                }
            }
        }

        // Check hand slots if nothing found in armor
        if (taggedItem == null) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = entity.getItemInHand(hand);
                if (!stack.isEmpty() && hasCurseOfSacrification(stack, entity)) {
                    taggedItem = stack;
                    final InteractionHand finalHand = hand;
                    removeItem = () -> entity.setItemInHand(finalHand, ItemStack.EMPTY);
                    break;
                }
            }
        }

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

            removeItem.run();
            cir.setReturnValue(true);
        }
    }

    private boolean hasCurseOfSacrification(ItemStack stack, LivingEntity entity) {
        return entity.level().registryAccess()
                .lookup(Registries.ENCHANTMENT)
                .flatMap(reg -> reg.get(ModEnchantments.CURSE_OF_SACRIFICATION))
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0)
                .orElse(false);
    }

    private boolean hasEnchantment(ItemStack stack, LivingEntity entity, ResourceKey<Enchantment> key) {
        return entity.level().registryAccess()
                .lookup(Registries.ENCHANTMENT)
                .flatMap(reg -> reg.get(key))
                .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0)
                .orElse(false);
    }

    @ModifyVariable(
            method = "travel",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private float ms$checkUnderAndAbove(float original) {
        float friction = original;
        BlockPos basePos = this.getBlockPosBelowThatAffectsMyMovement();
        boolean foundOverride = false;

        for (int i = 0; i <= 1; i++) {
            BlockPos checkPos = basePos.above(i);
            double relativeY = this.getY() - checkPos.getY();

            if (relativeY >= -0.01 && relativeY <= 0.6) {
                BlockState state = this.level().getBlockState(checkPos);
                if (state.getBlock() instanceof IceSheetBlock ice) {
                    friction = ice.getCustomFriction();
                    foundOverride = true;
                    break;
                }
            }
        }

        return foundOverride ? friction : original;
    }
}