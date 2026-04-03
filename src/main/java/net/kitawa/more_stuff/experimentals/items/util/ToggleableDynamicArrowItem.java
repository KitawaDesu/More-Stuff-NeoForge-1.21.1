package net.kitawa.more_stuff.experimentals.items.util;

import net.kitawa.more_stuff.items.util.weapons.dynamarrow.DynamicArrowItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ToggleableDynamicArrowItem extends DynamicArrowItem {

    private final Supplier<Boolean> enabledSupplier;
    private final String configName; // e.g. "Experimental Arrows Config"

    public ToggleableDynamicArrowItem(Item.Properties properties, Supplier<Boolean> enabledSupplier, String configName) {
        super(properties);
        this.enabledSupplier = enabledSupplier;
        this.configName = configName;
    }

    private boolean enabled() {
        return enabledSupplier.get();
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        if (!enabled()) {
            // Use vanilla arrow if disabled
            return new Arrow(level, shooter, ammo.copyWithCount(1), weapon);
        }
        return super.createArrow(level, ammo, shooter, weapon);
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
        if (!enabled()) {
            Arrow arrow = new Arrow(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
            arrow.pickup = AbstractArrow.Pickup.ALLOWED;
            return arrow;
        }
        return super.asProjectile(level, pos, stack, direction);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return enabled() && super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component baseName = super.getName(stack);
        if (!enabled()) {
            return Component.literal(baseName.getString() + " (disabled)")
                    .withStyle(ChatFormatting.GRAY);
        }
        return baseName;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (!enabled()) {
            tooltip.add(Component.literal("§7This item is Disabled. It functions as a regular arrow."));
            tooltip.add(Component.literal("§8Enable it in " + configName + " for custom effects."));
        } else {
            super.appendHoverText(stack, context, tooltip, flag);
        }
    }
}