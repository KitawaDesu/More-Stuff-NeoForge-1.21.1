package net.kitawa.more_stuff.entities;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.entities.monster.*;
import net.kitawa.more_stuff.entities.projectiles.VeilProjectile;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MoreStuff.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ZombieWolf>> ZOMBIE_WOLF =
            ENTITIES.register("zombie_wolf",
                    () -> EntityType.Builder.of(ZombieWolf::new, MobCategory.MONSTER)
                            .sized(0.85f, 0.85f)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("zombie_wolf"));

    public static final DeferredHolder<EntityType<?>, EntityType<AquandaSlime>> AQUANDA_SLIME =
            ENTITIES.register("aquanda_slime",
                    () -> EntityType.Builder.of(AquandaSlime::new, MobCategory.MONSTER)
                            .sized(0.52F, 0.52F)
                            .eyeHeight(0.325F)
                            .spawnDimensionsScale(4.0F)
                            .clientTrackingRange(10)
                            .build("aquanda_slime"));

    public static final DeferredHolder<EntityType<?>, EntityType<ColoredSlime>> COLORED_SLIME =
            ENTITIES.register("colored_slime",
                    () -> EntityType.Builder.of(ColoredSlime::new, MobCategory.MONSTER)
                            .sized(0.52F, 0.52F)
                            .eyeHeight(0.325F)
                            .spawnDimensionsScale(4.0F)
                            .clientTrackingRange(10)
                            .build("colored_slime"));

    public static final DeferredHolder<EntityType<?>, EntityType<VeilStalkerEntity>> VEIL_STALKER =
            ENTITIES.register("veil_stalker",
                    () -> EntityType.Builder.of(VeilStalkerEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.8F)
                            .eyeHeight(1.62F)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("veil_stalker"));

    public static final DeferredHolder<EntityType<?>, EntityType<VeilProjectile>> VEIL_PROJECTILE =
            ENTITIES.register("veil_projectile",
                    () -> EntityType.Builder.<VeilProjectile>of(VeilProjectile::new, MobCategory.MISC)
                            .sized(0.3F, 0.3F)
                            .clientTrackingRange(8)
                            .updateInterval(1)
                            .build("veil_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<VeilWraithEntity>> VEIL_WRAITH =
            ENTITIES.register("veil_wraith",
                    () -> EntityType.Builder.of(VeilWraithEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 2.9F)
                            .eyeHeight(2.55F)
                            .passengerAttachments(2.80625F)
                            .clientTrackingRange(8)
                            .updateInterval(3)
                            .build("veil_wraith"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ZOMBIE_WOLF.get(), ZombieWolf.createAttributes().build());
        event.put(AQUANDA_SLIME.get(), AquandaSlime.createAttributes().build());
        event.put(COLORED_SLIME.get(), ColoredSlime.createAttributes().build());
        event.put(VEIL_STALKER.get(), VeilStalkerEntity.createAttributes().build());
        event.put(VEIL_WRAITH.get(), VeilWraithEntity.createAttributes().build());
    }

    public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ZOMBIE_WOLF.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ZombieWolf::checkZombieWolfSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                AQUANDA_SLIME.get(),
                ModSpawnPlacementTypes.ON_GROUND_OR_IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AquandaSlime::checkAquandaSlimeSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                VEIL_STALKER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VeilStalkerEntity::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                VEIL_WRAITH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Monster::checkMonsterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }
}
