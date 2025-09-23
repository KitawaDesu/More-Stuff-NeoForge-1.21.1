package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.MonsterRoomFeature;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SpawnerBlock;
import net.minecraft.world.level.block.TrialSpawnerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;
import java.util.Optional;


public class MonsterRoomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<MonsterRoomFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.list(Replacement.CODEC).fieldOf("cobbled_replacements").forGetter(cfg -> cfg.cobbledReplacements),
                    Codec.list(Replacement.CODEC).fieldOf("mossy_replacements").forGetter(cfg -> cfg.mossyReplacements),
                    BlockStateProvider.CODEC.fieldOf("spawner_block").forGetter(cfg -> cfg.spawnerBlock),
                    BlockStateProvider.CODEC.fieldOf("container_block").forGetter(cfg -> cfg.containerBlock),
                    BlockStateProvider.CODEC.fieldOf("trap_block").forGetter(cfg -> cfg.trapBlock),
                    ResourceLocation.CODEC.optionalFieldOf("container_loot").forGetter(cfg -> cfg.containerLoot),
                    ResourceLocation.CODEC.optionalFieldOf("trap_loot").forGetter(cfg -> cfg.trapLoot),
                    ResourceLocation.CODEC.listOf().fieldOf("mobs").forGetter(cfg -> cfg.mobs),
                    BlockState.CODEC.fieldOf("default_cobble").forGetter(cfg -> cfg.defaultCobble),
                    BlockState.CODEC.fieldOf("default_mossy").forGetter(cfg -> cfg.defaultMossy)
            ).apply(instance, MonsterRoomFeatureConfiguration::new)
    );

    public final List<Replacement> cobbledReplacements;
    public final List<Replacement> mossyReplacements;
    public final BlockStateProvider spawnerBlock;
    public final BlockStateProvider containerBlock;
    public final BlockStateProvider trapBlock;
    public final Optional<ResourceLocation> containerLoot;
    public final Optional<ResourceLocation> trapLoot;
    public final List<ResourceLocation> mobs;
    public final BlockState defaultCobble;
    public final BlockState defaultMossy;

    public MonsterRoomFeatureConfiguration(
            List<Replacement> cobbledReplacements,
            List<Replacement> mossyReplacements,
            BlockStateProvider spawnerBlock,
            BlockStateProvider containerBlock,
            BlockStateProvider trapBlock,
            Optional<ResourceLocation> containerLoot,
            Optional<ResourceLocation> trapLoot,
            List<ResourceLocation> mobs,
            BlockState defaultCobble,
            BlockState defaultMossy
    ) {
        this.cobbledReplacements = cobbledReplacements;
        this.mossyReplacements = mossyReplacements;
        this.spawnerBlock = spawnerBlock;
        this.containerBlock = containerBlock;
        this.trapBlock = trapBlock;
        this.containerLoot = containerLoot;
        this.trapLoot = trapLoot;
        this.mobs = mobs;
        this.defaultCobble = defaultCobble;
        this.defaultMossy = defaultMossy;
    }

    public BlockState getReplacement(BlockState original, int yLevel, RandomSource random) {
        List<Replacement> list = yLevel == -1 && random.nextInt(4) != 0 ? mossyReplacements : cobbledReplacements;
        for (Replacement repl : list) {
            if (repl.target.test(original, random)) return repl.state;
        }
        return (yLevel == -1 && random.nextInt(4) != 0) ? defaultMossy : defaultCobble;
    }

    public BlockStateProvider spawnerBlock() { return spawnerBlock; }
    public BlockStateProvider containerBlock() { return containerBlock; }
    public BlockStateProvider trapBlock() { return trapBlock; }
    public Optional<ResourceLocation> containerLoot() { return containerLoot; }
    public Optional<ResourceLocation> trapLoot() { return trapLoot; }

    private static final EntityType<?>[] DEFAULT_MOBS = {
            EntityType.SKELETON, EntityType.ZOMBIE, EntityType.ZOMBIE, EntityType.SPIDER
    };

    public EntityType<?> randomMob(RandomSource random) {
        if (mobs.isEmpty()) return DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)];
        ResourceLocation id = mobs.get(random.nextInt(mobs.size()));
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id)
                .orElse(DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)]);
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
