package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.AltPlacementConfig;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.ColumnClustersConfig;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.SidePlacementConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import java.util.*;

public class ColumnClustersFeature extends Feature<ColumnClustersConfig> {

    public ColumnClustersFeature(Codec<ColumnClustersConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ColumnClustersConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        ColumnClustersConfig config = context.config();

        int count = config.pillarCount.sample(random);
        int[][] dirs = getSpreadDirs(config.direction);

        Set<BlockPos> occupied = new HashSet<>();
        Queue<BlockPos> frontier = new ArrayDeque<>();

        BlockPos start = findGround(level, origin, config.direction);
        occupied.add(start);
        frontier.add(start);

        // --------------------------
        // Place alt replacements first
        // --------------------------
        Set<BlockPos> altPositions = new HashSet<>();
        placeAltReplacements(level, random, config, altPositions, origin);

        // --------------------------
        // Place main cluster
        // --------------------------
        placePillar(level, random, start, 0f, config, altPositions);
        int placed = 1;

        while (placed < count && !frontier.isEmpty()) {
            List<BlockPos> frontierList = new ArrayList<>(frontier);
            BlockPos current = frontierList.get(random.nextInt(frontierList.size()));
            frontier.remove(current);

            List<int[]> shuffledDirs = new ArrayList<>(List.of(dirs));
            Collections.shuffle(shuffledDirs, new java.util.Random(random.nextLong()));

            for (int[] d : shuffledDirs) {
                if (placed >= count) break;

                BlockPos neighbour = findGround(level,
                        current.offset(d[0], d[1], d[2]),
                        config.direction);

                if (occupied.contains(neighbour)) continue;
                if (!level.getBlockState(neighbour.relative(config.direction.getOpposite())).isSolid()) continue;

                occupied.add(neighbour);
                frontier.add(neighbour);

                double dist = Math.sqrt(
                        Math.pow(neighbour.getX() - origin.getX(), 2) +
                                Math.pow(neighbour.getY() - origin.getY(), 2) +
                                Math.pow(neighbour.getZ() - origin.getZ(), 2)
                );
                double approxMaxDist = Math.sqrt(count / Math.PI);
                float t = (float) Mth.clamp(dist / approxMaxDist, 0.0, 1.0);

                placePillar(level, random, neighbour, t, config, altPositions);
                placed++;
            }
        }

        placeSideCrystals(level, random, config, occupied, altPositions);
        return true;
    }

    // ---------------------------
    // GROUND FINDING
    // ---------------------------

    private BlockPos findGround(WorldGenLevel level, BlockPos pos, Direction direction) {
        Direction opposite = direction.getOpposite();
        BlockPos.MutableBlockPos mut = pos.mutable();

        while (mut.getY() > level.getMinBuildHeight() &&
                mut.getY() < level.getMaxBuildHeight() &&
                isSolidIgnoringTrees(level, mut)) {
            mut.move(direction);
        }

        while (mut.getY() > level.getMinBuildHeight() &&
                mut.getY() < level.getMaxBuildHeight() &&
                !isSolidIgnoringTrees(level, mut.relative(opposite))) {
            mut.move(opposite);
        }

        return mut.immutable();
    }

    private boolean isSolidIgnoringTrees(WorldGenLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.is(BlockTags.LEAVES) && !state.is(BlockTags.LOGS) && state.isSolid();
    }

    // ---------------------------
    // PILLAR PLACEMENT
    // ---------------------------

    private void placePillar(WorldGenLevel level, RandomSource random,
                             BlockPos base, float t, ColumnClustersConfig config,
                             Set<BlockPos> altPositions) {
        float jittered = Mth.clamp(t + (random.nextFloat() - 0.5f) * 0.25f, 0f, 1f);
        int height = sampleBlended(config.innerHeights, config.outerHeights, jittered, random);

        for (int dy = 0; dy < height; dy++) {
            BlockPos pos = base.relative(config.direction, dy);

            if (!level.getBlockState(pos).canBeReplaced()) break;

            // Randomly replace with alt placement
            BlockState finalState = config.block;
            for (AltPlacementConfig alt : config.altPlacements) {
                if (random.nextFloat() <= alt.chance) {
                    finalState = applyWaterlogging(level, pos, alt.blockState);
                    altPositions.add(pos);
                    break; // only one alt per block
                }
            }

            this.setBlock(level, pos, finalState);
        }
    }

    private int sampleBlended(List<Integer> inner, List<Integer> outer, float t, RandomSource random) {
        List<Integer> source = random.nextFloat() > t ? inner : outer;
        if (source.isEmpty()) return 1;
        return source.get(random.nextInt(source.size()));
    }

    // ---------------------------
    // SIDE CRYSTALS
    // ---------------------------

    private void placeSideCrystals(WorldGenLevel level, RandomSource random,
                                   ColumnClustersConfig config, Set<BlockPos> occupied,
                                   Set<BlockPos> altPositions) {
        List<Direction> sides = Arrays.stream(Direction.values())
                .filter(d -> d.getAxis() != config.direction.getAxis())
                .toList();

        for (BlockPos pillarPos : occupied) {
            for (Direction dir : sides) {
                BlockPos sidePos = pillarPos.relative(dir);
                if (!level.getBlockState(sidePos).canBeReplaced()) continue;
                if (occupied.contains(sidePos)) continue;

                for (SidePlacementConfig placement : config.sidePlacements) {
                    if (placement.restricted && !altPositions.contains(pillarPos)) continue;
                    if (random.nextFloat() > placement.chance) continue;

                    BlockState state = placement.blockState;

                    if (state.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                        state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, dir);
                    } else if (state.hasProperty(BlockStateProperties.FACING)) {
                        state = state.setValue(BlockStateProperties.FACING, dir);
                    }

                    state = applyWaterlogging(level, sidePos, state);
                    this.setBlock(level, sidePos, state);
                    break; // only one side block per position
                }
            }
        }
    }

    // ---------------------------
    // ALT REPLACEMENTS
    // ---------------------------

    private void placeAltReplacements(WorldGenLevel level, RandomSource random,
                                      ColumnClustersConfig config, Set<BlockPos> altPositions,
                                      BlockPos origin) { // accept origin
        if (config.altPlacements.isEmpty()) return;

        // Use the actual feature origin, not BlockPos.ZERO
        for (AltPlacementConfig alt : config.altPlacements) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = 0; dy <= 1; dy++) {
                        BlockPos pos = origin.offset(dx, dy, dz);
                        if (random.nextFloat() <= alt.chance && level.getBlockState(pos).canBeReplaced()) {
                            BlockState state = applyWaterlogging(level, pos, alt.blockState);
                            this.setBlock(level, pos, state);
                            altPositions.add(pos);
                        }
                    }
                }
            }
        }
    }

    // ---------------------------
    // HELPERS
    // ---------------------------

    private int[][] getSpreadDirs(Direction direction) {
        return switch (direction.getAxis()) {
            case Y -> new int[][]{{1,0,0},{-1,0,0},{0,0,1},{0,0,-1}};
            case X -> new int[][]{{0,1,0},{0,-1,0},{0,0,1},{0,0,-1}};
            case Z -> new int[][]{{1,0,0},{-1,0,0},{0,1,0},{0,-1,0}};
        };
    }

    private BlockState applyWaterlogging(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            boolean isWater = level.getFluidState(pos).getType() == Fluids.WATER;
            return state.setValue(BlockStateProperties.WATERLOGGED, isWater);
        }
        return state;
    }
}