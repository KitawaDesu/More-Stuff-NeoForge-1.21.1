package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;

public class AltPlacementConfig {
    public final BlockState blockState;
    public final float chance; // 0.0 to 1.0

    public AltPlacementConfig(BlockState blockState, float chance) {
        this.blockState = blockState;
        this.chance = chance;
    }

    public static final Codec<AltPlacementConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BlockState.CODEC.fieldOf("block_state").forGetter(cfg -> cfg.blockState),
                    Codec.FLOAT.fieldOf("chance").forGetter(cfg -> cfg.chance)
            ).apply(instance, AltPlacementConfig::new)
    );
}