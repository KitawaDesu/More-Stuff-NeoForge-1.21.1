package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class SubchunkBiomeReplaceConfig implements FeatureConfiguration {

    // Codec for a single biome key
    private static final Codec<Either<TagKey<Biome>, ResourceKey<Biome>>> BIOME_TARGET_CODEC =
            Codec.either(
                    TagKey.hashedCodec(Registries.BIOME),   // handles "#namespace:path"
                    ResourceKey.codec(Registries.BIOME)     // handles "namespace:path"
            );

    public static final Codec<SubchunkBiomeReplaceConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("target_block")
                            .forGetter(cfg -> cfg.targetBlock),
                    BlockStateProvider.CODEC.fieldOf("replacement_block")
                            .forGetter(cfg -> cfg.replacementBlock),
                    BIOME_TARGET_CODEC.fieldOf("required_biome")
                            .forGetter(cfg -> cfg.requiredBiome),
                    Codec.INT.optionalFieldOf("thickness", 1)
                            .forGetter(cfg -> cfg.thickness)
            ).apply(instance, SubchunkBiomeReplaceConfig::new)
    );

    public final Block targetBlock;
    public final BlockStateProvider replacementBlock;
    public final Either<TagKey<Biome>, ResourceKey<Biome>> requiredBiome;
    public final int thickness;

    public SubchunkBiomeReplaceConfig(Block targetBlock, BlockStateProvider replacementBlock,
                                      Either<TagKey<Biome>, ResourceKey<Biome>> requiredBiome,
                                      int thickness) {
        this.targetBlock = targetBlock;
        this.replacementBlock = replacementBlock;
        this.requiredBiome = requiredBiome;
        this.thickness = thickness;
    }

    public Block targetBlock()                                          { return targetBlock; }
    public BlockStateProvider replacementBlock()                        { return replacementBlock; }
    public Either<TagKey<Biome>, ResourceKey<Biome>> requiredBiome()   { return requiredBiome; }
    public int thickness()                                              { return thickness; }

    /**
     * Returns true if the given biome holder matches this config's required_biome,
     * whether it's a tag or a direct resource key.
     */
    public boolean biomeMatches(Holder<Biome> biomeHolder) {
        return requiredBiome.map(
                biomeHolder::is,   // left: TagKey<Biome>
                biomeHolder::is    // right: ResourceKey<Biome>
        );
    }
}
