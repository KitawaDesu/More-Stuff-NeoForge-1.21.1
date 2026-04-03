package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.level.block.DispenserBlock;

public class ModDispenserBehaviors {
    public static void register() {
        DispenserBlock.registerBehavior(
                ExperimentalCombatItems.DYNAMIC_ARROW.get(),
                new ProjectileDispenseBehavior(ExperimentalCombatItems.DYNAMIC_ARROW.get())
        );
    }
}
