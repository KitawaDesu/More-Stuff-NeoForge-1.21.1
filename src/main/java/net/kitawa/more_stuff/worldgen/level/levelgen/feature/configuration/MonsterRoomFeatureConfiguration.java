package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;


public class MonsterRoomFeatureConfiguration implements FeatureConfiguration {
    public record WeightedLoot(ResourceLocation table, int weight) {
        public static final Codec<WeightedLoot> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        ResourceLocation.CODEC.fieldOf("table").forGetter(w -> w.table),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(w -> w.weight)
                ).apply(instance, WeightedLoot::new)
        );
    }

    public record NormalConfig(
            int spawnRange,
            float totalMobs,
            float simultaneousMobs,
            float totalMobsAddedPerPlayer,
            float simultaneousMobsAddedPerPlayer,
            int ticksBetweenSpawn,
            List<ResourceLocation> mobs,
            List<WeightedLoot> lootPool,
            Optional<ResourceLocation> ominousDropLoot
    ) {
        public static final Codec<NormalConfig> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("spawn_range").forGetter(n -> n.spawnRange),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("total_mobs").forGetter(n -> n.totalMobs),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("simultaneous_mobs").forGetter(n -> n.simultaneousMobs),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("total_mobs_added_per_player").forGetter(n -> n.totalMobsAddedPerPlayer),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("simultaneous_mobs_added_per_player").forGetter(n -> n.simultaneousMobsAddedPerPlayer),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("ticks_between_spawn").forGetter(n -> n.ticksBetweenSpawn),
                        ResourceLocation.CODEC.listOf().fieldOf("mobs").forGetter(n -> n.mobs),
                        WeightedLoot.CODEC.listOf().fieldOf("loot_pool").forGetter(n -> n.lootPool),
                        ResourceLocation.CODEC.optionalFieldOf("ominous_drop_loot").forGetter(n -> n.ominousDropLoot)
                ).apply(instance, NormalConfig::new)
        );
    }

    public record OminousConfig(
            int spawnRange,
            float totalMobs,
            float simultaneousMobs,
            float totalMobsAddedPerPlayer,
            float simultaneousMobsAddedPerPlayer,
            int ticksBetweenSpawn,
            List<ResourceLocation> mobs,
            List<WeightedLoot> lootPool,
            Optional<ResourceLocation> ominousDropLoot
    ) {
        public static final Codec<OminousConfig> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("spawn_range").forGetter(o -> o.spawnRange),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("total_mobs").forGetter(o -> o.totalMobs),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("simultaneous_mobs").forGetter(o -> o.simultaneousMobs),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("total_mobs_added_per_player").forGetter(o -> o.totalMobsAddedPerPlayer),
                        Codec.floatRange(0f, Float.MAX_VALUE).fieldOf("simultaneous_mobs_added_per_player").forGetter(o -> o.simultaneousMobsAddedPerPlayer),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("ticks_between_spawn").forGetter(o -> o.ticksBetweenSpawn),
                        ResourceLocation.CODEC.listOf().fieldOf("mobs").forGetter(o -> o.mobs),
                        WeightedLoot.CODEC.listOf().fieldOf("loot_pool").forGetter(o -> o.lootPool),
                        ResourceLocation.CODEC.optionalFieldOf("ominous_drop_loot").forGetter(o -> o.ominousDropLoot)
                ).apply(instance, OminousConfig::new)
        );
    }

    public static final Codec<MonsterRoomFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Replacement.CODEC).fieldOf("cobbled_replacements").forGetter(cfg -> cfg.cobbledReplacements),
                    Codec.list(Replacement.CODEC).fieldOf("mossy_replacements").forGetter(cfg -> cfg.mossyReplacements),
                    BlockStateProvider.CODEC.fieldOf("spawner_block").forGetter(cfg -> cfg.spawnerBlock),
                    BlockStateProvider.CODEC.fieldOf("container_block").forGetter(cfg -> cfg.containerBlock),
                    BlockStateProvider.CODEC.fieldOf("trap_block").forGetter(cfg -> cfg.trapBlock),
                    ResourceLocation.CODEC.optionalFieldOf("container_loot").forGetter(cfg -> cfg.containerLoot),
                    ResourceLocation.CODEC.optionalFieldOf("trap_loot").forGetter(cfg -> cfg.trapLoot),
                    BlockState.CODEC.fieldOf("default_cobble").forGetter(cfg -> cfg.defaultCobble),
                    BlockState.CODEC.fieldOf("default_mossy").forGetter(cfg -> cfg.defaultMossy),
                    NormalConfig.CODEC.fieldOf("normal_config").forGetter(cfg -> cfg.normalConfig),
                    OminousConfig.CODEC.fieldOf("ominous_config").forGetter(cfg -> cfg.ominousConfig)
            ).apply(instance, MonsterRoomFeatureConfiguration::new)
    );

    public final List<Replacement> cobbledReplacements;
    public final List<Replacement> mossyReplacements;
    public final BlockStateProvider spawnerBlock;
    public final BlockStateProvider containerBlock;
    public final BlockStateProvider trapBlock;
    public final Optional<ResourceLocation> containerLoot;
    public final Optional<ResourceLocation> trapLoot;
    public final BlockState defaultCobble;
    public final BlockState defaultMossy;

    public final NormalConfig normalConfig;
    public final OminousConfig ominousConfig;

    // Runtime fields
    public final SimpleWeightedRandomList<ResourceKey<LootTable>> normalLootPool;
    public final SimpleWeightedRandomList<ResourceKey<LootTable>> ominousLootPool;
    public final ResourceKey<LootTable> normalDropLoot;
    public final ResourceKey<LootTable> ominousDropLoot;

    public MonsterRoomFeatureConfiguration(
            List<Replacement> cobbledReplacements,
            List<Replacement> mossyReplacements,
            BlockStateProvider spawnerBlock,
            BlockStateProvider containerBlock,
            BlockStateProvider trapBlock,
            Optional<ResourceLocation> containerLoot,
            Optional<ResourceLocation> trapLoot,
            BlockState defaultCobble,
            BlockState defaultMossy,
            NormalConfig normalConfig,
            OminousConfig ominousConfig
    ) {
        this.cobbledReplacements = cobbledReplacements;
        this.mossyReplacements = mossyReplacements;
        this.spawnerBlock = spawnerBlock;
        this.containerBlock = containerBlock;
        this.trapBlock = trapBlock;
        this.containerLoot = containerLoot;
        this.trapLoot = trapLoot;
        this.defaultCobble = defaultCobble;
        this.defaultMossy = defaultMossy;
        this.normalConfig = normalConfig;
        this.ominousConfig = ominousConfig;

        // Initialize weighted loot for runtime
        this.normalLootPool = buildWeightedLoot(normalConfig.lootPool(),
                BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_CONSUMABLES,
                BuiltInLootTables.SPAWNER_TRIAL_CHAMBER_KEY);

        this.ominousLootPool = buildWeightedLoot(ominousConfig.lootPool(),
                BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_CONSUMABLES,
                BuiltInLootTables.SPAWNER_OMINOUS_TRIAL_CHAMBER_KEY);

        // Initialize drop loot keys
        this.normalDropLoot = normalConfig.ominousDropLoot()
                .map(loc -> ResourceKey.create(Registries.LOOT_TABLE, loc))
                .orElse(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS);

        this.ominousDropLoot = ominousConfig.ominousDropLoot()
                .map(loc -> ResourceKey.create(Registries.LOOT_TABLE, loc))
                .orElse(BuiltInLootTables.SPAWNER_TRIAL_ITEMS_TO_DROP_WHEN_OMINOUS);
    }

    private static SimpleWeightedRandomList<ResourceKey<LootTable>> buildWeightedLoot(List<WeightedLoot> list, ResourceKey<LootTable> default1, ResourceKey<LootTable> default2) {
        SimpleWeightedRandomList.Builder<ResourceKey<LootTable>> builder = SimpleWeightedRandomList.builder();
        if (list.isEmpty()) {
            builder.add(default1, 1);
            builder.add(default2, 1);
        } else {
            for (WeightedLoot w : list) {
                builder.add(ResourceKey.create(Registries.LOOT_TABLE, w.table), w.weight);
            }
        }
        return builder.build();
    }

    private static final EntityType<?>[] DEFAULT_MOBS = {
            EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER
    };

    public EntityType<?> randomNormalMob(RandomSource random) {
        if (normalConfig.mobs().isEmpty()) return DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)];
        ResourceLocation id = normalConfig.mobs().get(random.nextInt(normalConfig.mobs().size()));
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)]);
    }

    public EntityType<?> randomOminousMob(RandomSource random) {
        if (ominousConfig.mobs().isEmpty()) return DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)];
        ResourceLocation id = ominousConfig.mobs().get(random.nextInt(ominousConfig.mobs().size()));
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)]);
    }

    public BlockState getReplacement(BlockState original, int yLevel, RandomSource random) {
        List<Replacement> list = yLevel == -1 && random.nextInt(4) != 0 ? mossyReplacements : cobbledReplacements;
        for (Replacement repl : list) {
            if (repl.target.test(original, random)) return repl.state;
        }
        return (yLevel == -1 && random.nextInt(4) != 0) ? defaultMossy : defaultCobble;
    }

    public record Replacement(RuleTest target, BlockState state) {
        public static final Codec<Replacement> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        RuleTest.CODEC.fieldOf("target").forGetter(r -> r.target),
                        BlockState.CODEC.fieldOf("state").forGetter(r -> r.state)
                ).apply(instance, Replacement::new)
        );
    }
}
