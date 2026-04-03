package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.blocks.custom.general.entities.ModdedSpawnerBlockEntity;
import net.kitawa.more_stuff.entities.ModEntities;
import net.kitawa.more_stuff.util.ducks.TrialSpawnerDuck;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.MonsterRoomFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.SpawnData;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
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
        RandomSource random = context.random();
        WorldGenLevel world = context.level();

        if (cfg.debug) {
            return generateRoom(world, random, blockpos, cfg, predicate);
        }

        for (int attempt = 0; attempt < 10; attempt++) {
            int j = random.nextInt(2) + 2;
            int k = -j - 1;
            int l = j + 1;
            int k1 = random.nextInt(2) + 2;
            int l1 = -k1 - 1;
            int i2 = k1 + 1;
            int emptyCount = 0;
            boolean invalid = false;

            for (int x = k; x <= l && !invalid; x++) {
                for (int y = -1; y <= 4 && !invalid; y++) {
                    for (int z = l1; z <= i2 && !invalid; z++) {
                        BlockPos pos = blockpos.offset(x, y, z);
                        boolean solid = world.getBlockState(pos).isSolid();
                        if ((y == -1 || y == 4) && !solid) {
                            invalid = true;
                            break;
                        }
                        if ((x == k || x == l || z == l1 || z == i2)
                                && y == 0
                                && ALLOWED_EMPTY_BLOCKS.contains(world.getBlockState(pos).getBlock())
                                && ALLOWED_EMPTY_BLOCKS.contains(world.getBlockState(pos.above()).getBlock())) {
                            emptyCount++;
                        }
                    }
                }
            }

            if (invalid || emptyCount < 1 || emptyCount > 5) {
                blockpos = blockpos.above();
                continue;
            }

            return generateRoom(world, random, blockpos, cfg, predicate);
        }

        return false;
    }

    private boolean generateRoom(
            WorldGenLevel world,
            RandomSource random,
            BlockPos blockpos,
            MonsterRoomFeatureConfiguration cfg,
            Predicate<BlockState> predicate
    ) {
        int j = random.nextInt(2) + 2;
        int k = -j - 1;
        int l = j + 1;
        int k1 = random.nextInt(2) + 2;
        int l1 = -k1 - 1;
        int i2 = k1 + 1;

        // ── Fill walls and floor/ceiling ──────────────────────────────────────
        // If a wall position is already fluid, leave it — it will naturally
        // become the aquifer entry point without needing replacement.
        for (int x = k; x <= l; x++) {
            for (int y = 3; y >= -1; y--) {
                for (int z = l1; z <= i2; z++) {
                    BlockPos pos = blockpos.offset(x, y, z);
                    BlockState state = world.getBlockState(pos);

                    if (x == k || x == l || z == l1 || z == i2 || y == -1 || y == 4) {
                        if (pos.getY() >= world.getMinBuildHeight() && !world.getBlockState(pos.below()).isSolid()) {
                            world.setBlock(pos, AIR, 2);
                        } else if (state.isSolid() && !state.is(Blocks.CHEST)) {
                            // Don't overwrite fluid — wall intersecting water/lava stays as-is
                            if (!state.getFluidState().isEmpty()) continue;
                            BlockState replacement = cfg.getReplacement(state, y, random);
                            safeSetBlock(world, pos, replacement, predicate);
                        }
                    } else if (!state.is(Blocks.CHEST) && !state.is(Blocks.SPAWNER)) {
                        safeSetBlock(world, pos, AIR, predicate);
                    }
                }
            }
        }

        // ── Aquifer detection — horizontal wall neighbors only ────────────────
        int waterLevel = Integer.MIN_VALUE;
        int lavaLevel  = Integer.MIN_VALUE;

        for (int x = k; x <= l; x++) {
            for (int y = 0; y <= 3; y++) {
                for (int z = l1; z <= i2; z++) {
                    boolean isWall = x == k || x == l || z == l1 || z == i2;
                    if (!isWall) continue;

                    BlockPos wallPos = blockpos.offset(x, y, z);

                    // If the wall block itself is fluid, count it
                    FluidState wallFluid = world.getFluidState(wallPos);
                    if (!wallFluid.isEmpty() && wallFluid.isSource()) {
                        if (wallFluid.is(FluidTags.WATER)) {
                            waterLevel = Math.max(waterLevel, wallPos.getY());
                        } else if (wallFluid.is(FluidTags.LAVA)) {
                            lavaLevel = Math.max(lavaLevel, wallPos.getY());
                        }
                        continue;
                    }

                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        BlockPos neighbor = wallPos.relative(dir);
                        int nx = neighbor.getX() - blockpos.getX();
                        int nz = neighbor.getZ() - blockpos.getZ();

                        boolean insideX = nx > k && nx < l;
                        boolean insideZ = nz > l1 && nz < i2;
                        if (insideX && insideZ) continue;

                        FluidState fluidState = world.getFluidState(neighbor);
                        if (!fluidState.isEmpty() && fluidState.isSource()) {
                            if (fluidState.is(FluidTags.WATER)) {
                                waterLevel = Math.max(waterLevel, neighbor.getY());
                            } else if (fluidState.is(FluidTags.LAVA)) {
                                lavaLevel  = Math.max(lavaLevel,  neighbor.getY());
                            }
                        }
                    }
                }
            }
        }

        BlockState fluidBlock = null;
        boolean isWater = false;
        int fluidLevel = Integer.MIN_VALUE;

        if (waterLevel != Integer.MIN_VALUE) {
            fluidBlock = Blocks.WATER.defaultBlockState();
            isWater    = true;
            fluidLevel = waterLevel;
        } else if (lavaLevel != Integer.MIN_VALUE) {
            fluidBlock = Blocks.LAVA.defaultBlockState();
            fluidLevel = lavaLevel;
        }

        if (fluidBlock != null) {
            // ── Step 1: Replace solid wall blocks at or below fluid level ─────
            for (int x = k; x <= l; x++) {
                for (int z = l1; z <= i2; z++) {
                    boolean isWall = x == k || x == l || z == l1 || z == i2;
                    if (!isWall) continue;

                    for (int y = -1; y <= 3; y++) {
                        BlockPos pos = blockpos.offset(x, y, z);
                        if (pos.getY() > fluidLevel) continue;

                        BlockState current = world.getBlockState(pos);
                        if (current.isSolid() && !cfg.protectedStates.contains(current.getBlock())) {
                            world.setBlock(pos, fluidBlock, 3);
                        }
                    }
                }
            }

            // ── Step 2: Fill interior air at or below fluid level ─────────────
            for (int x = k + 1; x < l; x++) {
                for (int z = l1 + 1; z < i2; z++) {
                    for (int y = -1; y <= 3; y++) {
                        BlockPos pos = blockpos.offset(x, y, z);
                        if (pos.getY() > fluidLevel) continue;

                        BlockState current = world.getBlockState(pos);
                        if (current.isAir() || current.is(Blocks.CAVE_AIR)) {
                            world.setBlock(pos, fluidBlock, 3);
                        } else if (isWater
                                && current.hasProperty(BlockStateProperties.WATERLOGGED)
                                && current.getBlock() instanceof SimpleWaterloggedBlock) {
                            world.setBlock(pos, current.setValue(BlockStateProperties.WATERLOGGED, true), 3);
                        }
                    }
                }
            }
        }

        // ── Place containers ──────────────────────────────────────────────────
        for (int i = 0; i < 2; i++) {
            for (int j2 = 0; j2 < 3; j2++) {
                int x = blockpos.getX() + random.nextInt(j * 2 + 1) - j;
                int z = blockpos.getZ() + random.nextInt(k1 * 2 + 1) - k1;
                BlockState containerState = cfg.containerBlock.getState(random, blockpos);
                Block block = containerState.getBlock();
                int y = blockpos.getY();
                if (block instanceof ShulkerBoxBlock && random.nextBoolean()) y += 3;

                BlockPos pos = new BlockPos(x, y, z);

                BlockPos spawnerPos = blockpos;
                if ((block instanceof ChestBlock || block instanceof BarrelBlock || block instanceof ShulkerBoxBlock)
                        && pos.equals(spawnerPos)) {
                    Direction shift = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                    pos = pos.relative(shift);
                }

                if (!ALLOWED_EMPTY_BLOCKS.contains(world.getBlockState(pos).getBlock())) continue;

                if (block instanceof ChestBlock && containerState.hasProperty(ChestBlock.FACING)) {
                    for (Direction dir : Direction.Plane.HORIZONTAL) {
                        if (world.getBlockState(pos.relative(dir)).isSolid()) {
                            containerState = containerState.setValue(ChestBlock.FACING, dir.getOpposite());
                            break;
                        }
                    }
                } else if (block instanceof BarrelBlock && containerState.hasProperty(BarrelBlock.FACING)) {
                    containerState = containerState.setValue(BarrelBlock.FACING, Direction.UP);
                } else if (block instanceof ShulkerBoxBlock && containerState.hasProperty(ShulkerBoxBlock.FACING)) {
                    containerState = containerState.setValue(ShulkerBoxBlock.FACING,
                            y == blockpos.getY() ? Direction.UP : Direction.DOWN);
                }

                if (fluidBlock != null && isWater
                        && pos.getY() <= fluidLevel
                        && containerState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                    containerState = containerState.setValue(BlockStateProperties.WATERLOGGED, true);
                }

                safeSetBlock(world, pos, containerState, predicate);

                final BlockPos lootPos = pos;
                if (containerState.getBlock() instanceof BaseEntityBlock) {
                    cfg.containerLoot.ifPresent(loc -> {
                        ResourceKey<LootTable> lootKey = ResourceKey.create(Registries.LOOT_TABLE, loc);
                        RandomizableContainer.setBlockEntityLootTable(world, random, lootPos, lootKey);
                    });
                }

                if (block instanceof TrappedChestBlock) {
                    BlockPos trapPos = pos.below();
                    BlockState trapState = cfg.trapBlock.getState(random, trapPos);
                    if (trapState.hasProperty(BlockStateProperties.FACING)) {
                        trapState = trapState.setValue(BlockStateProperties.FACING, Direction.UP);
                    }
                    safeSetBlock(world, trapPos, trapState, predicate);

                    final BlockPos trapLootPos = trapPos;
                    if (trapState.getBlock() instanceof BaseEntityBlock) {
                        cfg.trapLoot.ifPresent(loc -> {
                            ResourceKey<LootTable> lootKey = ResourceKey.create(Registries.LOOT_TABLE, loc);
                            RandomizableContainer.setBlockEntityLootTable(world, random, trapLootPos, lootKey);
                        });
                    }
                }

                break;
            }
        }

        // ── Place spawner ─────────────────────────────────────────────────────
        BlockState spawnerState = cfg.spawnerBlock.getState(random, blockpos);
        safeSetBlock(world, blockpos, spawnerState, predicate);
        BlockEntity be = world.getBlockEntity(blockpos);

        if (be instanceof SpawnerBlockEntity vanillaSpawner) {
            EntityType<?> chosen = cfg.randomNormalMob(random);
            if (chosen.equals(ModEntities.COLORED_SLIME.get())) chosen = EntityType.SLIME;
            vanillaSpawner.setEntityId(chosen, random);
        } else if (be instanceof ModdedSpawnerBlockEntity moddedSpawner) {
            EntityType<?> chosen = cfg.randomNormalMob(random);
            if (chosen.equals(ModEntities.COLORED_SLIME.get())) chosen = EntityType.SLIME;
            moddedSpawner.setEntityId(chosen, random);
        } else if (be instanceof TrialSpawnerBlockEntity trialBE) {
            TrialSpawner trialSpawner = trialBE.getTrialSpawner();

            ResourceLocation normalMob = cfg.normalConfig.mobs().get(random.nextInt(cfg.normalConfig.mobs().size()));
            SimpleWeightedRandomList.Builder<SpawnData> normalBuilder = SimpleWeightedRandomList.builder();
            BuiltInRegistries.ENTITY_TYPE.getOptional(normalMob).ifPresent(et -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("id", normalMob.toString());
                normalBuilder.add(new SpawnData(tag, Optional.empty(), Optional.empty()), 1);
            });
            TrialSpawnerConfig normalConfig = new TrialSpawnerConfig(
                    cfg.normalConfig.spawnRange(), cfg.normalConfig.totalMobs(),
                    cfg.normalConfig.simultaneousMobs(), cfg.normalConfig.totalMobsAddedPerPlayer(),
                    cfg.normalConfig.simultaneousMobsAddedPerPlayer(), cfg.normalConfig.ticksBetweenSpawn(),
                    normalBuilder.build(), cfg.normalLootPool, cfg.normalDropLoot
            );
            ((TrialSpawnerDuck) (Object) trialSpawner).more_stuff$setNormalConfig(normalConfig);

            ResourceLocation ominousMob = cfg.ominousConfig.mobs().get(random.nextInt(cfg.ominousConfig.mobs().size()));
            SimpleWeightedRandomList.Builder<SpawnData> ominousBuilder = SimpleWeightedRandomList.builder();
            BuiltInRegistries.ENTITY_TYPE.getOptional(ominousMob).ifPresent(et -> {
                CompoundTag tag = new CompoundTag();
                tag.putString("id", ominousMob.toString());
                ominousBuilder.add(new SpawnData(tag, Optional.empty(), Optional.empty()), 1);
            });
            TrialSpawnerConfig ominousConfig = new TrialSpawnerConfig(
                    cfg.ominousConfig.spawnRange(), cfg.ominousConfig.totalMobs(),
                    cfg.ominousConfig.simultaneousMobs(), cfg.ominousConfig.totalMobsAddedPerPlayer(),
                    cfg.ominousConfig.simultaneousMobsAddedPerPlayer(), cfg.ominousConfig.ticksBetweenSpawn(),
                    ominousBuilder.build(), cfg.ominousLootPool, cfg.ominousDropLoot
            );
            ((TrialSpawnerDuck) (Object) trialSpawner).more_stuff$setOminousConfig(ominousConfig);
        } else {
            LOGGER.error("Failed to fetch mob spawner entity at {}", blockpos);
        }

        return true;
    }
}