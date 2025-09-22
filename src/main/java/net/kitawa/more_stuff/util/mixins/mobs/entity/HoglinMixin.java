package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.kitawa.more_stuff.worldgen.biome.ModBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.hoglin.HoglinBase;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.*;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

import static net.minecraft.tags.ItemTags.DYEABLE;

@Mixin(Hoglin.class)
public abstract class HoglinMixin extends Animal implements Enemy, HoglinBase {
    protected HoglinMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (level.getRandom().nextFloat() < 0.2F) {
            this.setBaby(true);
        }
        populateDefaultEquipmentSlots(random, difficulty);
        populateDefaultEquipmentEnchantments(level, random, difficulty);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Unique
    public boolean canArmorAbsorb(DamageSource damageSource) {
        return this.getBodyArmorItem().is(ModItemTags.ABSORBS_DAMAGE) && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        ResourceKey<Biome> biomeKey = this.level().getBiome(this.blockPosition()).unwrapKey().orElse(null);
        boolean inFrozenValley = biomeKey != null && biomeKey.equals(ModBiomes.FROZEN_VALLEY);

        boolean inBastion = false;
        if (this.level() instanceof ServerLevel serverLevel) {
            StructureManager structureManager = serverLevel.structureManager();
            Structure bastionStructure = serverLevel.registryAccess()
                    .registryOrThrow(Registries.STRUCTURE)
                    .get(BuiltinStructures.BASTION_REMNANT);

            if (bastionStructure != null) {
                StructureStart structureStart = structureManager.getStructureAt(this.blockPosition(), bastionStructure);
                inBastion = structureStart != StructureStart.INVALID_START && structureStart.isValid();
            }
        }

        if (inFrozenValley) {
            this.setItemSlot(EquipmentSlot.BODY, new ItemStack(ModItems.LEATHER_HOGLIN_ARMOR.get()));
            return;
        }

        if (inBastion) {
            float dayMultiplier = 1.0F;
            if (MoreStuffGeneralConfig.allowLogarithmicArmor) {
                long dayCount = this.level().getDayTime() / 24000L;
                dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
            }

            float baseChance = 0.6F * MoreStuffGeneralConfig.naturalArmorMultiplier;
            float finalChance = baseChance * difficulty.getSpecialMultiplier() * dayMultiplier;

            if (random.nextFloat() < finalChance) {
                int i = random.nextInt(4);
                float f = this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;

                if (random.nextFloat() < 0.095F) i++;
                if (random.nextFloat() < 0.095F) i++;
                if (random.nextFloat() < 0.095F) i++;

                boolean flag = true;

                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                        ItemStack current = this.getItemBySlot(slot);
                        if (!flag && random.nextFloat() < f) break;
                        flag = false;

                        if (current.isEmpty()) {
                            Item item;

                            if (slot == EquipmentSlot.BODY) {
                                item = switch (i) {
                                    case 0 -> {
                                        int roll = random.nextInt(3);
                                        yield switch (roll) {
                                            case 0 -> ModItems.HOGLIN_ARMOR.get();
                                            case 1 -> ModItems.LEATHER_HOGLIN_ARMOR.get();
                                            default -> ModItems.TURTLE_SCUTE_HOGLIN_ARMOR.get();
                                        };
                                    }
                                    case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_HOGLIN_ARMOR.get() : ModItems.GOLDEN_HOGLIN_ARMOR.get();
                                    case 2 -> ModItems.CHAINMAIL_HOGLIN_ARMOR.get();
                                    case 3 -> {
                                        if (ModList.get().isLoaded("create")) {
                                            int roll = random.nextInt(4);
                                            yield switch (roll) {
                                                case 0 -> ModItems.IRON_HOGLIN_ARMOR.get();
                                                case 1 -> ModItems.COPPER_HOGLIN_ARMOR.get();
                                                case 2 -> CreateCompatItems.ZINC_HOGLIN_ARMOR.get();
                                                default -> CreateCompatItems.BRASS_HOGLIN_ARMOR.get();
                                            };
                                        } else {
                                            yield random.nextBoolean() ? ModItems.IRON_HOGLIN_ARMOR.get() : ModItems.COPPER_HOGLIN_ARMOR.get();
                                        }
                                    }
                                    case 4 -> random.nextBoolean() ? ModItems.EMERALD_HOGLIN_ARMOR.get() : ModItems.DIAMOND_HOGLIN_ARMOR.get();
                                    case 5 -> random.nextBoolean() ? ModItems.ROSARITE_HOGLIN_ARMOR.get() : ModItems.NETHERITE_HOGLIN_ARMOR.get();
                                    default -> null;
                                };
                            } else {
                                item = getEquipmentForSlot(slot, i);
                            }

                            if (item != null) {
                                this.setItemSlot(slot, new ItemStack(item));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
        this.enchantSpawnedWeapon(level, random, difficulty);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                this.enchantSpawnedArmor(level, random, slot, difficulty);
                this.dyeSpawnedArmor(level, random, slot, difficulty);
            }
        }
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

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.canArmorAbsorb(damageSource)) {
            super.actuallyHurt(damageSource, damageAmount);
        } else {
            ItemStack itemstack = this.getBodyArmorItem();
            int i = itemstack.getDamageValue();
            int j = itemstack.getMaxDamage();
            itemstack.hurtAndBreak(Mth.ceil(damageAmount), this, EquipmentSlot.BODY);
            if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                if (this.level() instanceof ServerLevel serverlevel) {
                    serverlevel.sendParticles(
                            new ItemParticleOption(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE.getDefaultInstance()),
                            this.getX(),
                            this.getY() + 1.0,
                            this.getZ(),
                            20,
                            0.2,
                            0.1,
                            0.2,
                            0.1
                    );
                }
            }
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.canArmorAbsorb(damageSource) ? SoundEvents.WOLF_ARMOR_DAMAGE : SoundEvents.HOGLIN_HURT;
    }

    @Unique
    public boolean hasArmor() {
        return this.getBodyArmorItem().is(ModItemTags.ABSORBS_DAMAGE);
    }
}
