package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.tags.ModEnchantmentTags;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
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
                EquipmentSlot.FEET
        );

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = this.getItemBySlot(slot);

            if (tryAbsorb(this, stack, slot, damageAmount)) {
                ci.cancel(); // damage absorbed successfully, cancel the hit
                return;
            }
        }
    }

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
}
