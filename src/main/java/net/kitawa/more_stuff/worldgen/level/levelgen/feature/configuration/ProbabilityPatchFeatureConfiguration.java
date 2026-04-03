package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.List;
import java.util.Optional;

public class ProbabilityPatchFeatureConfiguration implements FeatureConfiguration {

    public static final Codec<ProbabilityPatchFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    // ─── Target block system ───
                    Codec.list(TargetBlockState.CODEC)
                            .fieldOf("targets")
                            .forGetter(cfg -> cfg.targetStates),

                    // ─── Placement probability ───
                    FloatProvider.CODEC
                            .fieldOf("chance")
                            .forGetter(cfg -> cfg.chance),

                    // ─── Optional loot table ───
                    ResourceKey.codec(Registries.LOOT_TABLE)
                            .optionalFieldOf("loot_table")
                            .forGetter(cfg -> Optional.ofNullable(cfg.lootTable)),

                    // ─── Placement direction (like vegetation patch) ───
                    CaveSurface.CODEC
                            .fieldOf("surface")
                            .forGetter(cfg -> cfg.surface),

                    // ─── Depth (how many blocks thick to place)
                    IntProvider.CODEC
                            .fieldOf("depth")
                            .forGetter(cfg -> cfg.depth),

                    // ─── Chance to add one extra depth block
                    Codec.floatRange(0.0F, 1.0F)
                            .fieldOf("extra_bottom_block_chance")
                            .forGetter(cfg -> cfg.extraBottomBlockChance),

                    // ─── Max search distance up/down
                    Codec.intRange(1, 256)
                            .fieldOf("vertical_range")
                            .forGetter(cfg -> cfg.verticalRange),

                    // ─── Horizontal radius
                    IntProvider.CODEC
                            .fieldOf("xz_radius")
                            .forGetter(cfg -> cfg.xzRadius),

                    // ─── Extra chance to extend edges
                    Codec.floatRange(0.0F, 1.0F)
                            .fieldOf("extra_edge_column_chance")
                            .forGetter(cfg -> cfg.extraEdgeColumnChance)
            ).apply(instance, ProbabilityPatchFeatureConfiguration::new)
    );

    public final List<TargetBlockState> targetStates;
    public final FloatProvider chance;
    public final ResourceKey<LootTable> lootTable;
    public final CaveSurface surface;
    public final IntProvider depth;
    public final float extraBottomBlockChance;
    public final int verticalRange;
    public final IntProvider xzRadius;
    public final float extraEdgeColumnChance;

    public ProbabilityPatchFeatureConfiguration(
            List<TargetBlockState> targetStates,
            FloatProvider chance,
            Optional<ResourceKey<LootTable>> lootTable,
            CaveSurface surface,
            IntProvider depth,
            float extraBottomBlockChance,
            int verticalRange,
            IntProvider xzRadius,
            float extraEdgeColumnChance
    ) {
        this.targetStates = targetStates;
        this.chance = chance;
        this.lootTable = lootTable.orElse(null);
        this.surface = surface;
        this.depth = depth;
        this.extraBottomBlockChance = extraBottomBlockChance;
        this.verticalRange = verticalRange;
        this.xzRadius = xzRadius;
        this.extraEdgeColumnChance = extraEdgeColumnChance;
    }

    public static class TargetBlockState {
        public static final Codec<TargetBlockState> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        RuleTest.CODEC.fieldOf("target").forGetter(t -> t.target),
                        BlockState.CODEC.fieldOf("state").forGetter(t -> t.state)
                ).apply(instance, TargetBlockState::new)
        );

        public final RuleTest target;
        public final BlockState state;

        public TargetBlockState(RuleTest target, BlockState state) {
            this.target = target;
            this.state = state;
        }
    }
}
