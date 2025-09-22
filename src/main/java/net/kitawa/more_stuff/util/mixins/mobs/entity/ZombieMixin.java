package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.helpers.IfArmorCanAbsorbHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(Zombie.class)
public abstract class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        if (random.nextFloat() < (this.level().getDifficulty() == Difficulty.HARD ? 0.5F : 0.1F)) {
            List<Item> weaponChoices = new ArrayList<>();
            List<Item> toolChoices = new ArrayList<>();

            // Base items
            weaponChoices.add(Items.IRON_SWORD);
            weaponChoices.add(ModItems.COPPER_SWORD.get());
            toolChoices.add(Items.IRON_SHOVEL);
            toolChoices.add(ModItems.COPPER_SHOVEL.get());

            // Add Create mod items if loaded
            if (ModList.get().isLoaded("create")) {
                weaponChoices.add(Items.IRON_SWORD);
                weaponChoices.add(ModItems.COPPER_SWORD.get());
                toolChoices.add(Items.IRON_SHOVEL);
                toolChoices.add(ModItems.COPPER_SHOVEL.get());
                weaponChoices.add(CreateCompatItems.ZINC_SWORD.get());
                weaponChoices.add(CreateCompatItems.BRASS_SWORD.get());
                toolChoices.add(CreateCompatItems.ZINC_SHOVEL.get());
                toolChoices.add(CreateCompatItems.BRASS_SHOVEL.get());
            }

            // Randomly decide between weapon (0) or tool (1)
            if (random.nextBoolean()) {
                Item chosenWeapon = weaponChoices.get(random.nextInt(weaponChoices.size()));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(chosenWeapon));
            } else {
                Item chosenTool = toolChoices.get(random.nextInt(toolChoices.size()));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(chosenTool));
            }
        }
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!IfArmorCanAbsorbHelper.absorbDamageWithArmor(this, damageSource, damageAmount)) {
            super.actuallyHurt(damageSource, damageAmount);
        }
    }

}
