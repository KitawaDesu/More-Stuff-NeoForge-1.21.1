package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.utils.ConditionalBlockStateProvider;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class NetherBambooFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<NetherBambooFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(ConditionalBlockStateProvider.CODEC).fieldOf("ground_blocks").forGetter(cfg -> cfg.groundBlocks),
                    Codec.list(ConditionalBlockStateProvider.CODEC).fieldOf("shoot_blocks").forGetter(cfg -> cfg.shootBlocks),
                    RuleTest.CODEC.fieldOf("ground_can_replace").forGetter(cfg -> cfg.groundCanReplace),
                    Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(cfg -> cfg.probability),
                    Codec.BOOL.optionalFieldOf("debug", false).forGetter(cfg -> cfg.debug)
            ).apply(instance, NetherBambooFeatureConfiguration::new)
    );

    public final List<ConditionalBlockStateProvider> groundBlocks;
    public final List<ConditionalBlockStateProvider> shootBlocks;
    public final RuleTest groundCanReplace;
    public final float probability;
    public final boolean debug;

    public NetherBambooFeatureConfiguration(
            List<ConditionalBlockStateProvider> groundBlocks,
            List<ConditionalBlockStateProvider> shootBlocks,
            RuleTest groundCanReplace,
            float probability,
            boolean debug
    ) {
        this.groundBlocks = groundBlocks;
        this.shootBlocks = shootBlocks;
        this.groundCanReplace = groundCanReplace;
        this.probability = probability;
        this.debug = debug;
    }

    public BlockState pickGroundBlock(RandomSource random, BlockPos pos) {
        return pickRandomBlock(random, pos, groundBlocks, debug);
    }

    public BlockState pickShootBlock(RandomSource random, BlockPos pos) {
        return pickRandomBlock(random, pos, shootBlocks, debug);
    }

    private static BlockState pickRandomBlock(RandomSource random, BlockPos pos,
                                              List<ConditionalBlockStateProvider> providers,
                                              boolean debug) {

        List<BlockState> valid = providers.stream()
                .map(p -> p.getState(random, pos, debug))
                .filter(Objects::nonNull)
                .toList();

        if (valid.isEmpty()) return null;
        return valid.get(random.nextInt(valid.size()));
    }
}
