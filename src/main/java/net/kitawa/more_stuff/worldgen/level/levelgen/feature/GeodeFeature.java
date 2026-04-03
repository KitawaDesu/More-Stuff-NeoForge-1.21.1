package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class GeodeFeature extends Feature<GeodeConfiguration> {
    private static final Direction[] DIRECTIONS = Direction.values();

    public GeodeFeature(Codec<GeodeConfiguration> p_159834_) {
        super(p_159834_);
    }

    @Override
    public boolean place(FeaturePlaceContext<GeodeConfiguration> p_159836_) {
        GeodeConfiguration geodeconfiguration = p_159836_.config();
        RandomSource randomsource = p_159836_.random();
        BlockPos blockpos = p_159836_.origin();
        WorldGenLevel worldgenlevel = p_159836_.level();
        int i = geodeconfiguration.minGenOffset;
        int j = geodeconfiguration.maxGenOffset;
        List<Pair<BlockPos, Integer>> list = Lists.newLinkedList();
        int k = geodeconfiguration.distributionPoints.sample(randomsource);
        WorldgenRandom worldgenrandom = new WorldgenRandom(new LegacyRandomSource(worldgenlevel.getSeed()));
        NormalNoise normalnoise = NormalNoise.create(worldgenrandom, -4, 1.0);
        List<BlockPos> list1 = Lists.newLinkedList();
        double d0 = (double)k / (double)geodeconfiguration.outerWallDistance.getMaxValue();
        GeodeLayerSettings geodelayersettings = geodeconfiguration.geodeLayerSettings;
        GeodeBlockSettings geodeblocksettings = geodeconfiguration.geodeBlockSettings;
        GeodeCrackSettings geodecracksettings = geodeconfiguration.geodeCrackSettings;
        double d1 = 1.0 / Math.sqrt(geodelayersettings.filling);
        double d2 = 1.0 / Math.sqrt(geodelayersettings.innerLayer + d0);
        double d3 = 1.0 / Math.sqrt(geodelayersettings.middleLayer + d0);
        double d4 = 1.0 / Math.sqrt(geodelayersettings.outerLayer + d0);
        double d5 = 1.0 / Math.sqrt(geodecracksettings.baseCrackSize + randomsource.nextDouble() / 2.0 + (k > 3 ? d0 : 0.0));
        boolean flag = (double)randomsource.nextFloat() < geodecracksettings.generateCrackChance;
        int l = 0;

        for (int i1 = 0; i1 < k; i1++) {
            int j1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            int k1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            int l1 = geodeconfiguration.outerWallDistance.sample(randomsource);
            BlockPos blockpos1 = blockpos.offset(j1, k1, l1);
            BlockState blockstate = worldgenlevel.getBlockState(blockpos1);
            if (blockstate.isAir() || blockstate.is(BlockTags.GEODE_INVALID_BLOCKS)) {
                if (++l > geodeconfiguration.invalidBlocksThreshold) {
                    return false;
                }
            }
            list.add(Pair.of(blockpos1, geodeconfiguration.pointOffset.sample(randomsource)));
        }

        if (flag) {
            int i2 = randomsource.nextInt(4);
            int j2 = k * 2 + 1;
            if (i2 == 0) {
                list1.add(blockpos.offset(j2, 7, 0));
                list1.add(blockpos.offset(j2, 5, 0));
                list1.add(blockpos.offset(j2, 1, 0));
            } else if (i2 == 1) {
                list1.add(blockpos.offset(0, 7, j2));
                list1.add(blockpos.offset(0, 5, j2));
                list1.add(blockpos.offset(0, 1, j2));
            } else if (i2 == 2) {
                list1.add(blockpos.offset(j2, 7, j2));
                list1.add(blockpos.offset(j2, 5, j2));
                list1.add(blockpos.offset(j2, 1, j2));
            } else {
                list1.add(blockpos.offset(0, 7, 0));
                list1.add(blockpos.offset(0, 5, 0));
                list1.add(blockpos.offset(0, 1, 0));
            }
        }

        List<BlockPos> list2 = Lists.newArrayList();
        List<BlockPos> crackPositions = Lists.newArrayList();
        List<BlockPos> fillingZone = Lists.newArrayList();
        Predicate<BlockState> predicate = isReplaceable(geodeconfiguration.geodeBlockSettings.cannotReplace);

        // ── Pass 1: Build the full geode, collect crack and filling positions ─
        for (BlockPos blockpos3 : BlockPos.betweenClosed(blockpos.offset(i, i, i), blockpos.offset(j, j, j))) {
            double d8 = normalnoise.getValue((double)blockpos3.getX(), (double)blockpos3.getY(), (double)blockpos3.getZ()) * geodeconfiguration.noiseMultiplier;
            double d6 = 0.0;
            double d7 = 0.0;

            for (Pair<BlockPos, Integer> pair : list) {
                d6 += Mth.invSqrt(blockpos3.distSqr(pair.getFirst()) + (double)pair.getSecond().intValue()) + d8;
            }
            for (BlockPos blockpos6 : list1) {
                d7 += Mth.invSqrt(blockpos3.distSqr(blockpos6) + (double)geodecracksettings.crackPointOffset) + d8;
            }

            if (!(d6 < d4)) {
                if (flag && d7 >= d5 && d6 < d1) {
                    // Defer crack — place air for now, flood in pass 2
                    this.safeSetBlock(worldgenlevel, blockpos3, Blocks.AIR.defaultBlockState(), predicate);
                    crackPositions.add(blockpos3.immutable());
                    for (Direction direction1 : DIRECTIONS) {
                        BlockPos blockpos2 = blockpos3.relative(direction1);
                        FluidState fluidstate = worldgenlevel.getFluidState(blockpos2);
                        if (!fluidstate.isEmpty()) {
                            worldgenlevel.scheduleTick(blockpos2, fluidstate.getType(), 0);
                        }
                    }
                } else if (d6 >= d1) {
                    this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.fillingProvider.getState(randomsource, blockpos3), predicate);
                    fillingZone.add(blockpos3.immutable());
                } else if (d6 >= d2) {
                    boolean flag1 = (double)randomsource.nextFloat() < geodeconfiguration.useAlternateLayer0Chance;
                    if (flag1) {
                        this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.alternateInnerLayerProvider.getState(randomsource, blockpos3), predicate);
                    } else {
                        this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.innerLayerProvider.getState(randomsource, blockpos3), predicate);
                    }
                    if ((!geodeconfiguration.placementsRequireLayer0Alternate || flag1)
                            && (double)randomsource.nextFloat() < geodeconfiguration.usePotentialPlacementsChance) {
                        list2.add(blockpos3.immutable());
                    }
                } else if (d6 >= d3) {
                    this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.middleLayerProvider.getState(randomsource, blockpos3), predicate);
                } else if (d6 >= d4) {
                    this.safeSetBlock(worldgenlevel, blockpos3, geodeblocksettings.outerLayerProvider.getState(randomsource, blockpos3), predicate);
                }
            }
        }

        // ── Step 1: Flood the crack from horizontal external fluid sources ────
        // Only the crack walls (positions adjacent to crack air) are scanned.
        // Horizontal only — water on top of the geode doesn't count.
        int crackWaterLevel = Integer.MIN_VALUE;
        int crackLavaLevel  = Integer.MIN_VALUE;

        Set<BlockPos> crackSet = new HashSet<>(crackPositions);
        for (BlockPos crackPos : crackPositions) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = crackPos.relative(dir);
                if (crackSet.contains(neighbor)) continue;

                FluidState fluidState = worldgenlevel.getFluidState(neighbor);
                if (!fluidState.isEmpty() && fluidState.isSource()) {
                    if (fluidState.is(FluidTags.WATER)) {
                        crackWaterLevel = Math.max(crackWaterLevel, neighbor.getY());
                    } else if (fluidState.is(FluidTags.LAVA)) {
                        crackLavaLevel = Math.max(crackLavaLevel, neighbor.getY());
                    }
                }
            }
        }

        BlockState crackFluid = null;
        boolean crackIsWater = false;
        int crackFluidLevel = Integer.MIN_VALUE;

        if (crackWaterLevel != Integer.MIN_VALUE) {
            crackFluid = Blocks.WATER.defaultBlockState();
            crackIsWater = true;
            crackFluidLevel = crackWaterLevel;
        } else if (crackLavaLevel != Integer.MIN_VALUE) {
            crackFluid = Blocks.LAVA.defaultBlockState();
            crackFluidLevel = crackLavaLevel;
        }

        // Fill crack positions at or below detected fluid level
        for (BlockPos crackPos : crackPositions) {
            if (crackFluid != null && crackPos.getY() <= crackFluidLevel) {
                worldgenlevel.setBlock(crackPos, crackFluid, 3);
                worldgenlevel.scheduleTick(crackPos,
                        crackIsWater ? Fluids.WATER : Fluids.LAVA, 0);
            }
            // positions above fluid level stay as air (already placed in pass 1)
        }

        // ── Step 2: Check if crack fluid has reached the filling zone ─────────
        // The filling zone (hollow interior) floods only if the crack fluid
        // is horizontally adjacent to it — like the monster room aquifer check.
        // This naturally handles air pockets: if the top of the interior is
        // sealed by inner layer blocks, the fluid can't reach it.
        int interiorWaterLevel = Integer.MIN_VALUE;
        int interiorLavaLevel  = Integer.MIN_VALUE;

        for (BlockPos fillPos : fillingZone) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = fillPos.relative(dir);

                FluidState fluidState = worldgenlevel.getFluidState(neighbor);
                if (!fluidState.isEmpty() && fluidState.isSource()) {
                    if (fluidState.is(FluidTags.WATER)) {
                        interiorWaterLevel = Math.max(interiorWaterLevel, neighbor.getY());
                    } else if (fluidState.is(FluidTags.LAVA)) {
                        interiorLavaLevel = Math.max(interiorLavaLevel, neighbor.getY());
                    }
                }
            }
        }

        BlockState interiorFluid = null;
        boolean interiorIsWater = false;
        int interiorFluidLevel = Integer.MIN_VALUE;

        if (interiorWaterLevel != Integer.MIN_VALUE) {
            interiorFluid = Blocks.WATER.defaultBlockState();
            interiorIsWater = true;
            interiorFluidLevel = interiorWaterLevel;
        } else if (interiorLavaLevel != Integer.MIN_VALUE) {
            interiorFluid = Blocks.LAVA.defaultBlockState();
            interiorFluidLevel = interiorLavaLevel;
        }

        // Fill hollow interior at or below detected fluid level
        if (interiorFluid != null) {
            for (BlockPos fillPos : fillingZone) {
                if (fillPos.getY() <= interiorFluidLevel) {
                    BlockState current = worldgenlevel.getBlockState(fillPos);
                    if (current.isAir() || current.is(Blocks.CAVE_AIR)) {
                        worldgenlevel.setBlock(fillPos, interiorFluid, 3);
                        worldgenlevel.scheduleTick(fillPos,
                                interiorIsWater ? Fluids.WATER : Fluids.LAVA, 0);
                    }
                }
            }
        }

        // ── Place crystal clusters, waterlog if submerged ─────────────────────
        List<BlockState> list3 = geodeblocksettings.innerPlacements;

        for (BlockPos blockpos4 : list2) {
            BlockState blockstate1 = Util.getRandom(list3, randomsource);

            for (Direction direction : DIRECTIONS) {
                if (blockstate1.hasProperty(BlockStateProperties.FACING)) {
                    blockstate1 = blockstate1.setValue(BlockStateProperties.FACING, direction);
                }

                BlockPos blockpos5 = blockpos4.relative(direction);
                BlockState blockstate2 = worldgenlevel.getBlockState(blockpos5);

                if (blockstate1.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    boolean submerged = blockstate2.getFluidState().isSource()
                            || (interiorIsWater && interiorFluid != null
                            && blockpos5.getY() <= interiorFluidLevel);
                    blockstate1 = blockstate1.setValue(BlockStateProperties.WATERLOGGED, submerged);
                }

                if (BuddingAmethystBlock.canClusterGrowAtState(blockstate2)) {
                    this.safeSetBlock(worldgenlevel, blockpos5, blockstate1, predicate);
                    break;
                }
            }
        }

        return true;
    }
}