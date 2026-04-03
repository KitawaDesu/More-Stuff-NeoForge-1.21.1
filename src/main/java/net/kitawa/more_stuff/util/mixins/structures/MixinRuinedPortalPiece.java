package net.kitawa.more_stuff.util.mixins.structures;

import net.kitawa.more_stuff.worldgen.biomelayers.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.structure.structures.RuinedPortalPiece;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(RuinedPortalPiece.class)
public abstract class MixinRuinedPortalPiece {
    private static final Map<Long, Map<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise>> NOISE_CACHE = new HashMap<>();

    private static NormalNoise getOrCreateNoise(LevelAccessor level, ResourceKey<NormalNoise.NoiseParameters> noiseKey) {
        long seed = (level instanceof ServerLevel sl) ? sl.getSeed() : 0L;
        RegistryAccess reg = level.registryAccess();

        Map<ResourceKey<NormalNoise.NoiseParameters>, NormalNoise> perWorld =
                NOISE_CACHE.computeIfAbsent(seed, s -> new HashMap<>());

        return perWorld.computeIfAbsent(noiseKey, key -> {
            Holder<NormalNoise.NoiseParameters> noiseHolder =
                    reg.registryOrThrow(Registries.NOISE).getHolderOrThrow(key);
            return NormalNoise.create(new LegacyRandomSource(seed), noiseHolder.value());
        });
    }

    /** Main placement method for ruined portal blocks */
    @Overwrite
    public void placeNetherrackOrMagma(RandomSource random, LevelAccessor level, BlockPos pos) {
        boolean inNether = level.dimensionType().ultraWarm();

        ResourceKey<Biome> targetBiomeKey = null;
        LevelAccessor biomeLookupLevel = level;
        BlockPos biomeLookupPos = pos;

        // === LINKED BIOME COORDS ===
        if (!inNether) {
            // Overworld portal → check linked Nether
            int netherX = pos.getX() / 8;
            int netherZ = pos.getZ() / 8;
            int netherY = Mth.clamp(pos.getY(), 0, 128);
            BlockPos linkedNetherPos = new BlockPos(netherX, netherY, netherZ);
            ServerLevel netherLevel = (level instanceof ServerLevel sl) ? sl.getServer().getLevel(Level.NETHER) : null;

            if (netherLevel != null) {
                biomeLookupLevel = netherLevel;
                biomeLookupPos = linkedNetherPos;

                Biome netherBiome = netherLevel.getBiome(linkedNetherPos).value();
                targetBiomeKey = netherLevel.registryAccess()
                        .registryOrThrow(Registries.BIOME)
                        .getResourceKey(netherBiome)
                        .orElse(null);
            }
        } else {
            // Nether portal → check linked Overworld
            int overworldX = pos.getX() * 8;
            int overworldZ = pos.getZ() * 8;
            int overworldY = Mth.clamp(pos.getY(), 0, 320);
            BlockPos linkedOverworldPos = new BlockPos(overworldX, overworldY, overworldZ);
            ServerLevel overworld = (level instanceof ServerLevel sl) ? sl.getServer().getLevel(Level.OVERWORLD) : null;

            if (overworld != null) {
                biomeLookupLevel = overworld;
                biomeLookupPos = linkedOverworldPos;

                Biome overworldBiome = overworld.getBiome(linkedOverworldPos).value();
                targetBiomeKey = overworld.registryAccess()
                        .registryOrThrow(Registries.BIOME)
                        .getResourceKey(overworldBiome)
                        .orElse(null);
            }
        }

        // === FALLBACK IF NO BIOME ===
        if (targetBiomeKey == null) {
            BlockState fallbackBlock = random.nextFloat() < 0.07F ? Blocks.MAGMA_BLOCK.defaultBlockState() : Blocks.NETHERRACK.defaultBlockState();
            level.setBlock(pos, fallbackBlock, 3);
            return;
        }

        // === SURFACE CHECK ===
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        boolean isSurface = aboveState.isAir() || aboveState.canBeReplaced();

        // === PICK BLOCK BASED ON DIMENSION ===
        BlockState chosen;

        if (!inNether) {
            // Overworld portal → pull from Nether JSON
            BiomeLayer biomeLayer = BiomeLayerManager.getNether(targetBiomeKey);
            if (biomeLayer == null) {
                biomeLayer = new BiomeLayer(
                        new LayerDefinition(List.of(), Blocks.NETHERRACK.defaultBlockState()),
                        new LayerDefinition(List.of(), Blocks.NETHERRACK.defaultBlockState())
                );
            }
            chosen = isSurface
                    ? pickBlockForLayer(biomeLookupLevel, biomeLookupPos, biomeLayer.topLayer())
                    : pickBlockForLayer(biomeLookupLevel, biomeLookupPos, biomeLayer.underLayer());
        } else {
            // Nether portal → pull from Overworld JSON
            OverworldBiomeLayer overworldLayer = BiomeLayerManager.getOverworld(targetBiomeKey);
            if (overworldLayer == null) {
                overworldLayer = new OverworldBiomeLayer(
                        new LayerDefinition(List.of(), Blocks.GRASS_BLOCK.defaultBlockState()),
                        new MiddleLayer(List.of(new MiddleSection(Blocks.DIRT.defaultBlockState(), null, 0.0, 1.0, 3)), Blocks.DIRT.defaultBlockState()),
                        new LayerDefinition(List.of(), Blocks.STONE.defaultBlockState())
                );
            }
            chosen = pickFromOverworld(overworldLayer, biomeLookupLevel, biomeLookupPos, isSurface);
        }

        // === SET BLOCK ===
        level.setBlock(pos, chosen, 3);
    }

    /** Handles top/middle/bottom logic for OverworldBiomeLayer */
    private static BlockState pickFromOverworld(OverworldBiomeLayer layer, LevelAccessor level, BlockPos pos, boolean isSurface) {
        if (isSurface) return pickBlockForLayer(level, pos, layer.top());
        if (!layer.middle().sections().isEmpty()) return pickFromMiddle(layer.middle(), level, pos);
        return pickBlockForLayer(level, pos, layer.bottom());
    }

    /** Middle layer block picker */
    private static BlockState pickFromMiddle(MiddleLayer middleLayer, LevelAccessor level, BlockPos pos) {
        for (MiddleSection section : middleLayer.sections()) {
            if (section.noise() == null) {
                for (int i = 0; i < section.height(); i++) {
                    level.setBlock(pos.below(i), section.block(), 3);
                }
                return section.block();
            }

            NormalNoise noise = getOrCreateNoise(level, section.noise());
            double noiseValue = noise.getValue(pos.getX() * section.scale(), pos.getY() * 0.05, pos.getZ() * section.scale());

            if (noiseValue > section.threshold()) {
                for (int i = 0; i < section.height(); i++) {
                    level.setBlock(pos.below(i), section.block(), 3);
                }
                return section.block();
            }
        }
        return middleLayer.fallback();
    }

    /** Picks a block from a LayerDefinition using noise rules */
    private static BlockState pickBlockForLayer(LevelAccessor level, BlockPos pos, LayerDefinition layer) {
        for (NoiseBlockRule rule : layer.rules()) {
            NormalNoise noise = getOrCreateNoise(level, rule.noiseKey());
            double noiseValue = noise.getValue(pos.getX() * rule.noiseScale(), pos.getY() * 0.05, pos.getZ() * rule.noiseScale());
            boolean pass = rule.invert() ? noiseValue <= rule.noiseThreshold() : noiseValue > rule.noiseThreshold();
            if (pass) return rule.block();
        }
        return layer.fallback();
    }
}
