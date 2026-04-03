package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public class SidePlacementConfig {
    public final BlockState blockState;
    public final float chance;   // 0.0 to 1.0
    public final boolean restricted; // new field

    public SidePlacementConfig(BlockState blockState, float chance, boolean restricted) {
        this.blockState = blockState;
        this.chance = chance;
        this.restricted = restricted;
    }

    public static final Codec<SidePlacementConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("block_state").forGetter(cfg -> cfg.blockState),
                    Codec.FLOAT.fieldOf("chance").forGetter(cfg -> cfg.chance),
                    Codec.BOOL.optionalFieldOf("restricted", false).forGetter(cfg -> cfg.restricted) // new field
            ).apply(instance, SidePlacementConfig::new)
    );
}
