package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.experimentals.items.entity.ThrownJavelin;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PointedDripstoneBlock.class)
public class PointedDripstoneBlockMixinJavelinHit {

    @Inject(
            method = "onProjectileHit",
            at = @At("HEAD"),
            cancellable = true
    )
    private void morestuff$allowTridentOrJavelin(
            Level level,
            BlockState state,
            BlockHitResult hit,
            Projectile projectile,
            CallbackInfo ci
    ) {
        if (!level.isClientSide) {
            BlockPos blockpos = hit.getBlockPos();

            boolean isValidProjectile =
                    projectile instanceof ThrownTrident || projectile instanceof ThrownJavelin;

            if (isValidProjectile
                    && projectile.mayInteract(level, blockpos)
                    && projectile.mayBreak(level)
                    && projectile.getDeltaMovement().length() > 0.6) {
                level.destroyBlock(blockpos, true);
                ci.cancel(); // stop vanilla from re-running its own check
            }
        }
    }
}
