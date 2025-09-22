package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.helpers.IfArmorCanAbsorbHelper;
import net.kitawa.more_stuff.worldgen.biome.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PiglinBrute.class)
public abstract class PiglinBruteMixin extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {
    public PiglinBruteMixin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        ResourceKey<Biome> biomeKey = this.level().getBiome(this.blockPosition()).unwrapKey().orElse(null);
        boolean inFrozenValley = biomeKey != null && biomeKey.equals(ModBiomes.FROZEN_VALLEY);

        // Injected biome-based override: Piglins in FROZEN_VALLEY always get leather boots
        if (inFrozenValley) {
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        }
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
        this.SpawnWithRoseGoldAxeChance(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ROSE_GOLDEN_AXE.get()), random);
    }

    @Unique
    private void SpawnWithRoseGoldAxeChance(EquipmentSlot slot, ItemStack stack, RandomSource random) {
        if (random.nextFloat() < 0.5F) {
            this.setItemSlot(slot, stack);
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!IfArmorCanAbsorbHelper.absorbDamageWithArmor(this, damageSource, damageAmount)) {
            super.actuallyHurt(damageSource, damageAmount);
        }
    }
}
