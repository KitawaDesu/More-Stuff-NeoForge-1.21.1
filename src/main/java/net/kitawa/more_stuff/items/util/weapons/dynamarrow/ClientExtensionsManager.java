package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.HashMap;
import java.util.Map;

public class ClientExtensionsManager {
    public static final Map<Item, IClientItemExtensions> ITEM_EXTENSIONS = new HashMap<>();

    public static void register(Item item, IClientItemExtensions extension) {
        ITEM_EXTENSIONS.put(item, extension);
    }
}
