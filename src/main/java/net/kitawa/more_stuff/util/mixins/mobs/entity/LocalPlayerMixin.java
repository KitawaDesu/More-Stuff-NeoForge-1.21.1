package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
@OnlyIn(Dist.CLIENT)
public class LocalPlayerMixin {

    @Inject(method = "canStartSprinting", at = @At("HEAD"), cancellable = true)
    private void veilSightAllowSprinting(CallbackInfoReturnable<Boolean> cir) {
        LocalPlayer self = (LocalPlayer)(Object)this;

        boolean result = !self.isSprinting()
                && self.hasEnoughImpulseToStartSprinting()
                && self.hasEnoughFoodToStartSprinting()
                && !self.isUsingItem()
                && (!self.hasEffect(MobEffects.BLINDNESS) || VeilSightHelper.localPlayerHasVeilSight())
                && (!self.isPassenger() || self.vehicleCanSprint(self.getVehicle()))
                && !self.isFallFlying();

        cir.setReturnValue(result);
    }
}
