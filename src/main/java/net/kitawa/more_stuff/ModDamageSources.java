package net.kitawa.more_stuff;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDamageSources {

    // DeferredRegister for DamageType
    public static final DeferredRegister<DamageType> DAMAGE_TYPES =
            DeferredRegister.create(Registries.DAMAGE_TYPE, MoreStuff.MOD_ID);

    // DeferredHolder for electricity damage
    public static final DeferredHolder<DamageType, DamageType> ELECTRICITY =
            DAMAGE_TYPES.register("electricity", () -> new DamageType(
                    "more_stuff.damage.electricity",            // msgId
                    DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, // scaling
                    0.1f,                                       // exhaustion
                    DamageEffects.HURT,                         // effects
                    DeathMessageType.DEFAULT                     // deathMessageType
            ));

    public static void register(IEventBus bus) {
        DAMAGE_TYPES.register(bus);
    }

    // Helper to create a DamageSource from a Holder<DamageType>
    public static DamageSource electricity(Holder<DamageType> typeHolder) {
        return new DamageSource(typeHolder);
    }
}