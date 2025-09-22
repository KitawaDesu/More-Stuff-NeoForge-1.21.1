package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.MonsterRoomFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.function.Predicate;

public class MonsterRoomFeature extends Feature<MonsterRoomFeatureConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterRoomFeature.class);
    private static final BlockState AIR = Blocks.CAVE_AIR.defaultBlockState();

    private static final Set<Block> ALLOWED_EMPTY_BLOCKS = Set.of(
            Blocks.AIR,
            Blocks.CAVE_AIR,
            Blocks.VOID_AIR,
            Blocks.WATER,
            Blocks.LAVA
    );

    public MonsterRoomFeature(Codec<MonsterRoomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<MonsterRoomFeatureConfiguration> context) {
        MonsterRoomFeatureConfiguration cfg = context.config();
        Predicate<BlockState> predicate = Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE);
        BlockPos blockpos = context.origin();
        RandomSource randomsource = context.random();
        WorldGenLevel worldgenlevel = context.level();

        int j = randomsource.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = randomsource.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;
        int j2 = 0;

        for (int k2 = k; k2 <= l; k2++) {
            for (int l2 = -1; l2 <= 4; l2++) {
                for (int i3 = l1; i3 <= i2; i3++) {
                    BlockPos blockpos1 = blockpos.offset(k2, l2, i3);
                    boolean flag = worldgenlevel.getBlockState(blockpos1).isSolid();
                    if (l2 == -1 && !flag) return false;
                    if (l2 == 4 && !flag) return false;
                    if ((k2 == k || k2 == l || i3 == l1 || i3 == i2)
                            && l2 == 0
                            && ALLOWED_EMPTY_BLOCKS.contains(worldgenlevel.getBlockState(blockpos1).getBlock())
                            && ALLOWED_EMPTY_BLOCKS.contains(worldgenlevel.getBlockState(blockpos1.above()).getBlock())) {
                        j2++;
                    }
                }
            }
        }

        if (j2 < 1 || j2 > 5) return false;

        for (int k3 = k; k3 <= l; k3++) {
            for (int i4 = 3; i4 >= -1; i4--) {
                for (int k4 = l1; k4 <= i2; k4++) {
                    BlockPos blockpos3 = blockpos.offset(k3, i4, k4);
                    BlockState blockstate = worldgenlevel.getBlockState(blockpos3);

                    if (k3 == k || i4 == -1 || k4 == l1 || k3 == l || i4 == 4 || k4 == i2) {
                        if (blockpos3.getY() >= worldgenlevel.getMinBuildHeight()
                                && !worldgenlevel.getBlockState(blockpos3.below()).isSolid()) {
                            worldgenlevel.setBlock(blockpos3, AIR, 2);
                        } else if (blockstate.isSolid() && !blockstate.is(Blocks.CHEST)) {
                            BlockState replacement = cfg.getReplacement(blockstate, i4, randomsource);
                            this.safeSetBlock(worldgenlevel, blockpos3, replacement, predicate);
                        }
                    } else if (!blockstate.is(Blocks.CHEST) && !blockstate.is(Blocks.SPAWNER)) {
                        this.safeSetBlock(worldgenlevel, blockpos3, AIR, predicate);
                    }
                }
            }
        }

        // Place containers
        for (int l3 = 0; l3 < 2; l3++) {
            for (int j4 = 0; j4 < 3; j4++) {
                int l4 = blockpos.getX() + randomsource.nextInt(j * 2 + 1) - j;
                int j5 = blockpos.getZ() + randomsource.nextInt(k1 * 2 + 1) - k1;

                BlockState containerState = cfg.containerBlock().getState(randomsource, blockpos);
                Block block = containerState.getBlock();

                // Pick Y based on block type
                int i5;
                if (block instanceof ShulkerBoxBlock) {
                    // Shulkers: floor OR ceiling
                    i5 = randomsource.nextBoolean()
                            ? blockpos.getY()
                            : blockpos.getY() + 3;
                } else {
                    // All others: floor only
                    i5 = blockpos.getY();
                }

                BlockPos blockpos2 = new BlockPos(l4, i5, j5);

                if (ALLOWED_EMPTY_BLOCKS.contains(worldgenlevel.getBlockState(blockpos2).getBlock())) {
                    // Floor placement
                    if (i5 == blockpos.getY()) {
                        if (block instanceof ChestBlock || block instanceof TrappedChestBlock) {
                            // Align chest to wall
                            Direction wallDir = null;
                            for (Direction dir : Direction.Plane.HORIZONTAL) {
                                if (worldgenlevel.getBlockState(blockpos2.relative(dir)).isSolid()) {
                                    wallDir = dir;
                                    break;
                                }
                            }
                            if (wallDir != null && containerState.hasProperty(ChestBlock.FACING)) {
                                containerState = containerState.setValue(ChestBlock.FACING, wallDir.getOpposite());
                            }
                        } else if (block instanceof BarrelBlock && containerState.hasProperty(BarrelBlock.FACING)) {
                            containerState = containerState.setValue(BarrelBlock.FACING, Direction.UP);
                        } else if (block instanceof ShulkerBoxBlock && containerState.hasProperty(ShulkerBoxBlock.FACING)) {
                            containerState = containerState.setValue(ShulkerBoxBlock.FACING, Direction.UP);
                        }

                        // Ceiling placement (only shulkers)
                    } else if (block instanceof ShulkerBoxBlock && containerState.hasProperty(ShulkerBoxBlock.FACING)) {
                        containerState = containerState.setValue(ShulkerBoxBlock.FACING, Direction.DOWN);
                    } else {
                        continue; // Skip invalid
                    }

                    // Place container
                    this.safeSetBlock(worldgenlevel, blockpos2, containerState, predicate);

                    BlockState finalContainerState = containerState;
                    cfg.containerLoot().ifPresent(lootLoc -> {
                        if (finalContainerState.getBlock() instanceof BaseEntityBlock) {
                            ResourceKey<LootTable> lootKey = ResourceKey.create(Registries.LOOT_TABLE, lootLoc);
                            RandomizableContainer.setBlockEntityLootTable(worldgenlevel, randomsource, blockpos2, lootKey);
                        }
                    });

                    // --- Always place trap block under trapped chests ---
                    if (block instanceof TrappedChestBlock) {
                        BlockPos trapPos = blockpos2.below();
                        BlockState trapState = cfg.trapBlock().getState(randomsource, trapPos);

                        // Force facing UP if supported
                        if (trapState.hasProperty(BlockStateProperties.FACING)) {
                            trapState = trapState.setValue(BlockStateProperties.FACING, Direction.UP);
                        }

                        this.safeSetBlock(worldgenlevel, trapPos, trapState, predicate);

                        BlockState finalTrapState = trapState;
                        cfg.trapLoot().ifPresent(lootLoc -> {
                            if (finalTrapState.getBlock() instanceof BaseEntityBlock) {
                                // Apply trap loot to the trap block, NOT the trapped chest
                                ResourceKey<LootTable> lootKey = ResourceKey.create(Registries.LOOT_TABLE, lootLoc);
                                RandomizableContainer.setBlockEntityLootTable(worldgenlevel, randomsource, trapPos, lootKey);
                            }
                        });
                    }

                    break;
                }
            }
        }

        // Place spawner block
        BlockState spawnerState = cfg.spawnerBlock().getState(randomsource, blockpos);
        this.safeSetBlock(worldgenlevel, blockpos, spawnerState, predicate);

        // Set spawner entity
        BlockEntity be = worldgenlevel.getBlockEntity(blockpos);
        if (be instanceof SpawnerBlockEntity spawnerblockentity) {
            spawnerblockentity.setEntityId(cfg.randomMob(randomsource), randomsource);

        } else if (be instanceof TrialSpawnerBlockEntity trialSpawnerBE) {
            trialSpawnerBE.setEntityId(cfg.randomMob(randomsource), randomsource);
        } else {
            LOGGER.error("Failed to fetch mob spawner entity at ({}, {}, {})",
                    blockpos.getX(), blockpos.getY(), blockpos.getZ());
        }

        return true;
    }
}