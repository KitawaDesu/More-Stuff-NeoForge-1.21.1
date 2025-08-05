package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(Witch.class)
public abstract class WitchMixin extends Raider implements RangedAttackMob {
    protected WitchMixin(EntityType<? extends Raider> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        RandomSource random = level.getRandom();

        // Get the raid instance if it exists
        if ((Object)this instanceof Raider raider) {
            Raid raid = raider.getCurrentRaid();
            if (raid != null) {
                int wave = raid.getGroupsSpawned(); // wave number starts at 1

                // Customize armor based on wave
                equipWaveBasedArmor(wave, raider, random);
            }
        }

        populateDefaultEquipmentSlots(random, difficulty); // You can still call this if you want base equipment
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Unique
    private void equipWaveBasedArmor(int wave, Raider raider, RandomSource random) {
        switch (wave) {
            case 0 -> equipArmorWithChances(raider, random, 0.1f, 0f, 0f, 0f, 0f);
            case 2 -> equipArmorWithChances(raider, random, 0.3f, 0.1f, 0f, 0f, 0f);
            case 3 -> equipArmorWithChances(raider, random, 0.4f, 0.2f, 0.1f, 0f, 0f);
            case 4 -> equipArmorWithChances(raider, random, 0.6f, 0.4f, 0.3f, 0.15f, 0f);
            case 5 -> equipArmorWithChances(raider, random, 0.9f, 0.7f, 0.6f, 0.35f, 0.10f);
            case 6 -> equipArmorWithChances(raider, random, 1.0f, 0.9f, 0.75f, 0.55f, 0.30f);
            case 7 -> equipArmorWithChances(raider, random, 0f, 1.0f, 0.95f, 0.75f, 0.50f);
            case 8 -> equipArmorWithChances(raider, random, 0f, 0f, 1.0f, 0.95f, 0.75f);
            case 9 -> equipArmorWithChances(raider, random, 0f, 0f, 0f, 1.0f, 0.90f);
            case 10 -> equipArmorWithChances(raider, random, 0f, 0f, 0f, 0f, 1.0f);
            default -> {
                // Beyond wave 10 (scale manually or max out)
                equipArmorWithChances(raider, random, 0f, 0f, 0f, 0f, 1.0f);
            }
        }

        // Prevent drops
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                raider.setDropChance(slot, 0.0F);
            }
        }
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
                raider.setItemSlot(slot, new ItemStack(armorItem));
            }
        }
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

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
    }
}
