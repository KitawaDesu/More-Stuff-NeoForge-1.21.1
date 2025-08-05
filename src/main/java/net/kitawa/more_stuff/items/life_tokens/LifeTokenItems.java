package net.kitawa.more_stuff.items.life_tokens;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.items.util.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.kitawa.more_stuff.items.util.ModSmithingTemplateItem.*;

public class LifeTokenItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MoreStuff.MOD_ID);

    public static final DeferredItem<Item> LIFE_BIT = ITEMS.register("life_bit",
            () -> new Item(new Properties()));
    public static final DeferredItem<Item> LIFE_SHARD = ITEMS.register("life_shard",
            () -> new Item(new Properties()));
    public static final DeferredItem<Item> LIFE_TOKEN = ITEMS.register("life_token",
            () -> new LifeTokenItem(new Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
