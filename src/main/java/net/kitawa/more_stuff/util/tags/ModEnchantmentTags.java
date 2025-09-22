package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;

public class ModEnchantmentTags {
    public static final TagKey<Enchantment> ALLOWS_ARMOR_ABSORPTION = create("allows_armor_absorption");
    public static final TagKey<Enchantment> ALLOWS_ITEM_TO_BE_SACRIFICED = create("allows_item_to_be_sacrificed");

    private ModEnchantmentTags() {}

    private static TagKey<Enchantment> create(String name) {
        return TagKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
