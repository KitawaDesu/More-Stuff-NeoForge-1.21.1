package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.BendingMushroomFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HugeGlowshroomFeature extends AbstractBendingHugeMushroomFeature {
    public HugeGlowshroomFeature(Codec<BendingMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected void makeCap(
            LevelAccessor level,
            RandomSource random,
            BlockPos pos,
            int treeHeight,
            BlockPos.MutableBlockPos mutable,
            BendingMushroomFeatureConfiguration config
    ) {
        int radius = config.foliageRadius;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                // Keep circular shape for larger caps
                if (radius <= 2 || dx * dx + dz * dz <= radius * radius) {
                    mutable.setWithOffset(pos, dx, treeHeight, dz);

                    if (canReplace(level.getBlockState(mutable))) {

                        BlockState blockstate = config.capProvider.getState(random, pos);

                        if (blockstate.hasProperty(HugeMushroomBlock.WEST)
                                && blockstate.hasProperty(HugeMushroomBlock.EAST)
                                && blockstate.hasProperty(HugeMushroomBlock.NORTH)
                                && blockstate.hasProperty(HugeMushroomBlock.SOUTH)) {

                            // Check for neighboring cap blocks
                            boolean west = !level.getBlockState(mutable.west()).is(blockstate.getBlock());
                            boolean east = !level.getBlockState(mutable.east()).is(blockstate.getBlock());
                            boolean north = !level.getBlockState(mutable.north()).is(blockstate.getBlock());
                            boolean south = !level.getBlockState(mutable.south()).is(blockstate.getBlock());

                            blockstate = blockstate
                                    .setValue(HugeMushroomBlock.WEST, west)
                                    .setValue(HugeMushroomBlock.EAST, east)
                                    .setValue(HugeMushroomBlock.NORTH, north)
                                    .setValue(HugeMushroomBlock.SOUTH, south);
                        }

                        this.setBlock(level, mutable, blockstate);
                    }
                }
            }
        }
    }

    @Override
    protected int getTreeRadiusForHeight(int unused, int height, int foliageRadius, int y) {
        return y <= 3 ? 0 : foliageRadius;
    }

    // Reuse same canReplace logic from AbstractBendingHugeMushroomFeature
    public static boolean canReplace(BlockState state) {
        return !state.isSolidRender(null, null); // replace any non-solid block, including water
    }
}
