package net.kitawa.more_stuff.worldgen.biomelayers;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import javax.annotation.Nullable;

public record MiddleSection(
        BlockState block,
        @Nullable ResourceKey<NormalNoise.NoiseParameters> noise,
        double threshold,
        double scale,
        int height
) {
    public boolean hasNoise() {
        return noise != null;
    }
}