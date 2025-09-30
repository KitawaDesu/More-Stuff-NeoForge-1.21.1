package net.kitawa.more_stuff.util.mixins.mobs.entity;

import com.google.common.collect.ImmutableList;
import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.kitawa.more_stuff.util.helpers.RangedSpearAttack;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.RememberIfHoglinWasKilled;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {

    @Inject(method = "initFightActivity", at = @At("RETURN"))
    private static void addSpearSupport(Piglin piglin, Brain<Piglin> brain, CallbackInfo ci) {
        ImmutableList.Builder<BehaviorControl<? super Piglin>> builder = ImmutableList.builder();

        // Stop attacking if target invalid
        builder.add(StopAttackingIfTargetInvalid.create(
                entity -> entity instanceof Piglin && !isNearestValidAttackTarget(piglin, getAttackTarget(piglin))
        ));

        // Ranged spear attack (handles both tridents and javelins)
        builder.add(new RangedSpearAttack());

        // Only melee attack if NOT holding a trident or javelin
        builder.add(BehaviorBuilder.triggerIf(
                p -> !isHoldingSpear(p),
                MeleeAttack.create(20)
        ));

        // Crossbow attack unchanged
        builder.add(new CrossbowAttack<>());

        // Remember hoglin killed
        builder.add(RememberIfHoglinWasKilled.create());

        // Stop attacking if zombified nearby, only for piglins not holding a spear
        builder.add(EraseMemoryIf.create(
                p -> !isHoldingSpear(p) && isZombifiedNearby(p),
                MemoryModuleType.ATTACK_TARGET
        ));

        brain.addActivityAndRemoveMemoryWhenStopped(
                Activity.FIGHT,
                10,
                builder.build(),
                MemoryModuleType.ATTACK_TARGET
        );
    }

    // --- Helpers ---
    private static LivingEntity getAttackTarget(Piglin piglin) {
        return piglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private static boolean isNearestValidAttackTarget(Piglin piglin, LivingEntity target) {
        return target != null && piglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET)
                .map(uuid -> uuid == target).orElse(false);
    }

    private static boolean isZombifiedNearby(Piglin piglin) {
        return !piglin.level().getEntitiesOfClass(
                Zombie.class,
                piglin.getBoundingBox().inflate(16),
                z -> z.getType() == EntityType.ZOMBIFIED_PIGLIN
        ).isEmpty();
    }

    private static boolean isHoldingSpear(Piglin piglin) {
        return piglin.isHolding(Items.TRIDENT) || piglin.getMainHandItem().getItem() instanceof JavelinItem;
    }
}
