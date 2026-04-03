package net.kitawa.more_stuff.entities.util;

import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, MoreStuff.MOD_ID);

    // ── Mining ────────────────────────────────────────────────────────────────

    public static final DeferredHolder<Attribute, Attribute> OBSIDIAN_BREAK_SPEED = ATTRIBUTES.register(
            "obsidian_break_speed",
            () -> new RangedAttribute("attribute.name.ger.obsidian_break_speed", 0.0, 0.0, 1024.0)
                    .setSyncable(true));

    // ── Registration ──────────────────────────────────────────────────────────

    public static void register(IEventBus bus) {
        ATTRIBUTES.register(bus);
        bus.addListener(ModAttributes::onAttributeModification);
    }

    private static void onAttributeModification(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> {
            event.add(entityType, OBSIDIAN_BREAK_SPEED,   0.0);
        });
    }
}