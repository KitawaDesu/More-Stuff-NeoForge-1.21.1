package net.kitawa.more_stuff.items.life_tokens.util;

import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LifeTokenItem extends Item {

    public LifeTokenItem(Properties properties) {
        super(properties);
    }

    private boolean enabled() {
        return LifeTokensConfig.addLifeTokens;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!enabled()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double currentMaxHealth = maxHealthAttr.getBaseValue();
                double increment = LifeTokensConfig.lifeIncrement;
                double maxLife = LifeTokensConfig.maxLife;

                if (currentMaxHealth < maxLife) {
                    double newMaxHealth = Math.min(currentMaxHealth + increment, maxLife);
                    maxHealthAttr.setBaseValue(newMaxHealth);
                    player.heal((float) increment);

                    stack.shrink(1);

                    level.playSound(null, player.blockPosition(),
                            SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS,
                            1.0F, 1.0F);

                    player.displayClientMessage(
                            Component.literal("Your maximum health has increased by " + increment + "!"),
                            false
                    );
                } else {
                    player.displayClientMessage(
                            Component.literal("You already have the maximum health of " + maxLife + "!"),
                            false
                    );
                }
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return enabled() && super.isFoil(stack);
    }

    @Override
    public Component getName(ItemStack stack) {
        Component baseName = super.getName(stack);
        if (!enabled()) {
            // Gray color + append "(is disabled)"
            return Component.literal(baseName.getString() + " (is disabled)")
                    .withStyle(ChatFormatting.GRAY);
        }
        return baseName;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        if (!enabled()) {
            tooltip.add(Component.literal("§7This item is disabled in the Life Tokens config."));
        } else {
            super.appendHoverText(stack, context, tooltip, flag);
        }
    }
}
