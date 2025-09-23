package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;

public class WaterloggedVegetationPatchFeatureConfiguration implements FeatureConfiguration {
    public static final Codec<WaterloggedVegetationPatchFeatureConfiguration> CODEC = RecordCodecBuilder.create(
            p_161304_ -> p_161304_.group(
                            TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable").forGetter(p_204869_ -> p_204869_.replaceable),
                            BlockStateProvider.CODEC.fieldOf("ground_state").forGetter(p_161322_ -> p_161322_.groundState),
                            PlacedFeature.CODEC.fieldOf("vegetation_feature").forGetter(p_204867_ -> p_204867_.vegetationFeature),
                            PlacedFeature.CODEC.optionalFieldOf("underwater_vegetation_feature").forGetter(p_ -> p_.underwaterVegetationFeature),
                            CaveSurface.CODEC.fieldOf("surface").forGetter(p_161318_ -> p_161318_.surface),
                            IntProvider.codec(1, 128).fieldOf("depth").forGetter(p_161316_ -> p_161316_.depth),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("extra_bottom_block_chance").forGetter(p_161314_ -> p_161314_.extraBottomBlockChance),
                            Codec.intRange(1, 256).fieldOf("vertical_range").forGetter(p_161312_ -> p_161312_.verticalRange),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("vegetation_chance").forGetter(p_161310_ -> p_161310_.vegetationChance),
                            IntProvider.CODEC.fieldOf("xz_radius").forGetter(p_161308_ -> p_161308_.xzRadius),
                            Codec.floatRange(0.0F, 1.0F).fieldOf("extra_edge_column_chance").forGetter(p_161306_ -> p_161306_.extraEdgeColumnChance)
                    )
                    .apply(p_161304_, WaterloggedVegetationPatchFeatureConfiguration::new)
    );

    public final TagKey<Block> replaceable;
    public final BlockStateProvider groundState;
    public final Holder<PlacedFeature> vegetationFeature;
    public final Optional<Holder<PlacedFeature>> underwaterVegetationFeature;
    public final CaveSurface surface;
    public final IntProvider depth;
    public final float extraBottomBlockChance;
    public final int verticalRange;
    public final float vegetationChance;
    public final IntProvider xzRadius;
    public final float extraEdgeColumnChance;

    public WaterloggedVegetationPatchFeatureConfiguration(
            TagKey<Block> replaceable,
            BlockStateProvider groundState,
            Holder<PlacedFeature> vegetationFeature,
            Optional<Holder<PlacedFeature>> underwaterVegetationFeature,
            CaveSurface surface,
            IntProvider depth,
            float extraBottomBlockChance,
            int verticalRange,
            float vegetationChance,
            IntProvider xzRadius,
            float extraEdgeColumnChance
    ) {
        this.replaceable = replaceable;
        this.groundState = groundState;
        this.vegetationFeature = vegetationFeature;
        this.underwaterVegetationFeature = underwaterVegetationFeature;
        this.surface = surface;
        this.depth = depth;
        this.extraBottomBlockChance = extraBottomBlockChance;
        this.verticalRange = verticalRange;
        this.vegetationChance = vegetationChance;
        this.xzRadius = xzRadius;
        this.extraEdgeColumnChance = extraEdgeColumnChance;
    }
}
