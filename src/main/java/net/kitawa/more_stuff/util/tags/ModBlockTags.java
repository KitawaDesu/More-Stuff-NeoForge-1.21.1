package net.kitawa.more_stuff.util.tags;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> INCORRECT_FOR_EMERALD_TOOL = create("incorrect_for_emerald_tool");
    public static final TagKey<Block> INCORRECT_FOR_ROSE_GOLDEN_TOOL = create("incorrect_for_rose_golden_tool");
    public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = create("incorrect_for_copper_tool");
    public static final TagKey<Block> CREATES_UPWARDS_BUBBLE_COLUMNS = create("creates_upwards_bubble_columns");
    public static final TagKey<Block> CREATES_DOWNWARDS_BUBBLE_COLUMNS = create("creates_downwards_bubble_columns");
    public static final TagKey<Block> AQUANDA_STEMS = create("aquanda_stems");
    public static final TagKey<Block> EBONY_STEMS = create("ebony_stems");
    public static final TagKey<Block> AZALEA_LOGS = create("ebony_logs");
    public static final TagKey<Block> MOSS_CAN_GENERATE_UNDER = create("moss_can_generate_under");
    public static final TagKey<Block> CAN_BE_ALTERED_GROUND = create("can_be_altered_ground");
    public static final TagKey<Block> GOLDEN_BAMBOO_PLANTABLE_ON = create("golden_bamboo_plantable_on");
    public static final TagKey<Block> GOLDEN_BAMBOO_GROWABLE_ON = create("golden_bamboo_growable_on");
    public static final TagKey<Block> COPPER_BAMBOO_PLANTABLE_ON = create("copper_bamboo_plantable_on");
    public static final TagKey<Block> COPPER_BAMBOO_GROWABLE_ON = create("copper_bamboo_growable_on");
    public static final TagKey<Block> IRON_BAMBOO_PLANTABLE_ON = create("iron_bamboo_plantable_on");
    public static final TagKey<Block> IRON_BAMBOO_GROWABLE_ON = create("iron_bamboo_growable_on");
    public static final TagKey<Block> ANCIENT_BAMBOO_PLANTABLE_ON = create("ancient_bamboo_plantable_on");
    public static final TagKey<Block> ANCIENT_BAMBOO_GROWABLE_ON = create("ancient_bamboo_growable_on");
    public static final TagKey<Block> PYROLIZED_BAMBOO_PLANTABLE_ON = create("pyrolized_bamboo_plantable_on");
    public static final TagKey<Block> PYROLIZED_BAMBOO_GROWABLE_ON = create("pyrolized_bamboo_growable_on");
    public static final TagKey<Block> INCORRECT_FOR_ZINC_TOOL = create("incorrect_for_zinc_tool");
    public static final TagKey<Block> INCORRECT_FOR_BRASS_TOOL = create("incorrect_for_brass_tool");
    public static final TagKey<Block> BLAZING_REEDS_PLANTABLE_ON = create("blazing_reeds_plantable_on");
    public static final TagKey<Block> UNSUITABLE_CEILINGS = create("unsuitable_ceilings");
    public static final TagKey<Block> STONES = create("stones");
    public static final TagKey<Block> ANCHOR_BLOCKS = create("anchor_blocks");
    public static final TagKey<Block> POWER_SOURCES = create("power_sources");
    public static final TagKey<Block> ELECTRICITY_CAN_TRAVEL_THROUGH = create("electricity_can_travel_through");
    public static final TagKey<Block> METALLIC_BAMBOOS = create("metallic_bamboos");
    public static final TagKey<Block> KELP_LIKE = create("kelp_like");
    public static final TagKey<Block> KELP_PLANT_LIKE = create("kelp_plant_like");
    public static final TagKey<Block> SCAFFOLDING = create("scaffolding");

    private ModBlockTags() {
    }

    private static TagKey<Block> create(String name) {
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
