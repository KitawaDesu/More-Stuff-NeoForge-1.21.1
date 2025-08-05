package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModItemTags {
    public static final TagKey<Item> ABSORBS_DAMAGE = create("absorbs_damage");
    public static final TagKey<Item> WOLF_ARMOR = create("wolf_armor");
    public static final TagKey<Item> HOGLIN_ARMOR = create("hoglin_armor");
    public static final TagKey<Item> GLIDERS = create("gliders");
    public static final TagKey<Item> ARMOR = create("armor");
    public static final TagKey<Item> UPGRADES_TO_DIAMOND_LEVEL_ELYTRA = create("upgrades_to_diamond_level_elytra");
    public static final TagKey<Item> ZINC_TOOL_MATERIALS = create("zinc_tool_materials");
    public static final TagKey<Item> BRASS_TOOL_MATERIALS = create("brass_tool_materials");
    public static final TagKey<Item> BARTERING_ITEMS = create("bartering_items");
    public static final TagKey<Item> TEIRED_GLIDERS = create("teired_gliders");
    public static final TagKey<Item> HORSE_ARMOR = create("horse_armor");
    public static final TagKey<Item> SHEARS = create("shears");

    private ModItemTags() {
    }

    private static TagKey<Item> create(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
