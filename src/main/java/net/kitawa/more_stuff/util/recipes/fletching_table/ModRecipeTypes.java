package net.kitawa.more_stuff.util.recipes.fletching_table;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeTypes {

    // DeferredRegister for RecipeType
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, MoreStuff.MOD_ID);

    // Register Fletching Recipe Type
    public static final DeferredHolder<RecipeType<?>, RecipeType<FletchingRecipe>> FLETCHING =
            TYPES.register("fletching", () -> new RecipeType<>() {
                @Override
                public String toString() {
                    return MoreStuff.MOD_ID + ":fletching";
                }
            });

    // Register all types to the event bus
    public static void register(IEventBus bus) {
        TYPES.register(bus);
    }
}
