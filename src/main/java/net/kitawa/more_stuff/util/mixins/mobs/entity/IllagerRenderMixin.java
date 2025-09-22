package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.mob_armor.layers.IllagerArmorLayer;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(IllagerRenderer.class)
public abstract class IllagerRenderMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;Lnet/minecraft/client/model/IllagerModel;F)V", // Match the constructor with context, model, and shadow radius
            at = @At("TAIL")  // Inject at the end of the constructor
    )
    private void init(EntityRendererProvider.Context context, IllagerModel model, float shadowRadius, CallbackInfo ci) {
        // Cast 'this' to IllagerRenderer
        IllagerRenderer illagerRenderer = (IllagerRenderer) (Object) this;

        // Add the custom armor layer to the renderer
        illagerRenderer.addLayer(new IllagerArmorLayer(illagerRenderer, context.getModelSet(), context.getModelManager()));
    }
}
