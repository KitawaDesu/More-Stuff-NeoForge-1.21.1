package net.kitawa.more_stuff.blocks.custom.general;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.kitawa.more_stuff.worldgen.ModConfiguredFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public class ModdedMossBlock extends Block implements BonemealableBlock {

    private final ResourceKey<ConfiguredFeature<?, ?>> defaultFeature;

    private final ResourceKey<ConfiguredFeature<?, ?>> underwaterFeature;

    public ModdedMossBlock(ResourceKey<ConfiguredFeature<?, ?>> defaultFeature, ResourceKey<ConfiguredFeature<?, ?>> underwaterFeature, Properties properties) {
        super(properties);
        this.defaultFeature = defaultFeature;
        this.underwaterFeature = underwaterFeature;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return level.getBlockState(pos.above()).is(ModBlockTags.MOSS_CAN_GENERATE_UNDER);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos abovePos = pos.above(3);
        BlockState aboveState = level.getBlockState(abovePos);

        ResourceKey<ConfiguredFeature<?, ?>> featureToUse;

        if (aboveState.is(Blocks.WATER)) {
            // Choose underwater feature
            featureToUse = this.underwaterFeature;
        } else {
            // Choose default/air feature
            featureToUse = this.defaultFeature;
        }

        level.registryAccess()
                .registry(Registries.CONFIGURED_FEATURE)
                .flatMap(registry -> registry.getHolder(featureToUse))
                .ifPresent(featureHolder -> featureHolder.value().place(level, level.getChunkSource().getGenerator(), random, abovePos));
    }

    @Override
    public BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }
}
