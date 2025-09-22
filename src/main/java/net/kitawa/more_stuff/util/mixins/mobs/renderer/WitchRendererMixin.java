package net.kitawa.more_stuff.util.mixins.mobs.renderer;

import net.kitawa.more_stuff.util.mob_armor.layers.WitchArmorLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitchRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WitchRenderer.class)
@OnlyIn(Dist.CLIENT)
public abstract class WitchRendererMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", // Check the constructor signature here
            at = @At("TAIL")
    )
    private void init(EntityRendererProvider.Context p_174443_, CallbackInfo ci) {
        // Cast 'this' to WitchRenderer (the correct renderer type)
        WitchRenderer witchRenderer = (WitchRenderer) (Object) this;

        // Add the custom armor layer to the renderer
        witchRenderer.addLayer(new WitchArmorLayer(witchRenderer, p_174443_.getModelSet(), p_174443_.getModelManager()));
    }
}
