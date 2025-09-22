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

import java.util.List;
import java.util.Optional;


public class MonsterRoomFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<MonsterRoomFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Replacement.CODEC.listOf().fieldOf("cobbled_replacements").forGetter(cfg -> cfg.cobbledReplacements),
                    Replacement.CODEC.listOf().fieldOf("mossy_replacements").forGetter(cfg -> cfg.mossyReplacements),
                    BlockStateProvider.CODEC.fieldOf("spawner_block").forGetter(cfg -> cfg.spawnerBlock),
                    BlockStateProvider.CODEC.fieldOf("container_block").forGetter(cfg -> cfg.containerBlock),
                    BlockStateProvider.CODEC.fieldOf("trap_block").forGetter(cfg -> cfg.trapBlock),
                    ResourceLocation.CODEC.optionalFieldOf("container_loot").forGetter(cfg -> cfg.containerLoot),
                    ResourceLocation.CODEC.optionalFieldOf("trap_loot").forGetter(cfg -> cfg.trapLoot), // NEW
                    ResourceLocation.CODEC.listOf().fieldOf("mobs").forGetter(cfg -> cfg.mobs)
            ).apply(instance, MonsterRoomFeatureConfiguration::new)
    );

    public final List<Replacement> cobbledReplacements;
    public final List<Replacement> mossyReplacements;
    public final BlockStateProvider spawnerBlock;
    public final BlockStateProvider containerBlock;
    public final BlockStateProvider trapBlock;
    public final Optional<ResourceLocation> containerLoot;
    public final Optional<ResourceLocation> trapLoot; // NEW
    public final List<ResourceLocation> mobs;

    public MonsterRoomFeatureConfiguration(
            List<Replacement> cobbledReplacements,
            List<Replacement> mossyReplacements,
            BlockStateProvider spawnerBlock,
            BlockStateProvider containerBlock,
            BlockStateProvider trapBlock,
            Optional<ResourceLocation> containerLoot,
            Optional<ResourceLocation> trapLoot, // NEW
            List<ResourceLocation> mobs
    ) {
        this.cobbledReplacements = cobbledReplacements;
        this.mossyReplacements = mossyReplacements;
        this.spawnerBlock = spawnerBlock;
        this.containerBlock = containerBlock;
        this.trapBlock = trapBlock;
        this.containerLoot = containerLoot;
        this.trapLoot = trapLoot; // NEW
        this.mobs = mobs;
    }

    public BlockState getReplacement(BlockState original, int yLevel, RandomSource random) {
        if (yLevel == -1 && random.nextInt(4) != 0) {
            for (Replacement repl : mossyReplacements) {
                if (original.is(repl.tag)) return repl.block.defaultBlockState();
            }
        }
        for (Replacement repl : cobbledReplacements) {
            if (original.is(repl.tag)) return repl.block.defaultBlockState();
        }
        return (yLevel == -1 && random.nextInt(4) != 0)
                ? Blocks.MOSSY_COBBLESTONE.defaultBlockState()
                : Blocks.COBBLESTONE.defaultBlockState();
    }

    public BlockStateProvider spawnerBlock() { return this.spawnerBlock; }
    public BlockStateProvider containerBlock() { return this.containerBlock; }
    public BlockStateProvider trapBlock() { return this.trapBlock; }
    public Optional<ResourceLocation> containerLoot() { return this.containerLoot; }
    public Optional<ResourceLocation> trapLoot() { return this.trapLoot; } // NEW

    private static final EntityType<?>[] DEFAULT_MOBS = new EntityType[]{
            EntityType.SKELETON,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE,
            EntityType.SPIDER
    };

    public EntityType<?> randomMob(RandomSource random) {
        if (mobs.isEmpty()) return DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)];
        ResourceLocation id = mobs.get(random.nextInt(mobs.size()));
        return BuiltInRegistries.ENTITY_TYPE.getOptional(id).orElse(
                DEFAULT_MOBS[random.nextInt(DEFAULT_MOBS.length)]
        );
    }

    public record Replacement(TagKey<Block> tag, Block block) {
        public static final Codec<Replacement> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                TagKey.hashedCodec(BuiltInRegistries.BLOCK.key()).fieldOf("tag").forGetter(Replacement::tag),
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Replacement::block)
        ).apply(instance, Replacement::new));
    }
}