package net.kitawa.more_stuff.util.mixins.blocks.fluidlogging;

import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FallingBlockEntity.class)
public abstract class FallingBlockEntityLavaMixin {

    // Strip lava when falling starts (like waterlogged)
    @ModifyArg(
            method = "fall(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/entity/item/FallingBlockEntity;",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;<init>(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/level/block/state/BlockState;)V"
            ),
            index = 4
    )
    private static BlockState morestuff$stripLavaLogged(BlockState state) {
        if (state.hasProperty(ModdedBlockStateProperties.LAVALOGGED)) {
            return state.setValue(ModdedBlockStateProperties.LAVALOGGED, false);
        }
        return state;
    }

    // Restore lava at placement (modify the state passed into Level.setBlock)
    @ModifyArg(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            ),
            index = 1
    )
    private BlockState morestuff$restoreLavaLogging(BlockState state) {
        FallingBlockEntity self = (FallingBlockEntity)(Object)this;
        Level level = self.level();
        BlockPos pos = self.blockPosition();

        if (state.hasProperty(ModdedBlockStateProperties.LAVALOGGED)
                && level.getFluidState(pos).getType() == Fluids.LAVA) {
            return state.setValue(ModdedBlockStateProperties.LAVALOGGED, true);
        }

        return state;
    }
}

