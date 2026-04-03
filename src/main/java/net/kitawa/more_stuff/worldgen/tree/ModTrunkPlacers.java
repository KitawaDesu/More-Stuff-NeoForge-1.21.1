package net.kitawa.more_stuff.worldgen.tree;

import com.mojang.serialization.MapCodec;
import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.HybernatusTrunkPlacer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTrunkPlacers {

    public static final DeferredRegister<TrunkPlacerType<?>> TRUNKS =
            DeferredRegister.create(Registries.TRUNK_PLACER_TYPE, MoreStuff.MOD_ID);

    public static final DeferredHolder<TrunkPlacerType<?>, TrunkPlacerType<HybernatusTrunkPlacer>> HYBERNATUS_TRUNK_PLACER =
            TRUNKS.register("hybernatus_trunk_placer",
                    () -> new TrunkPlacerType<>(HybernatusTrunkPlacer.CODEC));

    public static void register(IEventBus bus) {
        TRUNKS.register(bus);
    }
}
