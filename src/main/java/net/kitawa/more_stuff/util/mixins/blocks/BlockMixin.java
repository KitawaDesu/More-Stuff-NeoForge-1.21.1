package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.util.tags.ModBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike, net.neoforged.neoforge.common.extensions.IBlockExtension {

    @Shadow @Final private Holder.Reference<Block> builtInRegistryHolder;

    @Shadow protected abstract @NotNull Block asBlock();

    @Shadow @Deprecated public abstract Holder.Reference<Block> builtInRegistryHolder();

    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.builtInRegistryHolder.is(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS) || this.builtInRegistryHolder.is(ModBlockTags.CREATES_UPWARDS_BUBBLE_COLUMNS)) {
            BubbleColumnBlock.updateColumn(level, pos.above(), state);
        }
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
        if (this.builtInRegistryHolder.is(ModBlockTags.CREATES_DOWNWARDS_BUBBLE_COLUMNS) || this.builtInRegistryHolder.is(ModBlockTags.CREATES_UPWARDS_BUBBLE_COLUMNS)) {
            level.scheduleTick(pos, this.asBlock(), 20);
        }
    }
}
