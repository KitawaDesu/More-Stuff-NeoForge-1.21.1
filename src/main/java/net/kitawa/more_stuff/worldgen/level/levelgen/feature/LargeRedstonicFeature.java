package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.ModLargeDripstoneConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.Heightmap;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.utils.RedstonicUtils;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;

public class LargeRedstonicFeature extends Feature<ModLargeDripstoneConfiguration> {
    public LargeRedstonicFeature(Codec<ModLargeDripstoneConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<ModLargeDripstoneConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        ModLargeDripstoneConfiguration config = context.config();
        RandomSource random = context.random();

        if (!RedstonicUtils.isEmptyOrWater(level, origin)) {
            return false;
        }

        Optional<Column> optional = Column.scan(
                level,
                origin,
                config.floorToCeilingSearchRange,
                RedstonicUtils::isEmptyOrWater,
                RedstonicUtils::isDripstoneBaseOrLava
        );

        if (optional.isEmpty() || !(optional.get() instanceof Column.Range range)) {
            return false;
        }

        if (range.height() < 4) {
            return false;
        }

        int i = (int)((float)range.height() * config.maxColumnRadiusToCaveHeightRatio);
        int j = Mth.clamp(i, config.columnRadius.getMinValue(), config.columnRadius.getMaxValue());
        int k = Mth.randomBetweenInclusive(random, config.columnRadius.getMinValue(), j);

        LargeDripstone stalactite = makeDripstone(
                origin.atY(range.ceiling() - 1),
                false,
                random,
                k,
                config.stalactiteBluntness,
                config.heightScale
        );

        LargeDripstone stalagmite = makeDripstone(
                origin.atY(range.floor() + 1),
                true,
                random,
                k,
                config.stalagmiteBluntness,
                config.heightScale
        );

        WindOffsetter wind;
        if (stalactite.isSuitableForWind(config) && stalagmite.isSuitableForWind(config)) {
            wind = new WindOffsetter(origin.getY(), random, config.windSpeed);
        } else {
            wind = WindOffsetter.noWind();
        }

        boolean flag = stalactite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);
        boolean flag1 = stalagmite.moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(level, wind);

        if (flag) {
            stalactite.placeBlocks(level, random, wind, config.coreBlock, config.shellBlock);
        }
        if (flag1) {
            stalagmite.placeBlocks(level, random, wind, config.coreBlock, config.shellBlock);
        }

        return flag || flag1;
    }

    private static LargeDripstone makeDripstone(
            BlockPos root, boolean pointingUp, RandomSource random, int radius, FloatProvider bluntnessBase, FloatProvider scaleBase
    ) {
        return new LargeDripstone(
                root, pointingUp, radius, (double)bluntnessBase.sample(random), (double)scaleBase.sample(random)
        );
    }

    static final class LargeDripstone {
        private BlockPos root;
        private final boolean pointingUp;
        private int radius;
        private final double bluntness;
        private final double scale;

        LargeDripstone(BlockPos root, boolean pointingUp, int radius, double bluntness, double scale) {
            this.root = root;
            this.pointingUp = pointingUp;
            this.radius = radius;
            this.bluntness = bluntness;
            this.scale = scale;
        }

        boolean moveBackUntilBaseIsInsideStoneAndShrinkRadiusIfNecessary(WorldGenLevel level, WindOffsetter wind) {
            while (this.radius > 1) {
                BlockPos.MutableBlockPos mutable = this.root.mutable();
                int i = Math.min(10, this.getHeight());

                for (int j = 0; j < i; j++) {
                    if (level.getBlockState(mutable).is(Blocks.LAVA)) {
                        return false;
                    }

                    if (RedstonicUtils.isCircleMostlyEmbeddedInStone(level, wind.offset(mutable), this.radius)) {
                        this.root = mutable;
                        return true;
                    }

                    mutable.move(this.pointingUp ? Direction.DOWN : Direction.UP);
                }

                this.radius /= 2;
            }

            return false;
        }

        private int getHeight() {
            return this.getHeightAtRadius(0.0F);
        }

        private int getHeightAtRadius(float radius) {
            return (int) RedstonicUtils.getDripstoneHeight((double) radius, (double) this.radius, this.scale, this.bluntness);
        }

        void placeBlocks(WorldGenLevel level, RandomSource random, WindOffsetter wind,
                         BlockStateProvider coreBlock, BlockStateProvider shellBlock) {
            for (int i = -this.radius; i <= this.radius; i++) {
                for (int j = -this.radius; j <= this.radius; j++) {
                    float f = Mth.sqrt((float)(i * i + j * j));
                    if (!(f > (float) this.radius)) {
                        int height = this.getHeightAtRadius(f);
                        if (height > 0) {
                            // Slight randomness in column height
                            if (random.nextFloat() < 0.2f) {
                                height = (int) (height * Mth.randomBetween(random, 0.8f, 1.0f));
                            }

                            BlockPos.MutableBlockPos mutable = this.root.offset(i, 0, j).mutable();
                            boolean placed = false;
                            int surface = this.pointingUp
                                    ? level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, mutable.getX(), mutable.getZ())
                                    : Integer.MAX_VALUE;

                            for (int h = 0; h < height && mutable.getY() < surface; h++) {
                                BlockPos blockpos = wind.offset(mutable);
                                if (RedstonicUtils.isEmptyOrWaterOrLava(level, blockpos)) {
                                    placed = true;

                                    // ---- LAYERED PLACEMENT ----
                                    BlockState state;
                                    if (h < 2) {
                                        // Outer shell (first 2 layers)
                                        state = shellBlock.getState(random, mutable);
                                    } else if (h < height - 2) {
                                        // Middle bulk (core)
                                        state = coreBlock.getState(random, mutable);
                                    } else {
                                        // Inner cavity (like geode interior)
                                        if (random.nextFloat() < 0.5f) {
                                            state = shellBlock.getState(random, mutable); // maybe some decorative shell
                                        } else {
                                            state = shellBlock.getState(random, mutable); // hollow interior
                                        }
                                    }

                                    level.setBlock(blockpos, state, 2);

                                } else if (placed && level.getBlockState(blockpos).is(BlockTags.BASE_STONE_OVERWORLD)) {
                                    break;
                                }

                                mutable.move(this.pointingUp ? Direction.UP : Direction.DOWN);
                            }
                        }
                    }
                }
            }
        }

        boolean isSuitableForWind(ModLargeDripstoneConfiguration config) {
            return this.radius >= config.minRadiusForWind && this.bluntness >= (double) config.minBluntnessForWind;
        }
    }

    static final class WindOffsetter {
        private final int originY;
        @Nullable
        private final Vec3 windSpeed;

        WindOffsetter(int originY, RandomSource random, FloatProvider magnitude) {
            this.originY = originY;
            float f = magnitude.sample(random);
            float angle = Mth.randomBetween(random, 0.0F, (float)Math.PI);
            this.windSpeed = new Vec3((double)(Mth.cos(angle) * f), 0.0, (double)(Mth.sin(angle) * f));
        }

        private WindOffsetter() {
            this.originY = 0;
            this.windSpeed = null;
        }

        static WindOffsetter noWind() {
            return new WindOffsetter();
        }

        BlockPos offset(BlockPos pos) {
            if (this.windSpeed == null) {
                return pos;
            }
            int i = this.originY - pos.getY();
            Vec3 vec3 = this.windSpeed.scale((double)i);
            return pos.offset(Mth.floor(vec3.x), 0, Mth.floor(vec3.z));
        }
    }
}

