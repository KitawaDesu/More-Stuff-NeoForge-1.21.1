package net.kitawa.more_stuff.blocks.custom.general.entities;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrushableBlockRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModdedBrushableBlockRenderer implements BlockEntityRenderer<ModdedBrushableBlockEntity> {
    private final ItemRenderer itemRenderer;

    public ModdedBrushableBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ModdedBrushableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity.getLevel() != null) {
            int i = blockEntity.getBlockState().getValue(BlockStateProperties.DUSTED);
            if (i > 0) {
                Direction direction = blockEntity.getHitDirection();
                if (direction != null) {
                    ItemStack itemstack = blockEntity.getItem();
                    if (!itemstack.isEmpty()) {
                        poseStack.pushPose();
                        poseStack.translate(0.0F, 0.5F, 0.0F);
                        float[] afloat = translations(direction, i);
                        poseStack.translate(afloat[0], afloat[1], afloat[2]);
                        poseStack.mulPose(Axis.YP.rotationDegrees(75.0F));
                        boolean flag = direction == Direction.EAST || direction == Direction.WEST;
                        poseStack.mulPose(Axis.YP.rotationDegrees((float)((flag ? 90 : 0) + 11)));
                        poseStack.scale(0.5F, 0.5F, 0.5F);
                        int j = LevelRenderer.getLightColor(blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos().relative(direction));
                        this.itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, j, OverlayTexture.NO_OVERLAY, poseStack, bufferSource, blockEntity.getLevel(), 0);
                        poseStack.popPose();
                    }
                }
            }
        }
    }

    private float[] translations(Direction direction, int dustedLevel) {
        float[] afloat = new float[]{0.5F, 0.0F, 0.5F};
        float f = (float)dustedLevel / 10.0F * 0.75F;
        switch (direction) {
            case EAST -> afloat[0] = 0.73F + f;
            case WEST -> afloat[0] = 0.25F - f;
            case UP -> afloat[1] = 0.25F + f;
            case DOWN -> afloat[1] = -0.23F - f;
            case NORTH -> afloat[2] = 0.25F - f;
            case SOUTH -> afloat[2] = 0.73F + f;
        }
        return afloat;
    }

    @Override
    public AABB getRenderBoundingBox(ModdedBrushableBlockEntity blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos).inflate(0.5);
    }
}