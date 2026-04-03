package net.kitawa.more_stuff.entities.monster.renderers.layers;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BiomeWaterColorOuterLayer<T extends LivingEntity> extends SlimeOuterLayer<T> {

    public BiomeWaterColorOuterLayer(RenderLayerParent<T, SlimeModel<T>> renderer, EntityModelSet modelSet) {
        super(renderer, modelSet);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            T livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        Level level = livingEntity.level();
        BlockPos pos = livingEntity.blockPosition();

        int waterColor = BiomeColors.getAverageWaterColor(level, pos);
        float r = ((waterColor >> 16) & 255) / 255.0F;
        float g = ((waterColor >> 8) & 255) / 255.0F;
        float b = (waterColor & 255) / 255.0F;

        RenderSystem.setShaderColor(r, g, b, 1.0F);
        super.render(poseStack, buffer, packedLight, livingEntity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch);
    }
}
