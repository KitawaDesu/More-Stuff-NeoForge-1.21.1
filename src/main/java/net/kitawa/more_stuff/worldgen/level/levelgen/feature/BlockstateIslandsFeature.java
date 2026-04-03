package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.BlockstateIslandsConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;

public class BlockstateIslandsFeature extends Feature<BlockstateIslandsConfig> {

    public BlockstateIslandsFeature(Codec<BlockstateIslandsConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockstateIslandsConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        BlockstateIslandsConfig config = context.config();

        float radius = (float) random.nextInt(3) + 4.0F;

        for (int layer = 0; radius > 0.5F; layer--) {
            for (int dx = Mth.floor(-radius); dx <= Mth.ceil(radius); dx++) {
                for (int dz = Mth.floor(-radius); dz <= Mth.ceil(radius); dz++) {
                    if ((float)(dx * dx + dz * dz) <= (radius + 1.0F) * (radius + 1.0F)) {
                        BlockPos pos = origin.offset(dx, layer, dz);
                        BlockState state = resolveState(config, layer);
                        this.setBlock(level, pos, state);
                    }
                }
            }
            radius -= (float) random.nextInt(2) + 0.5F;
        }

        return true;
    }

    private BlockState resolveState(BlockstateIslandsConfig config, int layer) {
        int depth = -layer; // depth 0 = surface, depth 1 = one below, etc.

        if (config.mode == BlockstateIslandsConfig.Mode.SINGULAR) {
            return config.singularState;
        }

        List<BlockstateIslandsConfig.LayeredEntry> layers = config.layeredStates;

        if (layers.isEmpty()) return Blocks.END_STONE.defaultBlockState();
        if (layers.size() == 1) return layers.get(0).state;

        if (layers.size() == 2) {
            return depth == 0 ? layers.get(0).state : layers.get(1).state;
        }

        // 3+ entries
        if (depth == 0) return layers.get(0).state;

        int currentDepth = 1;
        for (int i = 1; i < layers.size() - 1; i++) {
            int range = Math.max(1, layers.get(i).layerRange);
            if (depth >= currentDepth && depth < currentDepth + range) {
                return layers.get(i).state;
            }
            currentDepth += range;
        }

        return layers.get(layers.size() - 1).state;
    }
}
