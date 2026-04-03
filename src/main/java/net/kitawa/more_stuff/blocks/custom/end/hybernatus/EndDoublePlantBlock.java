package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.neoforge.common.util.TriState;

public class EndDoublePlantBlock extends DoublePlantBlock {
    public EndDoublePlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState below = level.getBlockState(pos.below());
            if (state.getBlock() != this) return super.canSurvive(state, level, pos);
            return below.is(this) && below.getValue(HALF) == DoubleBlockHalf.LOWER;
        } else {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.getBlockState(belowPos);
            TriState soilDecision = belowState.canSustainPlant(level, belowPos, Direction.UP, state);
            if (!soilDecision.isDefault()) return soilDecision.isTrue();
            return belowState.is(BlockTags.DIRT)
                    || belowState.is(ModBlockTags.ENDER_NYLIUM)
                    || belowState.getBlock() instanceof FarmBlock;
        }
    }
}
