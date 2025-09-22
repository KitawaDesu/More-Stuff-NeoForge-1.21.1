package net.kitawa.more_stuff.util.records;

import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.level.ServerLevelAccessor;

public class ModEnchantmentUtils {
    public static void maybeEnchantSpawnedItem(
            LivingEntity entity,
            ServerLevelAccessor level,
            EquipmentSlot slot,
            RandomSource random,
            float enchantChance,
            DifficultyInstance difficulty
    ) {
        float dayMultiplier = 1.0F;
        if (MoreStuffGeneralConfig.allowLogarithmicEnchantments) {
            long dayCount = level.getLevel().getDayTime() / 24000L;
            dayMultiplier += (float)(Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor);
        }

        float finalChance = enchantChance * difficulty.getSpecialMultiplier() * dayMultiplier;
        ItemStack stack = entity.getItemBySlot(slot);

        if (!stack.isEmpty() && random.nextFloat() < finalChance) {
            EnchantmentHelper.enchantItemFromProvider(
                    stack,
                    level.registryAccess(),
                    VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT,
                    difficulty,
                    random
            );
            entity.setItemSlot(slot, stack);
        }
    }
}
