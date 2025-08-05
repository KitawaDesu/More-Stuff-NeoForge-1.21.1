package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;

import javax.annotation.Nullable;

@Mixin(AbstractVillager.class)
public abstract class VillagerMixin extends AgeableMob implements InventoryCarrier, Npc, Merchant {

    protected VillagerMixin(EntityType<? extends AgeableMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (spawnGroupData == null) {
            spawnGroupData = new AgeableMob.AgeableMobGroupData(false);
        }
        RandomSource random = level.getRandom();
        populateDefaultEquipmentSlots(random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        float dayMultiplier = 1.0F;

        // Add day-based logarithmic scaling if enabled in config
        if (MoreStuffGeneralConfig.CONFIG.AllowLogarithmicArmor()) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float)(Math.log(dayCount + 1) / 13.816); // ~1.5x at day 1000
        }

        // Combine all multipliers for final spawn chance
        float baseChance = 0.15F * MoreStuffGeneralConfig.CONFIG.naturalArmorMultiplier();
        float finalChance = baseChance * difficulty.getSpecialMultiplier() * MoreStuffGeneralConfig.CONFIG.villagerArmorMultiplier() * dayMultiplier;

        if (random.nextFloat() < finalChance) {
            int i = random.nextInt(2);
            float f = this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;

            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;

            boolean flag = true;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(slot);
                    if (!flag && random.nextFloat() < f) break;

                    flag = false;

                    if (itemstack.isEmpty()) {
                        Item item = getEquipmentForSlot(slot, i);
                        if (item != null) {
                            this.setItemSlot(slot, new ItemStack(item));
                        }
                    }
                }
            }
        }
    }

}
