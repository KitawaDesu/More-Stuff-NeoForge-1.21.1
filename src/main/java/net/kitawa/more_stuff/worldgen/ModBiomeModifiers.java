package net.kitawa.more_stuff.worldgen;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.stream.Collectors;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_NETHER_COPPER_ORE = registerKey("add_nether_copper_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_COPPER_ORE = registerKey("add_deltas_copper_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_IRON_ORE = registerKey("add_nether_iron_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_IRON_ORE = registerKey("add_deltas_iron_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_EXPERIENCE_ORE = registerKey("add_nether_experience_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_EXPERIENCE_ORE = registerKey("add_deltas_experience_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ZINC_ORE = registerKey("add_nether_zinc_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_ZINC_ORE = registerKey("add_deltas_zinc_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS = registerKey("add_deltas");
    public static final ResourceKey<BiomeModifier> ADD_BLAZING_REEDS = registerKey("add_blazing_reeds");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_MONSTER_ROOM = registerKey("add_nether_monster_room");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_VAULT = registerKey("add_nether_vault");
    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_VAULT = registerKey("add_overworld_vault");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_NETHER_COPPER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_COPPER_NETHER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_DELTAS_COPPER_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BASALT_DELTAS)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_COPPER_DELTAS)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_NETHER_IRON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_IRON_NETHER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_DELTAS_IRON_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BASALT_DELTAS)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_IRON_DELTAS)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_NETHER_EXPERIENCE_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_EXPERIENCE_NETHER)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_DELTAS_EXPERIENCE_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BASALT_DELTAS)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_EXPERIENCE_DELTAS)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_DELTAS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DELTA)),
                GenerationStep.Decoration.UNDERGROUND_DECORATION));

        context.register(ADD_BLAZING_REEDS, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BLAZING_REEDS)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_NETHER_MONSTER_ROOM, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.NETHER_MONSTER_ROOM)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        context.register(ADD_NETHER_VAULT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PLACED_NETHER_VAULT)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        context.register(ADD_OVERWORLD_VAULT, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.PLACED_OVERWORLD_VAULT)),
                GenerationStep.Decoration.UNDERGROUND_STRUCTURES));

        if (ModList.get().isLoaded("create")) {

            context.register(ADD_NETHER_ZINC_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                    biomes.getOrThrow(BiomeTags.IS_NETHER),
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_ZINC_NETHER)),
                    GenerationStep.Decoration.UNDERGROUND_ORES));

            context.register(ADD_DELTAS_ZINC_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                    HolderSet.direct(biomes.getOrThrow(Biomes.BASALT_DELTAS)),
                    HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.ORE_ZINC_DELTAS)),
                    GenerationStep.Decoration.UNDERGROUND_ORES));
        }
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}
