package net.kitawa.more_stuff.util.mixins.items;

import net.kitawa.more_stuff.items.util.ModItemUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {

    /**
     * Overwrites the `matches` method to support custom shield items.
     */
    @Overwrite
    public boolean matches(CraftingInput input, Level level) {
        ItemStack shield = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    if (!banner.isEmpty()) {
                        return false;
                    }
                    banner = stack;
                } else if (stack.is(Items.SHIELD) || ModItemUtils.isCustomShield(stack)) {
                    if (!shield.isEmpty()) {
                        return false;
                    }

                    BannerPatternLayers patterns = stack.getOrDefault(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY);
                    if (!patterns.layers().isEmpty()) {
                        return false; // Already decorated
                    }

                    shield = stack;
                } else {
                    return false; // Unknown item
                }
            }
        }

        return !shield.isEmpty() && !banner.isEmpty();
    }

    /**
     * Overwrites the `assemble` method to support custom shield items.
     */
    @Overwrite
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider provider) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack shield = ItemStack.EMPTY;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof BannerItem) {
                    banner = stack;
                } else if (stack.is(Items.SHIELD) || ModItemUtils.isCustomShield(stack)) {
                    shield = stack.copy(); // Copy to modify
                }
            }
        }

        if (shield.isEmpty()) {
            return ItemStack.EMPTY;
        }

        // Transfer banner data
        shield.set(DataComponents.BANNER_PATTERNS, banner.get(DataComponents.BANNER_PATTERNS));
        shield.set(DataComponents.BASE_COLOR, ((BannerItem) banner.getItem()).getColor());

        return shield;
    }
}
