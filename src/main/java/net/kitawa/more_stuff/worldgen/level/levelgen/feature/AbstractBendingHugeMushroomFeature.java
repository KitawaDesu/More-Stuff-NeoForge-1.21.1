package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.BendingMushroomFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public abstract class AbstractBendingHugeMushroomFeature extends Feature<BendingMushroomFeatureConfiguration> {

    public AbstractBendingHugeMushroomFeature(Codec<BendingMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BendingMushroomFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        BendingMushroomFeatureConfiguration config = context.config();

        int height = getTreeHeight(random);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        if (!isValidPosition(level, pos, height, mutable, config)) return false;

        // Grow trunks and collect endpoints
        List<BlockPos> branchEnds = placeTrunks(level, random, pos, config, height);

        // Place caps on endpoints
        for (BlockPos end : branchEnds) {
            BlockPos capBase = end.above();
            makeCap(level, random, capBase, 0, mutable, config);
        }

        return true;
    }

    /**
     * Places a main trunk with a drift and an optional secondary fork, modeled after ForkingTrunkPlacer.
     * Returns a list of last-placed stem positions (one for the main trunk, one for the fork if present).
     */
    protected List<BlockPos> placeTrunks(LevelAccessor level, RandomSource random, BlockPos pos,
                                         BendingMushroomFeatureConfiguration config, int freeTreeHeight) {
        List<BlockPos> finishedBranches = new ArrayList<>();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        // Ensure the block under the tree is handled by the same semantics (like setDirtAt)
        // We don't have setDirtAt here; if you need it, call it prior to calling this feature.

        // Choose a primary horizontal direction and compute drift start/length similar to ForkingTrunkPlacer
        Direction mainDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int i = freeTreeHeight - random.nextInt(4) - 1; // start drifting at this height
        int j = 3 - random.nextInt(3); // number of drift steps

        int k = pos.getX();
        int l = pos.getZ();
        OptionalInt optionalMainTop = OptionalInt.empty();

        // Main trunk: climb and drift
        for (int i1 = 0; i1 < freeTreeHeight; i1++) {
            int y = pos.getY() + i1;

            // start drifting horizontally when i1 >= i
            if (i1 >= i && j > 0) {
                k += mainDir.getStepX();
                l += mainDir.getStepZ();
                j--;
            }

            // place log / stem if possible
            BlockPos candidate = mutable.set(k, y, l);
            if (canReplace(level.getBlockState(candidate))) {
                this.setBlock(level, candidate, config.stemProvider.getState(random, pos));
                optionalMainTop = OptionalInt.of(y + 1); // store top as y+1 similar to trunk placer logic
            } else {
                // blocked: stop the trunk early (top is last placed)
                break;
            }
        }

        // If we placed at least one main trunk block, add its last placed stem position
        if (optionalMainTop.isPresent()) {
            int topY = optionalMainTop.getAsInt() - 1; // convert back to last stem Y
            finishedBranches.add(new BlockPos(k, topY, l));
        }

        // --- Secondary fork ---
        // Pick a second horizontal direction; if it's the same as mainDir, skip the fork (matches TrunkPlacer behavior)
        Direction forkDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        if (forkDir != mainDir) {
            // j2: vertical start for fork (a bit lower than main drift start)
            int j2 = i - random.nextInt(2) - 1;
            int k1 = 1 + random.nextInt(3); // how many drift steps the fork will perform
            int forkX = pos.getX();
            int forkZ = pos.getZ();
            OptionalInt optionalForkTop = OptionalInt.empty();

            int l1 = j2;
            while (l1 < freeTreeHeight && k1 > 0) {
                if (l1 >= 1) {
                    int y = pos.getY() + l1;
                    forkX += forkDir.getStepX();
                    forkZ += forkDir.getStepZ();
                    BlockPos forkPos = mutable.set(forkX, y, forkZ);
                    if (canReplace(level.getBlockState(forkPos))) {
                        this.setBlock(level, forkPos, config.stemProvider.getState(random, pos));
                        optionalForkTop = OptionalInt.of(y + 1);
                    } else {
                        // blocked: stop fork
                        break;
                    }
                }
                l1++;
                k1--;
            }

            if (optionalForkTop.isPresent()) {
                int topY = optionalForkTop.getAsInt() - 1;
                finishedBranches.add(new BlockPos(forkX, topY, forkZ));
            }
        }

        return finishedBranches;
    }

    protected static boolean canReplace(BlockState state) {
        // Replace if block is not solid (air, water, etc.)
        return !state.isSolidRender(null, null);
    }

    protected int getTreeHeight(RandomSource random) {
        int i = random.nextInt(3) + 4;
        if (random.nextInt(12) == 0) i *= 2;
        return i;
    }

    protected boolean isValidPosition(LevelAccessor level, BlockPos pos, int maxHeight,
                                      BlockPos.MutableBlockPos mutable, BendingMushroomFeatureConfiguration config) {
        int y = pos.getY();
        if (y < level.getMinBuildHeight() + 1 || y + maxHeight + 1 >= level.getMaxBuildHeight()) return false;

        BlockState ground = level.getBlockState(pos.below());
        if (!isDirt(ground) && !ground.is(BlockTags.MUSHROOM_GROW_BLOCK)) return false;

        for (int j = 0; j <= maxHeight; j++) {
            int radius = getTreeRadiusForHeight(-1, -1, config.foliageRadius, j);
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockState state = level.getBlockState(mutable.setWithOffset(pos, dx, j, dz));
                    if (!canReplace(state) && !state.is(BlockTags.LEAVES)) return false;
                }
            }
        }
        return true;
    }

    protected abstract int getTreeRadiusForHeight(int unused, int height, int foliageRadius, int y);

    protected abstract void makeCap(LevelAccessor level, RandomSource random, BlockPos branchEnd, int unusedHeight,
                                    BlockPos.MutableBlockPos mutable, BendingMushroomFeatureConfiguration config);
}
