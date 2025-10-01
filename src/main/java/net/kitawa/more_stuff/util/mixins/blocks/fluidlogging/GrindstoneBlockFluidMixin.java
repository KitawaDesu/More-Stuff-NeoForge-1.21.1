package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GrindstoneBlock.class)
public abstract class GrindstoneBlockFluidMixin extends FaceAttachedHorizontalDirectionalBlock implements SimpleFluidLoggedBlock {
    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    protected GrindstoneBlockFluidMixin(Properties properties) {
        super(properties);
    }

    // Inject fluid properties into the block state definition
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addFluidProperties(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(WATERLOGGED, LAVALOGGED);
    }

    // Inject fluid state after placement
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState state = super.getStateForPlacement(ctx); // <-- original rotation logic
        FluidState fluid = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return state
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
    }

    // Return the correct fluid state for water/lava logging
    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED)) return Fluids.WATER.getSource(false);
        if (state.getValue(LAVALOGGED)) return Fluids.LAVA.getSource(false);
        return ((Block) (Object) this).defaultBlockState().getFluidState();
    }

    // Inject default state
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(Properties props, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(WATERLOGGED, false)
                        .setValue(LAVALOGGED, false)
        );
    }

    // Schedule fluid ticks when the block updates
    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                  LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        } else if (state.getValue(LAVALOGGED)) {
            world.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(world));
        }
        return super.updateShape(state, dir, neighbor, world, pos, neighborPos);
    }

    // Correct light emission when fluids are present
    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int baseLight = super.getLightEmission(state, level, pos); // call Block's method directly
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}
