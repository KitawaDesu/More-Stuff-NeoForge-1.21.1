package net.kitawa.more_stuff.blocks.custom.electricity;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlockEntities;
import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
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

public class TeslaCoilBlock extends BaseEntityBlock implements SimpleFluidLoggedBlock {
    public static final MapCodec<TeslaCoilBlock> CODEC = simpleCodec(TeslaCoilBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty HAS_CHARGE = BooleanProperty.create("has_charge");

    private static final VoxelShape BOTTOM_SHAPE = Shapes.or(
            Block.box(0.0, 0.0, 0.0, 16.0, 13.0, 16.0),
            Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5),
            Block.box(1.0, 13.0, 1.0, 15.0, 14.0, 15.0)
    );

    private static final VoxelShape TOP_SHAPE = Shapes.or(
            Block.box(0.55, 3.0, 0.7, 15.05, 15.0, 15.2),
            Block.box(6.5, 0.0, 6.5, 9.5, 14.0, 9.5),
            Block.box(5, 10, 5, 11, 16, 11)
    );

    public TeslaCoilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(LAVALOGGED, false)
                .setValue(TOP, false)
                .setValue(HAS_CHARGE, false)
        );
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return state.getValue(TOP) ? TOP_SHAPE : BOTTOM_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LAVALOGGED, TOP, HAS_CHARGE);
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

    // === Placement ===
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();
        BlockPos above = pos.above();
        Level level = ctx.getLevel();

        if (!level.getBlockState(above).canBeReplaced(ctx) || !level.getWorldBorder().isWithinBounds(above)) {
            return null;
        }

        FluidState fluid = level.getFluidState(pos);
        return this.defaultBlockState()
                .setValue(TOP, false)
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA)
                .setValue(HAS_CHARGE, false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!state.getValue(TOP)) {
            BlockPos above = pos.above();
            FluidState topFluid = level.getFluidState(above);

            BlockState topState = this.defaultBlockState()
                    .setValue(TOP, true)
                    .setValue(WATERLOGGED, topFluid.getType() == Fluids.WATER)
                    .setValue(LAVALOGGED, topFluid.getType() == Fluids.LAVA)
                    .setValue(HAS_CHARGE, state.getValue(HAS_CHARGE));

            level.setBlock(above, topState, 3);
        }
    }

    // === Removal ===
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, level, pos, newState, isMoving);

        if (state.getBlock() != newState.getBlock()) {
            if (!state.getValue(TOP)) {
                BlockPos above = pos.above();
                BlockState top = level.getBlockState(above);
                if (top.getBlock() instanceof TeslaCoilBlock && top.getValue(TOP)) {
                    level.removeBlock(above, false);
                }
            } else {
                BlockPos below = pos.below();
                BlockState bottom = level.getBlockState(below);
                if (bottom.getBlock() instanceof TeslaCoilBlock && !bottom.getValue(TOP)) {
                    level.removeBlock(below, false);
                }
            }
        }
    }

    // === Fluids ===
    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // Delegate to helper in SimpleFluidLoggedBlock
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, super.getLightEmission(state, level, pos));
    }

    // === Redstone / BE / tick ===
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

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeslaCoilBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, ModBlockEntities.TESLA_COIL.get(),
                (lvl, p, st, be) -> {
                    if (be instanceof TeslaCoilBlockEntity coil) {
                        TeslaCoilBlockEntity.tick(lvl, p, st, coil);
                        propagateChargeNetwork(lvl, p);
                    }
                });
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}