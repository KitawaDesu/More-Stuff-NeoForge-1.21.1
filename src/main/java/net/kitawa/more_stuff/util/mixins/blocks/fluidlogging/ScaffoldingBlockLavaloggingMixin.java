package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.blocks.util.SimpleFluidLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ScaffoldingBlock;
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

@Mixin(ScaffoldingBlock.class)
public abstract class ScaffoldingBlockLavaloggingMixin extends Block implements SimpleFluidLoggedBlock {

    private static final BooleanProperty LAVALOGGED = ModdedBlockStateProperties.LAVALOGGED;

    public ScaffoldingBlockLavaloggingMixin(Properties properties) {
        super(properties);
    }

    // Inject LAVALOGGED property
    @Inject(method = "createBlockStateDefinition", at = @At("RETURN"))
    private void addLavaloggedProperty(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        builder.add(LAVALOGGED);
    }

    // Default state: LAVALOGGED = false
    @Inject(method = "<init>", at = @At("RETURN"))
    private void injectDefaultState(Properties props, CallbackInfo ci) {
        this.registerDefaultState(
                this.defaultBlockState()
                        .setValue(ScaffoldingBlock.WATERLOGGED, false)
                        .setValue(LAVALOGGED, false)
                        .setValue(ScaffoldingBlock.DISTANCE, 7)
                        .setValue(ScaffoldingBlock.BOTTOM, false)
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        FluidState fluid = level.getFluidState(pos);
        int distance = ScaffoldingBlock.getDistance(level, pos);

        BlockState state = this.defaultBlockState()
                .setValue(ScaffoldingBlock.WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(ScaffoldingBlock.DISTANCE, distance)
                .setValue(ScaffoldingBlock.BOTTOM, distance > 0 && !level.getBlockState(pos.below()).is(this));

        // Only allow LAVALOGGED if this is NOT vanilla scaffolding
        if (this != Blocks.SCAFFOLDING) {
            state = state.setValue(LAVALOGGED, fluid.getType() == Fluids.LAVA);
        }

        return state;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (this != Blocks.SCAFFOLDING && state.getValue(LAVALOGGED)) {
            return Fluids.LAVA.getSource(false);
        } else if (state.getValue(ScaffoldingBlock.WATERLOGGED)) {
            return Fluids.WATER.getSource(false);
        } else {
            return super.getFluidState(state);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (this != Blocks.SCAFFOLDING && state.getValue(LAVALOGGED)) {
            level.scheduleTick(pos, Fluids.LAVA, Fluids.LAVA.getTickDelay(level));
        } else if (state.getValue(ScaffoldingBlock.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!level.isClientSide()) {
            level.scheduleTick(pos, (Block) (Object) this, 1);
        }

        return state;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        int baseLight = super.getLightEmission(state, level, pos);
        return SimpleFluidLoggedBlock.super.getFluidLightEmission(state, baseLight);
    }
}

