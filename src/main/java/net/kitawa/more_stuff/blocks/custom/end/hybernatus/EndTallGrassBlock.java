package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.common.util.TriState;

public class EndTallGrassBlock extends TallGrassBlock {
    public EndTallGrassBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(BlockTags.DIRT)
                || state.is(ModBlockTags.ENDER_NYLIUM)
                || state.getBlock() instanceof FarmBlock;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();

        if (!level.getBlockState(above).canBeReplaced()) return;

        DoublePlantBlock.placeAt(level, ModBlocks.HYBERNATIC_TALL_GRASS.get().defaultBlockState(), pos, 3);
    }
}
