package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class HybernaticCrystalBlock extends HalfTransparentBlock {

    private static final int GROWTH_CHANCE = 5;
    private static final int CROWDED_SMALL_CHANCE  = 15;
    private static final int CROWDED_MEDIUM_CHANCE = 35;
    private static final int CROWDED_LARGE_CHANCE  = 80;

    private static final int CROWD_THRESHOLD_SMALL  = 6;
    private static final int CROWD_THRESHOLD_MEDIUM = 16;
    private static final int CROWD_THRESHOLD_LARGE  = 28;

    private final Supplier<? extends Block> crystalClusterBlock;

    public HybernaticCrystalBlock(BlockBehaviour.Properties properties,
                                  Supplier<? extends Block> crystalClusterBlock) {
        super(properties.randomTicks());
        this.crystalClusterBlock = crystalClusterBlock;
    }

    @Override
    public MapCodec<? extends HalfTransparentBlock> codec() {
        return simpleCodec(props -> new HybernaticCrystalBlock(props, crystalClusterBlock));
    }

    // ---------------------------
    // RANDOM TICK
    // ---------------------------

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        int nearby = countNearbyCrystals(level, pos);

        int chance;
        if      (nearby >= CROWD_THRESHOLD_LARGE)  chance = CROWDED_LARGE_CHANCE;
        else if (nearby >= CROWD_THRESHOLD_MEDIUM) chance = CROWDED_MEDIUM_CHANCE;
        else if (nearby >= CROWD_THRESHOLD_SMALL)  chance = CROWDED_SMALL_CHANCE;
        else                                        chance = GROWTH_CHANCE;

        if (random.nextInt(chance) != 0) return;

        // Only allow vertical growth for conversion
        Direction direction = random.nextBoolean() ? Direction.UP : Direction.DOWN;
        tryGrowVertical(level, pos, direction, random);

        // Always allow side cluster growth
        tryGrowHorizontal(level, pos, Direction.NORTH, random);
        tryGrowHorizontal(level, pos, Direction.SOUTH, random);
        tryGrowHorizontal(level, pos, Direction.EAST, random);
        tryGrowHorizontal(level, pos, Direction.WEST, random);
    }

    // ---------------------------
    // VERTICAL CONVERSION LOGIC
    // ---------------------------

    private void tryGrowVertical(ServerLevel level, BlockPos origin, Direction dir,
                                 RandomSource random) {

        int maxDist = Mth.nextInt(random, 2, 5);

        for (int i = 1; i <= maxDist; i++) {
            BlockPos target = origin.relative(dir, i);
            BlockState state = level.getBlockState(target);

            // Only convert clusters to full block
            if (state.is(crystalClusterBlock.get())) {

                // Check the 4-block limit in the opposite direction
                Direction checkDir = (dir == Direction.UP) ? Direction.DOWN : Direction.UP;
                if (hasFullStackInDirection(level, target, checkDir)) return;

                level.setBlockAndUpdate(target, this.defaultBlockState());
                return; // Stop after first conversion
            }

            // If it's air/water, we do NOT place anything vertically
            if (canClusterGrowAtState(state)) return;

            // Stop if blocked by other solid
            return;
        }
    }

    // ---------------------------
    // HORIZONTAL CLUSTER GROWTH
    // ---------------------------

    private void tryGrowHorizontal(ServerLevel level, BlockPos origin, Direction dir,
                                   RandomSource random) {

        int maxDist = Mth.nextInt(random, 2, 5);
        BlockPos.MutableBlockPos cursor = origin.mutable();

        for (int i = 1; i <= maxDist; i++) {
            cursor.move(dir);
            BlockState state = level.getBlockState(cursor);

            // Skip through same blocks
            if (state.getBlock() instanceof HybernaticCrystalBlock) continue;

            if (canClusterGrowAtState(state)) {
                BlockState newCluster = crystalClusterBlock.get().defaultBlockState()
                        .setValue(BlockStateProperties.FACING, dir);

                if (newCluster.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    newCluster = newCluster.setValue(
                            BlockStateProperties.WATERLOGGED,
                            state.getFluidState().getType() == Fluids.WATER
                    );
                }

                level.setBlockAndUpdate(cursor, newCluster);
            }

            return; // Stop after first valid attempt
        }
    }

    // ---------------------------
    // HELPER: CHECK VERTICAL STACK
    // ---------------------------

    private boolean hasFullStackInDirection(ServerLevel level, BlockPos start, Direction dir) {
        BlockPos.MutableBlockPos cursor = start.mutable();
        for (int i = 0; i < 4; i++) {
            cursor.move(dir);
            if (!(level.getBlockState(cursor).getBlock() instanceof HybernaticCrystalBlock)) {
                return false;
            }
        }
        return true;
    }

    // ---------------------------
    // DENSITY CHECK (4x4 + vertical)
    // ---------------------------

    private int countNearbyCrystals(ServerLevel level, BlockPos origin) {
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int count = 0;

        for (int x = -2; x <= 1; x++) {
            for (int z = -2; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {

                    if (x == 0 && y == 0 && z == 0) continue;

                    cursor.set(origin.getX() + x, origin.getY() + y, origin.getZ() + z);

                    if (level.getBlockState(cursor).getBlock() instanceof HybernaticCrystalBlock) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    // ---------------------------
    // HELPERS
    // ---------------------------

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir()
                || (state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8);
    }
}