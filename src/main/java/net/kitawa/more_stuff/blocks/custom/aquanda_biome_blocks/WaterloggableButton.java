package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class WaterloggableButton extends FaceAttachedHorizontalDirectionalBlock implements SimpleWaterloggedBlock {
    public static final MapCodec<WaterloggableButton> CODEC = RecordCodecBuilder.mapCodec(
            p_308803_ -> p_308803_.group(
                            BlockSetType.CODEC.fieldOf("block_set_type").forGetter(p_304607_ -> p_304607_.type),
                            Codec.intRange(1, 1024).fieldOf("ticks_to_stay_pressed").forGetter(p_304953_ -> p_304953_.ticksToStayPressed),
                            propertiesCodec()
                    )
                    .apply(p_308803_, WaterloggableButton::new)
    );
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final int PRESSED_DEPTH = 1;
    private static final int UNPRESSED_DEPTH = 2;
    protected static final int HALF_AABB_HEIGHT = 2;
    protected static final int HALF_AABB_WIDTH = 3;
    protected static final VoxelShape CEILING_AABB_X = Block.box(6.0, 14.0, 5.0, 10.0, 16.0, 11.0);
    protected static final VoxelShape CEILING_AABB_Z = Block.box(5.0, 14.0, 6.0, 11.0, 16.0, 10.0);
    protected static final VoxelShape FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 2.0, 11.0);
    protected static final VoxelShape FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 2.0, 10.0);
    protected static final VoxelShape NORTH_AABB = Block.box(5.0, 6.0, 14.0, 11.0, 10.0, 16.0);
    protected static final VoxelShape SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 2.0);
    protected static final VoxelShape WEST_AABB = Block.box(14.0, 6.0, 5.0, 16.0, 10.0, 11.0);
    protected static final VoxelShape EAST_AABB = Block.box(0.0, 6.0, 5.0, 2.0, 10.0, 11.0);
    protected static final VoxelShape PRESSED_CEILING_AABB_X = Block.box(6.0, 15.0, 5.0, 10.0, 16.0, 11.0);
    protected static final VoxelShape PRESSED_CEILING_AABB_Z = Block.box(5.0, 15.0, 6.0, 11.0, 16.0, 10.0);
    protected static final VoxelShape PRESSED_FLOOR_AABB_X = Block.box(6.0, 0.0, 5.0, 10.0, 1.0, 11.0);
    protected static final VoxelShape PRESSED_FLOOR_AABB_Z = Block.box(5.0, 0.0, 6.0, 11.0, 1.0, 10.0);
    protected static final VoxelShape PRESSED_NORTH_AABB = Block.box(5.0, 6.0, 15.0, 11.0, 10.0, 16.0);
    protected static final VoxelShape PRESSED_SOUTH_AABB = Block.box(5.0, 6.0, 0.0, 11.0, 10.0, 1.0);
    protected static final VoxelShape PRESSED_WEST_AABB = Block.box(15.0, 6.0, 5.0, 16.0, 10.0, 11.0);
    protected static final VoxelShape PRESSED_EAST_AABB = Block.box(0.0, 6.0, 5.0, 1.0, 10.0, 11.0);
    private final BlockSetType type;
    private final int ticksToStayPressed;

    @Override
    public MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public WaterloggableButton(BlockSetType type, int ticksToStayPressed, BlockBehaviour.Properties properties) {
        super(properties.sound(type.soundType()));
        this.type = type;
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(POWERED, Boolean.valueOf(false)).setValue(FACE, AttachFace.WALL).setValue(WATERLOGGED, Boolean.valueOf(false))
        );
        this.ticksToStayPressed = ticksToStayPressed;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        boolean flag = state.getValue(POWERED);
        switch ((AttachFace)state.getValue(FACE)) {
            case FLOOR:
                if (direction.getAxis() == Direction.Axis.X) {
                    return flag ? PRESSED_FLOOR_AABB_X : FLOOR_AABB_X;
                }

                return flag ? PRESSED_FLOOR_AABB_Z : FLOOR_AABB_Z;
            case WALL:
                return switch (direction) {
                    case EAST -> flag ? PRESSED_EAST_AABB : EAST_AABB;
                    case WEST -> flag ? PRESSED_WEST_AABB : WEST_AABB;
                    case SOUTH -> flag ? PRESSED_SOUTH_AABB : SOUTH_AABB;
                    case NORTH, UP, DOWN -> flag ? PRESSED_NORTH_AABB : NORTH_AABB;
                };
            case CEILING:
            default:
                if (direction.getAxis() == Direction.Axis.X) {
                    return flag ? PRESSED_CEILING_AABB_X : CEILING_AABB_X;
                } else {
                    return flag ? PRESSED_CEILING_AABB_Z : CEILING_AABB_Z;
                }
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (state.getValue(POWERED)) {
            return InteractionResult.CONSUME;
        } else {
            this.press(state, level, pos, player);
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, Level level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> dropConsumer) {
        if (explosion.canTriggerBlocks() && !state.getValue(POWERED)) {
            this.press(state, level, pos, null);
        }

        super.onExplosionHit(state, level, pos, explosion, dropConsumer);
    }

    public void press(BlockState state, Level level, BlockPos pos, @Nullable Player player) {
        level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(true)), 3);
        this.updateNeighbours(state, level, pos);
        level.scheduleTick(pos, this, this.ticksToStayPressed);
        this.playSound(player, level, pos, true);
        level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, pos);
    }

    protected void playSound(@Nullable Player player, LevelAccessor level, BlockPos pos, boolean hitByArrow) {
        level.playSound(hitByArrow ? player : null, pos, this.getSound(hitByArrow), SoundSource.BLOCKS);
    }

    protected SoundEvent getSound(boolean isOn) {
        return isOn ? this.type.buttonClickOn() : this.type.buttonClickOff();
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && !state.is(newState.getBlock())) {
            if (state.getValue(POWERED)) {
                this.updateNeighbours(state, level, pos);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    /**
     * Returns the signal this block emits in the given direction.
     *
     * <p>
     * NOTE: directions in redstone signal related methods are backwards, so this method
     * checks for the signal emitted in the <i>opposite</i> direction of the one given.
     *
     */
    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(POWERED) ? 15 : 0;
    }

    /**
     * Returns the direct signal this block emits in the given direction.
     *
     * <p>
     * NOTE: directions in redstone signal related methods are backwards, so this method
     * checks for the signal emitted in the <i>opposite</i> direction of the one given.
     *
     */
    @Override
    protected int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(POWERED) && getConnectedDirection(blockState) == side ? 15 : 0;
    }

    /**
     * Returns whether this block is capable of emitting redstone signals.
     *
     */
    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(POWERED)) {
            this.checkPressed(state, level, pos);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && this.type.canButtonBeActivatedByArrows() && !state.getValue(POWERED)) {
            this.checkPressed(state, level, pos);
        }
    }

    protected void checkPressed(BlockState state, Level level, BlockPos pos) {
        AbstractArrow abstractarrow = this.type.canButtonBeActivatedByArrows()
                ? level.getEntitiesOfClass(AbstractArrow.class, state.getShape(level, pos).bounds().move(pos)).stream().findFirst().orElse(null)
                : null;
        boolean flag = abstractarrow != null;
        boolean flag1 = state.getValue(POWERED);
        if (flag != flag1) {
            level.setBlock(pos, state.setValue(POWERED, Boolean.valueOf(flag)), 3);
            this.updateNeighbours(state, level, pos);
            this.playSound(null, level, pos, flag);
            level.gameEvent(abstractarrow, flag ? GameEvent.BLOCK_ACTIVATE : GameEvent.BLOCK_DEACTIVATE, pos);
        }

        if (flag) {
            level.scheduleTick(new BlockPos(pos), this, this.ticksToStayPressed);
        }
    }

    private void updateNeighbours(BlockState state, Level level, BlockPos pos) {
        level.updateNeighborsAt(pos, this);
        level.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, FACE, WATERLOGGED);
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                FluidState fluidstate = context.getLevel().getFluidState(blockpos);
                blockstate = this.defaultBlockState()
                        .setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
                        .setValue(FACING, context.getHorizontalDirection())
                        .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            } else {
                FluidState fluidstate = context.getLevel().getFluidState(blockpos);
                blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, direction.getOpposite())
                        .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
            }

            if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
                return blockstate;
            }
        }

        return null;
    }

    /**
     * Update the provided state given the provided neighbor direction and neighbor state, returning a new state.
     * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately returns its solidified counterpart.
     * Note that this method should ideally consider only the specific direction passed in.
     */
    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
