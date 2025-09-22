package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.blocks.custom.electricity.OmniBlock;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.StormveinConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Fluids;

import java.util.List;


public class StormveinFeature extends Feature<StormveinConfiguration> {

    public StormveinFeature(Codec<StormveinConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<StormveinConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        RandomSource random = ctx.random();
        StormveinConfiguration config = ctx.config();

        // Find an anchor block that matches anchor_target
        AnchorData anchor = findAnchor(level, origin, config);
        if (anchor == null) return false;

        // Place main anchor block in the target block itself
        placeConfiguredAnchor(level, anchor.pos, random, config);

        // Grow main vein
        growVein(level, anchor, random, config);

        // Chance to spawn extra anchors
        if (random.nextFloat() < config.extraAnchorChance()) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos candidate = anchor.pos.relative(dir);
                if (isExtraAnchorValid(level, candidate, config, random)) {
                    AnchorData extra = new AnchorData(candidate, config.startMode().direction());
                    placeConfiguredAnchor(level, extra.pos, random, config);
                    growVein(level, extra, random, config);
                }
            }
        }

        return true;
    }

    // ========== CORE GENERATION ==========
    private void growVein(WorldGenLevel level, AnchorData anchor, RandomSource random, StormveinConfiguration config) {
        int length = config.minLength() + random.nextInt(config.maxLength() - config.minLength() + 1);
        BlockPos pos = anchor.pos.relative(anchor.dir);

        for (int i = 0; i < length; i++) {
            if (!isReplaceable(level, pos)) break;

            placeArmBlock(level, pos, random, config);

            // Optional perpendicular arms
            if (i > 2 && random.nextFloat() < 0.4f) {
                Direction armDir = getRandomPerpendicular(anchor.dir, random);
                growArm(level, pos.relative(armDir), armDir, random, config, 0);
            }

            pos = pos.relative(anchor.dir);
        }
    }

    private void growArm(WorldGenLevel level, BlockPos pos, Direction dir, RandomSource random, StormveinConfiguration config, int depth) {
        int len = 3 + random.nextInt(3);
        Direction currentDir = dir;

        for (int i = 0; i < len; i++) {
            if (!isReplaceable(level, pos)) break;

            placeArmBlock(level, pos, random, config);

            // Chance to bend
            if (i > 1 && random.nextFloat() < 0.2f) {
                currentDir = getRandomPerpendicular(currentDir, random);
            }

            pos = pos.relative(currentDir);

            // Recursive branching
            if (depth < 2 && random.nextFloat() < 0.25f) {
                Direction newDir = getRandomPerpendicular(currentDir, random);
                growArm(level, pos.relative(newDir), newDir, random, config, depth + 1);
            }
        }
    }

    // ========== BLOCK PLACEMENT ==========
    private void placeArmBlock(WorldGenLevel level, BlockPos pos, RandomSource random, StormveinConfiguration config) {
        BlockState state = config.blockProvider().getState(random, pos);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED,
                    level.getFluidState(pos).isSourceOfType(Fluids.WATER));
        }

        // Update OmniBlock properties
        if (state.getBlock() instanceof OmniBlock) {
            state = ((OmniBlock) state.getBlock()).updateConnections(level, pos, state);
        }

        level.setBlock(pos, state, 3);

        // Update neighbors so connections appear immediately
        updateNeighbors(level, pos);
    }

    private void placeConfiguredAnchor(WorldGenLevel level, BlockPos pos, RandomSource random, StormveinConfiguration config) {
        BlockState state = config.anchorProvider().getState(random, pos);

        if (state.hasProperty(BlockStateProperties.WATERLOGGED)) {
            state = state.setValue(BlockStateProperties.WATERLOGGED,
                    level.getFluidState(pos).isSourceOfType(Fluids.WATER));
        }

        // Update OmniBlock properties
        if (state.getBlock() instanceof OmniBlock) {
            state = ((OmniBlock) state.getBlock()).updateConnections(level, pos, state);
        }

        level.setBlock(pos, state, 3);

        // Update neighbors so connections appear immediately
        updateNeighbors(level, pos);
    }

    // ========== ANCHOR LOGIC ==========
    private AnchorData findAnchor(WorldGenLevel level, BlockPos pos, StormveinConfiguration config) {
        Direction dir = config.startMode().direction();

        // Check the block at origin, above, or below
        for (BlockPos candidate : List.of(pos, pos.above(), pos.below())) {
            if (isValidAnchor(level, candidate, config)) {
                return new AnchorData(candidate, dir);
            }
        }
        return null;
    }

    private boolean isValidAnchor(WorldGenLevel level, BlockPos pos, StormveinConfiguration config) {
        BlockState state = level.getBlockState(pos);
        return config.anchorTarget().test(state, level.getRandom());
    }

    private boolean isExtraAnchorValid(WorldGenLevel level, BlockPos pos, StormveinConfiguration config, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        return config.extraAnchorsCanReplace().test(state, random);
    }

    // ========== UTILS ==========
    private boolean isReplaceable(WorldGenLevel level, BlockPos pos) {
        return level.isEmptyBlock(pos) || level.getFluidState(pos).isSourceOfType(Fluids.WATER);
    }

    private Direction getRandomPerpendicular(Direction dir, RandomSource random) {
        return dir.getAxis().isVertical()
                ? Direction.Plane.HORIZONTAL.getRandomDirection(random)
                : (random.nextBoolean() ? Direction.UP : Direction.DOWN);
    }

    private void updateNeighbors(LevelAccessor level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighborState.getBlock() instanceof OmniBlock) {
                BlockState updated = ((OmniBlock) neighborState.getBlock()).updateConnections(level, neighborPos, neighborState);
                level.setBlock(neighborPos, updated, 3);
            }
        }
    }

    private record AnchorData(BlockPos pos, Direction dir) {}
}