package net.kitawa.more_stuff.experimentals.items.util;

import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;
import java.util.function.Supplier;

public class ToggleableItem extends Item {

    private final Supplier<Boolean> enabledSupplier;
    private final String configName; // e.g. "Life Tokens Config"

    /**
     * @param properties   Standard Item properties
     * @param enabledSupplier  A boolean supplier that decides if the item is enabled
     * @param configName   A friendly name of the config category (shown in tooltip)
     */
    public ToggleableItem(Properties properties, Supplier<Boolean> enabledSupplier, String configName) {
        super(properties);
        this.enabledSupplier = enabledSupplier;
        this.configName = configName;
    }

    private boolean enabled() {
        return enabledSupplier.get();
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return enabled() && super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component baseName = super.getName(stack);
        if (!enabled()) {
            // Gray color + append "(disabled)"
            return Component.literal(baseName.getString() + " (disabled)")
                    .withStyle(ChatFormatting.GRAY);
        }
        return baseName;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (!enabled()) {
            tooltip.add(Component.literal("§7This item is disabled in the " + configName));
        } else {
            super.appendHoverText(stack, context, tooltip, flag);
        }
    }
}
