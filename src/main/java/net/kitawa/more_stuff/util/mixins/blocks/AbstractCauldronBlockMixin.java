package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.blocks.custom.general.ModdedPointedDripstoneBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractCauldronBlock.class)
public class AbstractCauldronBlockMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        AbstractCauldronBlock self = (AbstractCauldronBlock)(Object)this;

        BlockPos tip = ModdedPointedDripstoneBlock.findStalactiteTipAboveCauldron(level, pos);
        if (tip != null) {
            Fluid fluid = ModdedPointedDripstoneBlock.getCauldronFillFluidType(level, tip);
            if (fluid != Fluids.EMPTY && self.canReceiveStalactiteDrip(fluid)) {
                self.receiveStalactiteDrip(state, level, pos, fluid);
            }
        }
    }
}