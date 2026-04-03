package net.kitawa.more_stuff.items.util.weapons.dynamarrow;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.MoreStuff;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Register the DataComponentType instances your DynamicArrow will read.
 * NOTE: Use your mod's DeferredRegister.createDataComponents helper (name below is illustrative).
 */

public class ArrowDataComponents {

    // Create a deferred register for the DATA_COMPONENT_TYPE registry
    public static final DeferredRegister.DataComponents REGISTRAR =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MoreStuff.MOD_ID); // replace with your mod id

    // gravity multiplier (double)
    public static final Supplier<DataComponentType<Double>> GRAVITY =
            REGISTRAR.registerComponentType("arrow_gravity", builder ->
                    builder.persistent(Codec.DOUBLE)
                            .networkSynchronized(ByteBufCodecs.DOUBLE)
            );

    // absolute base damage (if present will override multiplier)
    public static final Supplier<DataComponentType<Double>> BASE_DAMAGE =
            REGISTRAR.registerComponentType("arrow_base_damage", builder ->
                    builder.persistent(Codec.DOUBLE)
                            .networkSynchronized(ByteBufCodecs.DOUBLE)
            );

    // damage multiplier (applied to default base damage)
    public static final Supplier<DataComponentType<Double>> DAMAGE_MULTIPLIER =
            REGISTRAR.registerComponentType("arrow_damage_multiplier", builder ->
                    builder.persistent(Codec.DOUBLE)
                            .networkSynchronized(ByteBufCodecs.DOUBLE)
            );

    // override water inertia (float)
    public static final Supplier<DataComponentType<Float>> WATER_INERTIA =
            REGISTRAR.registerComponentType("arrow_water_inertia", builder ->
                    builder.persistent(Codec.FLOAT)
                            .networkSynchronized(ByteBufCodecs.FLOAT)
            );

    // crit flag
    public static final Supplier<DataComponentType<Float>> CRIT =
            REGISTRAR.registerComponentType("arrow_crit_chance", builder ->
                    builder.persistent(Codec.FLOAT)
                            .networkSynchronized(ByteBufCodecs.FLOAT)
            );

    // no-physics flag
    public static final Supplier<DataComponentType<Boolean>> NO_PHYSICS =
            REGISTRAR.registerComponentType("arrow_no_physics", builder ->
                    builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );

    // sound event for hit (store as ResourceLocation string)
    public static final Supplier<DataComponentType<ResourceLocation>> SOUND_EVENT =
            REGISTRAR.registerComponentType("arrow_sound_event", builder ->
                    builder.persistent(ResourceLocation.CODEC)
            );

    // pierce level (int)
    public static final Supplier<DataComponentType<Integer>> PIERCE_LEVEL =
            REGISTRAR.registerComponentType("arrow_pierce_level", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Integer>> XP_LEVEL_COST =
            REGISTRAR.registerComponentType("arrow_xp_level_cost", builder ->
                    builder.persistent(Codec.INT)
                            .networkSynchronized(ByteBufCodecs.INT)
            );

    public static final Supplier<DataComponentType<Boolean>> HAS_POTION =
            REGISTRAR.registerComponentType("has_potion", builder ->
                    builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );

    public static final Supplier<DataComponentType<Boolean>> ON_FIRE =
            REGISTRAR.registerComponentType("arrow_on_fire", builder ->
                    builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );

    public static final Supplier<DataComponentType<Boolean>> EXPLOSIVE =
            REGISTRAR.registerComponentType("arrow_is_explosive", builder ->
                    builder.persistent(Codec.BOOL)
                            .networkSynchronized(ByteBufCodecs.BOOL)
            );


    // tip texture key
    public static final Supplier<DataComponentType<String>> TIP =
            REGISTRAR.registerComponentType("arrow_tip",
                    builder -> builder.persistent(Codec.STRING)
            );

    // shaft key (string)
    public static final Supplier<DataComponentType<String>> SHAFT =
            REGISTRAR.registerComponentType("arrow_shaft",
                    builder -> builder.persistent(Codec.STRING)
            );

    // fletching key (string)
    public static final Supplier<DataComponentType<String>> FLETCHING =
            REGISTRAR.registerComponentType("arrow_fletching",
                    builder -> builder.persistent(Codec.STRING)
            );

    // modifier key (string)
    public static final Supplier<DataComponentType<ResourceLocation>> MODIFIER =
            REGISTRAR.registerComponentType("arrow_modifier", builder ->
                    builder.persistent(ResourceLocation.CODEC)
            );


    public static void register(IEventBus eventBus) {
        REGISTRAR.register(eventBus);
    }
}