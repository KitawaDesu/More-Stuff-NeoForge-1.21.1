package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class ColumnClustersConfig implements FeatureConfiguration {

    public static final Codec<ColumnClustersConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("block").forGetter(cfg -> cfg.block),
                    IntProvider.codec(1, 128).fieldOf("pillar_count").forGetter(cfg -> cfg.pillarCount),
                    Codec.INT.listOf().fieldOf("outer_heights").forGetter(cfg -> cfg.outerHeights),
                    Codec.INT.listOf().fieldOf("inner_heights").forGetter(cfg -> cfg.innerHeights),
                    Direction.CODEC.optionalFieldOf("direction", Direction.UP).forGetter(cfg -> cfg.direction),
                    SidePlacementConfig.CODEC.listOf().fieldOf("side_placements").forGetter(cfg -> cfg.sidePlacements),
                    AltPlacementConfig.CODEC.listOf().optionalFieldOf("alt_placements", List.of()).forGetter(cfg -> cfg.altPlacements) // optional
            ).apply(instance, ColumnClustersConfig::new)
    );

    public final BlockState block;
    public final IntProvider pillarCount;
    public final List<Integer> outerHeights;
    public final List<Integer> innerHeights;
    public final Direction direction;
    public final List<SidePlacementConfig> sidePlacements;
    public final List<AltPlacementConfig> altPlacements; // now uses AltPlacementConfig

    public ColumnClustersConfig(BlockState block, IntProvider pillarCount,
                                List<Integer> outerHeights, List<Integer> innerHeights,
                                Direction direction,
                                List<SidePlacementConfig> sidePlacements,
                                List<AltPlacementConfig> altPlacements) {
        this.block = block;
        this.pillarCount = pillarCount;
        this.outerHeights = outerHeights;
        this.innerHeights = innerHeights;
        this.direction = direction;
        this.sidePlacements = sidePlacements;
        this.altPlacements = altPlacements != null ? altPlacements : List.of(); // default empty list
    }
}