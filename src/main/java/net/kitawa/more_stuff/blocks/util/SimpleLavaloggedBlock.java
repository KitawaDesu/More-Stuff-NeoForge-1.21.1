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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.Optional;

public interface SimpleLavaloggedBlock extends BucketPickup, LiquidBlockContainer {
    @Override
    default boolean canPlaceLiquid(@Nullable Player player, BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return fluid == Fluids.LAVA;
    }

    @Override
    default boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(ModdedBlockStateProperties.LAVALOGGED) && fluidState.getType() == Fluids.LAVA) {
            if (!level.isClientSide()) {
                level.setBlock(pos, state.setValue(ModdedBlockStateProperties.LAVALOGGED, Boolean.valueOf(true)), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    default ItemStack pickupBlock(@Nullable Player player, LevelAccessor level, BlockPos pos, BlockState state) {
        if (state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {
            level.setBlock(pos, state.setValue(ModdedBlockStateProperties.LAVALOGGED, Boolean.valueOf(false)), 3);
            if (!state.canSurvive(level, pos)) {
                level.destroyBlock(pos, true);
            }

            return new ItemStack(Items.LAVA_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    default Optional<SoundEvent> getPickupSound() {
        return Fluids.LAVA.getPickupSound();
    }
}
