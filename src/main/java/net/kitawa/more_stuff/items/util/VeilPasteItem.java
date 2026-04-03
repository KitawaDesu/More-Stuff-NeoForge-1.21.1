package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.blocks.custom.end.phantasmic.RevealableBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class VeilPasteItem extends Item {

    public VeilPasteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.hasProperty(RevealableBlock.FORCED_SOLID)) {
            if (!state.getValue(RevealableBlock.FORCED_SOLID)) {
                if (!level.isClientSide()) {
                    RevealableBlock.setForcedSolid(level, pos, true);
                    context.getItemInHand().shrink(1);
                }
                return InteractionResult.sidedSuccess(level.isClientSide());
            }
            // Already forced solid — don't consume
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }
}
