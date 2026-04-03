package net.kitawa.more_stuff.experimentals.entities;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.items.entity.ThrownJavelin;
import net.kitawa.more_stuff.items.util.weapons.dynamarrow.DynamicArrowEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ExperimentalCombatEntities {

    // --- Deferred register for entities ---
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MoreStuff.MOD_ID);

    // --- Example: Thrown Spear ---
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownJavelin>> JAVELIN = ENTITIES.register("javelin",
            () -> EntityType.Builder.<ThrownJavelin>of(ThrownJavelin::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)       // width, height
                    .clientTrackingRange(4)  // tracking range
                    .updateInterval(10)      // update interval ticks
                    .build("javelin")); // builder name (internal, no mod ID)

    public static final DeferredHolder<EntityType<?>, EntityType<DynamicArrowEntity>> DYNAMIC_ARROW =
            ENTITIES.register("dynamic_arrow",
                    () -> EntityType.Builder.<DynamicArrowEntity>of(DynamicArrowEntity::new, MobCategory.MISC)
                            .sized(0.5F, 0.5F)       // same size as vanilla arrows
                            .clientTrackingRange(4)  // same tracking range
                            .updateInterval(20)      // how often to sync to clients
                            .build("dynamic_arrow"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}