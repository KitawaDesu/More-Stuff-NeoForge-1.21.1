package net.kitawa.more_stuff.blocks.custom.general;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TintedSlimeBlock extends SlimeBlock {
    public TintedSlimeBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean propagatesSkylightDown(BlockState p_154824_, BlockGetter p_154825_, BlockPos p_154826_) {
        return false;
    }

    @Override
    protected int getLightBlock(BlockState p_154828_, BlockGetter p_154829_, BlockPos p_154830_) {
        return p_154829_.getMaxLightLevel();
    }
}
