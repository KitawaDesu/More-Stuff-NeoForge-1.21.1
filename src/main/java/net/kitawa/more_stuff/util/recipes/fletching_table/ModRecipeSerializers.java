package net.kitawa.more_stuff.util.recipes.fletching_table;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipeSerializers {

    // DeferredRegister for RecipeSerializer
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, MoreStuff.MOD_ID);

    // Register Fletching Recipe Serializer
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FletchingRecipe>> FLETCHING =
            SERIALIZERS.register("fletching", FletchingRecipe.Serializer::new);

    // Register all serializers to the event bus
    public static void register(IEventBus bus) {
        SERIALIZERS.register(bus);
    }
}
