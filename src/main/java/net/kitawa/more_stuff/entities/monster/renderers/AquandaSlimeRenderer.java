package net.kitawa.more_stuff.entities.monster.renderers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.AquandaSlime;
import net.kitawa.more_stuff.entities.monster.renderers.layers.BiomeWaterColorOuterLayer;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AquandaSlimeRenderer extends MobRenderer<AquandaSlime, SlimeModel<AquandaSlime>> {
    private static final ResourceLocation SLIME_LOCATION = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/slime/aquanda_slime.png");

    public AquandaSlimeRenderer(EntityRendererProvider.Context context) {
        super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
        // Add outer layer that can also be tinted
        this.addLayer(new BiomeWaterColorOuterLayer<>(this, context.getModelSet()));
    }

    @Override
    public void render(AquandaSlime entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Set the shadow size based on slime size
        this.shadowRadius = 0.25F * (float)entity.getSize();

        // Get biome water color
        Level level = entity.level();
        BlockPos pos = entity.blockPosition();

        int waterColor = BiomeColors.getAverageWaterColor(level, pos);
        float r = ((waterColor >> 16) & 255) / 255.0F;
        float g = ((waterColor >> 8) & 255) / 255.0F;
        float b = (waterColor & 255) / 255.0F;

        // --- Tint inner (main slime body) ---
        RenderSystem.setShaderColor(r, g, b, 1.0F);
        // Render the slime with the tint applied
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        // Reset shader color to default (white)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void scale(AquandaSlime livingEntity, PoseStack poseStack, float partialTickTime) {
        float size = livingEntity.getSize();
        // Your scaling logic
        float scaleFactor = 0.999F;
        poseStack.scale(scaleFactor, scaleFactor, scaleFactor);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        float f1 = size;
        float f2 = Mth.lerp(partialTickTime, livingEntity.oSquish, livingEntity.squish) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        poseStack.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    /**
     * Return the texture location for the slime.
     */
    @Override
    public ResourceLocation getTextureLocation(AquandaSlime entity) {
        return SLIME_LOCATION;
    }
}
