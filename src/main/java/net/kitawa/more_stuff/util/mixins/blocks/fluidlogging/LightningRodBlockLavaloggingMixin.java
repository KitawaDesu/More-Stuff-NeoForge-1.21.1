package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.RodBlock;
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

@Mixin(LightningRodBlock.class)
public abstract class LightningRodBlockLavaloggingMixin extends RodBlock implements SimpleFluidLoggedBlock {
    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public LightningRodBlockLavaloggingMixin(Properties properties) {
        super(properties);
    }

    // Add LAVALOGGED to the block state
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addLavaloggedProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
    }

    // Set default state with LAVALOGGED = false
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(Properties props, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(LightningRodBlock.FACING, Direction.UP)
                        .setValue(LightningRodBlock.WATERLOGGED, false)
                        .setValue(LightningRodBlock.POWERED, false)
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
                .setValue(LightningRodBlock.FACING, context.getClickedFace())
                .setValue(LightningRodBlock.WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(LightningRodBlock.POWERED, false)
                .setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (state.getValue(LAVALOGGED)) return Fluids.LAVA.getSource(false);
        if (state.getValue(LightningRodBlock.WATERLOGGED)) return Fluids.WATER.getSource(false);
        return super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor,
                                  LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(LAVALOGGED)) {
            world.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(world));
        } else if (state.getValue(LightningRodBlock.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        return super.updateShape(state, dir, neighbor, world, pos, neighborPos);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        // use the block's default light value
        int baseLight = super.getLightEmission(state, level, pos);
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}
