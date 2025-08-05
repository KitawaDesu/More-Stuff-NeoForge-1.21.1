package net.kitawa.more_stuff.items.util;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class ModFoods {

    public static final FoodProperties AQUANDA_BERRIES = new FoodProperties.Builder()
            .nutrition(2)
            .saturationModifier(0.1F)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 600, 0), 1.0F)
            .build();
}
