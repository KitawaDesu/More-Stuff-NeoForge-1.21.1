package net.kitawa.more_stuff.worldgen.biome;

import com.mojang.datafixers.util.Pair;
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


        // === Replace Dripstone Caves with Frostbitten Caverns in cold temps ===
        Climate.Parameter coldTemps       = Climate.Parameter.span(-1.0F, -0.45F); // Only cold band
        Climate.Parameter caveDepth       = Climate.Parameter.span(0.2F, 0.9F);    // Matches dripstone depth
        Climate.Parameter caveContinental = Climate.Parameter.span(0.7F, 1.0F);    // Interior-ish
        Climate.Parameter caveErosion     = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter caveHumidity    = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter caveWeirdness   = Climate.Parameter.span(-1.0F, 1.0F);
        float offset                      = 0.0F;

        Climate.ParameterPoint frostCavernsPoint = new Climate.ParameterPoint(
                coldTemps, caveHumidity, caveContinental, caveErosion, caveDepth, caveWeirdness, (long) offset
        );
        mapper.accept(Pair.of(frostCavernsPoint, ModBiomes.FROSTBITTEN_CAVERNS));

        Climate.Parameter continentalness = Climate.Parameter.span(-1.0F, -0.8F);
        Climate.Parameter depth           = Climate.Parameter.span(0.4F, 1.1F);
        Climate.Parameter erosion         = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter humidity        = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter temperature     = Climate.Parameter.span(-0.45F, 1F);
        Climate.Parameter weirdness       = Climate.Parameter.span(-1.0F, 1.0F);

        Climate.ParameterPoint redstonicCavesPoint = new Climate.ParameterPoint(
                temperature,
                humidity,
                continentalness,
                erosion,
                depth,
                weirdness,
                (long) offset
        );

        mapper.accept(Pair.of(redstonicCavesPoint, ModBiomes.REDSTONIC_CAVES));


        Climate.Parameter fullTemps       = Climate.Parameter.span(-1.0F, 1F); // Only cold band
        Climate.Parameter DeepCaveDepth       = Climate.Parameter.span(0.3F, 1.0F);
        Climate.Parameter fullContinental = Climate.Parameter.span(-1.0F, 1.0F);    // Interior-ish
        Climate.Parameter fullErosion     = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter fullHumidity    = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter voltaicWeirdness   = Climate.Parameter.span(0.6F, 1.0F);

        Climate.ParameterPoint VoltaicHollowsPoint = new Climate.ParameterPoint(
                fullTemps, fullHumidity, fullContinental, fullErosion, DeepCaveDepth, voltaicWeirdness, (long) offset
        );
        mapper.accept(Pair.of(VoltaicHollowsPoint, ModBiomes.VOLTAIC_HOLLOWS));

 // Only cold band
        Climate.Parameter warmTemps       = Climate.Parameter.span(-0.15F, 1F);
        Climate.Parameter DeeperCaveDepth       = Climate.Parameter.span(0.4F, 1.1F);
        Climate.Parameter lushHumidity    = Climate.Parameter.span(-1.0F, -0.7F);
        Climate.Parameter nonvoltaicWeirdness   = Climate.Parameter.span(-1.0F, 0.6F);

        Climate.ParameterPoint fungalPoint = new Climate.ParameterPoint(
                warmTemps, lushHumidity, fullContinental, fullErosion, DeeperCaveDepth, nonvoltaicWeirdness, (long) offset
        );
        mapper.accept(Pair.of(fungalPoint, ModBiomes.FUNGAL_CAVES));
    }
}
