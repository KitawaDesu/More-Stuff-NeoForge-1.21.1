package net.kitawa.more_stuff.worldgen;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {
    public static final ResourceKey<PlacedFeature> ORE_COPPER_NETHER = registerKey("ore_copper_nether");
    public static final ResourceKey<PlacedFeature> ORE_IRON_NETHER = registerKey("ore_iron_nether");
    public static final ResourceKey<PlacedFeature> ORE_EXPERIENCE_NETHER = registerKey("ore_experience_nether");
    public static final ResourceKey<PlacedFeature> ORE_COPPER_DELTAS = registerKey("ore_copper_deltas");
    public static final ResourceKey<PlacedFeature> ORE_IRON_DELTAS = registerKey("ore_iron_deltas");
    public static final ResourceKey<PlacedFeature> ORE_EXPERIENCE_DELTAS = registerKey("ore_experience_deltas");
    public static final ResourceKey<PlacedFeature> ORE_ZINC_NETHER = registerKey("ore_zinc_nether");
    public static final ResourceKey<PlacedFeature> ORE_ZINC_DELTAS = registerKey("ore_zinc_deltas");
    public static final ResourceKey<PlacedFeature> DELTA = registerKey("delta");
    public static final ResourceKey<PlacedFeature> BLAZING_REEDS = registerKey("blazing_reeds");
    public static final ResourceKey<PlacedFeature> NETHER_MONSTER_ROOM = registerKey("nether_monster_room");
    public static final ResourceKey<PlacedFeature> PLACED_NETHER_VAULT = registerKey("placed_nether_vault");
    public static final ResourceKey<PlacedFeature> PLACED_OVERWORLD_VAULT = registerKey("placed_overworld_vault");

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
