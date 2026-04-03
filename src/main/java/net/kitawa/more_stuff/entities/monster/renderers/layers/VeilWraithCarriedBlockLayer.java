package net.kitawa.more_stuff.entities.monster.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kitawa.more_stuff.entities.monster.VeilWraithEntity;
import net.minecraft.client.model.EndermanModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VeilWraithCarriedBlockLayer extends RenderLayer<VeilWraithEntity, EndermanModel<VeilWraithEntity>> {

    private final BlockRenderDispatcher blockRenderer;

    public VeilWraithCarriedBlockLayer(
            RenderLayerParent<VeilWraithEntity, EndermanModel<VeilWraithEntity>> renderer,
            BlockRenderDispatcher blockRenderer) {
        super(renderer);
        this.blockRenderer = blockRenderer;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       VeilWraithEntity entity, float limbSwing, float limbSwingAmount,
                       float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {

        BlockState blockState = entity.getCarriedBlock();
        if (blockState == null) return;

        float visibility = entity.getVisibility();
        int alpha = (int) Math.max(5, Math.min(255, visibility * 255));

        poseStack.pushPose();
        poseStack.translate(0.0F, 0.6875F, -0.75F);
        poseStack.mulPose(Axis.XP.rotationDegrees(20.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(45.0F));
        poseStack.translate(0.25F, 0.1875F, 0.25F);
        poseStack.scale(-0.5F, -0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        // Wrap the buffer source to inject alpha into every render call
        MultiBufferSource alphaBuffer = renderType -> {
            VertexConsumer consumer = bufferSource.getBuffer(renderType);
            return new AlphaOverrideVertexConsumer(consumer, alpha);
        };

        this.blockRenderer.renderSingleBlock(blockState, poseStack, alphaBuffer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    // Wraps a VertexConsumer and overrides the alpha on every vertex
    private static class AlphaOverrideVertexConsumer implements VertexConsumer {

        private final VertexConsumer delegate;
        private final int alpha;

        AlphaOverrideVertexConsumer(VertexConsumer delegate, int alpha) {
            this.delegate = delegate;
            this.alpha = alpha;
        }

        @Override
        public VertexConsumer setColor(int r, int g, int b, int a) {
            return delegate.setColor(r, g, b, alpha);
        }

        @Override
        public VertexConsumer addVertex(float x, float y, float z) {
            return delegate.addVertex(x, y, z);
        }

        @Override
        public VertexConsumer setUv(float u, float v) {
            return delegate.setUv(u, v);
        }

        @Override
        public VertexConsumer setUv1(int u, int v) {
            return delegate.setUv1(u, v);
        }

        @Override
        public VertexConsumer setUv2(int u, int v) {
            return delegate.setUv2(u, v);
        }

        @Override
        public VertexConsumer setNormal(float nx, float ny, float nz) {
            return delegate.setNormal(nx, ny, nz);
        }
    }
}