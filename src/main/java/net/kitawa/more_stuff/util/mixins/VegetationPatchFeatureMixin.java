package net.kitawa.more_stuff.util.mixins;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.VegetationPatchFeature;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

@Mixin(VegetationPatchFeature.class)
public abstract class VegetationPatchFeatureMixin extends Feature<VegetationPatchConfiguration> {
    public VegetationPatchFeatureMixin(Codec<VegetationPatchConfiguration> codec) {
        super(codec);
    }

    /**
     * Places the given feature at the given location.
     * During world generation, features are provided with a 3x3 region of chunks, centered on the chunk being generated, that they can safely generate into.
     *
     * @param context A context object with a reference to the level and the position
     *                the feature is being placed at
     */
    @Override
    public boolean place(FeaturePlaceContext<VegetationPatchConfiguration> context) {
        WorldGenLevel worldgenlevel = context.level();
        VegetationPatchConfiguration vegetationpatchconfiguration = context.config();
        RandomSource randomsource = context.random();
        BlockPos blockpos = context.origin();
        Predicate<BlockState> predicate = p_204782_ -> p_204782_.is(vegetationpatchconfiguration.replaceable);
        int i = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        int j = vegetationpatchconfiguration.xzRadius.sample(randomsource) + 1;
        Set<BlockPos> set = this.more_Stuff_NeoForge_1_21_1$placeGroundPatch(worldgenlevel, vegetationpatchconfiguration, randomsource, blockpos, predicate, i, j);
        this.more_Stuff_NeoForge_1_21_1$distributeVegetation(context, worldgenlevel, vegetationpatchconfiguration, randomsource, set, i, j);
        return !set.isEmpty();
    }

    @Unique
    protected Set<BlockPos> more_Stuff_NeoForge_1_21_1$placeGroundPatch(
            WorldGenLevel level,
            VegetationPatchConfiguration config,
            RandomSource random,
            BlockPos pos,
            Predicate<BlockState> state,
            int xRadius,
            int zRadius
    ) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = blockpos$mutableblockpos.mutable();
        Direction direction = config.surface.getDirection();
        Direction direction1 = direction.getOpposite();
        Set<BlockPos> set = new HashSet<>();

        for (int i = -xRadius; i <= xRadius; i++) {
            boolean flag = i == -xRadius || i == xRadius;

            for (int j = -zRadius; j <= zRadius; j++) {
                boolean flag1 = j == -zRadius || j == zRadius;
                boolean flag2 = flag || flag1;
                boolean flag3 = flag && flag1;
                boolean flag4 = flag2 && !flag3;
                if (!flag3 && (!flag4 || config.extraEdgeColumnChance != 0.0F && !(random.nextFloat() > config.extraEdgeColumnChance))) {
                    blockpos$mutableblockpos.setWithOffset(pos, i, 0, j);

                    for (int k = 0;
                         level.isStateAtPosition(blockpos$mutableblockpos, p_284926_ -> p_284926_.is(ModBlockTags.MOSS_CAN_GENERATE_UNDER)) && k < config.verticalRange;
                         k++
                    ) {
                        blockpos$mutableblockpos.move(direction);
                    }

                    for (int i1 = 0;
                         level.isStateAtPosition(blockpos$mutableblockpos, p_284926_ -> !p_284926_.is(ModBlockTags.MOSS_CAN_GENERATE_UNDER)) && i1 < config.verticalRange;
                         i1++
                    ) {
                        blockpos$mutableblockpos.move(direction1);
                    }

                    blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, config.surface.getDirection());
                    BlockState blockstate = level.getBlockState(blockpos$mutableblockpos1);
                    if (level.isWaterAt(blockpos$mutableblockpos) || level.isEmptyBlock(blockpos$mutableblockpos) || level.isStateAtPosition(blockpos$mutableblockpos, p_284926_ -> p_284926_.is(ModBlockTags.MOSS_CAN_GENERATE_UNDER))
                            && blockstate.isFaceSturdy(level, blockpos$mutableblockpos1, config.surface.getDirection().getOpposite())) {
                        int l = config.depth.sample(random)
                                + (config.extraBottomBlockChance > 0.0F && random.nextFloat() < config.extraBottomBlockChance ? 1 : 0);
                        BlockPos blockpos = blockpos$mutableblockpos1.immutable();
                        boolean flag5 = this.placeGround(level, config, state, random, blockpos$mutableblockpos1, l);
                        if (flag5) {
                            set.add(blockpos);
                        }
                    }
                }
            }
        }

        return set;
    }


    @Unique
    protected void more_Stuff_NeoForge_1_21_1$distributeVegetation(
            FeaturePlaceContext<VegetationPatchConfiguration> context,
            WorldGenLevel level,
            VegetationPatchConfiguration config,
            RandomSource random,
            Set<BlockPos> possiblePositions,
            int xRadius,
            int zRadius
    ) {
        for (BlockPos blockpos : possiblePositions) {
            if (config.vegetationChance > 0.0F && random.nextFloat() < config.vegetationChance) {
                this.placeVegetation(level, config, context.chunkGenerator(), random, blockpos);
            } else if (config.vegetationChance == 0.0F) {
                return;
            }
        }
    }

    protected boolean placeVegetation(
            WorldGenLevel level, VegetationPatchConfiguration config, ChunkGenerator chunkGenerator, RandomSource random, BlockPos pos
    ) {
        return config.vegetationFeature.value().place(level, chunkGenerator, random, pos.relative(config.surface.getDirection().getOpposite()));
    }

    protected boolean placeGround(
            WorldGenLevel level,
            VegetationPatchConfiguration config,
            Predicate<BlockState> replaceableblocks,
            RandomSource random,
            BlockPos.MutableBlockPos mutablePos,
            int maxDistance
    ) {
        for (int i = 0; i < maxDistance; i++) {
            BlockState blockstate = config.groundState.getState(random, mutablePos);
            BlockState blockstate1 = level.getBlockState(mutablePos);
            if (!blockstate.is(blockstate1.getBlock())) {
                if (!replaceableblocks.test(blockstate1)) {
                    return i != 0;
                }

                level.setBlock(mutablePos, blockstate, 2);
                mutablePos.move(config.surface.getDirection());
            }
        }

        return true;
    }
}
