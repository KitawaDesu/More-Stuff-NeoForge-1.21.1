package net.kitawa.more_stuff.util.mixins.mobs.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CapeLayer.class)
public class CapeRendererMixin {
    @Inject(
            method = "render*",
            at = @At("HEAD"),
            cancellable = true
    )
    public void injectCapeRenderCheck(
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int i,
            AbstractClientPlayer player,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch,
            CallbackInfo ci
    ) {
        ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
        if (itemStack.is(ModItemTags.GLIDERS)) {
            ci.cancel(); // Cancel vanilla cape rendering
        }
    }
}
