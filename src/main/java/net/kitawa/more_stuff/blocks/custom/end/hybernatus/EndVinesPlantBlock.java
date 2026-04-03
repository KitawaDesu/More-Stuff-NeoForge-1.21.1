package net.kitawa.more_stuff.blocks.custom.end.hybernatus;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.WeepingVinesPlantBlock;

public class EndVinesPlantBlock extends WeepingVinesPlantBlock {
    public EndVinesPlantBlock(Properties p_154975_) {
        super(p_154975_);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) Blocks.WEEPING_VINES;
    }
}
