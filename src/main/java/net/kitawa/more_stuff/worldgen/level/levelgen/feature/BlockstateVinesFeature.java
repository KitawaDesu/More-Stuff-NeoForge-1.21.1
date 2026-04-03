package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.BlockstateVinesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TwistingVinesConfig;

public class BlockstateVinesFeature extends Feature<BlockstateVinesConfig> {
    public BlockstateVinesFeature(Codec<BlockstateVinesConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockstateVinesConfig> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (isInvalidPlacementLocation(level, origin, context.config())) {
            return false;
        }

        RandomSource random = context.random();
        BlockstateVinesConfig config = context.config();
        int i = config.spreadWidth();
        int j = config.spreadHeight();
        int k = config.maxHeight();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int l = 0; l < i * i; l++) {
            mutable.set(origin)
                    .move(Mth.nextInt(random, -i, i), Mth.nextInt(random, -j, j), Mth.nextInt(random, -i, i));

            if (findFirstAirBlockAboveGround(level, mutable)
                    && !isInvalidPlacementLocation(level, mutable, config)) {
                int length = Mth.nextInt(random, 1, k);
                if (random.nextInt(6) == 0) length *= 2;
                if (random.nextInt(5) == 0) length = 1;

                placeVineColumn(level, random, mutable, length, 17, 25, config);
            }
        }

        return true;
    }

    private static boolean findFirstAirBlockAboveGround(LevelAccessor level, BlockPos.MutableBlockPos pos) {
        do {
            pos.move(0, -1, 0);
            if (level.isOutsideBuildHeight(pos)) return false;
        } while (level.getBlockState(pos).isAir());

        pos.move(0, 1, 0);
        return true;
    }

    public static void placeVineColumn(
            LevelAccessor level, RandomSource random, BlockPos.MutableBlockPos pos,
            int length, int minAge, int maxAge, BlockstateVinesConfig config) {

        for (int i = 1; i <= length; i++) {
            if (level.isEmptyBlock(pos)) {
                if (i == length || !level.isEmptyBlock(pos.above())) {
                    BlockState headState = config.headBlock().getState(random, pos)
                            .setValue(GrowingPlantHeadBlock.AGE, Mth.nextInt(random, minAge, maxAge));
                    level.setBlock(pos, headState, 2);
                    break;
                }
                BlockState bodyState = config.bodyBlock().getState(random, pos);
                level.setBlock(pos, bodyState, 2);
            }
            pos.move(Direction.UP);
        }
    }

    private static boolean isInvalidPlacementLocation(LevelAccessor level, BlockPos pos,
                                                      BlockstateVinesConfig config) {
        if (!level.isEmptyBlock(pos)) return true;
        BlockState below = level.getBlockState(pos.below());
        return config.placeOnBlocks().stream().noneMatch(below::is);
    }
}