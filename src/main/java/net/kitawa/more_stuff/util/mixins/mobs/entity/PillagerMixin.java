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
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Pillager.class)
public abstract class PillagerMixin extends AbstractIllager implements CrossbowAttackMob, InventoryCarrier {
    protected PillagerMixin(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        if (!ExperimentalUpdatesConfig.isCombatUpdateAllowed) {
            // Combat update disabled → always crossbow
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
            return;
        }

        // --- First roll: Crossbow vs Throwable ---
        if (random.nextFloat() < 0.85F) {
            // 85% chance: Crossbow
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        } else {
            // 15% chance: roll for throwable
            float roll = random.nextFloat();

            if (roll < 0.45F) {
                // 45%: Wooden Javelin
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.WOODEN_JAVELIN.get()));
            } else if (roll < 0.65F) {
                // 20%: Stone Javelin
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.STONE_JAVELIN.get()));
            } else if (roll < 0.80F) {
                // 15%: Copper Javelin
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.COPPER_JAVELIN.get()));
            } else if (roll < 0.93F) {
                // 13%: Iron Javelin
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.IRON_JAVELIN.get()));
            } else {
                // 7%: Emerald Javelin
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ExperimentalCombatItems.EMERALD_JAVELIN.get()));
            }
        }
    }


    @Override
    protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        super.enchantSpawnedWeapon(level, random, difficulty);
        if (random.nextInt(300) == 0) {
            ItemStack itemstack = this.getMainHandItem();
            if (itemstack.is(Items.CROSSBOW)) {
                EnchantmentHelper.enchantItemFromProvider(
                        itemstack, level.registryAccess(), VanillaEnchantmentProviders.PILLAGER_SPAWN_CROSSBOW, difficulty, random
                );
            } else {
                EnchantmentHelper.enchantItemFromProvider(
                        itemstack, level.registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, random
                );
            }
        }
    }

    @Overwrite
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        ItemStack mainHand = this.getMainHandItem();

        if (mainHand.getItem() instanceof JavelinItem) {
            // Throw javelin
            ThrownJavelin thrownJavelin = new ThrownJavelin(this.level(), this, mainHand);
            thrownJavelin.setItemStack(mainHand);

            double dx = target.getX() - this.getX();
            double dy = target.getY(0.333) - thrownJavelin.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            thrownJavelin.shoot(dx, dy + distance * 0.2F, dz,
                    JavelinItem.SHOOT_POWER,
                    14 - this.level().getDifficulty().getId() * 4);
            this.level().addFreshEntity(thrownJavelin);

            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));

        } else if (mainHand.is(Items.TRIDENT)) {
            // Throw trident
            ThrownTrident thrownTrident = new ThrownTrident(this.level(), this, mainHand);

            double dx = target.getX() - this.getX();
            double dy = target.getY(0.333) - thrownTrident.getY();
            double dz = target.getZ() - this.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            thrownTrident.shoot(dx, dy + distance * 0.2F, dz,
                    1.6F,
                    14 - this.level().getDifficulty().getId() * 4);
            this.level().addFreshEntity(thrownTrident);

            this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F,
                    1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        } else {
            // Default crossbow behavior if not holding a throwable
            this.performCrossbowAttack((Pillager)(Object)this, 1.6F);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!IfArmorCanAbsorbHelper.absorbDamageWithArmor(this, damageSource, damageAmount)) {
            super.actuallyHurt(damageSource, damageAmount);
        }
    }

    @Inject(method = "registerGoals", at = @At("RETURN"))
    private void addJavelinAttackGoal(CallbackInfo ci) {
        this.goalSelector.addGoal(3, new JavelinAttackGoal<>((Pillager)(Object)this, 1.0D, 40, 15.0F));
    }
}
