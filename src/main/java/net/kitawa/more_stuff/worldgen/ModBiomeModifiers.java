package net.kitawa.more_stuff.worldgen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.ModEntities;
import net.kitawa.more_stuff.util.datagen.ModBiomeTagProvider;
import net.kitawa.more_stuff.util.tags.ModBiomeTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class ModBiomeModifiers {

    // --- Feature Keys ---
    public static final ResourceKey<BiomeModifier> ADD_NETHER_COPPER_ORE = registerKey("add_nether_copper_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_COPPER_ORE = registerKey("add_deltas_copper_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_IRON_ORE = registerKey("add_nether_iron_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_IRON_ORE = registerKey("add_deltas_iron_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_EXPERIENCE_ORE = registerKey("add_nether_experience_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_EXPERIENCE_ORE = registerKey("add_deltas_experience_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_ZINC_ORE = registerKey("add_nether_zinc_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_ZINC_ORE = registerKey("add_deltas_zinc_ore");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_TIN_ORE = registerKey("add_nether_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS_TIN_ORE = registerKey("add_deltas_tin_ore");
    public static final ResourceKey<BiomeModifier> ADD_DELTAS = registerKey("add_deltas");
    public static final ResourceKey<BiomeModifier> ADD_BLAZING_REEDS = registerKey("add_blazing_reeds");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_MONSTER_ROOM = registerKey("add_nether_monster_room");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_VAULT = registerKey("add_nether_vault");
    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_VAULT = registerKey("add_overworld_vault");
    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_EXPOSED_BLOCK_CEILING = registerKey("add_overworld_exposed_block_ceiling");
    public static final ResourceKey<BiomeModifier> ADD_OVERWORLD_EXPOSED_BLOCK_FLOOR = registerKey("add_overworld_exposed_block_floor");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_EXPOSED_BLOCK_CEILING = registerKey("add_nether_exposed_block_ceiling");
    public static final ResourceKey<BiomeModifier> ADD_NETHER_EXPOSED_BLOCK_FLOOR = registerKey("add_nether_exposed_block_floor");

    // --- Remove Feature Keys ---
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_GRANITE_UPPER = registerKey("remove_ore_granite_upper");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_GRANITE_LOWER = registerKey("remove_ore_granite_lower");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_DIORITE_UPPER = registerKey("remove_ore_diorite_upper");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_DIORITE_LOWER = registerKey("remove_ore_diorite_lower");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_ANDESITE_UPPER = registerKey("remove_ore_andesite_upper");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_ANDESITE_LOWER = registerKey("remove_ore_andesite_lower");
    public static final ResourceKey<BiomeModifier> REMOVE_ORE_TUFF = registerKey("remove_ore_tuff");

    // --- Spawn Keys ---
    public static final ResourceKey<BiomeModifier> ADD_ZOMBIE_WOLF_SPAWNS = registerKey("add_zombie_wolf_spawns");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        // --- Features ---
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

        context.register(ADD_OVERWORLD_EXPOSED_BLOCK_FLOOR, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EXPOSED_BLOCK_FLOOR)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION));

        context.register(ADD_OVERWORLD_EXPOSED_BLOCK_CEILING, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EXPOSED_BLOCK_CEILING)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION));

        context.register(ADD_NETHER_EXPOSED_BLOCK_FLOOR, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EXPOSED_BLOCK_FLOOR)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION));

        context.register(ADD_NETHER_EXPOSED_BLOCK_CEILING, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_NETHER),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.EXPOSED_BLOCK_CEILING)),
                GenerationStep.Decoration.TOP_LAYER_MODIFICATION));

        HolderSet.Named<Biome> overworldBiomes = biomes.getOrThrow(BiomeTags.IS_OVERWORLD);

// --- Remove Features ---
        context.register(REMOVE_ORE_GRANITE_UPPER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_GRANITE_UPPER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_GRANITE_LOWER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_GRANITE_LOWER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_DIORITE_UPPER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_DIORITE_UPPER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_DIORITE_LOWER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_DIORITE_LOWER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_ANDESITE_UPPER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ANDESITE_UPPER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_ANDESITE_LOWER, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_ANDESITE_LOWER)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        context.register(REMOVE_ORE_TUFF, new BiomeModifiers.RemoveFeaturesBiomeModifier(
                overworldBiomes,
                HolderSet.direct(placedFeatures.getOrThrow(OrePlacements.ORE_TUFF)),
                EnumSet.allOf(GenerationStep.Decoration.class)));

        // --- Zombie Wolf Spawn ---
        MobSpawnSettings.SpawnerData zombieWolfSpawner = new MobSpawnSettings.SpawnerData(
                ModEntities.ZOMBIE_WOLF.get(), 10, 1, 3
        );

        context.register(ADD_ZOMBIE_WOLF_SPAWNS,
                new BiomeModifiers.AddSpawnsBiomeModifier(overworldBiomes, List.of(zombieWolfSpawner))
        );
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, name));
    }
}