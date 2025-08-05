package net.kitawa.more_stuff.blocks.custom.aquanda_biome_blocks;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.worldgen.tree.ModTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

import static net.minecraft.tags.FluidTags.WATER;

public class AquandaBlock extends BushBlock implements BonemealableBlock, SimpleWaterloggedBlock {
    public static final MapCodec<AquandaBlock> CODEC = simpleCodec(AquandaBlock::new);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Shapes.or(Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0), Block.box(6.0, 0.0, 6.0, 10.0, 8.0, 10.0));

    @Override
    public MapCodec<AquandaBlock> codec() {
        return CODEC;
    }

    public AquandaBlock(BlockBehaviour.Properties p_152067_) {
        super(p_152067_);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected VoxelShape getShape(BlockState p_152084_, BlockGetter p_152085_, BlockPos p_152086_, CollisionContext p_152087_) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_152089_, BlockGetter p_152090_, BlockPos p_152091_) {
        return p_152089_.is(ModBlocks.AQUANDA_GEL) || p_152089_.is(ModBlocks.GLOWING_AQUANDA_GEL) || p_152089_.is(ModBlocks.AQUANDA_MOSS_BLOCK) || super.mayPlaceOn(p_152089_, p_152090_, p_152091_);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader p_256329_, BlockPos p_256107_, BlockState p_255771_) {
        return p_256329_.getFluidState(p_256107_.above()).is(WATER) || p_256329_.getFluidState(p_256107_.above()).isEmpty();
    }

    @Override
    public boolean isBonemealSuccess(Level p_220712_, RandomSource p_220713_, BlockPos p_220714_, BlockState p_220715_) {
        return (double) p_220712_.random.nextFloat() < 0.45;
    }

    @Override
    public void performBonemeal(ServerLevel p_220707_, RandomSource p_220708_, BlockPos p_220709_, BlockState p_220710_) {
        ModTreeGrowers.AQUANDA.growTree(p_220707_, p_220707_.getChunkSource().getGenerator(), p_220709_, p_220710_, p_220708_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.isSuppressingBounce()) {
            super.fallOn(level, state, pos, entity, fallDistance);
        } else {
            entity.causeFallDamage(fallDistance, 0.0F, level.damageSources().fall());
        }
    }

    /**
     * Called when an Entity lands on this Block.
     * This method is responsible for doing any modification on the motion of the entity that should result from the landing.
     */
    @Override
    public void updateEntityAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y < 0.0) {
            double d0 = entity instanceof LivingEntity ? 1.0 : 0.8;
            entity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        double d0 = Math.abs(entity.getDeltaMovement().y);
        if (d0 < 0.1 && !entity.isSteppingCarefully()) {
            double d1 = 0.4 + d0 * 0.2;
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(d1, 1.0, d1));
        }

        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
    }
}
