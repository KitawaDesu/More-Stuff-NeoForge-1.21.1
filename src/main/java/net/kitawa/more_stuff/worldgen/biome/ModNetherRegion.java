package net.kitawa.more_stuff.worldgen.biome;

import net.minecraft.core.Registry;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

public class ModNetherRegion extends Region {

    public ModNetherRegion(ResourceLocation name, int weight) {
        super(name, RegionType.NETHER, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        this.addBiome(mapper, Climate.parameters(0f, 0f, 0f, 0f, 0f, 0.15f, 0f), ModBiomes.PYROLIZED_WASTES);
        this.addBiome(mapper, Climate.parameters(0f, 0.5f, 0f, 0f, 0f, 0.15f, 0.375f), ModBiomes.BLAZING_FOREST);
        this.addBiome(mapper, Climate.parameters(0.4f, 0f, 0f, 0f, 0f, 0.15f, 0f), ModBiomes.BLAZING_FOREST);
        this.addBiome(mapper, Climate.parameters(-0.5f, 0f, 0f, 0f, 0f, 0.15f, 0.175f), Biomes.BASALT_DELTAS);
        this.addBiome(mapper, Climate.parameters(0f, 0f, 0f, 0.25f, 0f, 0.15f, 0f), Biomes.NETHER_WASTES);
        this.addBiome(mapper, Climate.parameters(0f, -0.5f, 0f, 0.25f, 0f, 0.15f, 0f), ModBiomes.FROZEN_VALLEY);
        this.addBiome(mapper, Climate.parameters(0f, 0.5f, 0f, 0.25f, 0f, 0.15f, 0.375f), ModBiomes.PYROLIZED_FOREST);
        this.addBiome(mapper, Climate.parameters(0.4f, 0f, 0f, 0.25f, 0f, 0.15f, 0f), ModBiomes.PYROLIZED_FOREST);
        this.addBiome(mapper, Climate.parameters(0.4f, 0f, 0f, 0.25f, 0f, 0f, 0.375f), ModBiomes.METALLIC_FOREST);
        this.addBiome(mapper, Climate.parameters(0f, 0.5f, 0f, 0.25f, 0f, 0f, 0f), ModBiomes.METALLIC_FOREST);
        this.addBiome(mapper, Climate.parameters(-0.5f, 0f, 0f, 0.25f, 0f, 0f, 0.175f), Biomes.BASALT_DELTAS);
        this.addBiome(mapper, Climate.parameters(0f, 0f, 0f, 0.25f, 0f, 0.15f, 0f), ModBiomes.PYROLIZED_WASTES);
        this.addBiome(mapper, Climate.parameters(-0.5f, 0f, 0f, 0.25f, 0f, 0.15f, 0.175f), Biomes.BASALT_DELTAS);
    }
}
