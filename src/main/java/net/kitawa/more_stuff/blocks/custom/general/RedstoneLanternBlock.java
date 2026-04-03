package net.kitawa.more_stuff.blocks.custom.general;

import com.google.common.collect.Lists;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class RedstoneLanternBlock extends Block implements SimpleWaterloggedBlock {

    public static final MapCodec<RedstoneLanternBlock> CODEC = simpleCodec(RedstoneLanternBlock::new);

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    protected static final VoxelShape AABB = Shapes.or(
            Block.box(5, 0, 5, 11, 7, 11),
            Block.box(6, 7, 6, 10, 9, 10)
    );

    protected static final VoxelShape HANGING_AABB = Shapes.or(
            Block.box(5, 1, 5, 11, 8, 11),
            Block.box(6, 8, 6, 10, 10, 10)
    );

    @Override
    public MapCodec<? extends RedstoneLanternBlock> codec() {
        return CODEC;
    }

    public RedstoneLanternBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(HANGING, false)
                .setValue(WATERLOGGED, false)
                .setValue(LIT, true));
    }

    // ---------------------------
    // PLACEMENT
    // ---------------------------

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());

        for (Direction dir : context.getNearestLookingDirections()) {
            if (dir.getAxis() == Direction.Axis.Y) {
                BlockState state = defaultBlockState().setValue(HANGING, dir == Direction.UP);
                if (state.canSurvive(context.getLevel(), context.getClickedPos())) {
                    return state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    // ---------------------------
    // SHAPE
    // ---------------------------

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(HANGING) ? HANGING_AABB : AABB;
    }

    // ---------------------------
    // STATE DEFINITION
    // ---------------------------

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HANGING, WATERLOGGED, LIT);
    }

    // ---------------------------
    // SURVIVAL
    // ---------------------------

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction dir = getConnectedDirection(state).getOpposite();
        return Block.canSupportCenter(level, pos.relative(dir), dir.getOpposite());
    }

    protected static Direction getConnectedDirection(BlockState state) {
        return state.getValue(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return getConnectedDirection(state).getOpposite() == direction && !state.canSurvive(level, pos)
                ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    // ---------------------------
    // REDSTONE LOGIC (unchanged)
    // ---------------------------

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return state.getValue(LIT) && side != Direction.UP ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction side) {
        return side == Direction.UP ? getSignal(state, level, pos, side) : 0;
    }

    private boolean hasNeighborSignal(Level level, BlockPos pos, BlockState state) {
        return state.getValue(HANGING)
                ? level.hasSignal(pos.above(), Direction.UP)
                : level.hasSignal(pos.below(), Direction.DOWN);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos,
                                   Block block, BlockPos fromPos, boolean isMoving) {

        if (state.getValue(LIT) == hasNeighborSignal(level, pos, state)
                && !level.getBlockTicks().willTickThisTick(pos, this)) {
            level.scheduleTick(pos, this, 2);
        }
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        boolean powered = hasNeighborSignal(level, pos, state);

        if (state.getValue(LIT) && powered) {
            level.setBlock(pos, state.setValue(LIT, false), 3);
        } else if (!state.getValue(LIT) && !powered) {
            level.setBlock(pos, state.setValue(LIT, true), 3);
        }
    }
}