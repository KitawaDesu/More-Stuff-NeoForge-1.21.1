package net.kitawa.more_stuff.items.util;

import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
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
import net.minecraft.world.level.Level;

public class LifeTokenItem extends Item {

    public LifeTokenItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // If disabled in config, ignore usage
        if (!LifeTokensConfig.CONFIG.addLifeTokens()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            AttributeInstance maxHealthAttr = player.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                double currentMaxHealth = maxHealthAttr.getBaseValue();
                double increment = LifeTokensConfig.CONFIG.lifeIncrement();
                double maxLife = LifeTokensConfig.CONFIG.maxLife();

                if (currentMaxHealth < maxLife) {
                    double newMaxHealth = Math.min(currentMaxHealth + increment, maxLife);
                    maxHealthAttr.setBaseValue(newMaxHealth);
                    player.heal((float) increment);

                    stack.shrink(1);

                    level.playSound(null, player.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.displayClientMessage(Component.literal("Your maximum health has increased by " + increment + "!"), false);
                } else {
                    player.displayClientMessage(Component.literal("You already have the maximum health of " + maxLife + "!"), false);
                }
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
