package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;


public class BendingMushroomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<BendingMushroomFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // vanilla mushroom config fields
                    BlockStateProvider.CODEC.fieldOf("cap_provider").forGetter(cfg -> cfg.capProvider),
                    BlockStateProvider.CODEC.fieldOf("stem_provider").forGetter(cfg -> cfg.stemProvider),
                    Codec.INT.fieldOf("foliage_radius").orElse(2).forGetter(cfg -> cfg.foliageRadius),

                    // bending-specific
                    Codec.INT.fieldOf("bend_start").orElse(2).forGetter(cfg -> cfg.bendStart),
                    Codec.INT.fieldOf("bend_frequency").orElse(2).forGetter(cfg -> cfg.bendFrequency),
                    Codec.INT.fieldOf("max_bend").orElse(3).forGetter(cfg -> cfg.maxBend),
                    Codec.INT.fieldOf("bend_chance").orElse(3).forGetter(cfg -> cfg.bendChance),
                    Codec.INT.fieldOf("bend_amount").orElse(1).forGetter(cfg -> cfg.bendAmount),
                    Codec.BOOL.fieldOf("random_bend_direction").orElse(true).forGetter(cfg -> cfg.randomBendDirection),
                    Direction.CODEC.optionalFieldOf("bend_direction").orElse(Optional.empty()).forGetter(cfg -> cfg.bendDirection)
            ).apply(instance, BendingMushroomFeatureConfiguration::new)
    );

    // base mushroom fields
    public final BlockStateProvider capProvider;
    public final BlockStateProvider stemProvider;
    public final int foliageRadius;

    // bending fields
    public final int bendStart;
    public final int bendFrequency;
    public final int maxBend;
    public final int bendChance;
    public final int bendAmount;
    public final boolean randomBendDirection;
    public final Optional<Direction> bendDirection;

    public BendingMushroomFeatureConfiguration(
            BlockStateProvider capProvider,
            BlockStateProvider stemProvider,
            int foliageRadius,
            int bendStart,
            int bendFrequency,
            int maxBend,
            int bendChance,
            int bendAmount,
            boolean randomBendDirection,
            Optional<Direction> bendDirection
    ) {
        this.capProvider = capProvider;
        this.stemProvider = stemProvider;
        this.foliageRadius = foliageRadius;

        this.bendStart = bendStart;
        this.bendFrequency = bendFrequency;
        this.maxBend = maxBend;
        this.bendChance = bendChance;
        this.bendAmount = bendAmount;
        this.randomBendDirection = randomBendDirection;
        this.bendDirection = bendDirection;
    }
}
