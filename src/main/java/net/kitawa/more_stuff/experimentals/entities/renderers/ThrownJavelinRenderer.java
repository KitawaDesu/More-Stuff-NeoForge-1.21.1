package net.kitawa.more_stuff.experimentals.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kitawa.more_stuff.experimentals.entities.models.ThrownJavelinModel;
import net.kitawa.more_stuff.experimentals.items.entity.ThrownJavelin;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ThrownJavelinRenderer extends EntityRenderer<ThrownJavelin> {
    private final ThrownJavelinModel model;

    public ThrownJavelinRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ThrownJavelinModel(context.bakeLayer(ThrownJavelinModel.SPEAR_LAYER));
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownJavelin entity) {
        return entity.getTexture();
    }

    @Override
    public void render(ThrownJavelin entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Rotate spear to match entity rotation
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F;
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F;
        poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
        poseStack.mulPose(Axis.ZP.rotationDegrees(pitch));

        // Render the model using the entity's texture
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(
                buffer,
                model.renderType(getTextureLocation(entity)),
                false,
                entity.isFoil()
        );

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
