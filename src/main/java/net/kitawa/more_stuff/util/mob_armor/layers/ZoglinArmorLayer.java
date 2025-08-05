package net.kitawa.more_stuff.util.mob_armor.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatAnimalArmorItem;
import net.kitawa.more_stuff.items.util.ModdedAnimalArmorItem;
import net.kitawa.more_stuff.util.mob_armor.ModModelLayers;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.client.model.HoglinModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.Crackiness;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class ZoglinArmorLayer extends RenderLayer<Zoglin, HoglinModel<Zoglin>> {
    private final HoglinModel<Zoglin> model;
    private static final Map<Crackiness.Level, ResourceLocation> ARMOR_CRACK_LOCATIONS = Map.of(
            Crackiness.Level.LOW,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/cracks/zoglin_armor_crackiness_low.png"),
            Crackiness.Level.MEDIUM,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/cracks/zoglin_armor_crackiness_medium.png"),
            Crackiness.Level.HIGH,
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/entity/hoglin/armor/cracks/zoglin_armor_crackiness_high.png")
    );

    public ZoglinArmorLayer(RenderLayerParent<Zoglin, HoglinModel<Zoglin>> renderer, EntityModelSet models) {
        super(renderer);
        this.model = new HoglinModel<>(models.bakeLayer(ModModelLayers.HOGLIN_ARMOR));
    }

    public void render(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            Zoglin livingEntity,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        if (livingEntity.getBodyArmorItem().is(ModItemTags.HOGLIN_ARMOR)) {
            ItemStack itemstack = livingEntity.getBodyArmorItem();
            if (itemstack.getItem() instanceof ModdedAnimalArmorItem animalArmorItem && animalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.HOG) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, itemstack, animalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            }
        }
        if (livingEntity.getBodyArmorItem().is(ModItemTags.HOGLIN_ARMOR)) {
            ItemStack itemstack = livingEntity.getBodyArmorItem();
            Set<ModdedAnimalArmorItem.BodyType> supportedBodyTypes = Set.of(
                    ModdedAnimalArmorItem.BodyType.HOG,
                    ModdedAnimalArmorItem.BodyType.CHAIN_HOG,
                    ModdedAnimalArmorItem.BodyType.COPPER_HOG,
                    ModdedAnimalArmorItem.BodyType.EMERALD_HOG,
                    ModdedAnimalArmorItem.BodyType.DIAMOND_HOG,
                    ModdedAnimalArmorItem.BodyType.GOLDEN_HOG,
                    ModdedAnimalArmorItem.BodyType.IRON_HOG,
                    ModdedAnimalArmorItem.BodyType.NETHERITE_HOG,
                    ModdedAnimalArmorItem.BodyType.ROSE_GOLEN_HOG,
                    ModdedAnimalArmorItem.BodyType.ROSARITE_HOG,
                    ModdedAnimalArmorItem.BodyType.TURTLE_SCUTE_HOG
                    // Add others as needed
            );
            Set<CreateCompatAnimalArmorItem.BodyType> supportedBodyTypesCreate = Set.of(
                    CreateCompatAnimalArmorItem.BodyType.ZINC_HOG,
                    CreateCompatAnimalArmorItem.BodyType.BRASS_HOG
                    // Add others as needed
            );
            if (itemstack.getItem() instanceof ModdedAnimalArmorItem animalArmorItem &&
                    supportedBodyTypes.contains(animalArmorItem.getBodyType())) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, itemstack, animalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (itemstack.getItem() instanceof ModdedAnimalArmorItem animalArmorItem && animalArmorItem.getBodyType() == ModdedAnimalArmorItem.BodyType.LEATHER_HOG) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.maybeRenderColoredLayerForLeather(poseStack, bufferSource, packedLight, itemstack, animalArmorItem);
                this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
            } else if (ModList.get().isLoaded("create")) {
                if (itemstack.getItem() instanceof CreateCompatAnimalArmorItem animalArmorItem &&
                        supportedBodyTypesCreate.contains(animalArmorItem.getBodyType())) {
                    this.getParentModel().copyPropertiesTo(this.model);
                    this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                    this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                    VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(animalArmorItem.getTexture()));
                    this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                    this.maybeRenderColoredLayerCreate(poseStack, bufferSource, packedLight, itemstack, animalArmorItem);
                    this.maybeRenderCracks(poseStack, bufferSource, packedLight, itemstack);
                }
            }
        }
    }

    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, ModdedAnimalArmorItem animalArmorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = animalArmorItem.getOverlayTexture();
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

    private void maybeRenderColoredLayerCreate(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, CreateCompatAnimalArmorItem animalArmorItem) {
        if (armorStack.is(ItemTags.DYEABLE)) {
            int i = DyedItemColor.getOrDefault(armorStack, 0);
            if (FastColor.ARGB32.alpha(i) == 0) {
                return;
            }

            ResourceLocation resourcelocation = animalArmorItem.getOverlayTexture();
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

    private void maybeRenderCracks(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack) {
        Crackiness.Level crackiness$level = Crackiness.WOLF_ARMOR.byDamage(armorStack);
        if (crackiness$level != Crackiness.Level.NONE) {
            ResourceLocation resourcelocation = ARMOR_CRACK_LOCATIONS.get(crackiness$level);
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucent(resourcelocation));
            this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        }
    }

    @Unique
    private void maybeRenderColoredLayerForLeather(PoseStack poseStack, MultiBufferSource buffer, int packedLight, ItemStack armorStack, ModdedAnimalArmorItem armorItem) {
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
}
