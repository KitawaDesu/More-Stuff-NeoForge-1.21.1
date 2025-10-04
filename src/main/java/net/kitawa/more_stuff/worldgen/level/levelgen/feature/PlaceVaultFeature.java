package net.kitawa.more_stuff.worldgen.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.kitawa.more_stuff.blocks.util.ModdedBlockStateProperties;
import net.kitawa.more_stuff.worldgen.level.levelgen.feature.configuration.VaultFeatureConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.VaultBlock;
import net.minecraft.world.level.block.entity.trialspawner.PlayerDetector;
import net.minecraft.world.level.block.entity.vault.VaultBlockEntity;
import net.minecraft.world.level.block.entity.vault.VaultConfig;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Optional;
import java.util.Random;

public class PlaceVaultFeature extends Feature<VaultFeatureConfig> {

    public PlaceVaultFeature(Codec<VaultFeatureConfig> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<VaultFeatureConfig> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        VaultFeatureConfig cfg = ctx.config();
        RandomSource random = ctx.random();

        FluidState fluid = level.getFluidState(origin);

        // -----------------------------
        // Air / Water case
        // -----------------------------
        if (fluid.isEmpty() || fluid.is(Fluids.WATER)) {
            for (int i = 0; i < 10; i++) {
                BlockPos tryPos = origin.offset(
                        random.nextInt(7) - 3, // -3..+3 X
                        random.nextInt(5) - 2, // -2..+2 Y
                        random.nextInt(7) - 3  // -3..+3 Z
                );

                // require above block to be air or water
                if (isValidAbove(level, tryPos)) {
                    if (tryPlaceVault(level, tryPos, cfg, random)) return true;
                }
            }
            return false;
        }

        // -----------------------------
        // Lava case
        // -----------------------------
        if (fluid.is(Fluids.LAVA)) {
            BlockPos pos = origin;
            for (int i = 0; i < 20; i++) {
                if (level.getFluidState(pos).is(Fluids.LAVA) && isValidAbove(level, pos)) {
                    if (tryPlaceVault(level, pos, cfg, random)) return true;
                }
                pos = pos.above();
                if (pos.getY() > level.getMaxBuildHeight() - 1) break;
            }
            return false;
        }

        return false; // other fluids (like powder snow) → no vault
    }

    // -----------------------------
// Checks the "must have air or water above" rule
// -----------------------------
    private boolean isValidAbove(WorldGenLevel level, BlockPos pos) {
        BlockPos above = pos.above();
        FluidState aboveFluid = level.getFluidState(above);
        return aboveFluid.isEmpty() || aboveFluid.is(Fluids.WATER);
    }

    // -----------------------------
// Core vault placement logic (same as before)
// -----------------------------
    private boolean tryPlaceVault(WorldGenLevel level, BlockPos pos, VaultFeatureConfig cfg, RandomSource random) {
        BlockPos below = pos.below();
        BlockState currentState = level.getBlockState(pos);
        FluidState fluidState = level.getFluidState(pos);

        // Don’t replace solid blocks
        if (currentState.isSolid()) return false;

        // Floor must be solid
        if (!level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)) return false;

        // Find wall for orientation
        Direction wallFacing = null;
        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockPos adj = pos.relative(horizontal);
            if (level.getBlockState(adj).isFaceSturdy(level, adj, horizontal.getOpposite())) {
                wallFacing = horizontal;
                break;
            }
        }
        if (wallFacing == null) return false;

        // Build vault state
        BlockState stateToPlace = cfg.vaultBlock().getState(random, pos);
        if (!(stateToPlace.getBlock() instanceof VaultBlock))
            throw new IllegalStateException("Configured block is not a VaultBlock!");

        BlockState placedState = stateToPlace
                .setValue(VaultBlock.OMINOUS, random.nextBoolean())
                .setValue(VaultBlock.FACING, wallFacing.getOpposite());

        // Fluid flags
        if (placedState.hasProperty(BlockStateProperties.WATERLOGGED)) {
            placedState = placedState.setValue(BlockStateProperties.WATERLOGGED, fluidState.is(Fluids.WATER));
        }
        if (placedState.hasProperty(ModdedBlockStateProperties.LAVALOGGED)) {
            placedState = placedState.setValue(ModdedBlockStateProperties.LAVALOGGED, fluidState.is(Fluids.LAVA));
        }

        // Place block
        level.setBlock(pos, placedState, 3);

        if (level.getBlockEntity(pos) instanceof VaultBlockEntity vaultBE) {
            VaultFeatureConfig.VaultSubConfig chosenCfg =
                    placedState.getValue(VaultBlock.OMINOUS) ? cfg.ominousConfig() : cfg.normalConfig();

            ResourceKey<LootTable> lootTable = chosenCfg.pickRandom(random);

            vaultBE.setConfig(new VaultConfig(
                    lootTable,
                    chosenCfg.activationRange(),
                    chosenCfg.deactivationRange(),
                    new ItemStack(chosenCfg.keyItem().getItem()),
                    Optional.of(lootTable),
                    PlayerDetector.INCLUDING_CREATIVE_PLAYERS,
                    PlayerDetector.EntitySelector.SELECT_FROM_LEVEL
            ));
        }

        return true;
    }
}