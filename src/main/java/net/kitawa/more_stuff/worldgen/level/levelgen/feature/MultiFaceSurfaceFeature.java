package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;

import java.util.List;

public class MultiFaceSurfaceFeature extends Feature<MultifaceGrowthConfiguration> {
    public MultiFaceSurfaceFeature(Codec<MultifaceGrowthConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        MultifaceGrowthConfiguration config = context.config();

        boolean placed = false;

        // Define a cubic volume around the origin to try placements
        int radius = config.searchRange * 2; // expand area
        BlockPos min = origin.offset(-radius, -radius, -radius);
        BlockPos max = origin.offset(radius, radius, radius);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (BlockPos target : BlockPos.betweenClosed(min, max)) {
            pos.set(target);
            BlockState stateAt = level.getBlockState(pos);

            // ✅ Only allow empty air, not water
            if (!stateAt.isAir()) {
                continue;
            }

            List<Direction> shuffled = config.getShuffledDirections(random);
            if (placeGrowthIfPossible(level, pos, config, random, shuffled)) {
                placed = true;
            }
        }

        return placed;
    }

    public static boolean placeGrowthIfPossible(
            WorldGenLevel level,
            BlockPos pos,
            MultifaceGrowthConfiguration config,
            RandomSource random,
            List<Direction> directions
    ) {
        BlockPos.MutableBlockPos mutable = pos.mutable();

        for (Direction dir : directions) {
            BlockState support = level.getBlockState(mutable.setWithOffset(pos, dir));

            // ✅ Special case: only allow water as a support when placing DOWN
            if (support.is(Blocks.WATER) && dir != Direction.DOWN) {
                continue;
            }

            if (support.is(config.canBePlacedOn) || support.is(Blocks.WATER)) {
                BlockState newState = config.placeBlock.getStateForPlacement(Blocks.AIR.defaultBlockState(), level, pos, dir);
                if (newState == null) {
                    continue;
                }

                level.setBlock(pos, newState, 3);
                level.getChunk(pos).markPosForPostprocessing(pos);

                if (random.nextFloat() < config.chanceOfSpreading) {
                    config.placeBlock.getSpreader().spreadFromFaceTowardRandomDirection(newState, level, pos, dir, random, true);
                }

                return true;
            }
        }

        return false;
    }
}
