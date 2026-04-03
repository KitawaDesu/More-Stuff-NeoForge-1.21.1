package net.kitawa.more_stuff.entities.monster.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.VeilStalkerEntity;
import net.kitawa.more_stuff.entities.monster.renderers.models.VeilStalkerModel;
import net.kitawa.more_stuff.util.helpers.VeilSightHelper;
import net.kitawa.more_stuff.util.mob_armor.ModModelLayers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VeilStalkerRenderer extends MobRenderer<VeilStalkerEntity, VeilStalkerModel<VeilStalkerEntity>> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/veil_stalker.png");

    public VeilStalkerRenderer(EntityRendererProvider.Context context) {
        super(context, new VeilStalkerModel<>(context.bakeLayer(ModModelLayers.VEIL_STALKER)), 0.5F);
        this.addLayer(new VeilStalkerAlphaLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VeilStalkerEntity entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(VeilStalkerEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    protected RenderType getRenderType(VeilStalkerEntity entity, boolean bodyVisible,
                                       boolean translucent, boolean glowing) {
        // Return null to suppress the default model render — our layer handles it
        return null;
    }

    @Override
    protected void renderNameTag(VeilStalkerEntity entity, Component displayName,
                                 PoseStack poseStack, MultiBufferSource bufferSource,
                                 int packedLight, float partialTick) {
        if (entity.getVisibility() > 0.5f) {
            super.renderNameTag(entity, displayName, poseStack, bufferSource, packedLight, partialTick);
        }
    }

    // Layer that renders the model with dynamic alpha
    static class VeilStalkerAlphaLayer extends RenderLayer<VeilStalkerEntity, VeilStalkerModel<VeilStalkerEntity>> {

        public VeilStalkerAlphaLayer(RenderLayerParent<VeilStalkerEntity, VeilStalkerModel<VeilStalkerEntity>> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                           VeilStalkerEntity entity, float limbSwing, float limbSwingAmount,
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
}