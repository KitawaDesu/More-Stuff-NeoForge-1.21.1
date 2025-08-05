package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.util.mob_armor.layers.VillagerArmorLayer;
import net.kitawa.more_stuff.util.mob_armor.layers.WitchArmorLayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.client.renderer.entity.WitchRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VillagerRenderer.class)
public class VillagerRendererMixin {

    @Inject(
            method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", // Check the constructor signature here
            at = @At("TAIL")
    )
    private void init(EntityRendererProvider.Context p_174443_, CallbackInfo ci) {
        // Cast 'this' to WitchRenderer (the correct renderer type)
        VillagerRenderer villagerRenderer = (VillagerRenderer) (Object) this;

        // Add the custom armor layer to the renderer
        villagerRenderer.addLayer(new VillagerArmorLayer(villagerRenderer, p_174443_.getModelSet(), p_174443_.getModelManager()));
    }
}
