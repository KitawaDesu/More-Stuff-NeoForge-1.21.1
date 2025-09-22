package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ModModelBakery extends ModelBakery {

    public static final Material STONE_SHIELD_BASE = material("stone/base");
    public static final Material STONE_NO_PATTERN_SHIELD = material("stone/base_nopattern");

    public static final Material LAPIS_SHIELD_BASE = material("lapis/base");
    public static final Material LAPIS_NO_PATTERN_SHIELD = material("lapis/base_nopattern");

    public static final Material QUARTZ_SHIELD_BASE = material("quartz/base");
    public static final Material QUARTZ_NO_PATTERN_SHIELD = material("quartz/base_nopattern");

    public static final Material COPPER_SHIELD_BASE = material("copper/base");
    public static final Material COPPER_NO_PATTERN_SHIELD = material("copper/base_nopattern");

    public static final Material GOLDEN_SHIELD_BASE = material("gold/base");
    public static final Material GOLDEN_NO_PATTERN_SHIELD = material("gold/base_nopattern");

    public static final Material ROSE_GOLDEN_SHIELD_BASE = material("rose_gold/base");
    public static final Material ROSE_GOLDEN_NO_PATTERN_SHIELD = material("rose_gold/base_nopattern");

    public static final Material IRON_SHIELD_BASE = material("iron/base");
    public static final Material IRON_NO_PATTERN_SHIELD = material("iron/base_nopattern");

    public static final Material DIAMOND_SHIELD_BASE = material("diamond/base");
    public static final Material DIAMOND_NO_PATTERN_SHIELD = material("diamond/base_nopattern");

    public static final Material EMERALD_SHIELD_BASE = material("emerald/base");
    public static final Material EMERALD_NO_PATTERN_SHIELD = material("emerald/base_nopattern");

    public static final Material NETHERITE_SHIELD_BASE = material("netherite/base");
    public static final Material NETHERITE_NO_PATTERN_SHIELD = material("netherite/base_nopattern");

    public static final Material ROSARITE_SHIELD_BASE = material("rosarite/base");
    public static final Material ROSARITE_NO_PATTERN_SHIELD = material("rosarite/base_nopattern");

    // Optional: Zinc & Brass from Create mod
    public static final Material ZINC_SHIELD_BASE;
    public static final Material ZINC_NO_PATTERN_SHIELD;
    public static final Material BRASS_SHIELD_BASE;
    public static final Material BRASS_NO_PATTERN_SHIELD;

    static {
        if (ModList.get().isLoaded("create")) {
            ZINC_SHIELD_BASE = material("zinc/base");
            ZINC_NO_PATTERN_SHIELD = material("zinc/base_nopattern");

            BRASS_SHIELD_BASE = material("brass/base");
            BRASS_NO_PATTERN_SHIELD = material("brass/base_nopattern");
        } else {
            ZINC_SHIELD_BASE = null;
            ZINC_NO_PATTERN_SHIELD = null;
            BRASS_SHIELD_BASE = null;
            BRASS_NO_PATTERN_SHIELD = null;
        }
    }

    public ModModelBakery(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> modelResources, Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> blockStateResources) {
        super(blockColors, profilerFiller, modelResources, blockStateResources);
    }

    private static Material material(String name) {
        return new Material(Sheets.SHIELD_SHEET, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "entity/shield/" + name));
    }
}
