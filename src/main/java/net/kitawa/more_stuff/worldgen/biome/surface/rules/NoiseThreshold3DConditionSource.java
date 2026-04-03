package net.kitawa.more_stuff.worldgen.biome.surface.rules;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.Objects;

public class NoiseThreshold3DConditionSource implements SurfaceRules.ConditionSource {

    public static final KeyDispatchDataCodec<NoiseThreshold3DConditionSource> CODEC = KeyDispatchDataCodec.of(
            RecordCodecBuilder.mapCodec(
                    instance -> instance.group(
                            ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseThreshold3DConditionSource::noise),
                            Codec.DOUBLE.fieldOf("min_threshold").forGetter(NoiseThreshold3DConditionSource::minThreshold),
                            Codec.DOUBLE.fieldOf("max_threshold").forGetter(NoiseThreshold3DConditionSource::maxThreshold)
                    ).apply(instance, NoiseThreshold3DConditionSource::new)
            )
    );

    private final ResourceKey<NormalNoise.NoiseParameters> noise;
    private final double minThreshold;
    private final double maxThreshold;

    public NoiseThreshold3DConditionSource(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold) {
        this.noise = noise;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    public ResourceKey<NormalNoise.NoiseParameters> noise() {
        return noise;
    }

    public double minThreshold() {
        return minThreshold;
    }

    public double maxThreshold() {
        return maxThreshold;
    }

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(final SurfaceRules.Context context) {
        final NormalNoise normalNoise = context.randomState.getOrCreateNoise(this.noise);

        class NoiseThreshold3DCondition extends SurfaceRules.LazyYCondition {

            // Capture context in a local so it's available before super() finishes
            private final SurfaceRules.Context ctx;

            NoiseThreshold3DCondition(SurfaceRules.Context ctx) {
                super(ctx);
                this.ctx = ctx;
            }

            @Override
            protected boolean compute() {
                double value = normalNoise.getValue(
                        this.ctx.blockX,
                        this.ctx.blockY,
                        this.ctx.blockZ
                );
                return value >= NoiseThreshold3DConditionSource.this.minThreshold
                        && value <= NoiseThreshold3DConditionSource.this.maxThreshold;
            }
        }

        return new NoiseThreshold3DCondition(context);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof NoiseThreshold3DConditionSource that)) return false;
        return Double.compare(that.minThreshold, minThreshold) == 0
                && Double.compare(that.maxThreshold, maxThreshold) == 0
                && noise.equals(that.noise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noise, minThreshold, maxThreshold);
    }

    @Override
    public String toString() {
        return "NoiseThreshold3DConditionSource[noise=" + noise + ", minThreshold=" + minThreshold + ", maxThreshold=" + maxThreshold + "]";
    }
}