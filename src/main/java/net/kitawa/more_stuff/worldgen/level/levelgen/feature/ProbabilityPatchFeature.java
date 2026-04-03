package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedBrushableBlockEntity;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.ProbabilityPatchFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ProbabilityPatchFeature extends Feature<ProbabilityPatchFeatureConfiguration> {

    public ProbabilityPatchFeature(Codec<ProbabilityPatchFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ProbabilityPatchFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        ProbabilityPatchFeatureConfiguration config = context.config();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        int xRadius = config.xzRadius.sample(random);
        int zRadius = config.xzRadius.sample(random);

        Predicate<BlockState> replaceable = state -> {
            if (state.is(Blocks.WATER)) return true;
            for (ProbabilityPatchFeatureConfiguration.TargetBlockState target : config.targetStates) {
                if (target.target.test(state, random)) {
                    return true;
                }
            }
            return false;
        };

        Set<BlockPos> placedPositions = this.placeGroundPatch(
                level, config, random, origin, replaceable, xRadius, zRadius
        );

        this.distributeProbabilityEffects(level, config, random, placedPositions);

        return !placedPositions.isEmpty();
    }

    protected void distributeProbabilityEffects(
            WorldGenLevel level,
            ProbabilityPatchFeatureConfiguration config,
            RandomSource random,
            Set<BlockPos> possiblePositions
    ) {
        if (config.lootTable == null) return;

        for (BlockPos pos : possiblePositions) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BrushableBlockEntity brushable) {
                brushable.setLootTable(config.lootTable, random.nextLong());
            } else if (be instanceof ModdedBrushableBlockEntity brushable) {
                brushable.setLootTable(config.lootTable, random.nextLong());
            }
        }
    }

    protected Set<BlockPos> placeGroundPatch(
            WorldGenLevel level,
            ProbabilityPatchFeatureConfiguration config,
            RandomSource random,
            BlockPos origin,
            Predicate<BlockState> replaceable,
            int xRadius,
            int zRadius
    ) {
        Set<BlockPos> placedPositions = new HashSet<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos supportPos = new BlockPos.MutableBlockPos();
        Direction surfaceDir = config.surface.getDirection();
        Direction oppositeDir = surfaceDir.getOpposite();

        for (int dx = -xRadius; dx <= xRadius; dx++) {
            boolean edgeX = dx == -xRadius || dx == xRadius;

            for (int dz = -zRadius; dz <= zRadius; dz++) {
                boolean edgeZ = dz == -zRadius || dz == zRadius;
                boolean border = edgeX || edgeZ;
                boolean corner = edgeX && edgeZ;
                boolean borderNoCorner = border && !corner;

                if (!corner && (!borderNoCorner || config.extraEdgeColumnChance == 0.0F || random.nextFloat() <= config.extraEdgeColumnChance)) {

                    mutable.setWithOffset(origin, dx, 0, dz);

                    double distance = Math.sqrt(dx * dx + dz * dz);
                    double maxDistance = Math.sqrt(xRadius * xRadius + zRadius * zRadius);
                    float distanceFactor = (float) (distance / maxDistance);

                    float columnChance = config.chance.sample(random);
                    columnChance = columnChance * distanceFactor + (1 - distanceFactor) * columnChance;

                    if (random.nextFloat() > columnChance) {
                        continue;
                    }

                    // ---------------------------------------------------------
                    // ⭐ FIX APPLIED HERE — fully RANDOM depth selection
                    // ---------------------------------------------------------
                    int randomDepth = random.nextInt(config.verticalRange);
                    mutable.move(surfaceDir, randomDepth);

                    // Check for support block
                    supportPos.setWithOffset(mutable, surfaceDir);
                    BlockState supportState = level.getBlockState(supportPos);

                    if (supportState.isFaceSturdy(level, supportPos, surfaceDir.getOpposite())) {

                        int depth = config.depth.sample(random)
                                + ((config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance) ? 1 : 0);

                        this.placeGround(
                                level, config, replaceable, random,
                                supportPos, depth, placedPositions
                        );
                    }
                }
            }
        }

        return placedPositions;
    }

    protected boolean placeGround(
            WorldGenLevel level,
            ProbabilityPatchFeatureConfiguration config,
            Predicate<BlockState> replaceable,
            RandomSource random,
            BlockPos.MutableBlockPos mutablePos,
            int maxDistance,
            Set<BlockPos> placedPositions
    ) {
        Direction dir = config.surface.getDirection();

        for (int i = 0; i < maxDistance; i++) {
            BlockState currentState = level.getBlockState(mutablePos);

            if (!replaceable.test(currentState)) {
                return i != 0;
            }

            float layerChance = config.chance.sample(random);
            if (random.nextFloat() > layerChance) {
                mutablePos.move(dir);
                continue;
            }

            for (ProbabilityPatchFeatureConfiguration.TargetBlockState target : config.targetStates) {
                if (target.target.test(currentState, random)) {
                    BlockState newState = target.state;

                    if (newState.hasProperty(BlockStateProperties.WATERLOGGED)
                            && currentState.getFluidState().is(Fluids.WATER)) {
                        newState = newState.setValue(BlockStateProperties.WATERLOGGED, true);
                    }

                    level.setBlock(mutablePos, newState, 2);

                    // ✅ FIX: Add ALL placed suspicious blocks to the set
                    placedPositions.add(mutablePos.immutable());

                    break;
                }
            }

            mutablePos.move(dir);
        }

        return true;
    }
}
