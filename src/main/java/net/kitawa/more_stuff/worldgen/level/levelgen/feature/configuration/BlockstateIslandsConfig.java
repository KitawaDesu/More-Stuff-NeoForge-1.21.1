package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;

public class BlockstateIslandsConfig implements FeatureConfiguration {

    public static final Codec<BlockstateIslandsConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Mode.CODEC.fieldOf("mode")
                            .forGetter(cfg -> cfg.mode),
                    BlockState.CODEC.optionalFieldOf("singular_state", Blocks.END_STONE.defaultBlockState())
                            .forGetter(cfg -> cfg.singularState),
                    LayeredEntry.CODEC.listOf().optionalFieldOf("layered_states", List.of())
                            .forGetter(cfg -> cfg.layeredStates)
            ).apply(instance, BlockstateIslandsConfig::new)
    );

    public final Mode mode;
    public final BlockState singularState;
    public final List<LayeredEntry> layeredStates;

    public BlockstateIslandsConfig(
            Mode mode,
            BlockState singularState,
            List<LayeredEntry> layeredStates
    ) {
        this.mode = mode;
        this.singularState = singularState;
        this.layeredStates = layeredStates;
    }

    public enum Mode implements StringRepresentable {
        SINGULAR("singular"),
        LAYERED("layered");

        private final String name;
        public static final Codec<Mode> CODEC = StringRepresentable.fromEnum(Mode::values);

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public static class LayeredEntry {

        public static final Codec<LayeredEntry> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockState.CODEC.fieldOf("state")
                                .forGetter(e -> e.state),
                        Codec.intRange(0, 64).optionalFieldOf("layer_range", 0)
                                .forGetter(e -> e.layerRange)
                ).apply(instance, LayeredEntry::new)
        );

        public final BlockState state;
        public final int layerRange;

        public LayeredEntry(BlockState state, int layerRange) {
            this.state = state;
            this.layerRange = layerRange;
        }
    }
}