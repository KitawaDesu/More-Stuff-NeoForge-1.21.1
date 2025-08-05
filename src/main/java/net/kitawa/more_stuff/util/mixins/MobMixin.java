package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

import static net.minecraft.tags.ItemTags.DYEABLE;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements EquipmentUser, Leashable, Targeting {


    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    private final NonNullList<ItemStack> handItems = NonNullList.withSize(2, ItemStack.EMPTY);
    @Shadow
    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    @Shadow
    private ItemStack bodyArmorItem = ItemStack.EMPTY;

    @Overwrite
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        float dayMultiplier = 1.0F;

        if (MoreStuffGeneralConfig.CONFIG.AllowLogarithmicArmor()) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float)(Math.log(dayCount + 1) / MoreStuffGeneralConfig.CONFIG.LogarithmicArmorScalingFactor()); // ~1.5x at day 1000
        }

        float baseChance = 0.15F * MoreStuffGeneralConfig.CONFIG.naturalArmorMultiplier();
        float finalChance = baseChance * difficulty.getSpecialMultiplier() * dayMultiplier;

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


    @Overwrite
    public static Item getEquipmentForSlot(EquipmentSlot slot, int chance) {
        switch (slot) {
            case HEAD:
                if (chance == 0) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? Items.LEATHER_HELMET : Items.TURTLE_HELMET;
                } else if (chance == 1) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.ROSE_GOLDEN_HELMET.get() : Items.GOLDEN_HELMET;
                } else if (chance == 2) {
                    return Items.CHAINMAIL_HELMET;
                } else if (chance == 3) {
                    if (ModList.get().isLoaded("create")) {
                        RandomSource random = RandomSource.create();
                        int roll = random.nextInt(4); // 0, 1, or 2

                        if (roll == 0) {
                            return Items.IRON_HELMET;
                        } else if (roll == 1) {
                            return ModItems.COPPER_HELMET.get(); // Example custom item
                        } else if (roll == 2) {
                            return CreateCompatItems.ZINC_HELMET.get();
                        } else {
                            return CreateCompatItems.BRASS_HELMET.get();
                        }
                    } else {
                        RandomSource random = RandomSource.create();
                        return random.nextBoolean() ? Items.IRON_HELMET : ModItems.COPPER_HELMET.get();
                    }
                } else if (chance == 4) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.EMERALD_HELMET.get() : Items.DIAMOND_HELMET;
                }
            case CHEST:
                if (chance == 0) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.LEATHER_GLIDER.get() : Items.LEATHER_CHESTPLATE;
                } else if (chance == 1) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.ROSE_GOLDEN_CHESTPLATE.get() : Items.GOLDEN_CHESTPLATE;
                } else if (chance == 2) {
                    return Items.CHAINMAIL_CHESTPLATE;
                } else if (chance == 3) {
                    if (ModList.get().isLoaded("create")) {
                        RandomSource random = RandomSource.create();
                        int roll = random.nextInt(4); // 0, 1, or 2

                        if (roll == 0) {
                            return Items.IRON_CHESTPLATE;
                        } else if (roll == 1) {
                            return ModItems.COPPER_CHESTPLATE.get(); // Example custom item
                        } else if (roll == 2) {
                            return CreateCompatItems.ZINC_CHESTPLATE.get();
                        } else {
                            return CreateCompatItems.BRASS_CHESTPLATE.get();
                        }
                    } else {
                        RandomSource random = RandomSource.create();
                        return random.nextBoolean() ? Items.IRON_CHESTPLATE : ModItems.COPPER_CHESTPLATE.get();
                    }
                } else if (chance == 4) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.EMERALD_CHESTPLATE.get() : Items.DIAMOND_CHESTPLATE;
                }
            case LEGS:
                if (chance == 0) {
                    return Items.LEATHER_LEGGINGS;
                } else if (chance == 1) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.ROSE_GOLDEN_LEGGINGS.get() : Items.GOLDEN_LEGGINGS;
                } else if (chance == 2) {
                    return Items.CHAINMAIL_LEGGINGS;
                } else if (chance == 3) {
                    if (ModList.get().isLoaded("create")) {
                        RandomSource random = RandomSource.create();
                        int roll = random.nextInt(4); // 0, 1, or 2

                        if (roll == 0) {
                            return Items.IRON_LEGGINGS;
                        } else if (roll == 1) {
                            return ModItems.COPPER_LEGGINGS.get(); // Example custom item
                        } else if (roll == 2) {
                            return CreateCompatItems.ZINC_LEGGINGS.get();
                        } else {
                            return CreateCompatItems.BRASS_LEGGINGS.get();
                        }
                    } else {
                        RandomSource random = RandomSource.create();
                        return random.nextBoolean() ? Items.IRON_LEGGINGS : ModItems.COPPER_LEGGINGS.get();
                    }
                } else if (chance == 4) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.EMERALD_LEGGINGS.get() : Items.DIAMOND_LEGGINGS;
                }
            case FEET:
                if (chance == 0) {
                    return Items.LEATHER_BOOTS;
                } else if (chance == 1) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.ROSE_GOLDEN_BOOTS.get() : Items.GOLDEN_BOOTS;
                } else if (chance == 2) {
                    return Items.CHAINMAIL_BOOTS;
                } else if (chance == 3) {
                    if (ModList.get().isLoaded("create")) {
                        RandomSource random = RandomSource.create();
                        int roll = random.nextInt(4); // 0, 1, or 2

                        if (roll == 0) {
                            return Items.IRON_BOOTS;
                        } else if (roll == 1) {
                            return ModItems.COPPER_BOOTS.get(); // Example custom item
                        } else if (roll == 2) {
                            return CreateCompatItems.ZINC_BOOTS.get();
                        } else {
                            return CreateCompatItems.BRASS_BOOTS.get();
                        }
                    } else {
                        RandomSource random = RandomSource.create();
                        return random.nextBoolean() ? Items.IRON_BOOTS : ModItems.COPPER_BOOTS.get();
                    }
                } else if (chance == 4) {
                    RandomSource random = RandomSource.create();
                    return random.nextBoolean() ? ModItems.EMERALD_BOOTS.get() : Items.DIAMOND_BOOTS;
                }
            default:
                return null;
        }
    }

    @Overwrite
    protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        this.enchantSpawnedWeapon(level, random, difficulty);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                this.enchantSpawnedArmor(level, random, slot, difficulty);
                this.dyeSpawnedArmor(level, random, slot, difficulty);
            }
        }

        // Optional: Handle animal armor if this entity supports it
        if (this.getType() == EntityType.WOLF) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                    this.enchantSpawnedArmor(level, random, slot, difficulty);
                    this.dyeSpawnedArmor(level, random, slot, difficulty);
                }
            }
        }
    }

    @Overwrite
    protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        this.enchantSpawnedEquipment(level, EquipmentSlot.MAINHAND, random, MoreStuffGeneralConfig.CONFIG.naturalWeaponEnchantChance(), difficulty);
    }

    @Overwrite
    protected void enchantSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
        this.enchantSpawnedEquipment(level, slot, random,  MoreStuffGeneralConfig.CONFIG.naturalArmorEnchantChance(), difficulty);
    }

    @Unique
    protected void dyeSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
        float threshold = random.nextFloat(); // Random threshold between 0.0 and 1.0
        float chance = random.nextFloat();    // Actual chance roll

        if (chance >= threshold * MoreStuffGeneralConfig.CONFIG.applyArmorDyeingMultiplier()) {
            return; // Exit early if the chance does not meet the random threshold
        }

        ItemStack equippedItem = this.getItemBySlot(slot);

        // Only attempt to dye if the item exists and is dyeable
        if (!equippedItem.isEmpty() && isDyeable(equippedItem.getItem())) {
            int r = random.nextInt(MoreStuffGeneralConfig.CONFIG.R());
            int g = random.nextInt(MoreStuffGeneralConfig.CONFIG.G());
            int b = random.nextInt(MoreStuffGeneralConfig.CONFIG.B());
            int color = (r << 16) + (g << 8) + b;

            DyedItemColor dyedColor = new DyedItemColor(color, true);
            equippedItem.set(DataComponents.DYED_COLOR, dyedColor);

            // Re-apply the dyed item to the slot
            this.setItemSlot(slot, equippedItem);
        }
    }

    @Unique
    private boolean isDyeable(Item item) {
        // Get the ResourceLocation for the item
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);

        if (itemId == null) {
            return false; // If the item doesn't have a valid registry key, it's not dyeable
        }

        // Retrieve the Holder<Item> for the item
        Holder<Item> itemHolder = BuiltInRegistries.ITEM.getHolder(itemId).orElse(null);

        if (itemHolder == null) {
            return false; // No holder found, return false
        }

        // Get the tag for "dyeable" items and check if the item is in the tag
        return BuiltInRegistries.ITEM.getTag(DYEABLE)
                .map(tag -> tag.contains(itemHolder))
                .orElse(false);
    }


    @Overwrite
    private void enchantSpawnedEquipment(
            ServerLevelAccessor level, EquipmentSlot slot, RandomSource random, float enchantChance, DifficultyInstance difficulty
    ) {
        // Get day-based multiplier
        float dayMultiplier = 1.0F;
        if (MoreStuffGeneralConfig.CONFIG.AllowLogarithmicEnchantments()) {
            long dayCount = level.getLevel().getDayTime() / 24000L;
            dayMultiplier += (float)(Math.log(dayCount + 1) / MoreStuffGeneralConfig.CONFIG.LogarithmicEnchantmentScalingFactor()); // ~2.5x at day 1000
        }

        // Apply all multipliers to final enchant chance
        float finalChance = enchantChance * difficulty.getSpecialMultiplier() * dayMultiplier;

        ItemStack itemstack = this.getItemBySlot(slot);
        if (!itemstack.isEmpty() && random.nextFloat() < finalChance) {
            EnchantmentHelper.enchantItemFromProvider(
                    itemstack, level.registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, random
            );
            this.setItemSlot(slot, itemstack);
        }
    }

    @Shadow
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch (slot.getType()) {
            case HAND -> (ItemStack) this.handItems.get(slot.getIndex());
            case HUMANOID_ARMOR -> (ItemStack) this.armorItems.get(slot.getIndex());
            case ANIMAL_ARMOR -> this.bodyArmorItem;
        };
    }

    @Shadow
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.verifyEquippedItem(stack);
        switch (slot.getType()) {
            case HAND:
                this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
                break;
            case HUMANOID_ARMOR:
                this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
                break;
            case ANIMAL_ARMOR:
                ItemStack itemstack = this.bodyArmorItem;
                this.bodyArmorItem = stack;
                this.onEquipItem(slot, itemstack, stack);
        }
    }
}
