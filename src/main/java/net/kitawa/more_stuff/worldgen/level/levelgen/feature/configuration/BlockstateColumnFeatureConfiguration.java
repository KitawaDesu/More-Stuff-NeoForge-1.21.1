package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public class BlockstateColumnFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<BlockstateColumnFeatureConfiguration> CODEC =
            RecordCodecBuilder.create(instance ->
                    instance.group(
                            IntProvider.codec(0, 3)
                                    .fieldOf("reach")
                                    .forGetter(cfg -> cfg.reach),

                            IntProvider.codec(1, 10)
                                    .fieldOf("height")
                                    .forGetter(cfg -> cfg.height),

                            // Optional blockstate provider
                            BlockStateProvider.CODEC
                                    .optionalFieldOf("block_provider")
                                    .forGetter(cfg -> cfg.blockProvider),

                            // Optional tag that prevents column growth
                            TagKey.codec(Registries.BLOCK)
                                    .optionalFieldOf("prevent_growth_on")
                                    .forGetter(cfg -> cfg.preventGrowthOn)
                    ).apply(instance, BlockstateColumnFeatureConfiguration::new)
            );

    private final IntProvider reach;
    private final IntProvider height;
    private final Optional<BlockStateProvider> blockProvider;
    private final Optional<TagKey<Block>> preventGrowthOn;

    public BlockstateColumnFeatureConfiguration(
            IntProvider reach,
            IntProvider height,
            Optional<BlockStateProvider> blockProvider,
            Optional<TagKey<Block>> preventGrowthOn
    ) {
        this.reach = reach;
        this.height = height;
        this.blockProvider = blockProvider;
        this.preventGrowthOn = preventGrowthOn;
    }

    public IntProvider reach() {
        return reach;
    }

    public IntProvider height() {
        return height;
    }

    public Optional<BlockStateProvider> blockProvider() {
        return blockProvider;
    }

    public Optional<TagKey<Block>> preventGrowthOn() {
        return preventGrowthOn;
    }
}
