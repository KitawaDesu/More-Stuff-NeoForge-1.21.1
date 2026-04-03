package net.kitawa.more_stuff.enchantments;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;

public class ModEnchantments {
    public static final ResourceKey<Enchantment> SMELTING_TOUCH = registerKey("smelting_touch");
    public static final ResourceKey<Enchantment> SWIFT_HIT = registerKey("swift_hit");
    public static final ResourceKey<Enchantment> DIVINE_ABSORPTION = registerKey("divine_absorption");
    public static final ResourceKey<Enchantment> CURSE_OF_SACRIFICATION = registerKey("sacrification_curse");
    public static final ResourceKey<Enchantment> VEIL_SIGHT = registerKey("veil_sight");


    public static ResourceKey<Enchantment> registerKey(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,
                ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
