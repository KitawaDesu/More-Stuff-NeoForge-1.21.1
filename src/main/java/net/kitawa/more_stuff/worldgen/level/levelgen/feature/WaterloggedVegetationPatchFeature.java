package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.WaterloggedVegetationPatchFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class WaterloggedVegetationPatchFeature extends Feature<WaterloggedVegetationPatchFeatureConfiguration> {
    public WaterloggedVegetationPatchFeature(Codec<WaterloggedVegetationPatchFeatureConfiguration> codec) {
        super(codec);
    }

    protected void distributeVegetation(
            FeaturePlaceContext<WaterloggedVegetationPatchFeatureConfiguration> context,
            WorldGenLevel level,
            WaterloggedVegetationPatchFeatureConfiguration config,
            RandomSource random,
            Set<BlockPos> possiblePositions,
            int xRadius,
            int zRadius
    ) {
        for (BlockPos blockpos : possiblePositions) {
            if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
                this.placeVegetation(level, config, context.chunkGenerator(), random, blockpos);
            }
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<WaterloggedVegetationPatchFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        WaterloggedVegetationPatchFeatureConfiguration config = context.config();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        // Replaceable check: allow water as well as the configured replaceable
        Predicate<BlockState> predicate = state ->
                state.is(config.replaceable) || state.is(Blocks.WATER);

        int i = config.xzRadius.sample(random) + 1;
        int j = config.xzRadius.sample(random) + 1;

        Set<BlockPos> set = this.placeGroundPatch(level, config, random, origin, predicate, i, j);
        this.distributeVegetation(context, level, config, random, set, i, j);

        return !set.isEmpty();
    }

    protected Set<BlockPos> placeGroundPatch(
            WorldGenLevel level,
            WaterloggedVegetationPatchFeatureConfiguration config, // use your config
            RandomSource random,
            BlockPos pos,
            Predicate<BlockState> replaceable,
            int xRadius,
            int zRadius
    ) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        BlockPos.MutableBlockPos supportPos = mutable.mutable();
        Direction surfaceDir = config.surface.getDirection();
        Direction oppositeDir = surfaceDir.getOpposite();
        Set<BlockPos> positions = new HashSet<>();

        for (int dx = -xRadius; dx <= xRadius; dx++) {
            boolean edgeX = dx == -xRadius || dx == xRadius;

            for (int dz = -zRadius; dz <= zRadius; dz++) {
                boolean edgeZ = dz == -zRadius || dz == zRadius;
                boolean border = edgeX || edgeZ;
                boolean corner = edgeX && edgeZ;
                boolean borderNoCorner = border && !corner;

                if (!corner && (!borderNoCorner || config.extraEdgeColumnChance == 0.0F || random.nextFloat() <= config.extraEdgeColumnChance)) {
                    mutable.setWithOffset(pos, dx, 0, dz);

                    // Instead of stopping at air, stop at water or air
                    for (int k = 0;
                         level.isStateAtPosition(mutable, s -> s.isAir() || s.is(Blocks.WATER)) && k < config.verticalRange;
                         k++) {
                        mutable.move(surfaceDir);
                    }

                    for (int k = 0;
                         level.isStateAtPosition(mutable, s -> !s.isAir() && !s.is(Blocks.WATER)) && k < config.verticalRange;
                         k++) {
                        mutable.move(oppositeDir);
                    }

                    supportPos.setWithOffset(mutable, surfaceDir);
                    BlockState supportState = level.getBlockState(supportPos);

                    if ((level.isEmptyBlock(mutable) || level.getBlockState(mutable).is(Blocks.WATER))
                            && supportState.isFaceSturdy(level, supportPos, surfaceDir.getOpposite())) {

                        int depth = config.depth.sample(random)
                                + ((config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance) ? 1 : 0);

                        BlockPos finalPos = supportPos.immutable();
                        boolean placed = this.placeGround(level, config, replaceable, random, supportPos, depth);

                        if (placed) {
                            positions.add(finalPos);
                        }
                    }
                }
            }
        }

        return positions;
    }

    protected void placeVegetation(
            WorldGenLevel level,
            WaterloggedVegetationPatchFeatureConfiguration config,
            ChunkGenerator chunkGenerator,
            RandomSource random,
            BlockPos pos
    ) {
        // Compute the actual placement position
        BlockPos placementPos = pos.relative(config.surface.getDirection().getOpposite());

        // Check fluid state at the actual placement position
        BlockState stateAtPos = level.getBlockState(placementPos);
        boolean underwater = stateAtPos.getFluidState().is(Fluids.WATER);

        if (underwater && config.underwaterVegetationFeature.isPresent()) {
            config.underwaterVegetationFeature.get().value()
                    .place(level, chunkGenerator, random, placementPos);
        } else {
            config.vegetationFeature.value()
                    .place(level, chunkGenerator, random, placementPos);
        }
    }

    protected boolean placeGround(
            WorldGenLevel level,
            WaterloggedVegetationPatchFeatureConfiguration config,
            Predicate<BlockState> replaceable,
            RandomSource random,
            BlockPos.MutableBlockPos mutablePos,
            int maxDistance
    ) {
        for (int i = 0; i < maxDistance; i++) {
            BlockState newState = config.groundState.getState(random, mutablePos);
            BlockState currentState = level.getBlockState(mutablePos);

            if (!newState.is(currentState.getBlock())) {
                if (!replaceable.test(currentState)) {
                    return i != 0;
                }

                // If block is waterloggable, set waterlogged true
                if (newState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
                }

                level.setBlock(mutablePos, newState, 2);
                mutablePos.move(config.surface.getDirection());
            }
        }
        return true;
    }
}