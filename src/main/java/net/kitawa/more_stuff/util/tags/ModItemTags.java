package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

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
    public static final TagKey<Item> METALLIC_ARMOR = create("metallic_armor");
    public static final TagKey<Item> UNAFFECTED_COPPER_BLOCKS = create("unaffected_copper_blocks");
    public static final TagKey<Item> WAXED_UNAFFECTED_COPPER_BLOCKS = create("waxed_unaffected_copper_blocks");
    public static final TagKey<Item> EXPOSED_COPPER_BLOCKS = create("exposed_copper_blocks");
    public static final TagKey<Item> WAXED_EXPOSED_COPPER_BLOCKS = create("waxed_exposed_copper_blocks");
    public static final TagKey<Item> WEATHERED_COPPER_BLOCKS = create("weathered_copper_blocks");
    public static final TagKey<Item> WAXED_WEATHERED_COPPER_BLOCKS = create("waxed_weathered_copper_blocks");
    public static final TagKey<Item> OXIDIZED_COPPER_BLOCKS = create("oxidized_copper_blocks");
    public static final TagKey<Item> WAXED_OXIDIZED_COPPER_BLOCKS = create("waxed_oxidized_copper_blocks");
    public static final TagKey<Item> ROSE_GOLD_BLOCKS = create("rose_gold_blocks");
    public static final TagKey<Item> IS_PROJECTILE_IMMUNE_ARMOR = create("is_projectile_immune_armor");
    public static final TagKey<Item> SHULKER_SHELL_WOLF_ARMOR = create("shulker_shell_wolf_armor");
    public static final TagKey<Item> SHULKER_SHELL_HORSE_ARMOR = create("shulker_shell_horse_armor");
    public static final TagKey<Item> SHULKER_SHELL_HELMETS = create("shulker_shell_helmets");
    public static final TagKey<Item> SHULKER_SHELL_CHESTPLATES = create("shulker_shell_chestplates");
    public static final TagKey<Item> SHULKER_SHELL_LEGGINGS = create("shulker_shell_leggings");
    public static final TagKey<Item> SHULKER_SHELL_BOOTS = create("shulker_shell_boots");

    private ModItemTags() {
    }

    private static TagKey<Item> create(String name) {
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
