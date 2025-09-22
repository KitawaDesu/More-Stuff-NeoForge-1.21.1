package net.kitawa.more_stuff.items.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class ModShieldItem extends ShieldItem {

    private final Item repairItem;
    private final TagKey<Item> repairTag;

    public ModShieldItem(Properties properties, Item repairItem) {
        super(properties);
        this.repairItem = repairItem;
        this.repairTag = null;
    }

    public ModShieldItem(Properties properties, TagKey<Item> repairTag) {
        super(properties);
        this.repairItem = null;
        this.repairTag = repairTag;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        boolean isTagMatch = repairTag != null && repair.is(repairTag);
        boolean isItemMatch = repairItem != null && repair.getItem() == repairItem;

        return isTagMatch || isItemMatch || super.isValidRepairItem(toRepair, repair);
    }
}
