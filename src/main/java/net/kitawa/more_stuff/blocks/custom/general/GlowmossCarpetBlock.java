package net.kitawa.more_stuff.blocks.custom.general;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public class GlowmossCarpetBlock extends CarpetBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final int MAX_LEVEL = 15;
    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;
    public static final ToIntFunction<BlockState> LIGHT_EMISSION = s -> s.getValue(LEVEL);

    public GlowmossCarpetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(LEVEL, 3) // start at minimum glow
                        .setValue(WATERLOGGED, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED, LEVEL);
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
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        // waterlogged check
        FluidState fluidstate = level.getFluidState(pos);
        boolean waterlogged = fluidstate.getType() == Fluids.WATER;

        // calculate light level from below
        BlockPos below = pos.below();
        BlockState belowState = level.getBlockState(below);

        int belowLight = 0;
        if (!(belowState.getBlock() instanceof GlowmossCarpetBlock)) {
            belowLight = belowState.getLightEmission(level, below);
        }

        // clamp between 3 and 15
        int newLevel = Mth.clamp(Math.max(3, belowLight), 3, MAX_LEVEL);

        return this.defaultBlockState()
                .setValue(WATERLOGGED, waterlogged)
                .setValue(LEVEL, newLevel);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        // keep LEVEL synced dynamically
        BlockPos below = currentPos.below();
        BlockState belowState = level.getBlockState(below);

        int belowLight = 0;
        if (!(belowState.getBlock() instanceof GlowmossCarpetBlock)) {
            belowLight = belowState.getLightEmission(level, below);
        }

        int newLevel = Mth.clamp(Math.max(3, belowLight), 3, MAX_LEVEL);

        if (state.getValue(LEVEL) != newLevel) {
            return state.setValue(LEVEL, newLevel);
        }

        return super.updateShape(state, facing, facingState, level, currentPos, facingPos);
    }
}
