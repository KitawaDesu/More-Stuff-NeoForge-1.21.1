package net.kitawa.more_stuff.blocks.custom.frostbitten_caverns;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class IceSheetBlock extends MultifaceBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);

    private final float customFriction;

    public static final MapCodec<IceSheetBlock> CODEC =
            simpleCodec(props -> new IceSheetBlock(0.98F, props));

    private final MultifaceSpreader spreader = new MultifaceSpreader(this);

    public IceSheetBlock(float customFriction, BlockBehaviour.Properties properties) {
        super(properties);
        this.customFriction = customFriction;

        // Register *all* face properties + WATERLOGGED
        BlockState state = this.stateDefinition.any()
                .setValue(WATERLOGGED, Boolean.FALSE);
        for (Direction dir : Direction.values()) {
            state = state.setValue(getFaceProperty(dir), false);
        }
        this.registerDefaultState(state);
    }

    public float getCustomFriction() {
        return customFriction;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
        // Only needed if you don’t rely solely on the mixin
        if (entity instanceof LivingEntity && entity.onGround()) {
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(
                    this.getCustomFriction(), 1.0D, this.getCustomFriction()
            ));
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED)
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        FluidState neighborFluid = neighborState.getFluidState();

        // If water or lava is adjacent -> vanish
        if (neighborFluid.is(Fluids.FLOWING_WATER) || neighborFluid.is(Fluids.FLOWING_LAVA)) {
            return Blocks.AIR.defaultBlockState();
        }

        if (!hasAnyFace(state)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            return hasFace(state, direction) && !canAttachTo(level, direction, neighborPos, neighborState)
                    ? removeFace(state, getFaceProperty(direction))
                    : state;
        }
    }

    private static BlockState removeFace(BlockState state, BooleanProperty faceProp) {
        BlockState blockstate = state.setValue(faceProp, Boolean.valueOf(false));
        return hasAnyFace(blockstate) ? blockstate : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        boolean flag = false;

        for (Direction direction : DIRECTIONS) {
            if (hasFace(state, direction)) {
                BlockPos blockpos = pos.relative(direction);
                if (!canAttachTo(level, direction, blockpos, level.getBlockState(blockpos))) {
                    return false;
                }

                flag = true;
            }
        }

        return flag;
    }

    public boolean isValidStateForPlacement(BlockGetter level, BlockState state, BlockPos pos, Direction direction) {
        if (this.isFaceSupported(direction) && (!state.is(this) || !hasFace(state, direction))) {
            BlockPos blockpos = pos.relative(direction);
            return canAttachTo(level, direction, blockpos, level.getBlockState(blockpos));
        } else {
            return false;
        }
    }

    public static boolean canAttachTo(BlockGetter level, Direction direction, BlockPos pos, BlockState state) {
        return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction.getOpposite())
                || Block.isFaceFull(state.getCollisionShape(level, pos), direction.getOpposite())
                || (direction == Direction.DOWN && state.getFluidState().is(Fluids.WATER));
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    public MultifaceSpreader getSpreader() {
        return spreader;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        // Check directly if the neighbor is water or lava
        FluidState neighborFluid = level.getFluidState(fromPos);
        if (neighborFluid.is(Fluids.FLOWING_WATER) || neighborFluid.is(Fluids.FLOWING_LAVA)) {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }
}



