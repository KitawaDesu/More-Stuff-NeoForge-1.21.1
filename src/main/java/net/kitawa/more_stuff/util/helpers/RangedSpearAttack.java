package net.kitawa.more_stuff.util.helpers;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.items.entity.ThrownJavelin;
import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;

import java.util.Objects;

public class RangedSpearAttack implements BehaviorControl<Piglin> {
    private int attackCooldown = 0;
    private Behavior.Status status = Behavior.Status.STOPPED;
    private boolean isCharging = false;
    private boolean justThrew = false; // <-- new flag

    private static final AttributeModifier THROW_SLOWDOWN = new AttributeModifier(
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "spear_throw_slowdown"),
            -0.5,
            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    );

    private static final double MIN_DISTANCE = 5.0; // min distance to maintain
    private static final float BACKUP_SPEED = 0.75f;

    @Override
    public Behavior.Status getStatus() {
        return status;
    }

    @Override
    public boolean tryStart(ServerLevel level, Piglin piglin, long gameTime) {
        LivingEntity target = getTarget(piglin);
        if (hasSpear(piglin) && target != null) {
            status = Behavior.Status.RUNNING;
            attackCooldown = 0;
            isCharging = false;
            justThrew = false;

            applySlowdown(piglin);
            piglin.getNavigation().stop();
            piglin.getMoveControl().setWantedPosition(piglin.getX(), piglin.getY(), piglin.getZ(), 0);

            return true;
        }
        return false;
    }

    @Override
    public void tickOrStop(ServerLevel level, Piglin piglin, long gameTime) {
        LivingEntity target = getTarget(piglin);
        if (!hasSpear(piglin) || target == null) {
            doStop(level, piglin, gameTime);
            return;
        }

        piglin.getLookControl().setLookAt(target, 30.0F, 30.0F);

        if (isCharging) {
            // Freeze while charging
            piglin.setDeltaMovement(0, piglin.getDeltaMovement().y, 0);
        } else if (justThrew) {
            // After throwing, only adjust ONCE
            double distance = piglin.distanceTo(target);
            if (distance < MIN_DISTANCE) {
                double dx = piglin.getX() - target.getX();
                double dz = piglin.getZ() - target.getZ();
                double mag = Math.sqrt(dx * dx + dz * dz);
                if (mag > 0.001) {
                    double nx = dx / mag;
                    double nz = dz / mag;
                    piglin.setDeltaMovement(nx * BACKUP_SPEED, piglin.getDeltaMovement().y, nz * BACKUP_SPEED);
                }
            }
            justThrew = false; // only back up once, then stop
        } else {
            // Stand still otherwise
            piglin.setDeltaMovement(0, piglin.getDeltaMovement().y, 0);
        }

        if (--attackCooldown <= 0) {
            throwSpear(level, piglin, target);
            attackCooldown = 40; // 2 sec cooldown
        }
    }

    @Override
    public void doStop(ServerLevel level, Piglin piglin, long gameTime) {
        status = Behavior.Status.STOPPED;
        isCharging = false;
        justThrew = false;
        removeSlowdown(piglin);
    }

    @Override
    public String debugString() {
        return "RangedSpearAttack";
    }

    // --- Helpers ---
    private LivingEntity getTarget(Piglin piglin) {
        return piglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private static boolean hasSpear(Piglin piglin) {
        Item item = piglin.getMainHandItem().getItem();
        return item instanceof TridentItem || item instanceof JavelinItem;
    }

    private void applySlowdown(Piglin piglin) {
        if (!Objects.requireNonNull(piglin.getAttribute(Attributes.MOVEMENT_SPEED))
                .hasModifier(THROW_SLOWDOWN.id())) {
            piglin.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(THROW_SLOWDOWN);
        }
    }

    private void removeSlowdown(Piglin piglin) {
        Objects.requireNonNull(piglin.getAttribute(Attributes.MOVEMENT_SPEED))
                .removeModifier(THROW_SLOWDOWN);
    }

    private void throwSpear(ServerLevel level, Piglin piglin, LivingEntity target) {
        ItemStack stack = piglin.getMainHandItem();
        Item item = stack.getItem();
        if (!(item instanceof TridentItem || item instanceof JavelinItem)) return;

        // Begin charging
        isCharging = true;
        piglin.startUsingItem(InteractionHand.MAIN_HAND);

        level.getServer().execute(() -> {
            Projectile spear;
            if (item instanceof TridentItem) {
                spear = new ThrownTrident(level, piglin, stack);
            } else {
                ThrownJavelin javelin = new ThrownJavelin(level, piglin, stack);
                javelin.setItemStack(stack);
                spear = javelin;
            }

            double dx = target.getX() - piglin.getX();
            double dy = target.getY(0.333) - spear.getY();
            double dz = target.getZ() - piglin.getZ();
            double power = Math.sqrt(dx * dx + dz * dz) * 0.2D;

            float velocity = item instanceof TridentItem ? 1.6F : JavelinItem.SHOOT_POWER;
            spear.shoot(dx, dy + power, dz, velocity, 1.0F);

            level.addFreshEntity(spear);

            piglin.stopUsingItem();
            removeSlowdown(piglin);

            // Switch flags
            isCharging = false;
            justThrew = true; // back up *once* after throw

            level.playSound(null, piglin.blockPosition(),
                    SoundEvents.TRIDENT_THROW.value(),
                    SoundSource.HOSTILE,
                    1.0F, 1.0F);
        });
    }
}
