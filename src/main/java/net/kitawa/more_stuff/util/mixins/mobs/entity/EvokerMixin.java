package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.util.helpers.IfArmorCanAbsorbHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Evoker.class)
public abstract class EvokerMixin extends SpellcasterIllager {
    protected EvokerMixin(EntityType<? extends SpellcasterIllager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!IfArmorCanAbsorbHelper.absorbDamageWithArmor(this, damageSource, damageAmount)) {
            super.actuallyHurt(damageSource, damageAmount);
        }
    }
}
