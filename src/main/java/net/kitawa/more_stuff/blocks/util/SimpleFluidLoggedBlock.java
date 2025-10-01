package net.kitawa.more_stuff.blocks.util;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;

public interface SimpleFluidLoggedBlock extends SimpleWaterloggedBlock {

    @Override
    default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.LAVA;
    }

    @Override
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        Fluid fluid = fluidState.getType();

        if (fluid == Fluids.WATER && !state.getValue(BlockStateProperties.WATERLOGGED)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
            }
            return true;
        } else if (fluid == Fluids.LAVA && !state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(ModdedBlockStateProperties.LAVALOGGED, true), 3);
                level.scheduleTick(pos, fluid, fluid.getTickDelay(level));
            }
            return true;
        }

        return false;
    }

    @Override
    default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            level.setBlock(pos, state.setValue(BlockStateProperties.WATERLOGGED, false), 3);
            if (!state.canSurvive(level, pos)) level.destroyBlock(pos, true);
            return new ItemStack(Items.WATER_BUCKET);
        } else if (state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {
            level.setBlock(pos, state.setValue(ModdedBlockStateProperties.LAVALOGGED, false), 3);
            if (!state.canSurvive(level, pos)) level.destroyBlock(pos, true);
            return new ItemStack(Items.LAVA_BUCKET);
        }

        return ItemStack.EMPTY;
    }

    @Override
    default Optional<SoundEvent> getPickupSound(BlockState state) {
        if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            return Fluids.WATER.getPickupSound();
        } else if (state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {
            return Fluids.LAVA.getPickupSound();
        }
        return Fluids.WATER.getPickupSound();
    }

    /**
     * Helper to get light emission based on fluid state.
     * Call this in your Block's getLightEmission method.
     */
    default int getFluidLightEmission(BlockState state, int defaultLight) {
        if (state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {
            return 15; // lava always full light
        } else if (state.getValue(BlockStateProperties.WATERLOGGED)) {
            return defaultLight; // keep original light if waterlogged
        }
        return defaultLight; // normal light otherwise
    }
}
