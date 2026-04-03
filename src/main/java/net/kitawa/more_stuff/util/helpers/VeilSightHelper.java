package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.enchantments.ModEnchantments;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.stream.StreamSupport;

@OnlyIn(Dist.CLIENT)
public class VeilSightHelper {
    public static boolean localPlayerHasVeilSight() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;

        var registry = player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        return StreamSupport.stream(player.getArmorSlots().spliterator(), false)
                .anyMatch(stack -> stack.getEnchantmentLevel(registry.getOrThrow(ModEnchantments.VEIL_SIGHT)) > 0)
                || player.getMainHandItem().getEnchantmentLevel(registry.getOrThrow(ModEnchantments.VEIL_SIGHT)) > 0;
    }
}