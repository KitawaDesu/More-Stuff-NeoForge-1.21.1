package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.List;

public class IfArmorCanAbsorbHelper {
    private IfArmorCanAbsorbHelper() {}

    public static boolean absorbDamageWithArmor(
            LivingEntity entity, DamageSource damageSource, float damageAmount) {

        if (damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR)) return false;

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

            boolean hasTag = stack.is(ModItemTags.ABSORBS_DAMAGE);
            boolean hasEnchantment = entity.level().registryAccess()
                    .lookup(Registries.ENCHANTMENT)
                    .flatMap(reg -> reg.get(ModEnchantments.DIVINE_ABSORPTION))
                    .map(holder -> EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0)
                    .orElse(false);

            if (!hasTag && !hasEnchantment) continue;

            int max = stack.getMaxDamage();
            int durabilityLeft = max - stack.getDamageValue();

            if (durabilityLeft > (int)(max * 0.1f)) {
                return tryDamageArmorPiece(entity, stack, slot, damageAmount);
            }
        }

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

        int minDurabilityLeft = Math.max(1, Math.round(maxDamage * 0.10f));
        int minDamageValue = maxDamage - minDurabilityLeft;

        if (armorPiece.getDamageValue() > minDamageValue) {
            armorPiece.setDamageValue(minDamageValue);
        }

        int newDamage = armorPiece.getDamageValue();
        if (Crackiness.WOLF_ARMOR.byDamage(oldDamage, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(newDamage, maxDamage)) {
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