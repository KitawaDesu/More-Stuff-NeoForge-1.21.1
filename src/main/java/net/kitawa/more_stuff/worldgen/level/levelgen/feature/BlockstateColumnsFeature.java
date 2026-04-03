package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.BlockstateColumnFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import javax.annotation.Nullable;

public class BlockstateColumnsFeature extends Feature<BlockstateColumnFeatureConfiguration> {

    private static final ImmutableList<Block> CANNOT_PLACE_ON = ImmutableList.of(
            Blocks.LAVA,
            Blocks.BEDROCK,
            Blocks.MAGMA_BLOCK,
            Blocks.SOUL_SAND,
            Blocks.NETHER_BRICKS,
            Blocks.NETHER_BRICK_FENCE,
            Blocks.NETHER_BRICK_STAIRS,
            Blocks.NETHER_WART,
            Blocks.CHEST,
            Blocks.SPAWNER
    );

    public BlockstateColumnsFeature(Codec<BlockstateColumnFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockstateColumnFeatureConfiguration> context) {
        int seaLevel = context.chunkGenerator().getSeaLevel();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockstateColumnFeatureConfiguration cfg = context.config();

        if (!canPlaceAt(level, seaLevel, origin.mutable(), cfg)) return false;

        int height = cfg.height().sample(random);
        boolean clustered = random.nextFloat() < 0.9F;
        int reach = Math.min(height, clustered ? 5 : 8);
        int attempts = clustered ? 50 : 15;

        boolean placedAny = false;

        for (BlockPos pos : BlockPos.randomBetweenClosed(
                random,
                attempts,
                origin.getX() - reach,
                origin.getY(),
                origin.getZ() - reach,
                origin.getX() + reach,
                origin.getY(),
                origin.getZ() + reach
        )) {
            int dist = height - pos.distManhattan(origin);
            if (dist >= 0) {
                placedAny |= placeColumn(level, seaLevel, pos, dist, cfg.reach().sample(random), cfg, random);
            }
        }

        return placedAny;
    }

    private boolean placeColumn(
            LevelAccessor level,
            int seaLevel,
            BlockPos origin,
            int distance,
            int reach,
            BlockstateColumnFeatureConfiguration cfg,
            RandomSource random
    ) {
        boolean placed = false;

        for (BlockPos pos : BlockPos.betweenClosed(
                origin.getX() - reach, origin.getY(), origin.getZ() - reach,
                origin.getX() + reach, origin.getY(), origin.getZ() + reach
        )) {
            int manhattan = pos.distManhattan(origin);

            // Find the starting block position (air or water)
            BlockPos start = findSurfaceOrAir(level, seaLevel, pos.mutable(), manhattan);
            if (start == null) continue;

            // SAMPLE ONCE: get the block for the entire column
            BlockState columnState = cfg.blockProvider()
                    .map(provider -> provider.getState(random, start))
                    .orElse(Blocks.BASALT.defaultBlockState());

            // Place the **first block no matter what**
            level.setBlock(start, columnState, 2);
            placed = true;

            // Check prevent-growth tag for this first block
            BlockState below = level.getBlockState(start);
            if (cfg.preventGrowthOn().isPresent() && below.is(cfg.preventGrowthOn().get())) {
                continue; // stop this column immediately
            }

            // Grow the rest of the column
            int columnHeight = distance - manhattan / 2 - 1; // -1 because first block is already placed
            BlockPos.MutableBlockPos cursor = start.mutable().move(Direction.UP);

            while (columnHeight-- >= 0) {
                BlockState current = level.getBlockState(cursor);
                // Only replace non-solid blocks
                if (!current.isSolid()) {
                    level.setBlock(cursor, columnState, 2);
                    placed = true;
                } else {
                    break; // stop column if we hit a solid block
                }
                cursor.move(Direction.UP);
            }
        }

        return placed;
    }


    @Nullable
    private static BlockPos findSurfaceOrAir(LevelAccessor level, int seaLevel, BlockPos.MutableBlockPos pos, int maxDepth) {
        for (int i = 0; i < maxDepth; i++) {
            BlockState state = level.getBlockState(pos);
            if (!state.isSolid()) { // air, water, etc.
                return pos;
            }
            pos.move(Direction.DOWN);
            if (pos.getY() <= level.getMinBuildHeight()) return null;
        }
        return pos;
    }

    private static boolean canPlaceAt(LevelAccessor level, int seaLevel, BlockPos.MutableBlockPos pos, BlockstateColumnFeatureConfiguration cfg) {
        BlockState below = level.getBlockState(pos.move(Direction.DOWN));
        pos.move(Direction.UP);

        // Must be solid-ish and not in CANNOT_PLACE_ON
        if (below.isAir() || CANNOT_PLACE_ON.contains(below.getBlock())) return false;

        // Check prevent-growth tag
        return cfg.preventGrowthOn().map(tag -> !below.is(tag)).orElse(true);
    }
}
