package net.kitawa.more_stuff.util.mixins.blocks;

import net.kitawa.more_stuff.blocks.ModBlocks;
import net.kitawa.more_stuff.util.helpers.SlimeColorMap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.util.Map;
import java.util.Set;

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
        Block block = state.getBlock();

        // Vanilla
        if (block == Blocks.SLIME_BLOCK || block == Blocks.HONEY_BLOCK) {
            return true;
        }

        // Aquanda gels
        if (block == ModBlocks.AQUANDA_GEL.get()
                || block == ModBlocks.GLOWING_AQUANDA_GEL.get()) {
            return true;
        }

        // All modded slime blocks
        return ModBlocks.SLIME_BLOCKS.values().stream()
                .anyMatch(deferred -> deferred.get() == block);
    }

    private static String getSlimeColor(Block block) {
        for (Map.Entry<String, DeferredBlock<SlimeBlock>> entry : ModBlocks.SLIME_BLOCKS.entrySet()) {
            if (entry.getValue().get() == block) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static boolean slimeColorsCanStick(String a, String b) {
        Set<String> allowed = SlimeColorMap.SLIME_STICK_RULES.get(a);
        return allowed != null && allowed.contains(b);
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

        Block a = state.getBlock();
        Block b = other.getBlock();

        // Vanilla exclusions
        if ((a == Blocks.HONEY_BLOCK && b == Blocks.SLIME_BLOCK) ||
                (b == Blocks.HONEY_BLOCK && a == Blocks.SLIME_BLOCK)) {
            return false;
        }

        // Aquanda gel exclusions
        if ((a == Blocks.SLIME_BLOCK || b == Blocks.SLIME_BLOCK)
                && (a == ModBlocks.AQUANDA_GEL.get()
                || a == ModBlocks.GLOWING_AQUANDA_GEL.get()
                || b == ModBlocks.AQUANDA_GEL.get()
                || b == ModBlocks.GLOWING_AQUANDA_GEL.get())) {
            return false;
        }

        String colorA = getSlimeColor(a);
        String colorB = getSlimeColor(b);

        if (colorA != null && colorB != null) {
            return slimeColorsCanStick(colorA, colorB)
                    || slimeColorsCanStick(colorB, colorA);
        }

        return state.isStickyBlock() && other.isStickyBlock();
    }
}
