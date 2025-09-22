package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class TeslaPatchConfig implements FeatureConfiguration {

    public static final Codec<TeslaPatchConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("side_anchor_chance").forGetter(cfg -> cfg.sideAnchorChance),
            Codec.FLOAT.fieldOf("stormvein_chance").forGetter(cfg -> cfg.stormveinChance)
    ).apply(instance, TeslaPatchConfig::new));

    /** Chance to spawn an anchor block on each side */
    public final float sideAnchorChance;

    /** Chance to spawn a Stormvein on top of a side anchor */
    public final float stormveinChance;

    public TeslaPatchConfig(float sideAnchorChance, float stormveinChance) {
        this.sideAnchorChance = sideAnchorChance;
        this.stormveinChance = stormveinChance;
    }
}

