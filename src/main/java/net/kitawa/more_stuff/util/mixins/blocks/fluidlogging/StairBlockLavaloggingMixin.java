package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;


@Mixin(StairBlock.class)
public abstract class StairBlockLavaloggingMixin extends Block implements SimpleFluidLoggedBlock {
    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public StairBlockLavaloggingMixin(Properties properties) {
        super(properties);
    }

    // Inject LAVALOGGED into block state definition
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addLavaloggedProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
    }

    // Inject default state with LAVALOGGED = false
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(BlockState baseState, Properties properties, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(LAVALOGGED, false)
        );
    }

    // post-process stateForPlacement
    @Inject(method = "getStateForPlacement", at = @At("RETURN"), cancellable = true)
    private void addLavaLogging(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = cir.getReturnValue();
        if (state != null && state.hasProperty(LAVALOGGED)) {
            FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
            cir.setReturnValue(
                    state.setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA)
            );
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(LAVALOGGED)) {
            return Fluids.LAVA.getSource(false);
        } else if (state.getValue(StairBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        } else {
            return super.getFluidState(state);
        }
    }

    @Inject(method = "updateShape", at = @At("RETURN"), cancellable = true)
    private void addLavaUpdateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor world,
                                    BlockPos pos, BlockPos neighborPos, CallbackInfoReturnable<BlockState> cir) {
        BlockState result = cir.getReturnValue();

        if (result != null) {
            if (result.hasProperty(LAVALOGGED) && result.getValue(LAVALOGGED)) {
                world.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(world));
            } else if (result.hasProperty(StairBlock.WATERLOGGED) && result.getValue(StairBlock.WATERLOGGED)) {
                world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
            }
        }

        cir.setReturnValue(result);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // use the block's default light value
        int baseLight = super.getLightEmission(state, level, pos);
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}
