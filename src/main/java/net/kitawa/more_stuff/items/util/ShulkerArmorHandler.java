package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = MoreStuff.MOD_ID)
public class ShulkerArmorHandler {

    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        LivingEntity living = event.getEntity();

        // Only care about projectile damage
        if (!event.getSource().is(DamageTypeTags.IS_PROJECTILE)) return;

        EquipmentSlot hitSlot;

        // Wolves and horses only use BODY slot
        if (living instanceof Wolf || living instanceof AbstractHorse) {
            hitSlot = EquipmentSlot.BODY;
        } else {
            hitSlot = getRandomArmorSlot(living.getRandom());
        }

        ItemStack armorPiece = living.getItemBySlot(hitSlot);

        if (armorPiece.getItem() instanceof ShulkerArmorItem) {
            // Cancel projectile damage to entity
            event.setCanceled(true);

            // Still damage the armor piece
            armorPiece.hurtAndBreak(1, living, hitSlot);
        }
    }

    private static EquipmentSlot getRandomArmorSlot(RandomSource random) {
        int roll = random.nextInt(100); // 0–99
        if (roll < 15) return EquipmentSlot.HEAD;       // 15%
        if (roll < 55) return EquipmentSlot.CHEST;      // +40% = 55%
        if (roll < 85) return EquipmentSlot.LEGS;       // +30% = 85%
        return EquipmentSlot.FEET;                      // remaining 15%
    }
}