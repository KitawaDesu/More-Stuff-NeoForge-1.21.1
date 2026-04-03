package net.kitawa.more_stuff.blocks;

import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedSpawnerBlockEntity;
import net.kitawa.more_stuff.blocks.custom.overworld.electricity.OmniBlockEntity;
import net.kitawa.more_stuff.blocks.custom.overworld.electricity.TeslaCoilBlockEntity;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedBrushableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BLOCK_ENTITY_TYPE, "more_stuff");

    // ✅ use DeferredHolder instead of Registry
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OmniBlockEntity>> OMNI =
            BLOCK_ENTITIES.register("omni",
                    () -> BlockEntityType.Builder.of(
                            OmniBlockEntity::new,
                            ModBlocks.STORMVEIN.get() // or your OmniBlock
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TeslaCoilBlockEntity>> TESLA_COIL =
            BLOCK_ENTITIES.register("tesla_coil",
                    () -> BlockEntityType.Builder.of(
                            TeslaCoilBlockEntity::new,
                            ModBlocks.TESLA_COIL.get() // or your OmniBlock
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModdedBrushableBlockEntity>> BRUSHABLE_BLOCK_NO_FALL =
            BLOCK_ENTITIES.register("brushable_block_no_fall",
                    () -> BlockEntityType.Builder.of(
                            ModdedBrushableBlockEntity::new,
                            ModBlocks.SUSPICIOUS_DIRT.get(),
                            ModBlocks.SUSPICIOUS_COARSE_DIRT.get(),
                            ModBlocks.SUSPICIOUS_SOUL_SOIL.get(),
                            ModBlocks.SUSPICIOUS_SOUL_SAND.get(),
                            ModBlocks.SUSPICIOUS_RED_SAND.get()
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModdedSpawnerBlockEntity>> MOD_SPAWNER =
            BLOCK_ENTITIES.register("mod_spawner",
                    () -> BlockEntityType.Builder.of(
                            ModdedSpawnerBlockEntity::new,
                            ModBlocks.GOLDEN_SPAWNER.get()
                    ).build(null));


    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}