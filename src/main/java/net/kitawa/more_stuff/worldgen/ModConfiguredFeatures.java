package net.kitawa.more_stuff.worldgen;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> AQUANDA = registerKey("aquanda");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AQUANDA_MOSS_PATCH_BONEMEAL = registerKey("aquanda_moss_patch_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AQUANDA_MOSS_PATCH_BONEMEAL_UNDERWATER = registerKey("aquanda_moss_patch_bonemeal_underwater");
    public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY_FUNGUS_PLANTED = registerKey("ebony_fungus_planted");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLAZING_FOREST_VEGETATION_BONEMEAL = registerKey("blazing_forest_vegetation_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_COPPER = registerKey("ore_nether_copper");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_IRON = registerKey("ore_nether_iron");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_EXPERIENCE = registerKey("ore_nether_experience");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_NETHER_ZINC = registerKey("ore_nether_zinc");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DELTA = registerKey("delta");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLAZING_REEDS = registerKey("blazing_reeds");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
