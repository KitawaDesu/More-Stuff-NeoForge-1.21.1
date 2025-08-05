package net.kitawa.more_stuff.blocks.custom.general;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiFunction;

public class StonesBlock extends BushBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<StonesBlock> CODEC = simpleCodec(StonesBlock::new);
    public static final int MIN_FLOWERS = 1;
    public static final int MAX_FLOWERS = 4;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final BiFunction<Direction, Integer, VoxelShape> SHAPE_BY_PROPERTIES = Util.memoize(
            (p_296142_, p_294775_) -> {
                VoxelShape[] avoxelshape = new VoxelShape[]{
                        Block.box(8.0, 0.0, 8.0, 16.0, 0.1, 16.0),
                        Block.box(8.0, 0.0, 0.0, 16.0, 0.1, 8.0),
                        Block.box(0.0, 0.0, 0.0, 8.0, 0.1, 8.0),
                        Block.box(0.0, 0.0, 8.0, 8.0, 0.1, 16.0)
                };
                VoxelShape voxelshape = Shapes.empty();

                for (int i = 0; i < p_294775_; i++) {
                    int j = Math.floorMod(i - p_296142_.get2DDataValue(), 4);
                    voxelshape = Shapes.or(voxelshape, avoxelshape[j]);
                }

                return voxelshape.singleEncompassing();
            }
    );

    @Override
    public MapCodec<StonesBlock> codec() {
        return CODEC;
    }

    public StonesBlock(BlockBehaviour.Properties p_273335_) {
        super(p_273335_);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AMOUNT, Integer.valueOf(1)).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState rotate(BlockState p_273485_, Rotation p_273021_) {
        return p_273485_.setValue(FACING, p_273021_.rotate(p_273485_.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState p_272961_, Mirror p_273278_) {
        return p_272961_.rotate(p_273278_.getRotation(p_272961_.getValue(FACING)));
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        return fluidState.is(Fluids.LAVA) || floor.isFaceSturdy(world, pos, Direction.UP);
    }

    @Override
    public boolean canBeReplaced(BlockState p_272922_, BlockPlaceContext p_273534_) {
        return !p_273534_.isSecondaryUseActive() && p_273534_.getItemInHand().is(this.asItem()) && p_272922_.getValue(AMOUNT) < 4
                ? true
                : super.canBeReplaced(p_272922_, p_273534_);
    }

    @Override
    public VoxelShape getShape(BlockState p_273399_, BlockGetter p_273568_, BlockPos p_273314_, CollisionContext p_273274_) {
        return SHAPE_BY_PROPERTIES.apply(p_273399_.getValue(FACING), p_273399_.getValue(AMOUNT));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState existingState = level.getBlockState(pos);

        if (existingState.is(this)) {
            return existingState.setValue(AMOUNT, Math.min(4, existingState.getValue(AMOUNT) + 1));
        } else {
            FluidState fluidState = level.getFluidState(pos);
            boolean isWaterlogged = fluidState.getType() == Fluids.WATER;
            return this.defaultBlockState()
                    .setValue(FACING, context.getHorizontalDirection().getOpposite())
                    .setValue(WATERLOGGED, isWaterlogged);
        }
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_272634_) {
        p_272634_.add(FACING, AMOUNT, WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return SimpleWaterloggedBlock.super.placeLiquid(level, pos, state, fluidState);
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return SimpleWaterloggedBlock.super.canPlaceLiquid(player, level, pos, state, fluid);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}

