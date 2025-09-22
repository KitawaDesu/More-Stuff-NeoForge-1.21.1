package net.kitawa.more_stuff.items.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
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

public class TeiredElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation LEATHER_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/leather.png");
    private static final ResourceLocation LEATHER_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/leather_overlay.png");
    private static final ResourceLocation IRON_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/iron.png");
    private static final ResourceLocation IRON_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/iron_overlay.png");
    private static final ResourceLocation CHAINED_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/chainmail.png");
    private static final ResourceLocation CHAINED_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/chainmail_overlay.png");
    private static final ResourceLocation GOLDEN_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/gold.png");
    private static final ResourceLocation GOLDEN_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/gold_overlay.png");
    private static final ResourceLocation COPPER_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/copper.png");
    private static final ResourceLocation COPPER_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/copper_overlay.png");
    private static final ResourceLocation ROSE_GOLDEN_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rose_gold.png");
    private static final ResourceLocation ROSE_GOLDEN_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rose_gold_overlay.png");
    private static final ResourceLocation DIAMOND_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/diamond.png");
    private static final ResourceLocation DIAMOND_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/diamond_overlay.png");
    private static final ResourceLocation EMERALD_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/emerald.png");
    private static final ResourceLocation EMERALD_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/emerald_overlay.png");
    private static final ResourceLocation NETHERITE_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/netherite.png");
    private static final ResourceLocation NETHERITE_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/netherite_overlay.png");
    private static final ResourceLocation ROSARITE_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rosarite.png");
    private static final ResourceLocation ROSARITE_ELYTRA_OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rosarite_overlay.png");

    private final ElytraModel<T> elytraModel;

    public TeiredElytraLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
        super(parent);
        this.elytraModel = new ElytraModel<>(modelSet.bakeLayer(ModelLayers.ELYTRA));
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
        if (!itemstack.isEmpty() && itemstack.is(ModItems.LEATHER_GLIDER)) {
            ResourceLocation resourcelocation;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    resourcelocation = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    resourcelocation = playerskin.capeTexture();
                } else {
                    resourcelocation = LEATHER_ELYTRA_TEXTURE;
                }
            } else {
                resourcelocation = LEATHER_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Default elytra render
            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(
                    buffer, RenderType.armorCutoutNoCull(LEATHER_ELYTRA_OVERLAY_TEXTURE), itemstack.hasFoil()
            );
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            // 💡 New: Dyeable elytra overlay render (if applicable)
            if (itemstack.is(ItemTags.DYEABLE)) {
                int i = DyedItemColor.getOrDefault(itemstack, -6265536);
                if (FastColor.ARGB32.alpha(i) == 0) {
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

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.CHAINED_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = CHAINED_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = CHAINED_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
                VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(CHAINED_ELYTRA_OVERLAY_TEXTURE), false);
                this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.GOLDEN_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = GOLDEN_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = GOLDEN_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(GOLDEN_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.COPPER_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = COPPER_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = COPPER_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(COPPER_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.ROSE_GOLDEN_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = ROSE_GOLDEN_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = ROSE_GOLDEN_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ROSE_GOLDEN_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.IRON_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = IRON_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = IRON_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(IRON_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.EMERALD_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = EMERALD_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = EMERALD_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(EMERALD_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.DIAMOND_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = DIAMOND_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = DIAMOND_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(DIAMOND_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.NETHERITE_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = NETHERITE_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = NETHERITE_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(NETHERITE_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.ROSARITE_ELYTRA)) {
            ResourceLocation primaryTexture;
            if (livingEntity instanceof AbstractClientPlayer abstractclientplayer) {
                PlayerSkin playerskin = abstractclientplayer.getSkin();
                if (playerskin.elytraTexture() != null) {
                    primaryTexture = playerskin.elytraTexture();
                } else if (playerskin.capeTexture() != null && abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    primaryTexture = playerskin.capeTexture();
                } else {
                    primaryTexture = ROSARITE_ELYTRA_TEXTURE;
                }
            } else {
                primaryTexture = ROSARITE_ELYTRA_TEXTURE;
            }

            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // Render primary layer
            VertexConsumer primaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(primaryTexture), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, primaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
            VertexConsumer secondaryVertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ROSARITE_ELYTRA_OVERLAY_TEXTURE), false);
            this.elytraModel.renderToBuffer(poseStack, secondaryVertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

            poseStack.popPose();
        }
    }
}