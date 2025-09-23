package net.kitawa.more_stuff.worldgen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.*;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, MoreStuff.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<DripstoneClusterConfiguration>> REDSTONIC_CLUSTER =
            FEATURES.register("redstonic_cluster",
                    () -> new RedstonicClusterFeature(DripstoneClusterConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<ModLargeDripstoneConfiguration>> LARGE_REDSTONIC =
            FEATURES.register("large_redstonic",
                    () -> new LargeRedstonicFeature(ModLargeDripstoneConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<PointedDripstoneConfiguration>> POINTED_REDSTONIC =
            FEATURES.register("pointed_redstonic",
                    () -> new PointedRedstonicFeature(PointedDripstoneConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<MultifaceGrowthConfiguration>> MULTI_FACE_SURFACE =
            FEATURES.register("multi_face_surface",
                    () -> new MultiFaceSurfaceFeature(MultifaceGrowthConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<DripstoneClusterConfiguration>> ICICLE_CLUSTER =
            FEATURES.register("icicle_cluster",
                    () -> new IcicleClusterFeature(DripstoneClusterConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<PointedDripstoneConfiguration>> ICICLE =
            FEATURES.register("icicle",
                    () -> new IcicleFeature(PointedDripstoneConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<WaterloggedVegetationPatchFeatureConfiguration>> WATERLOGGED_VEGETATION_PATCH =
            FEATURES.register("waterlogged_vegetation_patch",
                    () -> new WaterloggedVegetationPatchFeature(WaterloggedVegetationPatchFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<StormveinConfiguration>> STORMVEIN =
            FEATURES.register("stormvein",
                    () -> new StormveinFeature(StormveinConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, Feature<VoltaicPatchConfig>> VOLTAIC_PATCH =
            FEATURES.register("voltaic_patch",
                    () -> new VoltaicPatch(VoltaicPatchConfig.CODEC));

    public static final DeferredHolder<Feature<?>, TeslaPatchFeature> TESLA_PATCH =
            FEATURES.register("tesla_patch",
                    () -> new TeslaPatchFeature(TeslaPatchConfig.CODEC));

    public static final DeferredHolder<Feature<?>, HugeGlowshroomFeature> HUGE_GLOWSHROOM =
            FEATURES.register("huge_glowshroom",
                    () -> new HugeGlowshroomFeature(BendingMushroomFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, KelpColumnFeature> KELP_COLUMN =
            FEATURES.register("kelp_column",
                    () -> new KelpColumnFeature(KelpColumnFeatureConfiguration.CODEC));

    public static final DeferredHolder<Feature<?>, MonsterRoomFeature> MONSTER_ROOM =
            FEATURES.register("monster_room",
                    () -> new MonsterRoomFeature(MonsterRoomFeatureConfiguration.CODEC));
}
