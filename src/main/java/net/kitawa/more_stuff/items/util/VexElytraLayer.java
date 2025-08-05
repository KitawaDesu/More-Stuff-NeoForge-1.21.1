package net.kitawa.more_stuff.items.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;

public class VexElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation LEATHER_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/leather_overlay.png");
    private static final ResourceLocation ELYTRA_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/elytra.png");
    private static final ResourceLocation IRON_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/iron.png");
    private static final ResourceLocation CHAINED_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/chainmail.png");
    private static final ResourceLocation GOLDEN_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/gold.png");
    private static final ResourceLocation COPPER_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/copper.png");
    private static final ResourceLocation ROSE_GOLDEN_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rose_gold.png");
    private static final ResourceLocation DIAMOND_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/diamond.png");
    private static final ResourceLocation EMERALD_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/emerald.png");
    private static final ResourceLocation NETHERITE_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/netherite.png");
    private static final ResourceLocation ROSARITE_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/rosarite.png");

    private final ElytraModel<T> elytraModel;

    public VexElytraLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
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
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(LEATHER_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayerLeather(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(Items.ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.CHAINED_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(CHAINED_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.GOLDEN_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(GOLDEN_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.COPPER_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(COPPER_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.ROSE_GOLDEN_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ROSE_GOLDEN_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.IRON_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(IRON_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.COPPER_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(COPPER_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.DIAMOND_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(DIAMOND_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.EMERALD_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(EMERALD_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.NETHERITE_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(NETHERITE_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(ModItems.ROSARITE_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ROSARITE_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof TeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        }
    }


    private void maybeRenderColoredLayerLeather(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, TeiredElytraItem elytraItem) {
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


    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, TeiredElytraItem elytraItem) {
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
