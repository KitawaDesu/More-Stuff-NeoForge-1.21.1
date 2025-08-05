package net.kitawa.more_stuff.util.mob_armor.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.WitchModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimPattern;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.ClientHooks;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class WitchArmorLayer extends RenderLayer<Witch, WitchModel<Witch>> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final HumanoidModel innerModel;
    private final HumanoidModel outerModel;
    private final TextureAtlas armorTrimAtlas;

    public WitchArmorLayer(RenderLayerParent<Witch, WitchModel<Witch>> pRenderer, EntityModelSet modelSets, ModelManager modelManager) {
        super(pRenderer);
        this.innerModel = new HumanoidModel(modelSets.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_INNER_ARMOR));
        this.outerModel = new HumanoidModel(modelSets.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_OUTER_ARMOR));
        this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    private void copyPropertiesTo(HumanoidModel pModel) {
        pModel.head.copyFrom(((WitchModel)this.getParentModel()).root().getChild("head"));
        pModel.body.copyFrom(((WitchModel)this.getParentModel()).root().getChild("body"));
        pModel.rightArm.copyFrom(((WitchModel)this.getParentModel()).root().getChild("arms"));
        pModel.leftArm.copyFrom(((WitchModel)this.getParentModel()).root().getChild("arms"));
        pModel.rightLeg.copyFrom(((WitchModel)this.getParentModel()).root().getChild("right_leg"));
        pModel.leftLeg.copyFrom(((WitchModel)this.getParentModel()).root().getChild("left_leg"));
    }

    @Override
    public void render(PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, Witch pLivingEntity,
                       float pLimbSwing, float pLimbSwingAmount, float pPartialTicks,
                       float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        this.copyPropertiesTo(this.innerModel);
        this.copyPropertiesTo(this.outerModel);

        // Render armor for each piece
        this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.CHEST, pPackedLight, this.getArmorModel(EquipmentSlot.CHEST));
        this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.LEGS, pPackedLight, this.getArmorModel(EquipmentSlot.LEGS));
        this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.FEET, pPackedLight, this.getArmorModel(EquipmentSlot.FEET));
        this.renderArmorPiece(pMatrixStack, pBuffer, pLivingEntity, EquipmentSlot.HEAD, pPackedLight, this.getArmorModel(EquipmentSlot.HEAD));
    }

    private void renderArmorPiece(PoseStack p_117119_, MultiBufferSource p_117120_, Witch p_117121_, EquipmentSlot p_117122_, int p_117123_, HumanoidModel p_117124_) {
        ItemStack itemstack = p_117121_.getItemBySlot(p_117122_);
        Item model = itemstack.getItem();
        if (model instanceof ArmorItem armoritem) {
            if (armoritem.getEquipmentSlot() == p_117122_) {
                this.copyPropertiesTo(p_117124_);
                this.setPartVisibility(p_117124_, p_117122_);
                boolean flag = this.usesInnerModel(p_117122_);
                ArmorMaterial armormaterial = (ArmorMaterial)armoritem.getMaterial().value();
                int i = itemstack.is(ItemTags.DYEABLE) ? FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(itemstack, -6265536)) : -1;

                for(ArmorMaterial.Layer armormaterial$layer : armormaterial.layers()) {
                    int j = armormaterial$layer.dyeable() ? i : -1;
                    ResourceLocation texture = ClientHooks.getArmorTexture(p_117121_, itemstack, armormaterial$layer, flag, p_117122_);
                    this.renderModel(p_117119_, p_117120_, p_117123_, p_117124_, j, texture);
                }

                ArmorTrim armortrim = (ArmorTrim)itemstack.get(DataComponents.TRIM);
                if (armortrim != null) {
                    this.renderTrim(armoritem.getMaterial(), p_117119_, p_117120_, p_117123_, armortrim, this.getArmorModel(p_117122_), flag);
                }

                if (itemstack.hasFoil()) {
                    this.renderGlint(p_117119_, p_117120_, p_117123_, this.getArmorModel(p_117122_));
                }
            }
        }
    }

    private void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, HumanoidModel p_289658_, int p_350798_, ResourceLocation p_324344_) {
        this.renderModel(p_289664_, p_289689_, p_289681_, (Model)p_289658_, p_350798_, p_324344_);
    }

    private void renderModel(PoseStack p_289664_, MultiBufferSource p_289689_, int p_289681_, Model p_289658_, int p_350798_, ResourceLocation p_324344_) {
        VertexConsumer vertexconsumer = p_289689_.getBuffer(RenderType.armorCutoutNoCull(p_324344_));
        p_289658_.renderToBuffer(p_289664_, vertexconsumer, p_289681_, OverlayTexture.NO_OVERLAY, p_350798_);
    }

    private void setPartVisibility(HumanoidModel pModel, EquipmentSlot pSlot) {
        pModel.setAllVisible(false);

        if (pSlot == EquipmentSlot.HEAD) {
            pModel.head.visible = true;
            pModel.head.xScale = 1.4F;
            pModel.head.yScale = 1.4F;
            pModel.head.zScale = 1.4F;
            pModel.head.y -= 15.0F;
        } else if (pSlot == EquipmentSlot.CHEST) {
            pModel.body.visible = true;
            pModel.body.xScale = 2.0F;
            pModel.body.yScale = 2.0F;
            pModel.body.zScale = 2.4F;
            pModel.body.y -= 22.0F;
            pModel.rightArm.visible = true;
            pModel.rightArm.xScale = 2.0F;
            pModel.rightArm.yScale = 2.0F;
            pModel.rightArm.zScale = 2.0F;
            pModel.rightArm.y -= 22.0F;
            pModel.rightArm.z += 0.25F;
            pModel.rightArm.x -= 10.0F;
            pModel.leftArm.visible = true;
            pModel.leftArm.xScale = 2.0F;
            pModel.leftArm.yScale = 2.0F;
            pModel.leftArm.zScale = 2.0F;
            pModel.leftArm.y -= 22.0F;
            pModel.leftArm.z += 0.25F;
            pModel.leftArm.x -= -10.0F;
        } else if (pSlot == EquipmentSlot.LEGS) {
            pModel.body.visible = true;
            pModel.body.xScale = 2.0F;
            pModel.body.yScale = 2.0F;
            pModel.body.zScale = 2.0F;
            pModel.body.y -= 22.0F;
            pModel.rightLeg.visible = true;
            pModel.rightLeg.xScale = 2.0F;
            pModel.rightLeg.yScale = 2.0F;
            pModel.rightLeg.zScale = 2.0F;
            pModel.rightLeg.y -= 12.0F;
            pModel.rightLeg.x -= 1.75F;
            pModel.leftLeg.visible = true;
            pModel.leftLeg.xScale = 2.0F;
            pModel.leftLeg.yScale = 2.0F;
            pModel.leftLeg.zScale = 2.0F;
            pModel.leftLeg.y -= 12.0F;
            pModel.leftLeg.x += 1.75F;
        } else if (pSlot == EquipmentSlot.FEET) {
            pModel.rightLeg.visible = true;
            pModel.rightLeg.xScale = 2.0F;
            pModel.rightLeg.yScale = 2.0F;
            pModel.rightLeg.zScale = 2.0F;
            pModel.rightLeg.y -= 12.0F;
            pModel.rightLeg.x -= 1.75F;
            pModel.leftLeg.visible = true;
            pModel.leftLeg.xScale = 2.0F;
            pModel.leftLeg.yScale = 2.0F;
            pModel.leftLeg.zScale = 2.0F;
            pModel.leftLeg.y -= 12.0F;
            pModel.leftLeg.x += 1.75F;
        }
    }

    private HumanoidModel getArmorModel(EquipmentSlot pSlot) {
        return this.usesInnerModel(pSlot) ? this.innerModel : this.outerModel;
    }

    private boolean usesInnerModel(EquipmentSlot pSlot) {
        return pSlot == EquipmentSlot.LEGS;
    }

    private void renderTrim(Holder<ArmorMaterial> p_323506_, PoseStack p_289687_, MultiBufferSource p_289643_, int p_289683_, ArmorTrim p_289692_, Model p_289663_, boolean p_289651_) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_289651_ ? p_289692_.innerTexture(p_323506_) : p_289692_.outerTexture(p_323506_));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(p_289643_.getBuffer(Sheets.armorTrimsSheet(((TrimPattern)p_289692_.pattern().value()).decal())));
        p_289663_.renderToBuffer(p_289687_, vertexconsumer, p_289683_, OverlayTexture.NO_OVERLAY);
    }

    private void renderGlint(PoseStack p_289673_, MultiBufferSource p_289654_, int p_289649_, Model p_289659_) {
        p_289659_.renderToBuffer(p_289673_, p_289654_.getBuffer(RenderType.armorEntityGlint()), p_289649_, OverlayTexture.NO_OVERLAY);
    }
}


