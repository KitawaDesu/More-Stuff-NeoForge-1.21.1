package net.kitawa.more_stuff.util.recipes.fletching_table;

import net.kitawa.more_stuff.experimentals.blocks.util.FletchingTableMenu;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record FletchingInput(Container container) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> container.getItem(FletchingTableMenu.TIP_SLOT);
            case 1 -> container.getItem(FletchingTableMenu.SHAFT_SLOT);
            case 2 -> container.getItem(FletchingTableMenu.FLETCHING_SLOT);
            case 3 -> container.getItem(FletchingTableMenu.MODIFIER_SLOT);
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 4;
    }
}