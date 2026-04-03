package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import net.kitawa.more_stuff.util.block_colors.HybernaticFoliageColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HybernaticGlassBlock extends TransparentBlock implements BeaconBeamBlock {

    public HybernaticGlassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    // -------------------------------------
    // Beacon beam coloring
    // -------------------------------------

    /**
     * Returns the DyeColor for vanilla beacon tinting.
     * Vanilla only supports 16 dye colors, so we use WHITE as a placeholder.
     */
    @Override
    public DyeColor getColor() {
        return DyeColor.WHITE;
    }

    /**
     * Returns the exact RGB of the beacon beam based on the block's position in the world.
     * This allows full RGB color from your HybernaticFoliageColor noise system.
     */
    private static final Map<BlockPos, Integer> BEACON_COLOR_CACHE = new ConcurrentHashMap<>();

    @Nullable
    @Override
    public Integer getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        if (level == null) return null;

        return BEACON_COLOR_CACHE.computeIfAbsent(pos, p ->
                HybernaticFoliageColor.foliageColor(HybernaticFoliageColor.CRYSTAL_NOISE, pos.getX(), pos.getY(), pos.getZ())
        );
    }
}