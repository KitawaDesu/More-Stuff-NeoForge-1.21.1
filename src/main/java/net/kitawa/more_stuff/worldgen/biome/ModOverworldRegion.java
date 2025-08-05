package net.kitawa.more_stuff.worldgen.biome;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModOverworldRegion extends Region {

    public ModOverworldRegion(ResourceLocation name, int weight)
    {
        super(name, RegionType.OVERWORLD, weight);
    }

    public void addBiomes(Registry<Biome> registry, Consumer<com.mojang.datafixers.util.Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper)
    {
        this.addModifiedVanillaOverworldBiomes(mapper, builder -> {
            builder.replaceBiome(Biomes.FROZEN_OCEAN, ModBiomes.FROZEN_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.DEEP_FROZEN_OCEAN, ModBiomes.FROZEN_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.COLD_OCEAN, ModBiomes.COLD_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.DEEP_COLD_OCEAN, ModBiomes.COLD_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.OCEAN, ModBiomes.AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.DEEP_OCEAN, ModBiomes.AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.LUKEWARM_OCEAN, ModBiomes.LUKEWARM_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.DEEP_LUKEWARM_OCEAN, ModBiomes.LUKEWARM_AQUAPURANDA_FOREST);
            builder.replaceBiome(Biomes.WARM_OCEAN, ModBiomes.WARM_AQUAPURANDA_FOREST);
        });
    }
}
