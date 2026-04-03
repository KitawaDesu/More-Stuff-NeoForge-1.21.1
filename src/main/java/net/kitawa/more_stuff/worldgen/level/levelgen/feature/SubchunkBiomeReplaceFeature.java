package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.SubchunkBiomeReplaceConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class SubchunkBiomeReplaceFeature extends Feature<SubchunkBiomeReplaceConfig> {

    public SubchunkBiomeReplaceFeature(Codec<SubchunkBiomeReplaceConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SubchunkBiomeReplaceConfig> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        SubchunkBiomeReplaceConfig config = context.config();

        int centerX = (origin.getX() >> 4 << 4) + 8;
        int centerZ = (origin.getZ() >> 4 << 4) + 8;
        int y = origin.getY();
        int thickness = config.thickness();
        Block targetBlock = config.targetBlock();
        BlockStateProvider replacementBlock = config.replacementBlock();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int dx = -8; dx < 8; dx++) {
            for (int dz = -8; dz < 8; dz++) {
                for (int dy = 0; dy < thickness; dy++) {
                    pos.set(centerX + dx, y + dy, centerZ + dz);

                    if (!level.getBlockState(pos).is(targetBlock)) continue;
                    if (!config.biomeMatches(level.getBiome(pos))) continue;

                    level.setBlock(pos, replacementBlock.getState(random, pos), 2);
                }
            }
        }

        return true;
    }
}