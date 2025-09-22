package net.kitawa.more_stuff.util.mixins.mobs.entity;

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

        if (MoreStuffGeneralConfig.allowLogarithmicArmor) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
        }

        float baseChance = 0.15F * MoreStuffGeneralConfig.naturalArmorMultiplier;
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
        RandomSource random = RandomSource.create();
        switch (slot) {
            case HEAD -> {
                return switch (chance) {
                    case 0 -> random.nextBoolean() ? Items.LEATHER_HELMET : Items.TURTLE_HELMET;
                    case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_HELMET.get() : Items.GOLDEN_HELMET;
                    case 2 -> Items.CHAINMAIL_HELMET;
                    case 3 -> {
                        if (ModList.get().isLoaded("create")) {
                            int roll = random.nextInt(4);
                            yield switch (roll) {
                                case 0 -> Items.IRON_HELMET;
                                case 1 -> ModItems.COPPER_HELMET.get();
                                case 2 -> CreateCompatItems.ZINC_HELMET.get();
                                default -> CreateCompatItems.BRASS_HELMET.get();
                            };
                        } else {
                            yield random.nextBoolean() ? Items.IRON_HELMET : ModItems.COPPER_HELMET.get();
                        }
                    }
                    case 4 -> random.nextBoolean() ? ModItems.EMERALD_HELMET.get() : Items.DIAMOND_HELMET;
                    default -> null;
                };
            }
            case CHEST -> {
                return switch (chance) {
                    case 0 -> random.nextBoolean() ? ModItems.LEATHER_GLIDER.get() : Items.LEATHER_CHESTPLATE;
                    case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_CHESTPLATE.get() : Items.GOLDEN_CHESTPLATE;
                    case 2 -> Items.CHAINMAIL_CHESTPLATE;
                    case 3 -> {
                        if (ModList.get().isLoaded("create")) {
                            int roll = random.nextInt(4);
                            yield switch (roll) {
                                case 0 -> Items.IRON_CHESTPLATE;
                                case 1 -> ModItems.COPPER_CHESTPLATE.get();
                                case 2 -> CreateCompatItems.ZINC_CHESTPLATE.get();
                                default -> CreateCompatItems.BRASS_CHESTPLATE.get();
                            };
                        } else {
                            yield random.nextBoolean() ? Items.IRON_CHESTPLATE : ModItems.COPPER_CHESTPLATE.get();
                        }
                    }
                    case 4 -> random.nextBoolean() ? ModItems.EMERALD_CHESTPLATE.get() : Items.DIAMOND_CHESTPLATE;
                    default -> null;
                };
            }
            case LEGS -> {
                return switch (chance) {
                    case 0 -> Items.LEATHER_LEGGINGS;
                    case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_LEGGINGS.get() : Items.GOLDEN_LEGGINGS;
                    case 2 -> Items.CHAINMAIL_LEGGINGS;
                    case 3 -> {
                        if (ModList.get().isLoaded("create")) {
                            int roll = random.nextInt(4);
                            yield switch (roll) {
                                case 0 -> Items.IRON_LEGGINGS;
                                case 1 -> ModItems.COPPER_LEGGINGS.get();
                                case 2 -> CreateCompatItems.ZINC_LEGGINGS.get();
                                default -> CreateCompatItems.BRASS_LEGGINGS.get();
                            };
                        } else {
                            yield random.nextBoolean() ? Items.IRON_LEGGINGS : ModItems.COPPER_LEGGINGS.get();
                        }
                    }
                    case 4 -> random.nextBoolean() ? ModItems.EMERALD_LEGGINGS.get() : Items.DIAMOND_LEGGINGS;
                    default -> null;
                };
            }
            case FEET -> {
                return switch (chance) {
                    case 0 -> Items.LEATHER_BOOTS;
                    case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_BOOTS.get() : Items.GOLDEN_BOOTS;
                    case 2 -> Items.CHAINMAIL_BOOTS;
                    case 3 -> {
                        if (ModList.get().isLoaded("create")) {
                            int roll = random.nextInt(4);
                            yield switch (roll) {
                                case 0 -> Items.IRON_BOOTS;
                                case 1 -> ModItems.COPPER_BOOTS.get();
                                case 2 -> CreateCompatItems.ZINC_BOOTS.get();
                                default -> CreateCompatItems.BRASS_BOOTS.get();
                            };
                        } else {
                            yield random.nextBoolean() ? Items.IRON_BOOTS : ModItems.COPPER_BOOTS.get();
                        }
                    }
                    case 4 -> random.nextBoolean() ? ModItems.EMERALD_BOOTS.get() : Items.DIAMOND_BOOTS;
                    default -> null;
                };
            }
            default -> {
            }
        }
        return null;
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
        this.enchantSpawnedEquipment(level, EquipmentSlot.MAINHAND, random, (float) MoreStuffGeneralConfig.naturalWeaponEnchantChance, difficulty);
    }

    @Overwrite
    protected void enchantSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
        this.enchantSpawnedEquipment(level, slot, random, (float) MoreStuffGeneralConfig.naturalArmorEnchantChance, difficulty);
    }

    @Unique
    protected void dyeSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
        float threshold = random.nextFloat();
        float chance = random.nextFloat();

        if (chance >= threshold * MoreStuffGeneralConfig.applyArmorDyeingMultiplier) {
            return;
        }

        ItemStack equippedItem = this.getItemBySlot(slot);
        if (!equippedItem.isEmpty() && isDyeable(equippedItem.getItem())) {
            int r = random.nextInt(MoreStuffGeneralConfig.r);
            int g = random.nextInt(MoreStuffGeneralConfig.g);
            int b = random.nextInt(MoreStuffGeneralConfig.b);
            int color = (r << 16) + (g << 8) + b;

            DyedItemColor dyedColor = new DyedItemColor(color, true);
            equippedItem.set(DataComponents.DYED_COLOR, dyedColor);
            this.setItemSlot(slot, equippedItem);
        }
    }

    @Unique
    private boolean isDyeable(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        if (itemId == null) return false;

        Holder<Item> itemHolder = BuiltInRegistries.ITEM.getHolder(itemId).orElse(null);
        if (itemHolder == null) return false;

        return BuiltInRegistries.ITEM.getTag(DYEABLE)
                .map(tag -> tag.contains(itemHolder))
                .orElse(false);
    }

    @Overwrite
    private void enchantSpawnedEquipment(ServerLevelAccessor level, EquipmentSlot slot, RandomSource random, float enchantChance, DifficultyInstance difficulty) {
        float dayMultiplier = 1.0F;
        if (MoreStuffGeneralConfig.allowLogarithmicEnchantments) {
            long dayCount = level.getLevel().getDayTime() / 24000L;
            dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor);
        }

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
