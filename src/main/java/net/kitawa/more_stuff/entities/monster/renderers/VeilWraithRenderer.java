package net.kitawa.more_stuff.entities.monster.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.VeilWraithEntity;
import net.kitawa.more_stuff.entities.monster.renderers.layers.VeilWraithCarriedBlockLayer;
import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CarriedBlockLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VeilWraithRenderer extends MobRenderer<VeilWraithEntity, EndermanModel<VeilWraithEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/veil_wraith/veil_wraith.png");
    private static final ResourceLocation EYES_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/veil_wraith/veil_wraith_eyes.png");

    private final RandomSource random = RandomSource.create();

    public VeilWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new EndermanModel<>(context.bakeLayer(ModelLayers.ENDERMAN)), 0.5F);
        this.addLayer(new VeilWraithAlphaLayer(this));
        this.addLayer(new VeilWraithEyesLayer(this));
        this.addLayer(new VeilWraithCarriedBlockLayer(this, context.getBlockRenderDispatcher()));
    }

    @Override
    public void render(VeilWraithEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        EndermanModel<VeilWraithEntity> model = this.getModel();
        model.carrying = entity.getCarriedBlock() != null;
        model.creepy = entity.isCreepy();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public Vec3 getRenderOffset(VeilWraithEntity entity, float partialTicks) {
        if (entity.isCreepy()) {
            double d0 = 0.02 * (double) entity.getScale();
            return new Vec3(this.random.nextGaussian() * d0, 0.0, this.random.nextGaussian() * d0);
        }
        return super.getRenderOffset(entity, partialTicks);
    }

    @Override
    protected RenderType getRenderType(VeilWraithEntity entity, boolean bodyVisible,
                                       boolean translucent, boolean glowing) {
        // Suppress default render — VeilWraithAlphaLayer handles it with dynamic alpha
        return null;
    }

    @Override
    protected void renderNameTag(VeilWraithEntity entity, Component displayName,
                                 PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, float partialTick) {
        if (entity.getVisibility() > 0.5f) {
            super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight, partialTick);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(VeilWraithEntity entity) {
        return TEXTURE;
    }

    // ---------------------------
    // ALPHA LAYER
    // ---------------------------

    static class VeilWraithAlphaLayer extends RenderLayer<VeilWraithEntity, EndermanModel<VeilWraithEntity>> {

        public VeilWraithAlphaLayer(RenderLayerParent<VeilWraithEntity, EndermanModel<VeilWraithEntity>> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                           VeilWraithEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

            float visibility = VeilSightHelper.localPlayerHasVeilSight()
                    ? 1.0f
                    : entity.getVisibility();
            int alpha = (int) Math.max(5, Math.min(255, visibility * 255));
            int color = (alpha << 24) | 0x00FFFFFF;

            // Use the passed-in bufferSource — never grab rawSource directly,
            // it bypasses the shader pipeline and breaks under Iris/Optifine
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucentCull(TEXTURE));

            this.getParentModel().renderToBuffer(poseStack, consumer, packedLight,
                    OverlayTexture.NO_OVERLAY, color);
        }
    }

    // ---------------------------
    // EYES LAYER
    // ---------------------------

    static class VeilWraithEyesLayer extends RenderLayer<VeilWraithEntity, EndermanModel<VeilWraithEntity>> {

        public VeilWraithEyesLayer(RenderLayerParent<VeilWraithEntity, EndermanModel<VeilWraithEntity>> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                           VeilWraithEntity entity, float limbSwing, float limbSwingAmount,
                           float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

            // Eyes always fully opaque and fullbright regardless of body visibility
            VertexConsumer consumer = bufferSource.getBuffer(RenderType.eyes(EYES_TEXTURE));

            this.getParentModel().renderToBuffer(poseStack, consumer,
                    LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1);
        }
    }
}