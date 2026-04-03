package net.kitawa.more_stuff.items.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ModdedBrushItem extends BrushItem {
    private final int useDuration;
    private final TagKey<Item> repairTag;
    private final ItemLike repairItem;

    // ───── Constructors ─────

    /** Use when repairing with an Item Tag (e.g., ItemTags.PLANKS) */
    public ModdedBrushItem(Properties properties, int useDuration, TagKey<Item> repairTag) {
        super(properties);
        this.useDuration = useDuration;
        this.repairTag = repairTag;
        this.repairItem = null;
    }

    /** Use when repairing with a specific Item (e.g., Items.COPPER_INGOT) */
    public ModdedBrushItem(Properties properties, int useDuration, ItemLike repairItem) {
        super(properties);
        this.useDuration = useDuration;
        this.repairItem = repairItem;
        this.repairTag = null;
    }

    // ───── Behavior ─────

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return useDuration;
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        if (repairTag != null) {
            return repair.is(repairTag);
        } else if (repairItem != null) {
            return repair.is(repairItem.asItem());
        }
        return false;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_BRUSH_ACTIONS.contains(itemAbility);
    }
}

