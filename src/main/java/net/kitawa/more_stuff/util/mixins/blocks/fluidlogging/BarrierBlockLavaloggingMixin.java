package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BarrierBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(BarrierBlock.class)
public abstract class BarrierBlockLavaloggingMixin extends Block implements SimpleFluidLoggedBlock {
    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public BarrierBlockLavaloggingMixin(Properties properties) {
        super(properties);
    }

    // Add LAVALOGGED to block state
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addLavaloggedProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
    }

    // Set default state with LAVALOGGED = false
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(Properties props, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(BarrierBlock.WATERLOGGED, false)
                        .setValue(LAVALOGGED, false)
        );
    }

    // Provide state for placement
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluid = level.getFluidState(pos);

        return this.defaultBlockState()
                .setValue(BarrierBlock.WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(LAVALOGGED)) {
            return Fluids.LAVA.getSource(false);
        } else if (state.getValue(BarrierBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        } else {
            return super.getFluidState(state);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        } else if (state.getValue(BarrierBlock.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int baseLight = super.getLightEmission(state, level, pos); // call Block's method directly
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}
