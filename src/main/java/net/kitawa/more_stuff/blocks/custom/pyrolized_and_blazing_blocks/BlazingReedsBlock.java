package net.kitawa.more_stuff.blocks.custom.pyrolized_and_blazing_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlazingReedsBlock extends Block {
    public static final MapCodec<BlazingReedsBlock> CODEC = simpleCodec(BlazingReedsBlock::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    @Override
    public MapCodec<BlazingReedsBlock> codec() {
        return CODEC;
    }

    public BlazingReedsBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, Integer.valueOf(0)).setValue(TOP, Boolean.valueOf(true)));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    /**
     * Performs a random tick on a block.
     */
    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isEmptyBlock(pos.above())) {
            int i = 1;
            while (level.getBlockState(pos.below(i)).is(this)) {
                i++;
            }

            if (i < 3) {
                int age = state.getValue(AGE);
                if (net.neoforged.neoforge.common.CommonHooks.canCropGrow(level, pos, state, true)) {
                    if (age == 15) {
                        BlockState newTop = this.defaultBlockState().setValue(TOP, true);
                        BlockState newBase = state.setValue(AGE, 0).setValue(TOP, false);

                        level.setBlockAndUpdate(pos.above(), newTop);
                        net.neoforged.neoforge.common.CommonHooks.fireCropGrowPost(level, pos.above(), newTop);
                        level.setBlock(pos, newBase, 4);
                    } else {
                        level.setBlock(pos, state.setValue(AGE, age + 1), 4);
                    }
                }
            }
        }
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!state.canSurvive(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }

        if (facing == Direction.UP) {
            // If the block above is not the same, this block must be the top
            boolean isNowTop = !facingState.is(this);
            if (state.getValue(TOP) != isNowTop) {
                return state.setValue(TOP, isNowTop);
            }
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());

        if (blockBelow.is(this)) {
            return true;
        } else {
            net.neoforged.neoforge.common.util.TriState soilDecision =
                    blockBelow.canSustainPlant(level, pos.below(), Direction.UP, state);
            if (!soilDecision.isDefault()) return soilDecision.isTrue();

            if (blockBelow.is(ModBlockTags.BLAZING_REEDS_PLANTABLE_ON)) {
                BlockPos blockpos = pos.below();

                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    BlockState adjacentState = level.getBlockState(blockpos.relative(direction));
                    if (adjacentState.is(Blocks.LAVA)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, TOP);
    }
}
