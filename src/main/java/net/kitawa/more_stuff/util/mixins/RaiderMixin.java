package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.util.records.ArmorChances;
import net.kitawa.more_stuff.util.records.ModEnchantmentUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.util.Map;

import static net.minecraft.tags.ItemTags.DYEABLE;

@Mixin(Raider.class)
public abstract class RaiderMixin extends PatrollingMonster {
    @Shadow
    private boolean canJoinRaid;

    protected RaiderMixin(EntityType<? extends PatrollingMonster> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.setCanJoinRaid(this.getType() != EntityType.WITCH || spawnType != MobSpawnType.NATURAL);
        RandomSource random = level.getRandom();

        if ((Object)this instanceof Raider raider) {
            Raid raid = raider.getCurrentRaid();
            if (raid != null) {
                int wave = raid.getGroupsSpawned();
                equipWaveBasedArmor(wave, raider, random);
            } else {
                populateDefaultEquipmentSlots(random, difficulty);
            }
        } else {
            populateDefaultEquipmentSlots(random, difficulty);
        }

        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Unique
    private void equipWaveBasedArmor(int wave, Raider raider, RandomSource random) {
        ArmorChances base = getBaseChances(wave);
        Difficulty difficulty = raider.level().getDifficulty();
        ArmorChances adjusted = applyDifficultyModifiers(difficulty, base);

        equipArmorWithChances(
                raider, random,
                adjusted.leather(),
                adjusted.gold(),
                adjusted.chain(),
                adjusted.iron(),
                adjusted.diamond()
        );

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                raider.setDropChance(slot, 0.0F);
            }
        }
    }

    @Unique
    private ArmorChances getBaseChances(int wave) {
        return switch (wave) {
            case 0 -> new ArmorChances(0.1f, 0f, 0f, 0f, 0f);
            case 2 -> new ArmorChances(0.3f, 0.1f, 0f, 0f, 0f);
            case 3 -> new ArmorChances(0.4f, 0.2f, 0.1f, 0f, 0f);
            case 4 -> new ArmorChances(0.6f, 0.4f, 0.3f, 0.15f, 0f);
            case 5 -> new ArmorChances(0.9f, 0.7f, 0.6f, 0.35f, 0.10f);
            case 6 -> new ArmorChances(1.0f, 0.9f, 0.75f, 0.55f, 0.30f);
            case 7 -> new ArmorChances(0f, 1.0f, 0.95f, 0.75f, 0.50f);
            case 8 -> new ArmorChances(0f, 0f, 1.0f, 0.95f, 0.75f);
            case 9 -> new ArmorChances(0f, 0f, 0f, 1.0f, 0.90f);
            default -> new ArmorChances(0f, 0f, 0f, 0f, 1.0f);
        };
    }

    @Unique
    private ArmorChances applyDifficultyModifiers(Difficulty difficulty, ArmorChances base) {
        return base;
    }

    @Unique
    private void equipArmorWithChances(Raider raider, RandomSource random, float leatherChance, float goldChance, float chainChance, float ironChance, float diamondChance) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) continue;

            float roll = random.nextFloat();
            Item armorItem = null;

            // Calculate cumulative probabilities
            float cumulative = 0f;

            cumulative += leatherChance;
            if (roll < cumulative) {
                armorItem = getLeather(slot, random);
            } else {
                cumulative += goldChance;
                if (roll < cumulative) {
                    armorItem = getGold(slot, random);
                } else {
                    cumulative += chainChance;
                    if (roll < cumulative) {
                        armorItem = getChainmail(slot, random);
                    } else {
                        cumulative += ironChance;
                        if (roll < cumulative) {
                            armorItem = getIron(slot, random);
                        } else {
                            cumulative += diamondChance;
                            if (roll < cumulative) {
                                armorItem = getDiamond(slot, random);
                            }
                        }
                    }
                }
            }

            if (armorItem != null) {
                ItemStack armor = new ItemStack(armorItem);
                raider.setItemSlot(slot, armor); // must be set before enchantment

                // Enchant the armor if conditions are right
                if (raider.level() instanceof ServerLevel serverLevel) {
                    Raid raid = raider.getCurrentRaid();
                    DifficultyInstance difficulty = serverLevel.getCurrentDifficultyAt(raider.blockPosition());

                    ModEnchantmentUtils.maybeEnchantSpawnedItem(
                            raider,
                            serverLevel,
                            slot,
                            random,
                            getEnchantChanceForWave(raid, slot),
                            difficulty
                    );

                    // Dye (only if the item is dyeable and chance is met)
                    dyeSpawnedArmor(serverLevel, random, slot, difficulty);
                }
            }
        }
    }

    @Unique
    private float getEnchantChanceForWave(@Nullable Raid raid, EquipmentSlot slot) {
        if (raid == null || slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR) return 0f;
        int wave = raid.getGroupsSpawned();
        return Math.min(wave * 0.08f, 1.0f); // Tune as needed
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

    @Unique
    private Item getLeather(EquipmentSlot slot, RandomSource random) {
        return switch (slot) {
            case HEAD -> random.nextBoolean() ? Items.LEATHER_HELMET : Items.TURTLE_HELMET;
            case CHEST -> random.nextBoolean() ? Items.LEATHER_CHESTPLATE : ModItems.LEATHER_GLIDER.get();
            case LEGS -> Items.LEATHER_LEGGINGS;
            case FEET -> Items.LEATHER_BOOTS;
            default -> null;
        };
    }

    @Unique
    private Item getGold(EquipmentSlot slot, RandomSource random) {
        return switch (slot) {
            case HEAD -> random.nextBoolean() ? Items.GOLDEN_HELMET : ModItems.ROSE_GOLDEN_HELMET.get();
            case CHEST -> random.nextBoolean() ? Items.GOLDEN_CHESTPLATE : ModItems.ROSE_GOLDEN_CHESTPLATE.get();
            case LEGS -> random.nextBoolean() ? Items.GOLDEN_LEGGINGS : ModItems.ROSE_GOLDEN_LEGGINGS.get();
            case FEET -> random.nextBoolean() ? Items.GOLDEN_BOOTS : ModItems.ROSE_GOLDEN_BOOTS.get();
            default -> null;
        };
    }

    @Unique
    private Item getChainmail(EquipmentSlot slot, RandomSource random) {
        return switch (slot) {
            case HEAD -> Items.CHAINMAIL_HELMET;
            case CHEST -> Items.CHAINMAIL_CHESTPLATE;
            case LEGS -> Items.CHAINMAIL_LEGGINGS;
            case FEET -> Items.CHAINMAIL_BOOTS;
            default -> null;
        };
    }

    @Unique
    private Item getIron(EquipmentSlot slot, RandomSource random) {
        if (ModList.get().isLoaded("create")) {
            int roll = random.nextInt(4); // 0, 1, 2, or 3

            switch (slot) {
                case HEAD -> {
                    return switch (roll) {
                        case 0 -> Items.IRON_HELMET;
                        case 1 -> ModItems.COPPER_HELMET.get();
                        case 2 -> CreateCompatItems.ZINC_HELMET.get();
                        default -> CreateCompatItems.BRASS_HELMET.get();
                    };
                }
                case CHEST -> {
                    return switch (roll) {
                        case 0 -> Items.IRON_CHESTPLATE;
                        case 1 -> ModItems.COPPER_CHESTPLATE.get();
                        case 2 -> CreateCompatItems.ZINC_CHESTPLATE.get();
                        default -> CreateCompatItems.BRASS_CHESTPLATE.get();
                    };
                }
                case LEGS -> {
                    return switch (roll) {
                        case 0 -> Items.IRON_LEGGINGS;
                        case 1 -> ModItems.COPPER_LEGGINGS.get();
                        case 2 -> CreateCompatItems.ZINC_LEGGINGS.get();
                        default -> CreateCompatItems.BRASS_LEGGINGS.get();
                    };
                }
                case FEET -> {
                    return switch (roll) {
                        case 0 -> Items.IRON_BOOTS;
                        case 1 -> ModItems.COPPER_BOOTS.get();
                        case 2 -> CreateCompatItems.ZINC_BOOTS.get();
                        default -> CreateCompatItems.BRASS_BOOTS.get();
                    };
                }
                default -> {
                    return null;
                }
            }
        } else {
            // If Create mod is not loaded, fallback to the original behavior
            return switch (slot) {
                case HEAD -> random.nextBoolean() ? Items.IRON_HELMET : ModItems.COPPER_HELMET.get();
                case CHEST -> random.nextBoolean() ? Items.IRON_CHESTPLATE : ModItems.COPPER_CHESTPLATE.get();
                case LEGS -> random.nextBoolean() ? Items.IRON_LEGGINGS : ModItems.COPPER_LEGGINGS.get();
                case FEET -> random.nextBoolean() ? Items.IRON_BOOTS : ModItems.COPPER_BOOTS.get();
                default -> null;
            };
        }
    }

    @Unique
    private Item getDiamond(EquipmentSlot slot, RandomSource random) {
        return switch (slot) {
            case HEAD -> random.nextBoolean() ? Items.DIAMOND_HELMET : ModItems.EMERALD_HELMET.get();
            case CHEST -> random.nextBoolean() ? Items.DIAMOND_CHESTPLATE : ModItems.EMERALD_CHESTPLATE.get();
            case LEGS -> random.nextBoolean() ? Items.DIAMOND_LEGGINGS : ModItems.EMERALD_LEGGINGS.get();
            case FEET -> random.nextBoolean() ? Items.DIAMOND_BOOTS : ModItems.EMERALD_BOOTS.get();
            default -> null;
        };
    }

    @Unique
    public void setCanJoinRaid(boolean canJoinRaid) {
        this.canJoinRaid = canJoinRaid;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
    }
}
