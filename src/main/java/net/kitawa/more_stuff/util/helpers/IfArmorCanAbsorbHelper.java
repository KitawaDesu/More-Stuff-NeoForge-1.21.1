package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.util.tags.ModEnchantmentTags;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;

public class IfArmorCanAbsorbHelper {
    private IfArmorCanAbsorbHelper() {} // prevent instantiation

    /**
     * Handles armor absorption of damage. Returns true if damage was absorbed.
     */
    public static boolean absorbDamageWithArmor(
            LivingEntity entity, DamageSource damageSource, float damageAmount) {

        if (damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR)) {
            return false;
        }

        List<EquipmentSlot> armorSlots = List.of(
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET,
                EquipmentSlot.BODY
        );

        for (EquipmentSlot slot : armorSlots) {
            ItemStack stack = entity.getItemBySlot(slot);

            if (stack.isEmpty()) continue;

            boolean canAbsorb = stack.is(ModItemTags.ABSORBS_DAMAGE) ||
                    EnchantmentHelper.hasTag(stack, ModEnchantmentTags.ALLOWS_ARMOR_ABSORPTION);

            if (!canAbsorb) continue;

            int max = stack.getMaxDamage();
            int damage = stack.getDamageValue();
            int durabilityLeft = max - damage;

            if (durabilityLeft > (int)(max * 0.1f)) {
                return tryDamageArmorPiece(entity, stack, slot, damageAmount);
            }
        }

        // No suitable armor piece found
        return false;
    }

    private static boolean tryDamageArmorPiece(
            LivingEntity entity,
            ItemStack armorPiece,
            EquipmentSlot slot,
            float damageAmount) {

        int oldDamage = armorPiece.getDamageValue();
        int maxDamage = armorPiece.getMaxDamage();

        armorPiece.hurtAndBreak(Mth.ceil(damageAmount), entity, slot);

        // Calculate 10% durability threshold
        int minDurabilityLeft = Math.max(1, Math.round(maxDamage * 0.10f));
        int minDamageValue = maxDamage - minDurabilityLeft;

        // Clamp if durability falls below the threshold
        if (armorPiece.getDamageValue() > minDamageValue) {
            armorPiece.setDamageValue(minDamageValue);
        }

        if (Crackiness.WOLF_ARMOR.byDamage(oldDamage, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(armorPiece)) {
            entity.playSound(SoundEvents.WOLF_ARMOR_CRACK);

            if (entity.level() instanceof ServerLevel serverLevel && !armorPiece.isEmpty()) {
                serverLevel.sendParticles(
                        new ItemParticleOption(ParticleTypes.ITEM, armorPiece.copy()),
                        entity.getX(), entity.getY() + 1.0, entity.getZ(),
                        20, 0.2, 0.1, 0.2, 0.1
                );
            }
        }

        return true;
    }
}

