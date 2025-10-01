package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
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

@Mixin(ChainBlock.class)
public abstract class ChainBlockLavaMixin extends RotatedPillarBlock implements SimpleWaterloggedBlock, SimpleFluidLoggedBlock {

    @Shadow @Final
    public static BooleanProperty WATERLOGGED;

    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public ChainBlockLavaMixin(Properties props) {
        super(props);
    }

    /**
     * Add LAVALOGGED to blockstate definition.
     */
    @Inject(method = "createBlockStateDefinition", at = @At("TAIL"))
    private void addLavaProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
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
     * Update shape: tick lava like waterlogging does.
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

    /**
     * Add fluid state for lava.
     */
    @Inject(method = "getFluidState", at = @At("HEAD"), cancellable = true)
    private void injectFluidState(BlockState state, CallbackInfoReturnable<FluidState> cir) {
        if (state.getValue(LAVALOGGED)) {
            cir.setReturnValue(Fluids.LAVA.getSource(false));
        }
    }

    /**
     * Set lavalogged/waterlogged on placement.
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
        int baseLight = super.getLightEmission(state, level, pos); // call Block's method directly
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}