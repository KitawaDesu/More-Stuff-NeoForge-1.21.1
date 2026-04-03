package net.kitawa.more_stuff.util.mixins.rendering;

import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.renderer.LightTexture;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
@OnlyIn(Dist.CLIENT)
public class LightTextureMixin {

    @Inject(method = "getDarknessGamma", at = @At("HEAD"), cancellable = true)
    private void veilSightNullifyDarknessGamma(float partialTick, CallbackInfoReturnable<Float> cir) {
        if (VeilSightHelper.localPlayerHasVeilSight()) {
            cir.setReturnValue(0.0F);
        }
    }
}