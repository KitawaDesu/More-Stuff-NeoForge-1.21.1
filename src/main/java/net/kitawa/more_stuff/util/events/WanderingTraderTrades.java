package net.kitawa.more_stuff.util.events;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

@EventBusSubscriber(modid = MoreStuff.MOD_ID)
public class WanderingTraderTrades {

    @SubscribeEvent
    public static void onWandererTrades(WandererTradesEvent event) {
        // Add a generic trade (common)
        event.getGenericTrades().add(
                new BasicItemListing(
                new ItemStack(Items.EMERALD, 1),   // price
                new ItemStack(ModBlocks.AQUANDA_MOSS_BLOCK.asItem(), 2),    // item the player gets
                5,                                 // max uses
                1,                                  // XP for the trader
                0.05F                               // price multiplier
        ));
    }
}