package net.kitawa.more_stuff.experimentals.items.util;

import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Supplier;

public class ToggleableJavelinItem extends JavelinItem {
    private final Supplier<Boolean> enabledSupplier;
    private final String configName;

    public ToggleableJavelinItem(String registryName, Tier tier, ResourceLocation texture, Properties properties, Supplier<Boolean> enabledSupplier, String configName) {
        super(registryName, tier, texture, properties);
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
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // Only apply full javelin damage if enabled
        if (!enabled()) return super.hurtEnemy(stack, target, attacker);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!enabled()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        ItemStack itemstack = player.getItemInHand(hand);
        if (isTooDamagedToUse(itemstack)) {
            return InteractionResultHolder.fail(itemstack);
        } else if (EnchantmentHelper.getTridentSpinAttackStrength(itemstack, player) > 0.0F && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
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
            tooltip.add(Component.literal("§7This item is Disabled, To use, Enable The " + configName));
        } else {
            super.appendHoverText(stack, context, tooltip, flag);
        }
    }
}