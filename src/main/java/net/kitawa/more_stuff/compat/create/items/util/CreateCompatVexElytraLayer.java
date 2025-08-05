package net.kitawa.more_stuff.compat.create.items.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.MoreStuff;
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
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;

public class CreateCompatVexElytraLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    private static final ResourceLocation BRASS_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/brass.png");
    private static final ResourceLocation ZINC_ELYTRA_TEXTURE = ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/wings/zinc.png");

    private final ElytraModel<T> elytraModel;

    public CreateCompatVexElytraLayer(RenderLayerParent<T, M> parent, EntityModelSet modelSet) {
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
        if (ModList.get().isLoaded("create")) {
        if (!itemstack.isEmpty() && itemstack.is(CreateCompatItems.ZINC_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(ZINC_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof CreateCompatTeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayerLeather(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

            poseStack.popPose();
        } else if (!itemstack.isEmpty() && itemstack.is(CreateCompatItems.BRASS_ELYTRA)) {
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.080F, 0.05F); // Adjust if necessary for positioning
            poseStack.scale(0.25F, 0.25F, 0.25F);
            this.getParentModel().copyPropertiesTo(this.elytraModel);
            this.elytraModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(BRASS_ELYTRA_TEXTURE), itemstack.hasFoil());
            this.elytraModel.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);

            if (itemstack.getItem() instanceof CreateCompatTeiredElytraItem elytraItem) {
                this.maybeRenderColoredLayer(poseStack, buffer, packedLight, itemstack, elytraItem);
            }

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