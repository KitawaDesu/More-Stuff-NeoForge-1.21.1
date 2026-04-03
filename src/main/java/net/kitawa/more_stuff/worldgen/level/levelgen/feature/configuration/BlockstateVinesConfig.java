package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

public class BlockstateVinesConfig implements FeatureConfiguration {

    public static final Codec<BlockstateVinesConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_INT.fieldOf("spread_width")
                            .forGetter(cfg -> cfg.spreadWidth),
                    ExtraCodecs.POSITIVE_INT.fieldOf("spread_height")
                            .forGetter(cfg -> cfg.spreadHeight),
                    ExtraCodecs.POSITIVE_INT.fieldOf("max_height")
                            .forGetter(cfg -> cfg.maxHeight),
                    BlockStateProvider.CODEC.fieldOf("head_block")
                            .forGetter(cfg -> cfg.headBlock),
                    BlockStateProvider.CODEC.fieldOf("body_block")
                            .forGetter(cfg -> cfg.bodyBlock),
                    BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("place_on_blocks")
                            .forGetter(cfg -> cfg.placeOnBlocks)
            ).apply(instance, BlockstateVinesConfig::new)
    );

    public final int spreadWidth;
    public final int spreadHeight;
    public final int maxHeight;
    public final BlockStateProvider headBlock;
    public final BlockStateProvider bodyBlock;
    public final List<Block> placeOnBlocks;

    public BlockstateVinesConfig(int spreadWidth, int spreadHeight, int maxHeight,
                                 BlockStateProvider headBlock, BlockStateProvider bodyBlock,
                                 List<Block> placeOnBlocks) {
        this.spreadWidth = spreadWidth;
        this.spreadHeight = spreadHeight;
        this.maxHeight = maxHeight;
        this.headBlock = headBlock;
        this.bodyBlock = bodyBlock;
        this.placeOnBlocks = placeOnBlocks;
    }

    public int spreadWidth()          { return spreadWidth; }
    public int spreadHeight()         { return spreadHeight; }
    public int maxHeight()            { return maxHeight; }
    public BlockStateProvider headBlock()  { return headBlock; }
    public BlockStateProvider bodyBlock()  { return bodyBlock; }
    public List<Block> placeOnBlocks() { return placeOnBlocks; }
}