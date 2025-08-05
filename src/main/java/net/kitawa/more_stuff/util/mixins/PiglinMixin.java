package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.worldgen.biome.ModBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Piglin.class)
public abstract class PiglinMixin extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {
    public PiglinMixin(EntityType<? extends AbstractPiglin> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        ResourceKey<Biome> biomeKey = this.level().getBiome(this.blockPosition()).unwrapKey().orElse(null);
        boolean inFrozenValley = biomeKey != null && biomeKey.equals(ModBiomes.FROZEN_VALLEY);

        // === Calculate Day Multiplier ===
        float dayMultiplier = 1.0F;
        if (MoreStuffGeneralConfig.CONFIG.AllowLogarithmicArmor()) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.CONFIG.LogarithmicArmorScalingFactor());
        }

        // === Feet Armor ===
        if (inFrozenValley) {
            this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        } else if (this.isAdult()) {
            RandomSource randomSource = RandomSource.create();
            Item selectedBoots = randomSource.nextBoolean() ? Items.GOLDEN_BOOTS : ModItems.ROSE_GOLDEN_BOOTS.get();
            this.maybeWearArmor(EquipmentSlot.FEET, new ItemStack(selectedBoots), randomSource, dayMultiplier);
        }

        // === Other Armor + Weapon ===
        if (this.isAdult()) {
            RandomSource randomSource = RandomSource.create();
            Item helmet = randomSource.nextBoolean() ? Items.GOLDEN_HELMET : ModItems.ROSE_GOLDEN_HELMET.get();
            Item chestplate = randomSource.nextBoolean() ? Items.GOLDEN_CHESTPLATE : ModItems.ROSE_GOLDEN_CHESTPLATE.get();
            Item leggings = randomSource.nextBoolean() ? Items.GOLDEN_LEGGINGS : ModItems.ROSE_GOLDEN_LEGGINGS.get();

            this.SpawnWithRoseGoldSwordChance(EquipmentSlot.MAINHAND, new ItemStack(ModItems.ROSE_GOLDEN_SWORD.get()), random, dayMultiplier);
            this.maybeWearArmor(EquipmentSlot.HEAD, new ItemStack(helmet), randomSource, dayMultiplier);
            this.maybeWearArmor(EquipmentSlot.CHEST, new ItemStack(chestplate), randomSource, dayMultiplier);
            this.maybeWearArmor(EquipmentSlot.LEGS, new ItemStack(leggings), randomSource, dayMultiplier);
        }
    }

    @Unique
    private void maybeWearArmor(EquipmentSlot slot, ItemStack stack, RandomSource random, float dayMultiplier) {
        float chance = 0.05F * MoreStuffGeneralConfig.CONFIG.naturalArmorMultiplier() * dayMultiplier;
        if (random.nextFloat() < chance) {
            this.setItemSlot(slot, stack);
        }
    }

    @Unique
    private void SpawnWithRoseGoldSwordChance(EquipmentSlot slot, ItemStack stack, RandomSource random, float dayMultiplier) {
        float chance = 0.33F * dayMultiplier;
        if (random.nextFloat() < chance) {
            this.setItemSlot(slot, stack);
        }
    }

}
