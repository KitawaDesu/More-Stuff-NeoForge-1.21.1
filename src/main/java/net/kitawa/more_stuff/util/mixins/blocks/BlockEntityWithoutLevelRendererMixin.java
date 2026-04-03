package net.kitawa.more_stuff.util.mixins.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.util.helpers.shield.ModShieldTextureHelper;
import net.kitawa.more_stuff.util.helpers.shield.ShieldTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

    @Shadow
    private ShieldModel shieldModel;

    @Inject(
            method = "renderByItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/world/item/Item;)Z",
                    ordinal = 0
            ),
            cancellable = true
    )
    private void onRenderByItem(ItemStack stack, ItemDisplayContext displayContext,
                                PoseStack poseStack, MultiBufferSource buffer,
                                int packedLight, int packedOverlay, CallbackInfo ci) {

        ShieldTextures textures = ModShieldTextureHelper.SHIELD_TEXTURES.get(stack.getItem());
        if (textures == null) return;

        BannerPatternLayers bannerPatterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
        DyeColor baseColor = stack.get(DataComponents.BASE_COLOR);
        boolean hasPattern = !bannerPatterns.layers().isEmpty() || baseColor != null;

        poseStack.pushPose();
        poseStack.scale(1.0F, -1.0F, -1.0F);

        Material material = hasPattern ? textures.base() : textures.noPattern();
        VertexConsumer vertexConsumer = material.sprite()
                .wrap(ItemRenderer.getFoilBufferDirect(
                        buffer, this.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));

        this.shieldModel.handle().render(poseStack, vertexConsumer, packedLight, packedOverlay);

        if (hasPattern) {
            BannerRenderer.renderPatterns(
                    poseStack, buffer, packedLight, packedOverlay,
                    this.shieldModel.plate(), material, false,
                    Objects.requireNonNullElse(baseColor, DyeColor.WHITE),
                    bannerPatterns, stack.hasFoil()
            );
        } else {
            this.shieldModel.plate().render(poseStack, vertexConsumer, packedLight, packedOverlay);
        }

        poseStack.popPose();
        ci.cancel(); // prevent vanilla shield/trident branch from also running
    }
}