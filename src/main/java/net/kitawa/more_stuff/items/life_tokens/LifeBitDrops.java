package net.kitawa.more_stuff.items.life_tokens;

import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;

@EventBusSubscriber(modid = "more_stuff", bus = EventBusSubscriber.Bus.GAME)
public class LifeBitDrops {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (LifeTokensConfig.CONFIG.addLifeTokens() && !event.getEntity().level().isClientSide) {
            if (event.getEntity() != null) {
                execute((ServerLevel) event.getEntity().level(), event.getEntity(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            }
        }
    }

    private static double safeGetAttribute(LivingEntity entity, Holder<Attribute> attributeHolder) {
        AttributeInstance instance = entity.getAttribute(attributeHolder);
        if (instance == null) return 0.0;
        return instance.getValue();
    }

    private static void execute(ServerLevel world, LivingEntity entity, double x, double y, double z) {
        if (!LifeTokensConfig.CONFIG.addLifeTokens()) return;

        float maxHealth = entity.getMaxHealth();

        // Safely get mob's own stats
        double mobArmor = safeGetAttribute(entity, Attributes.ARMOR);
        double mobToughness = safeGetAttribute(entity, Attributes.ARMOR_TOUGHNESS);
        double mobAttackDamage = safeGetAttribute(entity, Attributes.ATTACK_DAMAGE);

        // Drop % calculation (biased toward config value)
        float random = world.getRandom().nextFloat();
        float percentage = (random + LifeBitDropsConfig.CONFIG.BiasedTwordsPercentageValue() * 2f) / 3f;

        // Skip if under configured minimum threshold
        if (percentage < LifeBitDropsConfig.CONFIG.MinimumDropPercentageValue()) return;

        // Difficulty-based chance gate
        float difficultyModifier = getDifficultyModifier(world, world.getDifficulty());
        if (world.getRandom().nextFloat() > difficultyModifier) return;

        // Bonuses from the mob's own armor and toughness
        float armorBonus = (float)(mobArmor * LifeBitDropsConfig.CONFIG.ArmorBonusPercentageValue());
        float toughnessBonus = (float)(mobToughness * LifeBitDropsConfig.CONFIG.ToughnessBonusPercentageValue());

        // --- New Section: Player penalty and mob attack bonus ---
        float mobAttackBonus = (float)(mobAttackDamage * LifeBitDropsConfig.CONFIG.MobAttackDamageBonus());
        float healthPenalty = 0f;
        float armorPenalty = 0f;
        float toughnessPenalty = 0f;
        float attackDamagePenalty = 0f;

        // Check if a player killed this entity
        LivingEntity killer = entity.getKillCredit();
        if (killer instanceof Player player) {
            double playerHealth = player.getMaxHealth();
            double playerArmor = safeGetAttribute(player, Attributes.ARMOR);
            double playerToughness = safeGetAttribute(player, Attributes.ARMOR_TOUGHNESS);
            double playerAttackDamage = safeGetAttribute(player, Attributes.ATTACK_DAMAGE);

            // Penalty formula
            healthPenalty = (float) (
                    Math.max(0, playerHealth - LifeBitDropsConfig.CONFIG.PlayerHealthPenaltyThreshold()) *
                            LifeBitDropsConfig.CONFIG.PlayerHealthPenalty()
            );

            armorPenalty = (float) (
                    Math.max(0, playerArmor - LifeBitDropsConfig.CONFIG.PlayerArmorPenaltyThreshold()) *
                            LifeBitDropsConfig.CONFIG.PlayerArmorPenalty()
            );

            toughnessPenalty = (float) (
                    Math.max(0, playerToughness - LifeBitDropsConfig.CONFIG.PlayerToughnessPenaltyThreshold()) *
                            LifeBitDropsConfig.CONFIG.PlayerToughnessPenalty()
            );

            attackDamagePenalty = (float) (
                    Math.max(0, playerAttackDamage - LifeBitDropsConfig.CONFIG.PlayerAttackDamagePenaltyThreshold()) *
                            LifeBitDropsConfig.CONFIG.PlayerAttackDamagePenalty()
            );
        }

        // Final drop amount
        float rawDropAmount = (maxHealth * percentage) + (armorBonus + toughnessBonus + mobAttackBonus) - (healthPenalty + armorPenalty + toughnessPenalty + attackDamagePenalty);
        int dropAmount = Math.max(0, (int) rawDropAmount); // Prevent negative drops

        // Drop item(s)
        int maxStackSize = new ItemStack(LifeTokenItems.LIFE_BIT.get()).getMaxStackSize();
        while (dropAmount > 0) {
            int stackSize = Math.min(dropAmount, maxStackSize);
            ItemStack stack = new ItemStack(LifeTokenItems.LIFE_BIT.get(), stackSize);
            ItemEntity itemEntity = new ItemEntity(world, x, y, z, stack);
            itemEntity.setPickUpDelay(0);
            world.addFreshEntity(itemEntity);
            dropAmount -= stackSize;
        }
    }

    // Helper method to get the difficulty modifier
    private static float getDifficultyModifier(ServerLevel world, Difficulty difficulty) {
        float minModifier = 1.0f; // Default value
        float maxModifier = 1.0f; // Default value

        switch (difficulty) {
            case PEACEFUL:
                return 0f; // No drops in peaceful
            case EASY:
                return 1.0f;
            case NORMAL:
                minModifier = 0.75f; // 25% fewer drops in normal
                break;
            case HARD:
                minModifier = 0.25f; // 75% fewer drops in hard
                maxModifier = 0.5f;  // Allow up to 50% more drops in hard
                break;
            default:
                return 1.0f; // Default modifier if somehow the difficulty is not recognized
        }

        // Randomly choose a modifier within the range
        return minModifier + (world.getRandom().nextFloat() * (maxModifier - minModifier));
    }
}
