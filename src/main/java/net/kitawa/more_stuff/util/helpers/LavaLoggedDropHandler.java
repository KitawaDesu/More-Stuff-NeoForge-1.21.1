package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

@EventBusSubscriber(modid = MoreStuff.MOD_ID)
public class LavaLoggedDropHandler {

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        BlockState state = event.getState();

        if (state.hasProperty(ModdedBlockStateProperties.LAVALOGGED)
                && state.getValue(ModdedBlockStateProperties.LAVALOGGED)) {

            for (ItemEntity itemEntity : event.getDrops()) {
                ItemStack stack = itemEntity.getItem();
                // Mark dropped stack as fire resistant
                stack.set(DataComponents.FIRE_RESISTANT, Unit.INSTANCE);
            }
        }
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        ItemEntity itemEntity = event.getItemEntity();
        ItemStack stack = itemEntity.getItem();

        if (stack.has(DataComponents.FIRE_RESISTANT)) {
            if (!isNetherite(stack)) {
                stack.remove(DataComponents.FIRE_RESISTANT);
            }
        }
    }

    private static boolean isNetherite(ItemStack stack) {
        // Direct item checks - you can expand this list
        return stack.is(Items.NETHERITE_INGOT)
                || stack.is(Items.NETHERITE_SCRAP)
                || stack.is(Items.NETHERITE_BLOCK)
                || stack.is(Items.NETHERITE_SWORD)
                || stack.is(Items.NETHERITE_PICKAXE)
                || stack.is(Items.NETHERITE_AXE)
                || stack.is(Items.NETHERITE_SHOVEL)
                || stack.is(Items.NETHERITE_HOE)
                || stack.is(Items.NETHERITE_HELMET)
                || stack.is(Items.NETHERITE_CHESTPLATE)
                || stack.is(Items.NETHERITE_LEGGINGS)
                || stack.is(Items.NETHERITE_BOOTS)
                || stack.is(ModItems.NETHERITE_WOLF_ARMOR.get())
                || stack.is(ModItems.NETHERITE_ELYTRA.get())
                || stack.is(ModItems.NETHERITE_MACE.get())
                || stack.is(ModItems.NETHERITE_SHEARS.get())
                || stack.is(ModItems.NETHERITE_HOGLIN_ARMOR.get())
                || stack.is(ModItems.NETHERITE_SHIELD.get())
                || stack.is(ModItems.NETHERITE_HORSE_ARMOR.get())
                || stack.is(ExperimentalCombatItems.NETHERITE_JAVELIN.get())
                || stack.is(ModItems.ROSARITE_INGOT.get())
                || stack.is(ModItems.ROSARITE_BLOCK.get())
                || stack.is(ModItems.ROSARITE_SWORD.get())
                || stack.is(ModItems.ROSARITE_PICKAXE.get())
                || stack.is(ModItems.ROSARITE_AXE.get())
                || stack.is(ModItems.ROSARITE_SHOVEL.get())
                || stack.is(ModItems.ROSARITE_HOE.get())
                || stack.is(ModItems.ROSARITE_HELMET.get())
                || stack.is(ModItems.ROSARITE_CHESTPLATE.get())
                || stack.is(ModItems.ROSARITE_LEGGINGS.get())
                || stack.is(ModItems.ROSARITE_BOOTS.get())
                || stack.is(ModItems.ROSARITE_WOLF_ARMOR.get())
                || stack.is(ModItems.ROSARITE_ELYTRA.get())
                || stack.is(ModItems.ROSARITE_MACE.get())
                || stack.is(ModItems.ROSARITE_SHEARS.get())
                || stack.is(ModItems.ROSARITE_HOGLIN_ARMOR.get())
                || stack.is(ModItems.ROSARITE_SHIELD.get())
                || stack.is(ModItems.ROSARITE_HORSE_ARMOR.get())
                || stack.is(ExperimentalCombatItems.ROSARITE_JAVELIN.get())
                || stack.is(ModItems.ANCIENT_BAMBOO.get())
                || stack.is(ModItems.ANCIENT_CHUNK.get())
                || stack.is(ModItems.ANCIENT_SCAFFOLDING.get())
                || stack.is(Items.ANCIENT_DEBRIS);
    }
}