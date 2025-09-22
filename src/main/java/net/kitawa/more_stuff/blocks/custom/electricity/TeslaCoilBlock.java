package net.kitawa.more_stuff.blocks.custom.electricity;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlockEntities;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
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

public class TeslaCoilBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<TeslaCoilBlock> CODEC = simpleCodec(TeslaCoilBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty HAS_CHARGE = BooleanProperty.create("has_charge");

    // === Collision Shapes ===
    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(
            // Main body [0,0,0] -> [16,13,16]
            Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
            // Center rod [6.5,0,6.5] -> [9.5,16,9.5]
            Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5),
            // Top plate [1,13,1] -> [15,14,15]
            Block.box(1.0, 13.0, 1.0, 15.0, 14.0, 15.0)
    );

    private static final VoxelShape TOP_SHAPE = Shapes.or(
            // Outer body [0.55,3,0.7] -> [15.05,15,15.2]
            Block.box(0.55, 3.0, 0.7, 15.05, 15.0, 15.2),
            // Center rod [6.5,0,6.5] -> [9.5,14,9.5]
            Block.box(6.5, 0.0, 6.5, 9.5, 14.0, 9.5),
            // Top cube [5,10,5] -> [11,16,11]
            Block.box(5.0, 10.0, 5.0, 11.0, 16.0, 11.0)
    );

    public TeslaCoilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(TOP, false)
                .setValue(HAS_CHARGE, false) // default off
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return state.getValue(TOP) ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, TOP, HAS_CHARGE);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    // === Redstone Power ===
    @Override
    public boolean isSignalSource(BlockState state) { return true; }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        BlockEntity be = level.getBlockEntity(pos);
        return (be instanceof TeslaCoilBlockEntity tbe) ? tbe.getRedstonePower() : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return getSignal(state, level, pos, dir);
    }

    // === BlockEntity ===
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeslaCoilBlockEntity(pos, state);
    }

    // === Ticking ===
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.TESLA_COIL.get(),
                (lvl, pos, st, be) -> {
                    if (be instanceof TeslaCoilBlockEntity coil) {
                        TeslaCoilBlockEntity.tick(lvl, pos, st, coil); // run BE logic
                        propagateChargeNetwork(lvl, pos);              // also run network updates
                    }
                });
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidstate = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER)
                .setValue(TOP, false)
                .setValue(HAS_CHARGE, false); // always place bottom first
    }

    private void propagateChargeNetwork(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof TeslaCoilBlockEntity coil)) return;

        // If this is the top block, copy the bottom's charge
        if (state.getValue(TOP)) {
            BlockPos bottomPos = pos.below();
            BlockEntity bottomBE = level.getBlockEntity(bottomPos);
            if (bottomBE instanceof TeslaCoilBlockEntity bottomCoil) {
                coil.setStoredCharge(bottomCoil.getStoredCharge());
            } else {
                coil.setStoredCharge(0);
            }
            return; // done, no need to calculate further
        }

        // Bottom block: calculate total contribution from neighbors
        int totalContribution = 0;

        // Check DOWN, NORTH, SOUTH, EAST, WEST
        for (Direction dir : new Direction[]{Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST}) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighborPos);
            BlockEntity neighborBE = level.getBlockEntity(neighborPos);

            // Power source → +30
            if (neighborState.is(ModBlockTags.POWER_SOURCES)) {
                totalContribution += 30;
            }

            // OmniBlock → add its stored charge
            if (neighborBE instanceof OmniBlockEntity omni) {
                totalContribution += omni.getStoredCharge();
            }
        }

        // Clamp storedCharge to max 180
        coil.setStoredCharge(totalContribution);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);

        if (!state.getValue(TOP)) { // bottom block
            // Ensure bottom keeps its own waterlogging
            BlockState bottomState = this.defaultBlockState()
                    .setValue(TOP, false)
                    .setValue(WATERLOGGED, state.getValue(WATERLOGGED))
                    .setValue(HAS_CHARGE, state.getValue(HAS_CHARGE));
            level.setBlock(pos, bottomState, 3);

            // Place the top block above, preserve water if present
            BlockPos topPos = pos.above();
            BlockState existingTop = level.getBlockState(topPos);
            if (existingTop.canBeReplaced()) {
                boolean waterlogged = existingTop.getFluidState().getType() == Fluids.WATER;
                BlockState topState = this.defaultBlockState()
                        .setValue(TOP, true)
                        .setValue(WATERLOGGED, waterlogged)
                        .setValue(HAS_CHARGE, bottomState.getValue(HAS_CHARGE));
                level.setBlock(topPos, topState, 3);
            } else {
                // Fail-safe: remove bottom if top cannot be placed
                level.removeBlock(pos, false);
                return;
            }
        }

        if (!level.isClientSide) {
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!state.getValue(TOP)) {
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (topState.getBlock() instanceof TeslaCoilBlock && topState.getValue(TOP)) {
                level.removeBlock(topPos, false);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (level.isClientSide) return;

        // Recalculate bottom charge network
        if (!state.getValue(TOP)) {
            propagateChargeNetwork(level, pos);

            // Update the top block's HAS_CHARGE to match bottom
            BlockPos topPos = pos.above();
            BlockState topState = level.getBlockState(topPos);
            if (topState.getBlock() instanceof TeslaCoilBlock && topState.getValue(TOP)) {
                boolean bottomCharge = state.getValue(HAS_CHARGE);
                level.setBlock(topPos, topState.setValue(HAS_CHARGE, bottomCharge), 3);
            }
        }

        // Optional: allow top to recalc without destroying it
        if (state.getValue(TOP)) {
            BlockPos bottomPos = pos.below();
            BlockState bottomState = level.getBlockState(bottomPos);
            if (bottomState.getBlock() instanceof TeslaCoilBlock && !bottomState.getValue(TOP)) {
                // Copy HAS_CHARGE from bottom
                boolean bottomCharge = bottomState.getValue(HAS_CHARGE);
                if (state.getValue(HAS_CHARGE) != bottomCharge) {
                    level.setBlock(pos, state.setValue(HAS_CHARGE, bottomCharge), 3);
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}