package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;

import java.util.ArrayList;
import java.util.List;

@Mixin(Vindicator.class)
public abstract class VindicatorMixin extends AbstractIllager {
    protected VindicatorMixin(EntityType<? extends AbstractIllager> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        if (this.getCurrentRaid() == null) {
            List<Item> weaponChoices = new ArrayList<>();

            // Base items
            weaponChoices.add(Items.IRON_AXE);
            weaponChoices.add(ModItems.COPPER_AXE.get());

            // Add Create mod items if loaded
            if (ModList.get().isLoaded("create")) {
                weaponChoices.add(Items.IRON_AXE);
                weaponChoices.add(ModItems.COPPER_AXE.get());
                weaponChoices.add(CreateCompatItems.ZINC_AXE.get());
                weaponChoices.add(CreateCompatItems.BRASS_AXE.get());
            }

            // Always assign a weapon or tool
            if (random.nextBoolean()) {
                Item chosenWeapon = weaponChoices.get(random.nextInt(weaponChoices.size()));
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(chosenWeapon));
            }
        }
    }
}
