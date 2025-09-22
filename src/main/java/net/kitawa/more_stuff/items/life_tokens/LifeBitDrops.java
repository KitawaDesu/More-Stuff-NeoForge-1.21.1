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

@EventBusSubscriber(modid = "more_stuff", bus = EventBusSubscriber.Bus.GAME)
public class LifeBitDrops {

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity == null || entity.level().isClientSide()) return;

        execute((ServerLevel) entity.level(), entity, entity.getX(), entity.getY(), entity.getZ());
    }

    private static double safeGetAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        AttributeInstance instance = entity.getAttribute(attribute);
        return instance != null ? instance.getValue() : 0.0;
    }

    private static void execute(ServerLevel world, LivingEntity entity, double x, double y, double z) {
        // Check config dynamically at runtime
        if (!LifeTokensConfig.addLifeTokens) return;

        float maxHealth = entity.getMaxHealth();

        double mobArmor = safeGetAttribute(entity, Attributes.ARMOR);
        double mobToughness = safeGetAttribute(entity, Attributes.ARMOR_TOUGHNESS);
        double mobAttackDamage = safeGetAttribute(entity, Attributes.ATTACK_DAMAGE);

        // Use runtime getters instead of static fields
        float random = world.getRandom().nextFloat();
        Double biasedTowards = LifeBitDropsConfig.BIASED_TOWARDS_PERCENTAGE.get();
        Double minimumDrop = LifeBitDropsConfig.MINIMUM_DROP_PERCENTAGE.get();
        float percentage = (float) ((random + biasedTowards * 2f) / 3f);

        if (percentage < minimumDrop) return;

        float difficultyModifier = getDifficultyModifier(world, world.getDifficulty());
        if (world.getRandom().nextFloat() > difficultyModifier) return;

        float armorBonus = (float)(mobArmor * LifeBitDropsConfig.ARMOR_BONUS_PERCENTAGE.get());
        float toughnessBonus = (float)(mobToughness * LifeBitDropsConfig.TOUGHNESS_BONUS_PERCENTAGE.get());
        float mobAttackBonus = (float)(mobAttackDamage * LifeBitDropsConfig.MOB_ATTACK_DAMAGE_BONUS.get());

        float healthPenalty = 0f, armorPenalty = 0f, toughnessPenalty = 0f, attackDamagePenalty = 0f;

        LivingEntity killer = entity.getKillCredit();
        if (killer instanceof Player player) {
            double playerHealth = player.getMaxHealth();
            double playerArmor = safeGetAttribute(player, Attributes.ARMOR);
            double playerToughness = safeGetAttribute(player, Attributes.ARMOR_TOUGHNESS);
            double playerAttackDamage = safeGetAttribute(player, Attributes.ATTACK_DAMAGE);

            healthPenalty = (float)(Math.max(0, playerHealth - LifeBitDropsConfig.PLAYER_HEALTH_THRESHOLD.get())
                    * LifeBitDropsConfig.PLAYER_HEALTH_PENALTY.get());
            armorPenalty = (float)(Math.max(0, playerArmor - LifeBitDropsConfig.PLAYER_ARMOR_THRESHOLD.get())
                    * LifeBitDropsConfig.PLAYER_ARMOR_PENALTY.get());
            toughnessPenalty = (float)(Math.max(0, playerToughness - LifeBitDropsConfig.PLAYER_TOUGHNESS_THRESHOLD.get())
                    * LifeBitDropsConfig.PLAYER_TOUGHNESS_PENALTY.get());
            attackDamagePenalty = (float)(Math.max(0, playerAttackDamage - LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_THRESHOLD.get())
                    * LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_PENALTY.get());
        }

        float rawDropAmount = (maxHealth * percentage) + (armorBonus + toughnessBonus + mobAttackBonus)
                - (healthPenalty + armorPenalty + toughnessPenalty + attackDamagePenalty);

        int dropAmount = Math.max(0, (int) rawDropAmount);

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

    private static float getDifficultyModifier(ServerLevel world, Difficulty difficulty) {
        float minModifier = 1.0f, maxModifier = 1.0f;
        switch (difficulty) {
            case PEACEFUL -> { return 0f; }
            case EASY -> { return 1.0f; }
            case NORMAL -> { minModifier = 0.75f; }
            case HARD -> { minModifier = 0.25f; maxModifier = 0.5f; }
        }
        return minModifier + (world.getRandom().nextFloat() * (maxModifier - minModifier));
    }
}
