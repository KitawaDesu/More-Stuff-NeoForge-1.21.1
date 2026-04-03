package net.kitawa.more_stuff.entities.monster.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.ZombieWolf;
import net.kitawa.more_stuff.entities.monster.renderers.layers.ZombieWolfArmorLayer;
import net.kitawa.more_stuff.entities.monster.renderers.models.ZombieWolfModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ZombieWolfRenderer extends MobRenderer<ZombieWolf, ZombieWolfModel<ZombieWolf>> {

    public ZombieWolfRenderer(EntityRendererProvider.Context context) {
        super(context, new ZombieWolfModel<>(context.bakeLayer(ModelLayers.WOLF)), 0.5F);

        // Non-generic version (your environment)
        this.addLayer(new ZombieWolfArmorLayer(this, context.getModelSet()));
    }

    @Override
    public void render(ZombieWolf entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieWolf wolf) {
        return ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/zombie_wolf/zombie_wolf.png");
    }
}