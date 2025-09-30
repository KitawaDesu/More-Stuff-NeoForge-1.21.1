package net.kitawa.more_stuff.util.recipes;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.MoreStuff;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ExperimentalConditions {

    // --- Deferred register for condition codecs ---
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITION_CODECS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, MoreStuff.MOD_ID);

    // --- Register the ConfigEnabledCondition ---
    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ConfigEnabledCondition>> CONFIG_ENABLED =
            CONDITION_CODECS.register("config_enabled", () -> ConfigEnabledCondition.CODEC);

    // --- Register method for event bus ---
    public static void register(IEventBus eventBus) {
        CONDITION_CODECS.register(eventBus);
    }
}