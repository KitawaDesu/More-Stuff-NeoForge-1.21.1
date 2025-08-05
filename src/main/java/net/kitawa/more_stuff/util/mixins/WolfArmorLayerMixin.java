package net.kitawa.more_stuff.util.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatAnimalArmorItem;
import net.kitawa.more_stuff.items.util.ModdedAnimalArmorItem;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.WolfArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.*;

import java.util.Map;

@Mixin(WolfArmorLayer.class)
public abstract class WolfArmorLayerMixin extends RenderLayer<Wolf, WolfModel<Wolf>> {
    @Mutable
    @Final
    @Shadow private final WolfModel<Wolf> model;
    public WolfArmorLayerMixin(RenderLayerParent<Wolf, WolfModel<Wolf>> renderer, WolfModel<Wolf> model) {
        super(renderer);
        this.model = model;
    }
    @Shadow
    private static final Map<Crackiness.Level, ResourceLocation> ARMOR_CRACK_LOCATIONS = Map.of(
            Crackiness.Level.LOW,
            ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_low.png"),
            Crackiness.Level.MEDIUM,
            ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_medium.png"),
            Crackiness.Level.HIGH,
            ResourceLocation.withDefaultNamespace("textures/entity/wolf/wolf_armor_crackiness_high.png")
    );

    @Override
    public void render(
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            Wolf livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (livingEntity.hasArmor()) {
            ItemStack itemstack = livingEntity.getBodyArmorItem();
            if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.LEATHER_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerForLeather(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof AnimalArmorItem animalarmoritem && animalarmoritem.getBodyType() == AnimalArmorItem.BodyType.CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalarmoritem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, itemstack, animalarmoritem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.CHAIN_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.GOLDEN_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.IRON_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.DIAMOND_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.EMERALD_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.NETHERITE_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.ROSARITE_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.COPPER_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.ROSE_GOLEN_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem moddedAnimalArmorItem && moddedAnimalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.TURTLE_SCUTE_CANINE) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(moddedAnimalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(poseStack, bufferSource, packedLight, itemstack, moddedAnimalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (ModList.get().isLoaded("create")) {
                 if (itemstack.getItem() instanceof CreateCompatAnimalArmorItem createCompatAnimalArmorItem && createCompatAnimalArmorItem.getBodyType() == CreateCompatAnimalArmorItem.BodyType.ZINC_CANINE) {
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                    this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(createCompatAnimalArmorItem.getTexture()));
                    this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                    this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerCreateCompat(poseStack, bufferSource, packedLight, itemstack, createCompatAnimalArmorItem);
                    this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
                }
                 else if (itemstack.getItem() instanceof CreateCompatAnimalArmorItem createCompatAnimalArmorItem && createCompatAnimalArmorItem.getBodyType() == CreateCompatAnimalArmorItem.BodyType.BRASS_CANINE) {
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                    this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(createCompatAnimalArmorItem.getTexture()));
                    this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                    this.more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerCreateCompat(poseStack, bufferSource, packedLight, itemstack, createCompatAnimalArmorItem);
                    this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
                }
            }
        }
    }

    @Shadow
    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, AnimalArmorItem armorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = armorItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.model
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }

    @Unique
    private void more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerCreateCompat(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, CreateCompatAnimalArmorItem armorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = armorItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.model
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }

    @Unique
    private void more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerModded(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, ModdedAnimalArmorItem armorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = armorItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.model
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }

    @Unique
    private void more_Stuff_NeoForge_1_21_1$maybeRenderColoredLayerForLeather(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, ModdedAnimalArmorItem armorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, -6265536);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = armorItem.getOverlayTexture();
            if (resourcelocation == null) {
                return;
            }

            this.model
                    .renderToBuffer(
                            poseStack,
                            buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)),
                            packedLight,
                            OverlayTexture.NO_OVERLAY,
                            FastColor.ARGB32.opaque(i)
                    );
        }
    }

    @Shadow
    private void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack) {
        Crackiness.Level crackiness$level = Crackiness.WOLF_ARMOR.byDamage(armorStack);
        if (crackiness$level != Crackiness.Level.NONE) {
            ResourceLocation resourcelocation = ARMOR_CRACK_LOCATIONS.get(crackiness$level);
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(resourcelocation));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }
}
