package net.kitawa.more_stuff.util.screen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.blocks.util.FletchingTableMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, MoreStuff.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<FletchingTableMenu>> FLETCHING_TABLE =
            MENUS.register("fletching_table",
                    () -> new MenuType<>(
                            (id, playerInventory) -> new FletchingTableMenu(id, playerInventory, ContainerLevelAccess.NULL),
                            FeatureFlags.VANILLA_SET
                    )
            );

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}