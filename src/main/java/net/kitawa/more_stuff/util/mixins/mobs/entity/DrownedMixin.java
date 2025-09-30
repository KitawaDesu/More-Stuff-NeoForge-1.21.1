package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.experimentals.items.ExperimentalCombatItems;
import net.kitawa.more_stuff.experimentals.items.entity.ThrownJavelin;
import net.kitawa.more_stuff.items.util.weapons.JavelinItem;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.helpers.IfArmorCanAbsorbHelper;
import net.kitawa.more_stuff.util.helpers.JavelinAttackGoal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Drowned.class)
public abstract class DrownedMixin extends Zombie implements RangedAttackMob {
    public DrownedMixin(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        if (random.nextFloat() > 0.9) {
            int i = random.nextInt(16);

            if (i < 10) {
                // Weighted trident/javelin selection
                float roll = random.nextFloat(); // 0.0 to 1.0

                if (!ExperimentalUpdatesConfig.isCombatUpdateAllowed) {
                    // Combat update disabled → always trident
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
                    return;
                }

                // Combat update enabled → use weighted selection
                if (roll < 0.65F) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.TRIDENT));
                } else if (roll < 0.65F + 0.15F) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.COPPER_JAVELIN.get()));
                } else if (roll < 0.65F + 0.15F + 0.13F) {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.IRON_JAVELIN.get()));
                } else {
                    this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.GOLDEN_JAVELIN.get()));
                }
            } else {
                // Fallback: Fishing rod
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.FISHING_ROD));
            }
        }
    }

    @Overwrite
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack mainHand = this.getMainHandItem();

        if (mainHand.getItem() instanceof JavelinItem) {
            // Use ThrownJavelin if holding a javelin
            ThrownJavelin thrownJavelin = new ThrownJavelin(this.level(), this, mainHand);
            thrownJavelin.setItemStack(mainHand); // ensures correct appearance, enchantments, etc.

            double dx = target.getX() - this.getX();
            double dy = target.getY(0.333) - thrownJavelin.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            thrownJavelin.shoot(dx, dy + distance * 0.2F, dz, JavelinItem.SHOOT_POWER, 14 - this.level().getDifficulty().getId() * 4);
            this.level().addFreshEntity(thrownJavelin);

        } else if (mainHand.getItem() == Items.TRIDENT) {
            // Default trident behavior
            ThrownTrident thrownTrident = new ThrownTrident(this.level(), this, mainHand);
            double dx = target.getX() - this.getX();
            double dy = target.getY(0.333) - thrownTrident.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            thrownTrident.shoot(dx, dy + distance * 0.2F, dz, 1.6F, 14 - this.level().getDifficulty().getId() * 4);
            this.level().addFreshEntity(thrownTrident);
        }

        // Play attack sound regardless of projectile type
        this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!IfArmorCanAbsorbHelper.absorbDamageWithArmor(this, damageSource, damageAmount)) {
            super.actuallyHurt(damageSource, damageAmount);
        }
    }

    @Inject(method = "addBehaviourGoals", at = @At("RETURN"))
    private void addJavelinAttackGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new JavelinAttackGoal<>(this, 1.0D, 40, 15.0F));
    }
}
