package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CrossCollisionBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronBarsBlock.class)
public abstract class IronBarsLavaMixin extends CrossCollisionBlock implements SimpleWaterloggedBlock, SimpleFluidLoggedBlock {


    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public IronBarsLavaMixin(Properties props) {
        super(1.0F, 1.0F, 16.0F, 16.0F, 16.0F, props);
    }

    /**
     * Add LAVALOGGED property to state definition.
     */
    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void addLavaProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
    }

    /**
     * Schedule lava/water ticks.
     */
    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void injectUpdateShape(BlockState state, Direction facing, BlockState neighborState,
                                   LevelAccessor level, BlockPos pos, BlockPos neighborPos,
                                   CallbackInfoReturnable<BlockState> cir) {
        BlockState result = cir.getReturnValue();

        if (state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        } else if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        cir.setReturnValue(result);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(Properties props, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(WATERLOGGED, false)
                        .setValue(LAVALOGGED, false)
        );
    }

    /**
     * Return correct fluid state for lava.
     */
    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
        if (state.getValue(LAVALOGGED)) return Fluids.LAVA.getSource(false);
        return super.getFluidState(state);
    }

    /**
     * Handle placement: set waterlogged/lavalogged flags properly.
     */
    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void injectPlacement(BlockPlaceContext ctx, CallbackInfoReturnable<BlockState> cir) {
        BlockState result = cir.getReturnValue();
        if (result == null) return;

        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());

        result = result
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);

        cir.setReturnValue(result);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int baseLight = super.getLightEmission(state, level, pos);
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}