package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.BubbleColumnBlock.DRAG_DOWN;

@Mixin(BubbleColumnBlock.class)
public abstract class BubbleColumnMixin extends Block implements BucketPickup {
    public BubbleColumnMixin(Properties properties) {
        super(properties);
    }

    @Inject(
            method = {"getColumnState"},
            at = {@At("TAIL")},
            cancellable = true
    )


    private static void getColumnState(BlockState blockState, CallbackInfoReturnable<BlockState> cir) {
        if (blockState.is(Blocks.BUBBLE_COLUMN)) {
            cir.setReturnValue(blockState);
        } else if (blockState.is(ModBlockTags.CREATES_UPWARDS_BUBBLE_COLUMNS)) {
            cir.setReturnValue(Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(DRAG_DOWN, Boolean.FALSE));
        } else {
            cir.setReturnValue(blockState.is(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS)
                    ? Blocks.BUBBLE_COLUMN.defaultBlockState().setValue(DRAG_DOWN, Boolean.TRUE) : Blocks.WATER.defaultBlockState());
        }
    }
}
