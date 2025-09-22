package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class KelpColumnFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<KelpColumnFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("tip").forGetter(cfg -> cfg.tip),
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("body").forGetter(cfg -> cfg.body),
                    Codec.intRange(1, 64).fieldOf("max_height").forGetter(cfg -> cfg.maxHeight),
                    Codec.BOOL.optionalFieldOf("use_ocean_floor_heightmap", true).forGetter(cfg -> cfg.useOceanFloorHeightmap)
            ).apply(instance, KelpColumnFeatureConfiguration::new)
    );

    public final Block tip;
    public final Block body;
    public final int maxHeight;
    public final boolean useOceanFloorHeightmap;

    public KelpColumnFeatureConfiguration(Block tip, Block body, int maxHeight, boolean useOceanFloorHeightmap) {
        this.tip = tip;
        this.body = body;
        this.maxHeight = maxHeight;
        this.useOceanFloorHeightmap = useOceanFloorHeightmap;
    }

    public void validate() {
        if (!tip.defaultBlockState().is(ModBlockTags.KELP_LIKE)) {
            throw new IllegalArgumentException("Tip block must be in tag #more_stuff:kelp_like (" + tip + ")");
        }
        if (!body.defaultBlockState().is(ModBlockTags.KELP_PLANT_LIKE)) {
            throw new IllegalArgumentException("Body block must be in tag #more_stuff:kelp_plant_like (" + body + ")");
        }
        if (maxHeight <= 0) {
            throw new IllegalArgumentException("Max height must be positive");
        }
    }

    public BlockState getTipState() {
        return tip.defaultBlockState();
    }

    public BlockState getBodyState() {
        return body.defaultBlockState();
    }
}
