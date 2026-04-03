package net.kitawa.more_stuff.worldgen.biome;

import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import terrablender.api.EndBiomeRegistry;

public class ModEndBiomeRegistry {

    public static void addBiomes() {

        // Don't register anything if End Update is disabled
        if (!ExperimentalUpdatesConfig.isEndUpdateAllowed) return;

        // --- Phantasmic Biomes ---
        if (ExperimentalUpdatesConfig.isPhantasmicBiomesAllowed) {
            EndBiomeRegistry.registerHighlandsBiome(ModBiomes.PHANTASMIC_HIGHLANDS, 4);
            EndBiomeRegistry.registerMidlandsBiome(ModBiomes.PHANTASMIC_MIDLANDS, 4);
            EndBiomeRegistry.registerEdgeBiome(ModBiomes.PHANTASMIC_BARRENS, 4);
            EndBiomeRegistry.registerEdgeBiome(ModBiomes.PHANTASMIC_ISLANDS, 4);
            EndBiomeRegistry.registerIslandBiome(ModBiomes.PHANTASMIC_ISLANDS, 4);
        }

        // --- Hybernatus Biomes ---
        if (ExperimentalUpdatesConfig.isHybernatusBiomesAllowed) {
            EndBiomeRegistry.registerHighlandsBiome(ModBiomes.HYBERNATUS_HIGHLANDS, 8);
            EndBiomeRegistry.registerHighlandsBiome(ModBiomes.HYBERNATUS_MIDLANDS, 8);
            EndBiomeRegistry.registerEdgeBiome(ModBiomes.HYBERNATUS_BARRENS, 8);
            EndBiomeRegistry.registerEdgeBiome(ModBiomes.HYBERNATUS_ISLANDS, 8);
            EndBiomeRegistry.registerIslandBiome(ModBiomes.HYBERNATUS_ISLANDS, 8);
        }

        // Always allow vanilla small islands (optional, but recommended)
        EndBiomeRegistry.registerEdgeBiome(Biomes.SMALL_END_ISLANDS, 10);
        EndBiomeRegistry.registerIslandBiome(Biomes.SMALL_END_ISLANDS, 10);
    }
}