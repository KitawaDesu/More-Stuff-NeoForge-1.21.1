package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class JavelinAttackGoal<T extends Mob & RangedAttackMob> extends RangedAttackGoal {
    private final T mob;

    public JavelinAttackGoal(T mob, double speedModifier, int attackInterval, float attackRadius) {
        super(mob, speedModifier, attackInterval, attackRadius);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        // Only run goal if mob is holding trident or javelin
        Item heldItem = this.mob.getMainHandItem().getItem();
        return super.canUse() && (heldItem == Items.TRIDENT || heldItem instanceof JavelinItem);
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        this.mob.startUsingItem(InteractionHand.MAIN_HAND);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.stopUsingItem();
        this.mob.setAggressive(false);
    }
}
