package net.kitawa.more_stuff.blocks.custom.electricity;


import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlockEntities;
import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
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
import java.util.*;

public class OmniBlock extends BaseEntityBlock implements SimpleFluidLoggedBlock {
    public static final MapCodec<? extends BaseEntityBlock> CODEC = simpleCodec(OmniBlock::new);

    // === Blockstate properties ===
    public static final BooleanProperty CORE = BooleanProperty.create("core");
    public static final BooleanProperty UP    = BooleanProperty.create("up");
    public static final BooleanProperty DOWN  = BooleanProperty.create("down");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");

    public static final BooleanProperty ANCHOR_UP    = BooleanProperty.create("anchor_up");
    public static final BooleanProperty ANCHOR_DOWN  = BooleanProperty.create("anchor_down");
    public static final BooleanProperty ANCHOR_NORTH = BooleanProperty.create("anchor_north");
    public static final BooleanProperty ANCHOR_SOUTH = BooleanProperty.create("anchor_south");
    public static final BooleanProperty ANCHOR_EAST  = BooleanProperty.create("anchor_east");
    public static final BooleanProperty ANCHOR_WEST  = BooleanProperty.create("anchor_west");

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED; // 🔥 new
    public static final BooleanProperty END_POINT = BooleanProperty.create("end_point");

    // === Shapes ===
    private static final VoxelShape CORE_SHAPE  = Block.box(5, 5, 5, 11, 11, 11);
    private static final VoxelShape ARM_DOWN    = Block.box(6.5, 0, 6.5, 9.5, 8, 9.5);
    private static final VoxelShape ARM_UP      = Block.box(6.5, 8, 6.5, 9.5, 16, 9.5);
    private static final VoxelShape ARM_NORTH   = Block.box(6.5, 6.5, 0, 9.5, 9.5, 8);
    private static final VoxelShape ARM_SOUTH   = Block.box(6.5, 6.5, 8, 9.5, 9.5, 16);
    private static final VoxelShape ARM_WEST    = Block.box(0, 6.5, 6.5, 8, 9.5, 9.5);
    private static final VoxelShape ARM_EAST    = Block.box(8, 6.5, 6.5, 16, 9.5, 9.5);
    private static final VoxelShape END_SHAPE  = Block.box(5, 5, 5, 11, 11, 11);

    public OmniBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(CORE, false)
                .setValue(UP, false).setValue(DOWN, false)
                .setValue(NORTH, false).setValue(SOUTH, false)
                .setValue(EAST, false).setValue(WEST, false)
                .setValue(ANCHOR_UP, false).setValue(ANCHOR_DOWN, false)
                .setValue(ANCHOR_NORTH, false).setValue(ANCHOR_SOUTH, false)
                .setValue(ANCHOR_EAST, false).setValue(ANCHOR_WEST, false)
                .setValue(WATERLOGGED, false)
                .setValue(LAVALOGGED, false) // 🔥
                .setValue(END_POINT, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CORE,
                UP, DOWN, NORTH, SOUTH, EAST, WEST,
                ANCHOR_UP, ANCHOR_DOWN, ANCHOR_NORTH, ANCHOR_SOUTH, ANCHOR_EAST, ANCHOR_WEST,
                WATERLOGGED, LAVALOGGED, // 🔥
                END_POINT
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA); // 🔥
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        } else if (state.getValue(LAVALOGGED)) {
            return Fluids.LAVA.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        } else if (state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    // === Connection Logic ===
    public BlockState updateConnections(LevelAccessor level, BlockPos pos, BlockState state) {
        for (Direction dir : Direction.values()) {
            BlockPos neighborPos = pos.relative(dir);
            BlockState neighbor = level.getBlockState(neighborPos);

            boolean connect = neighbor.getBlock() instanceof OmniBlock || neighbor.is(ModBlockTags.ANCHOR_BLOCKS);
            boolean anchor  = neighbor.is(ModBlockTags.ANCHOR_BLOCKS);

            state = state
                    .setValue(getPropertyFor(dir), connect)
                    .setValue(getAnchorPropertyFor(dir), anchor);
        }

        boolean anyArm = state.getValue(UP) || state.getValue(DOWN) ||
                state.getValue(NORTH) || state.getValue(SOUTH) ||
                state.getValue(EAST) || state.getValue(WEST);
        state = state.setValue(CORE, !anyArm || isCorner(state));

        int activeConnections = 0;
        for (Direction dir : Direction.values()) {
            if (state.getValue(getPropertyFor(dir)) || state.getValue(getAnchorPropertyFor(dir))) activeConnections++;
        }

        state = state.setValue(END_POINT, activeConnections == 1);
        return state;
    }

    private static BooleanProperty getPropertyFor(Direction dir) {
        return switch (dir) {
            case UP -> UP; case DOWN -> DOWN; case NORTH -> NORTH;
            case SOUTH -> SOUTH; case EAST -> EAST; case WEST -> WEST;
        };
    }

    private static BooleanProperty getAnchorPropertyFor(Direction dir) {
        return switch (dir) {
            case UP -> ANCHOR_UP; case DOWN -> ANCHOR_DOWN; case NORTH -> ANCHOR_NORTH;
            case SOUTH -> ANCHOR_SOUTH; case EAST -> ANCHOR_EAST; case WEST -> ANCHOR_WEST;
        };
    }

    private boolean isCorner(BlockState state) {
        boolean vertical   = state.getValue(UP) || state.getValue(DOWN);
        boolean northSouth = state.getValue(NORTH) || state.getValue(SOUTH);
        boolean eastWest   = state.getValue(EAST) || state.getValue(WEST);
        return (vertical && (northSouth || eastWest)) || (northSouth && eastWest);
    }
    // === Shape ===
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        VoxelShape shape = Shapes.empty();
        if (state.getValue(CORE)) shape = Shapes.or(shape, CORE_SHAPE);
        if (state.getValue(DOWN)) shape = Shapes.or(shape, ARM_DOWN);
        if (state.getValue(UP)) shape = Shapes.or(shape, ARM_UP);
        if (state.getValue(NORTH)) shape = Shapes.or(shape, ARM_NORTH);
        if (state.getValue(SOUTH)) shape = Shapes.or(shape, ARM_SOUTH);
        if (state.getValue(WEST)) shape = Shapes.or(shape, ARM_WEST);
        if (state.getValue(EAST)) shape = Shapes.or(shape, ARM_EAST);
        if (state.getValue(END_POINT)) shape = Shapes.or(shape, END_SHAPE);
        return shape;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return getShape(state, level, pos, ctx);
    }

    // === Neighbor Updates ===
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        propagateChargeNetwork(level, pos);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (!oldState.is(state.getBlock())) propagateChargeNetwork(level, pos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);
        if (!state.is(newState.getBlock())) propagateChargeNetwork(level, pos);
    }

    // === Charge Propagation ===
    void propagateChargeNetwork(Level level, BlockPos startPos) {
        if (level.isClientSide) return;

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(startPos);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (!visited.add(current)) continue;

            BlockState state = level.getBlockState(current);
            if (!(state.getBlock() instanceof OmniBlock)) continue;

            BlockEntity be = level.getBlockEntity(current);
            if (!(be instanceof OmniBlockEntity omni)) continue;

            // Update connections
            BlockState updated = ((OmniBlock) state.getBlock()).updateConnections(level, current, state);
            if (!updated.equals(state)) level.setBlock(current, updated, 3);

            // Calculate max charge
            int maxNeighbor = 0;
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = current.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);
                BlockEntity neighborBE = level.getBlockEntity(neighborPos);

                if (neighborState.is(ModBlockTags.POWER_SOURCES)) {
                    maxNeighbor = 30;
                    break;
                }

                if (neighborBE instanceof OmniBlockEntity n) maxNeighbor = Math.max(maxNeighbor, n.getStoredCharge());
            }

            int newCharge = Math.max(maxNeighbor - 1, 0);
            omni.setStoredCharge(newCharge);

            // Enqueue neighbors
            for (Direction dir : Direction.values()) {
                BlockPos neighborPos = current.relative(dir);
                BlockState neighborState = level.getBlockState(neighborPos);
                if (neighborState.getBlock() instanceof OmniBlock && !visited.contains(neighborPos)) {
                    queue.add(neighborPos);
                }
            }
        }
    }

    // === Redstone Power ===
    @Override
    public boolean isSignalSource(BlockState state) { return true; }

    @Override
    public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        BlockEntity be = level.getBlockEntity(pos);
        return (be instanceof OmniBlockEntity omni) ? omni.getRedstonePower() : 0;
    }

    @Override
    public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        return getSignal(state, level, pos, dir);
    }

    // === BlockEntity / Ticker ===
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new OmniBlockEntity(pos, state); }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;

        return createTickerHelper(type, ModBlockEntities.OMNI.get(),
                (lvl, pos, st, be) -> {

                    // Check for nearest player within 40 blocks
                    if (lvl.hasNearbyAlivePlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 40)) {
                        OmniBlockEntity.tick(lvl, pos, st, be);
                    }
                });
    }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // use the block's default light value
        int baseLight = super.getLightEmission(state, level, pos);
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}