package net.kitawa.more_stuff.worldgen.biomelayers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public record NoiseBlockRule(
        BlockState block,
        ResourceKey<NormalNoise.NoiseParameters> noiseKey,
        double noiseThreshold,
        double noiseScale,
        boolean invert // new flag
) {
    public NoiseBlockRule(BlockState block, ResourceKey<NormalNoise.NoiseParameters> noiseKey, double noiseThreshold, double noiseScale) {
        this(block, noiseKey, noiseThreshold, noiseScale, false);
    }
}
