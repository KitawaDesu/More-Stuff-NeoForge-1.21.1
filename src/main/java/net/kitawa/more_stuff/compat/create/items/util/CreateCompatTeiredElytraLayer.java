package net.kitawa.more_stuff.compat.create.items.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;

public class CreateCompatTeiredElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation BRASS_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/brass.png");
    private static final ResourceLocation BRASS_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/brass_overlay.png");
    private static final ResourceLocation ZINC_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/zinc.png");
    private static final ResourceLocation ZINC_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/zinc_overlay.png");

    private final ElytraModel<T> elytraModel;

    public CreateCompatTeiredElytraLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
        this.elytraModel = new ElytraModel(modelSet.bakeLayer(ModelLayers.ELYTRA));
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
        ItemStack itemstack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
        if (ModList.get().isLoaded("create")) {
        if (!itemstack.isEmpty() && itemstack.is(CreateCompatItems.ZINC_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = ZINC_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = ZINC_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ZINC_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(CreateCompatItems.BRASS_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = BRASS_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = BRASS_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(BRASS_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        }
        }
    }


    private void maybeRenderColoredLayerLeather(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, CreateCompatTeiredElytraItem elytraItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, -6265536);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = elytraItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.elytraModel
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }


    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, CreateCompatTeiredElytraItem elytraItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = elytraItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.elytraModel
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }
}