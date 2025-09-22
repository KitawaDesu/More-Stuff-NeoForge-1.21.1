package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public record StormveinConfiguration(
        BlockStateProvider blockProvider,
        BlockStateProvider anchorProvider,
        StartMode startMode,
        RuleTest anchorTarget,
        int minLength,
        int maxLength,
        float extraAnchorChance,
        RuleTest extraAnchorsCanReplace // NEW
) implements FeatureConfiguration {

    public static final Codec<StormveinConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("block_provider").forGetter(StormveinConfiguration::blockProvider),
            BlockStateProvider.CODEC.fieldOf("anchor_provider").forGetter(StormveinConfiguration::anchorProvider),
            StartMode.CODEC.fieldOf("start_mode").forGetter(StormveinConfiguration::startMode),
            RuleTest.CODEC.fieldOf("anchor_target").forGetter(StormveinConfiguration::anchorTarget),
            Codec.INT.fieldOf("min_length").forGetter(StormveinConfiguration::minLength),
            Codec.INT.fieldOf("max_length").forGetter(StormveinConfiguration::maxLength),
            Codec.FLOAT.optionalFieldOf("extra_anchor_chance", 0f) // optional with default
                    .forGetter(StormveinConfiguration::extraAnchorChance),
            RuleTest.CODEC.optionalFieldOf("extra_anchors_can_replace", AlwaysTrueTest.INSTANCE)
                    .forGetter(StormveinConfiguration::extraAnchorsCanReplace)
    ).apply(instance, StormveinConfiguration::new));

    public enum StartMode implements StringRepresentable {
        FLOOR("floor", Direction.UP),
        CEILING("ceiling", Direction.DOWN),
        NORTH("north", Direction.SOUTH),
        SOUTH("south", Direction.NORTH),
        EAST("east", Direction.WEST),
        WEST("west", Direction.EAST);

        private final String name;
        private final Direction dir;

        StartMode(String name, Direction dir) {
            this.name = name;
            this.dir = dir;
        }

        public Direction direction() {
            return dir;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        public static final Codec<StartMode> CODEC = StringRepresentable.fromEnum(StartMode::values);
    }
}