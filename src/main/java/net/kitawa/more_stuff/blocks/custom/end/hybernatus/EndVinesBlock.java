package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WeepingVinesBlock;

public class EndVinesBlock extends WeepingVinesBlock {
    public EndVinesBlock(Properties p_154966_) {
        super(p_154966_);
    }

    @Override
    protected Block getBodyBlock() {
        return Blocks.WEEPING_VINES_PLANT;
    }
}
