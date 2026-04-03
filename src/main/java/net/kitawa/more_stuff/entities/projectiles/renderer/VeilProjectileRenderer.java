package net.kitawa.more_stuff.entities.projectiles.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.projectiles.VeilProjectile;
import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VeilProjectileRenderer extends EntityRenderer<VeilProjectile> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/veil_spark.png");

    private final ShulkerBulletModel<VeilProjectile> model;

    public VeilProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ShulkerBulletModel<>(context.bakeLayer(ModelLayers.SHULKER_BULLET));
    }

    @Override
    protected int getBlockLightLevel(VeilProjectile entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(VeilProjectile entity) {
        return TEXTURE;
    }

    @Override
    public void render(VeilProjectile entity, float entityYaw, float partialTick,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        float f2  = (float) entity.tickCount + partialTick;
        float yRot = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
        float xRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());

        poseStack.translate(0.0f, 0.15f, 0.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1f) * 180.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1f) * 180.0f));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.15f) * 360.0f));
        poseStack.scale(-0.5f, -0.5f, 0.5f);

        this.model.setupAnim(entity, 0.0f, 0.0f, 0.0f, yRot, xRot);

        // Veil Sight: if the local player has the enchantment, always render fully visible
        float visibility = VeilSightHelper.localPlayerHasVeilSight()
                ? 1.0f
                : entity.getVisibility();
        int alpha      = (int) Math.max(5, Math.min(255, visibility * 255));
        int innerColor = (alpha << 24) | 0x00FFFFFF;
        int outerColor = ((alpha / 2) << 24) | 0x00FFFFFF;

        // Use the passed-in bufferSource and entityTranslucentCull —
        // avoids bypassing the shader pipeline under Iris/Optifine
        VertexConsumer inner = bufferSource.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        this.model.renderToBuffer(poseStack, inner, packedLight,
                OverlayTexture.NO_OVERLAY, innerColor);

        poseStack.scale(1.5f, 1.5f, 1.5f);

        VertexConsumer outer = bufferSource.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        this.model.renderToBuffer(poseStack, outer, packedLight,
                OverlayTexture.NO_OVERLAY, outerColor);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}

