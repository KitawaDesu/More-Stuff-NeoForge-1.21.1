package net.kitawa.more_stuff.items.util.shears;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class StoneShearsItem extends Item {
    private final Tier tier;
    public StoneShearsItem(Tier tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }

    public static Tool createToolProperties() {
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), Tiers.STONE.getSpeed() + 9.0F),
                        Tool.Rule.overrideSpeed(BlockTags.LEAVES, Tiers.STONE.getSpeed() + 9.0F),
                        Tool.Rule.overrideSpeed(BlockTags.WOOL, Tiers.STONE.getSpeed() - 1.0F),
                        Tool.Rule.overrideSpeed(List.of(Blocks.VINE, Blocks.GLOW_LICHEN), 2.0F)
                ),
                1.0F,
                1
        );
    }

    /**
     * Called when a {@link net.minecraft.world.level.block.Block} is destroyed using this Item. Return {@code true} to trigger the "Use Item" statistic.
     */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!level.isClientSide && !state.is(BlockTags.FIRE)) {
            stack.hurtAndBreak(1, entityLiving, EquipmentSlot.MAINHAND);
        }

        return state.is(BlockTags.LEAVES)
                || state.is(Blocks.COBWEB)
                || state.is(Blocks.SHORT_GRASS)
                || state.is(Blocks.FERN)
                || state.is(Blocks.DEAD_BUSH)
                || state.is(Blocks.HANGING_ROOTS)
                || state.is(Blocks.VINE)
                || state.is(Blocks.TRIPWIRE)
                || state.is(BlockTags.WOOL);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return this.tier.getRepairIngredient().test(repairCandidate);
    }

    /**
     * Neo: Migrate shear behavior into {@link ShearsItem#interactLivingEntity} to call into IShearable instead of relying on
     * <p>
     * To preserve vanilla behavior, this method retains the original flow shared by the various mobInteract overrides.
     */
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity entity, net.minecraft.world.InteractionHand hand) {
        if (entity instanceof net.neoforged.neoforge.common.IShearable target) {
            BlockPos pos = entity.blockPosition();
            boolean isClient = entity.level().isClientSide();
            // Check isShearable on both sides (mirrors vanilla readyForShearing())
            if (target.isShearable(player, stack, entity.level(), pos)) {
                // Call onSheared on both sides (mirrors vanilla shear())
                List<ItemStack> drops = target.onSheared(player, stack, entity.level(), pos);
                // Spawn drops on the server side using spawnShearedDrop to retain vanilla mob-specific behavior
                if (!isClient) {
                    for(ItemStack drop : drops) {
                        target.spawnShearedDrop(entity.level(), pos, drop);
                    }
                }
                // Call GameEvent.SHEAR on both sides
                entity.gameEvent(GameEvent.SHEAR, player);
                // Damage the shear item stack by 1 on the server side
                if (!isClient) {
                    stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                }
                // Return sided success if the entity was shearable
                return InteractionResult.sidedSuccess(isClient);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, net.neoforged.neoforge.common.ItemAbility itemAbility) {
        return net.neoforged.neoforge.common.ItemAbilities.DEFAULT_SHEARS_ACTIONS.contains(itemAbility);
    }

    /**
     * Called when this item is used when targeting a Block
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = level.getBlockState(blockpos);
        BlockState blockstate1 = blockstate.getToolModifiedState(context, net.neoforged.neoforge.common.ItemAbilities.SHEARS_TRIM, false);
        if (blockstate1 != null) {
            Player player = context.getPlayer();
            ItemStack itemstack = context.getItemInHand();
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
            }

            level.setBlockAndUpdate(blockpos, blockstate1);
            level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(context.getPlayer(), blockstate1));
            if (player != null) {
                itemstack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.getHand()));
            }

            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }
}
