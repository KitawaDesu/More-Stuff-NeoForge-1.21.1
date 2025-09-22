package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(IBlockExtension.class)
public interface IBlockExtensionMixin {

    /**
     * @param state The state
     * @return true if the block is sticky block which used for pull or push adjacent blocks (use by piston)
     * @author
     * KitawaDesu
     * @reason
     * To Make My Custom Aquanda Gel Block Stick
     */
    @Overwrite
    default boolean isStickyBlock(BlockState state) {
        return state.getBlock() == Blocks.SLIME_BLOCK || state.getBlock() == Blocks.HONEY_BLOCK || state.getBlock() == ModBlocks.AQUANDA_GEL.get() || state.getBlock() == ModBlocks.GLOWING_AQUANDA_GEL.get();
    }

    /**
     * Determines if this block can stick to another block when pushed by a piston.
     *
     * @param state My state
     * @param other Other block
     * @return True to link blocks
     * @author
     * KitawaDesu
     * @reason
     * To Make My Custom Aquanda Gel Block Stick
     */
    @Overwrite
    default boolean canStickTo(BlockState state, BlockState other) {
        if (state.getBlock() == Blocks.HONEY_BLOCK && other.getBlock() == Blocks.SLIME_BLOCK) return false;
        if (state.getBlock() == Blocks.SLIME_BLOCK && other.getBlock() == Blocks.HONEY_BLOCK) return false;
        if (state.getBlock() == Blocks.SLIME_BLOCK && other.getBlock() == ModBlocks.GLOWING_AQUANDA_GEL.get()) return false;
        if (state.getBlock() == Blocks.SLIME_BLOCK && other.getBlock() == ModBlocks.AQUANDA_GEL.get()) return false;
        if (state.getBlock() == Blocks.HONEY_BLOCK && other.getBlock() == ModBlocks.GLOWING_AQUANDA_GEL.get()) return false;
        if (state.getBlock() == Blocks.HONEY_BLOCK && other.getBlock() == ModBlocks.AQUANDA_GEL.get()) return false;
        if (state.getBlock() == ModBlocks.AQUANDA_GEL.get() && other.getBlock() == Blocks.HONEY_BLOCK) return false;
        if (state.getBlock() == ModBlocks.GLOWING_AQUANDA_GEL.get() && other.getBlock() == Blocks.HONEY_BLOCK) return false;
        if (state.getBlock() == ModBlocks.AQUANDA_GEL.get() && other.getBlock() == Blocks.SLIME_BLOCK) return false;
        if (state.getBlock() == ModBlocks.GLOWING_AQUANDA_GEL.get() && other.getBlock() == Blocks.SLIME_BLOCK) return false;
        return state.isStickyBlock() || other.isStickyBlock();
    }
}
