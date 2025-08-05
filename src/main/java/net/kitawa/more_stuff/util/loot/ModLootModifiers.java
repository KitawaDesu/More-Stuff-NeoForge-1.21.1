package net.kitawa.more_stuff.util.loot;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.util.loot.modifiers.AddItemModifier;
import net.kitawa.more_stuff.util.loot.modifiers.DyeRandomlyModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MoreStuff.MOD_ID);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemModifier.CODEC);

    public static final Supplier<MapCodec<? extends IGlobalLootModifier>> DYE_RANDOMLY =
            LOOT_MODIFIER_SERIALIZERS.register("dye_randomly", () -> DyeRandomlyModifier.CODEC);

    public static void register(IEventBus eventBus) {
        LOOT_MODIFIER_SERIALIZERS.register(eventBus);
    }
}
