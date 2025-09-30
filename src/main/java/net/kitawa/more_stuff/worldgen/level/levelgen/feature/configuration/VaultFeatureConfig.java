package net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class VaultFeatureConfig implements FeatureConfiguration {

    public static final Codec<VaultFeatureConfig> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    VaultSubConfig.CODEC.fieldOf("normal_config").forGetter(cfg -> cfg.normalConfig),
                    VaultSubConfig.CODEC.fieldOf("ominous_config").forGetter(cfg -> cfg.ominousConfig),
                    BlockStateProvider.CODEC.fieldOf("vault_block").forGetter(cfg -> cfg.vaultBlock)
            ).apply(instance, VaultFeatureConfig::new)
    );

    public final VaultSubConfig normalConfig;
    public final VaultSubConfig ominousConfig;
    public final BlockStateProvider vaultBlock;

    public VaultFeatureConfig(VaultSubConfig normalConfig, VaultSubConfig ominousConfig, BlockStateProvider vaultBlock) {
        this.normalConfig = normalConfig;
        this.ominousConfig = ominousConfig;
        this.vaultBlock = vaultBlock;
    }

    public VaultSubConfig normalConfig() { return normalConfig; }
    public VaultSubConfig ominousConfig() { return ominousConfig; }
    public BlockStateProvider vaultBlock() { return vaultBlock; }

    // --- nested sub-config ---
    public record VaultSubConfig(
            List<WeightedLoot> lootPool,
            double activationRange,
            double deactivationRange,
            ItemStack keyItem
    ) {
        public static final Codec<VaultSubConfig> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        WeightedLoot.CODEC.listOf().fieldOf("loot_pool").forGetter(cfg -> cfg.lootPool),
                        Codec.DOUBLE.fieldOf("activation_range").forGetter(VaultSubConfig::activationRange),
                        Codec.DOUBLE.fieldOf("deactivation_range").forGetter(VaultSubConfig::deactivationRange),
                        ItemStack.CODEC.fieldOf("key_item").forGetter(VaultSubConfig::keyItem)
                ).apply(instance, VaultSubConfig::new)
        );

        /** WeightedLoot record for codec */
        public static record WeightedLoot(ResourceKey<LootTable> table, int weight) {
            public static final Codec<WeightedLoot> CODEC = RecordCodecBuilder.create(instance ->
                    instance.group(
                            ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(w -> w.table),
                            Codec.INT.fieldOf("weight").forGetter(w -> w.weight)
                    ).apply(instance, WeightedLoot::new)
            );
        }

        /** Pick one random loot table from the weighted list */
        public ResourceKey<LootTable> pickRandom(RandomSource random) {
            SimpleWeightedRandomList.Builder<ResourceKey<LootTable>> builder = SimpleWeightedRandomList.builder();
            for (WeightedLoot loot : lootPool) {
                builder.add(loot.table, loot.weight);
            }
            return builder.build().getRandom(random)
                    .orElseThrow(() -> new IllegalStateException("VaultSubConfig lootPool is empty")).data();
        }
    }
}
