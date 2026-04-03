package net.kitawa.more_stuff.entities.monster.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.ColoredSlime;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ColoredSlimeRenderer extends MobRenderer<ColoredSlime, SlimeModel<ColoredSlime>> {

    public ColoredSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public void render(ColoredSlime slime, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Check invisibility
        boolean invisible = slime.isInvisible();

        // Save current shader color
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (invisible) {
            // Fully transparent
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.0F);
        } else {
            // Fully opaque
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        // Call the superclass render method
        super.render(slime, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void scale(ColoredSlime slime, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);

        float size = slime.getSize();
        float squish = Mth.lerp(partialTickTime, slime.oSquish, slime.squish) / (size * 0.5F + 1.0F);
        float inv = 1.0F / (squish + 1.0F);

        poseStack.scale(inv * size, 1.0F / inv * size, inv * size);
    }

    @Override
    public ResourceLocation getTextureLocation(ColoredSlime slime) {
        String color = slime.getColorName();
        return ResourceLocation.fromNamespaceAndPath(
                MoreStuff.MOD_ID,
                "textures/entity/slime/colors/" + color + ".png"
        );
    }

    // --- THIS MAKES THE INNER SLIME MODEL TRANSLUCENT ---
    @Override
    protected RenderType getRenderType(ColoredSlime slime, boolean glowing, boolean translucent, boolean outline) {
        return RenderType.entityTranslucentCull(getTextureLocation(slime));
    }
}
