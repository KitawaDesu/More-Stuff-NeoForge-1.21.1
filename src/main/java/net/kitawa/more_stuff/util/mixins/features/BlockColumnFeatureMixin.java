package net.kitawa.more_stuff.util.mixins.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.BlockColumnFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockColumnFeature.class)
public abstract class BlockColumnFeatureMixin extends Feature<BlockColumnConfiguration> {

    public BlockColumnFeatureMixin(Codec<BlockColumnConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockColumnConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        BlockColumnConfiguration blockcolumnconfiguration = context.config();
        RandomSource randomsource = context.random();
        int i = blockcolumnconfiguration.layers().size();
        int[] aint = new int[i];
        int j = 0;

        for (int k = 0; k < i; k++) {
            aint[k] = blockcolumnconfiguration.layers().get(k).height().sample(randomsource);
            j += aint[k];
        }

        if (j == 0) {
            return false;
        } else {
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = context.origin().mutable();
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos$mutableblockpos1.mutable().move(blockcolumnconfiguration.direction());

            for (int l = 0; l < j; l++) {
                if (!blockcolumnconfiguration.allowedPlacement().test(worldgenlevel, blockpos$mutableblockpos)) {
                    truncate(aint, j, l, blockcolumnconfiguration.prioritizeTip());
                    break;
                }

                blockpos$mutableblockpos.move(blockcolumnconfiguration.direction());
            }

            for (int k1 = 0; k1 < i; k1++) {
                int i1 = aint[k1];
                if (i1 != 0) {
                    BlockColumnConfiguration.Layer blockcolumnconfiguration$layer = blockcolumnconfiguration.layers().get(k1);

                    for (int j1 = 0; j1 < i1; j1++) {
                        BlockState stateToPlace = blockcolumnconfiguration$layer.state().getState(randomsource, blockpos$mutableblockpos1);
                        BlockState existingState = worldgenlevel.getBlockState(blockpos$mutableblockpos1);

                        // Waterlogging logic with fallback
                        if (existingState.getFluidState().isSourceOfType(Fluids.WATER)) {
                            if (stateToPlace.hasProperty(BlockStateProperties.WATERLOGGED)) {
                                stateToPlace = stateToPlace.setValue(BlockStateProperties.WATERLOGGED, true);
                            }
                            // If not waterloggable, still place it without change
                        }

                        worldgenlevel.setBlock(blockpos$mutableblockpos1, stateToPlace, 2);
                        blockpos$mutableblockpos1.move(blockcolumnconfiguration.direction());
                    }
                }
            }

            return true;
        }
    }

    @Shadow
    private static void truncate(int[] layerHeights, int totalHeight, int currentHeight, boolean prioritizeTip) {
        int i = totalHeight - currentHeight;
        int j = prioritizeTip ? 1 : -1;
        int k = prioritizeTip ? 0 : layerHeights.length - 1;
        int l = prioritizeTip ? layerHeights.length : -1;

        for (int i1 = k; i1 != l && i > 0; i1 += j) {
            int j1 = layerHeights[i1];
            int k1 = Math.min(j1, i);
            i -= k1;
            layerHeights[i1] -= k1;
        }
    }
}
