package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChainBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.item.BlockItem;

import static net.minecraft.world.InteractionResult.FAIL;
import static net.minecraft.world.InteractionResult.SUCCESS;

@Mixin(ItemStack.class)
public abstract class ItemMixin {

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void onUseBlazeRodPlaceChain(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack stack = (ItemStack)(Object)this;

        // Only do this for blaze rod
        if (stack.getItem() == Items.BLAZE_ROD) {
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            Level level = placeContext.getLevel();
            BlockPos clickedPos = placeContext.getClickedPos();
            Direction clickedFace = placeContext.getClickedFace();
            Player player = placeContext.getPlayer();

            // Determine if clicked block is replaceable
            boolean replaceClicked = level.getBlockState(clickedPos).canBeReplaced(placeContext);
            BlockPos placePos = replaceClicked ? clickedPos : clickedPos.relative(clickedFace);

            // Injected: check player collision with actual block collision shape
            if (player != null) {
                BlockState futureState = ModBlocks.BLAZE_ROD.get().defaultBlockState()
                        .setValue(ChainBlock.AXIS, clickedFace.getAxis());

                VoxelShape collisionShape = futureState.getCollisionShape(level, placePos);
                AABB playerBox = player.getBoundingBox();

                for (AABB shapeBox : collisionShape.toAabbs()) {
                    if (shapeBox.move(placePos).intersects(playerBox)) {
                        cir.setReturnValue(InteractionResult.FAIL);
                        return;
                    }
                }
            }

            // If player can't place at that position
            if (!level.getBlockState(placePos).canBeReplaced(placeContext) || !placeContext.canPlace()) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            // Get correct CHAIN orientation based on face clicked
            Direction.Axis axis = clickedFace.getAxis();

            // Determine if block should be waterlogged
            boolean isWaterlogged = level.getFluidState(placePos).getType() == Fluids.WATER;

            // Create block state with orientation and waterlogged state
            BlockState chainState = ModBlocks.BLAZE_ROD.get()
                    .defaultBlockState()
                    .setValue(ChainBlock.AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED, isWaterlogged);

            // Place block
            if (!level.setBlock(placePos, chainState, 3)) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            // Schedule water update if waterlogged
            if (isWaterlogged) {
                level.scheduleTick(placePos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }

            // Trigger placement events
            if (player != null) {
                chainState.getBlock().setPlacedBy(level, placePos, chainState, player, stack);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, placePos, stack);
                }
            }

            SoundType sound = chainState.getSoundType(level, placePos, player);
            level.playSound(
                    player,
                    placePos,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F,
                    sound.getPitch() * 0.8F
            );

            level.gameEvent(GameEvent.BLOCK_PLACE, placePos, GameEvent.Context.of(player, chainState));

            // Consume item if not in creative
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            cir.cancel();
        }

        if (stack.getItem() == Items.BREEZE_ROD) {
            BlockPlaceContext placeContext = new BlockPlaceContext(context);
            Level level = placeContext.getLevel();
            BlockPos clickedPos = placeContext.getClickedPos();
            Direction clickedFace = placeContext.getClickedFace();
            Player player = placeContext.getPlayer();

            // Determine if clicked block is replaceable
            boolean replaceClicked = level.getBlockState(clickedPos).canBeReplaced(placeContext);
            BlockPos placePos = replaceClicked ? clickedPos : clickedPos.relative(clickedFace);

            // Injected: check player collision with actual block collision shape
            if (player != null) {
                BlockState futureState = ModBlocks.BREEZE_ROD.get().defaultBlockState()
                        .setValue(ChainBlock.AXIS, clickedFace.getAxis());

                VoxelShape collisionShape = futureState.getCollisionShape(level, placePos);
                AABB playerBox = player.getBoundingBox();

                for (AABB shapeBox : collisionShape.toAabbs()) {
                    if (shapeBox.move(placePos).intersects(playerBox)) {
                        cir.setReturnValue(InteractionResult.FAIL);
                        return;
                    }
                }
            }

            // If player can't place at that position
            if (!level.getBlockState(placePos).canBeReplaced(placeContext) || !placeContext.canPlace()) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            // Get correct CHAIN orientation based on face clicked
            Direction.Axis axis = clickedFace.getAxis();

            // Determine if block should be waterlogged
            boolean isWaterlogged = level.getFluidState(placePos).getType() == Fluids.WATER;

            // Create block state with orientation and waterlogged state
            BlockState chainState = ModBlocks.BREEZE_ROD.get()
                    .defaultBlockState()
                    .setValue(ChainBlock.AXIS, axis)
                    .setValue(BlockStateProperties.WATERLOGGED, isWaterlogged);

            // Place block
            if (!level.setBlock(placePos, chainState, 3)) {
                cir.setReturnValue(InteractionResult.FAIL);
                return;
            }

            // Schedule water update if waterlogged
            if (isWaterlogged) {
                level.scheduleTick(placePos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }

            // Trigger placement events
            if (player != null) {
                chainState.getBlock().setPlacedBy(level, placePos, chainState, player, stack);
                if (player instanceof ServerPlayer serverPlayer) {
                    CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, placePos, stack);
                }
            }

            SoundType sound = chainState.getSoundType(level, placePos, player);
            level.playSound(
                    player,
                    placePos,
                    sound.getPlaceSound(),
                    SoundSource.BLOCKS,
                    (sound.getVolume() + 1.0F) / 2.0F,
                    sound.getPitch() * 0.8F
            );

            level.gameEvent(GameEvent.BLOCK_PLACE, placePos, GameEvent.Context.of(player, chainState));

            // Consume item if not in creative
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }

            cir.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            cir.cancel();
        }
    }
}



