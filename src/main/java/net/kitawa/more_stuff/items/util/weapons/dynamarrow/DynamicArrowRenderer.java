package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DynamicArrowRenderer extends EntityRenderer<DynamicArrowEntity> {

    private static final ResourceLocation DEFAULT_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");

    public DynamicArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(DynamicArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // Rotate based on entity yaw/pitch
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        // Shake effect
        float shake = (float) entity.shakeTime - partialTicks;
        if (shake > 0.0F) {
            float angle = -Mth.sin(shake * 3.0F) * shake;
            poseStack.mulPose(Axis.ZP.rotationDegrees(angle));
        }

        // Standard arrow scaling/tilt
        poseStack.mulPose(Axis.XP.rotationDegrees(45.0F));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0F, 0.0F, 0.0F);

        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));

        // Render the arrow geometry
        renderArrowGeometry(poseStack, vertexConsumer, packedLight);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private void renderArrowGeometry(PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        PoseStack.Pose pose = poseStack.last();

        // Shaft quad
        vertex(pose, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
        vertex(pose, consumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
        vertex(pose, consumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
        vertex(pose, consumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);

        vertex(pose, consumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
        vertex(pose, consumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
        vertex(pose, consumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
        vertex(pose, consumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);

        // Four rotations around shaft for sides
        for (int j = 0; j < 4; j++) {
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose = poseStack.last();

            vertex(pose, consumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            vertex(pose, consumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
            vertex(pose, consumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
            vertex(pose, consumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
        }
    }

    private void vertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z,
                        float u, float v, int normalX, int normalY, int normalZ, int packedLight) {
        consumer.addVertex(pose, (float) x, (float) y, (float) z)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(pose, (float) normalX, (float) normalZ, (float) normalY);
    }

    @Override
    public ResourceLocation getTextureLocation(DynamicArrowEntity entity) {
        // Get the ItemStack representing the arrow
        ItemStack stack = entity.getDefaultPickupItem();
        if (stack == null) return DEFAULT_TEXTURE;

        // Get the component strings from the DataComponents
        String tip = stack.get(ArrowDataComponents.TIP.get());
        String shaft = stack.get(ArrowDataComponents.SHAFT.get());
        String fletching = stack.get(ArrowDataComponents.FLETCHING.get());

        // If all components are missing, fallback to default
        if (tip == null && shaft == null && fletching == null) return DEFAULT_TEXTURE;

        // Compose the texture path for each component individually
        ResourceLocation tipTex = ResourceLocation.fromNamespaceAndPath("more_stuff",
                "textures/entity/arrow/tip/" + (tip != null ? tip : "default") + ".png");
        ResourceLocation shaftTex = ResourceLocation.fromNamespaceAndPath("more_stuff",
                "textures/entity/arrow/shaft/" + (shaft != null ? shaft : "default") + ".png");
        ResourceLocation fletchingTex = ResourceLocation.fromNamespaceAndPath("more_stuff",
                "textures/entity/arrow/fletching/" + (fletching != null ? fletching : "default") + ".png");

        // For now, just return the tip texture as the "main" texture
        // You can modify renderArrowGeometry to render all three separately using these
        return tipTex;
    }
}
