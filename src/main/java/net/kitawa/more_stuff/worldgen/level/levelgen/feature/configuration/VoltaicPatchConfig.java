package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class VoltaicPatchConfig implements FeatureConfiguration {

    public static final Codec<VoltaicPatchConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("block").forGetter(cfg -> cfg.block),
            IntProvider.codec(1, 12).fieldOf("radius").forGetter(cfg -> cfg.radius),
            IntProvider.codec(1, 8).fieldOf("tendril_length").forGetter(cfg -> cfg.tendrilLength),
            IntProvider.codec(0, 6).fieldOf("tendril_count").forGetter(cfg -> cfg.tendrilCount),
            TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(cfg -> cfg.replaceable)
    ).apply(instance, VoltaicPatchConfig::new));

    /** The main block to place in the blob and tendrils */
    public final BlockStateProvider block;

    /** Radius of the initial blob core */
    public final IntProvider radius;

    /** Maximum length of the tendrils extending from the core */
    public final IntProvider tendrilLength;

    /** Number of tendrils to generate */
    public final IntProvider tendrilCount;

    /** Tag of blocks that this feature can replace or "crawl into" */
    public final TagKey<Block> replaceable;

    public VoltaicPatchConfig(BlockStateProvider block,
                              IntProvider radius,
                              IntProvider tendrilLength,
                              IntProvider tendrilCount,
                              TagKey<Block> replaceable) {
        this.block = block;
        this.radius = radius;
        this.tendrilLength = tendrilLength;
        this.tendrilCount = tendrilCount;
        this.replaceable = replaceable;
    }
}
