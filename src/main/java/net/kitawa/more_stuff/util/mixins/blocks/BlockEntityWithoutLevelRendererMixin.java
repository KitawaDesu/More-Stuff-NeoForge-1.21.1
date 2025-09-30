package net.kitawa.more_stuff.util.mixins.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kitawa.more_stuff.experimentals.entities.models.ThrownJavelinModel;
import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin implements ResourceManagerReloadListener {
    @Shadow
    private static final ShulkerBoxBlockEntity[] SHULKER_BOXES = Arrays.stream(DyeColor.values())
            .sorted(Comparator.comparingInt(DyeColor::getId))
            .map(p_172557_ -> new ShulkerBoxBlockEntity(p_172557_, BlockPos.ZERO, Blocks.SHULKER_BOX.defaultBlockState()))
            .toArray(ShulkerBoxBlockEntity[]::new);
    @Shadow
    private static final ShulkerBoxBlockEntity DEFAULT_SHULKER_BOX = new ShulkerBoxBlockEntity(BlockPos.ZERO, Blocks.SHULKER_BOX.defaultBlockState());
    @Shadow
    private final ChestBlockEntity chest = new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
    @Shadow
    private final ChestBlockEntity trappedChest = new TrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());
    @Shadow
    private final EnderChestBlockEntity enderChest = new EnderChestBlockEntity(BlockPos.ZERO, Blocks.ENDER_CHEST.defaultBlockState());
    @Shadow
    private final BannerBlockEntity banner = new BannerBlockEntity(BlockPos.ZERO, Blocks.WHITE_BANNER.defaultBlockState());
    @Shadow
    private final BedBlockEntity bed = new BedBlockEntity(BlockPos.ZERO, Blocks.RED_BED.defaultBlockState());
    @Shadow
    private final ConduitBlockEntity conduit = new ConduitBlockEntity(BlockPos.ZERO, Blocks.CONDUIT.defaultBlockState());
    @Shadow
    private final DecoratedPotBlockEntity decoratedPot = new DecoratedPotBlockEntity(BlockPos.ZERO, Blocks.DECORATED_POT.defaultBlockState());
    @Shadow
    private ShieldModel shieldModel;
    @Shadow
    private TridentModel tridentModel;
    @Unique
    private ThrownJavelinModel more_Stuff_NeoForge_1_21_1$javelinModel;
    @Shadow
    private Map<SkullBlock.Type, SkullModelBase> skullModels;
    @Shadow
    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    @Shadow
    private final EntityModelSet entityModelSet;

    public BlockEntityWithoutLevelRendererMixin(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
        this.entityModelSet = entityModelSet;
    }

    @Overwrite
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof AbstractSkullBlock abstractskullblock) {
                ResolvableProfile resolvableprofile = stack.get(DataComponents.PROFILE);
                if (resolvableprofile != null && !resolvableprofile.isResolved()) {
                    stack.remove(DataComponents.PROFILE);
                    resolvableprofile.resolve().thenAcceptAsync(p_329787_ -> stack.set(DataComponents.PROFILE, p_329787_), Minecraft.getInstance());
                    resolvableprofile = null;
                }

                SkullModelBase skullmodelbase = this.skullModels.get(abstractskullblock.getType());
                RenderType rendertype = SkullBlockRenderer.getRenderType(abstractskullblock.getType(), resolvableprofile);
                SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, buffer, packedLight, skullmodelbase, rendertype);
            } else {
                BlockState blockstate = block.defaultBlockState();
                BlockEntity blockentity;
                if (block instanceof AbstractBannerBlock) {
                    this.banner.fromItem(stack, ((AbstractBannerBlock) block).getColor());
                    blockentity = this.banner;
                } else if (block instanceof BedBlock) {
                    this.bed.setColor(((BedBlock) block).getColor());
                    blockentity = this.bed;
                } else if (blockstate.is(Blocks.CONDUIT)) {
                    blockentity = this.conduit;
                } else if (blockstate.is(Blocks.CHEST)) {
                    blockentity = this.chest;
                } else if (blockstate.is(Blocks.ENDER_CHEST)) {
                    blockentity = this.enderChest;
                } else if (blockstate.is(Blocks.TRAPPED_CHEST)) {
                    blockentity = this.trappedChest;
                } else if (blockstate.is(Blocks.DECORATED_POT)) {
                    this.decoratedPot.setFromItem(stack);
                    blockentity = this.decoratedPot;
                } else {
                    if (!(block instanceof ShulkerBoxBlock)) {
                        return;
                    }

                    DyeColor dyecolor1 = ShulkerBoxBlock.getColorFromItem(item);
                    if (dyecolor1 == null) {
                        blockentity = DEFAULT_SHULKER_BOX;
                    } else {
                        blockentity = SHULKER_BOXES[dyecolor1.getId()];
                    }
                }

                this.blockEntityRenderDispatcher.renderItem(blockentity, poseStack, buffer, packedLight, packedOverlay);
            }
        } else {
            if (stack.is(Items.SHIELD)) {
                BannerPatternLayers bannerpatternlayers = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
                DyeColor dyecolor = stack.get(DataComponents.BASE_COLOR);
                boolean flag = !bannerpatternlayers.layers().isEmpty() || dyecolor != null;
                poseStack.pushPose();
                poseStack.scale(1.0F, -1.0F, -1.0F);
                Material material = flag ? ModelBakery.SHIELD_BASE : ModelBakery.NO_PATTERN_SHIELD;
                VertexConsumer vertexconsumer = material.sprite()
                        .wrap(ItemRenderer.getFoilBufferDirect(buffer, this.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));
                this.shieldModel.handle().render(poseStack, vertexconsumer, packedLight, packedOverlay);
                if (flag) {
                    BannerRenderer.renderPatterns(
                            poseStack,
                            buffer,
                            packedLight,
                            packedOverlay,
                            this.shieldModel.plate(),
                            material,
                            false,
                            Objects.requireNonNullElse(dyecolor, DyeColor.WHITE),
                            bannerpatternlayers,
                            stack.hasFoil()
                    );
                } else {
                    this.shieldModel.plate().render(poseStack, vertexconsumer, packedLight, packedOverlay);
                }

                poseStack.popPose();
            } else {
                ShieldTextures textures = ModShieldTextureHelper.SHIELD_TEXTURES.get(item);
                if (textures != null) {
                    BannerPatternLayers bannerPatterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
                    DyeColor baseColor = stack.get(DataComponents.BASE_COLOR);
                    boolean hasPattern = !bannerPatterns.layers().isEmpty() || baseColor != null;

                    poseStack.pushPose();
                    poseStack.scale(1.0F, -1.0F, -1.0F);

                    Material material = hasPattern ? textures.base() : textures.noPattern();
                    VertexConsumer vertexConsumer = material.sprite()
                            .wrap(ItemRenderer.getFoilBufferDirect(buffer, this.shieldModel.renderType(material.atlasLocation()), true, stack.hasFoil()));

                    this.shieldModel.handle().render(poseStack, vertexConsumer, packedLight, packedOverlay);

                    if (hasPattern) {
                        BannerRenderer.renderPatterns(
                                poseStack,
                                buffer,
                                packedLight,
                                packedOverlay,
                                this.shieldModel.plate(),
                                material,
                                false,
                                Objects.requireNonNullElse(baseColor, DyeColor.WHITE),
                                bannerPatterns,
                                stack.hasFoil()
                        );
                    } else {
                        this.shieldModel.plate().render(poseStack, vertexConsumer, packedLight, packedOverlay);
                    }

                    poseStack.popPose();
                } else if (stack.is(Items.TRIDENT)) {
                    poseStack.pushPose();
                    poseStack.scale(1.0F, -1.0F, -1.0F);
                    VertexConsumer vertexconsumer1 = ItemRenderer.getFoilBufferDirect(
                            buffer, this.tridentModel.renderType(TridentModel.TEXTURE), false, stack.hasFoil()
                    );
                    this.tridentModel.renderToBuffer(poseStack, vertexconsumer1, packedLight, packedOverlay);
                    poseStack.popPose();
                }
            }
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.shieldModel = new ShieldModel(this.entityModelSet.bakeLayer(ModelLayers.SHIELD));
        this.tridentModel = new TridentModel(this.entityModelSet.bakeLayer(ModelLayers.TRIDENT));
        this.more_Stuff_NeoForge_1_21_1$javelinModel = new ThrownJavelinModel(this.entityModelSet.bakeLayer(ThrownJavelinModel.SPEAR_LAYER));
        this.skullModels = SkullBlockRenderer.createSkullRenderers(this.entityModelSet);
    }
}
