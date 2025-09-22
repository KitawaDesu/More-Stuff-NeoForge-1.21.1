package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatArmorMaterials;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.util.ModArmorMaterials;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.animal.WolfVariants;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static net.minecraft.tags.ItemTags.DYEABLE;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob, VariantHolder<Holder<WolfVariant>> {

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        Holder<WolfVariant> holder1;
        if (spawnGroupData instanceof Wolf.WolfPackData wolf$wolfpackdata) {
            holder1 = wolf$wolfpackdata.type;
        } else {
            holder1 = WolfVariants.getSpawnVariant(this.registryAccess(), holder);
            spawnGroupData = new Wolf.WolfPackData(holder1);
        }

        RandomSource random = level.getRandom();
        populateDefaultEquipmentSlots(random, difficulty);
        populateDefaultEquipmentEnchantments(level, random, difficulty); // You can refactor this method to suit your needs

        this.setVariant(holder1);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Unique
    public boolean canArmorAbsorb(DamageSource damageSource) {
        return this.getBodyArmorItem().is(ModItemTags.ABSORBS_DAMAGE) && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    public void tick(CallbackInfo ci) {
        this.more_Stuff_NeoForge_1_21_1$turtleHelmetTick();
    }

    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        float dayMultiplier = 1.0F;

        // Add logarithmic day scaling if enabled in config
        if (MoreStuffGeneralConfig.allowLogarithmicArmor) {
            long dayCount = this.level().getDayTime() / 24000L;
            dayMultiplier += (float) (Math.log(dayCount + 1) / MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
        }

        // Final spawn chance with all multipliers
        float baseChance = 0.6F * MoreStuffGeneralConfig.naturalArmorMultiplier;
        float finalChance = baseChance * difficulty.getSpecialMultiplier() * dayMultiplier;

        if (random.nextFloat() < finalChance) {
            int i = random.nextInt(2);
            float f = this.level().getDifficulty() == Difficulty.HARD ? 0.1F : 0.25F;

            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;
            if (random.nextFloat() < 0.095F) i++;

            boolean flag = true;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() == EquipmentSlot.Type.ANIMAL_ARMOR) {
                    ItemStack itemstack = this.getItemBySlot(slot);
                    if (!flag && random.nextFloat() < f) break;

                    flag = false;

                    if (itemstack.isEmpty()) {
                        Item item;

                        // Insert custom armor selection logic for BODY slot
                        if (slot == EquipmentSlot.BODY) {
                            item = switch (i) {
                                case 0 -> {
                                    int roll = random.nextInt(3);
                                    yield switch (roll) {
                                        case 0 -> Items.WOLF_ARMOR;
                                        case 1 -> ModItems.LEATHER_WOLF_ARMOR.get();
                                        case 2 -> ModItems.WOOD_PLATE_WOLF_ARMOR.get();
                                        default -> ModItems.TURTLE_SCUTE_WOLF_ARMOR.get();
                                    };
                                }
                                case 1 -> random.nextBoolean() ? ModItems.ROSE_GOLDEN_WOLF_ARMOR.get() : ModItems.GOLDEN_WOLF_ARMOR.get();
                                case 2 -> ModItems.CHAINMAIL_WOLF_ARMOR.get();
                                case 3 -> {
                                    if (ModList.get().isLoaded("create")) {
                                        int roll = random.nextInt(4); // 0–3
                                        yield switch (roll) {
                                            case 0 -> ModItems.IRON_WOLF_ARMOR.get();
                                            case 1 -> ModItems.COPPER_WOLF_ARMOR.get();
                                            case 2 -> CreateCompatItems.ZINC_WOLF_ARMOR.get();
                                            default -> CreateCompatItems.BRASS_WOLF_ARMOR.get();
                                        };
                                    } else {
                                        yield random.nextBoolean() ? ModItems.IRON_WOLF_ARMOR.get() : ModItems.COPPER_WOLF_ARMOR.get();
                                    }
                                }
                                case 4 -> random.nextBoolean() ? ModItems.EMERALD_WOLF_ARMOR.get() : ModItems.DIAMOND_WOLF_ARMOR.get();
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
        float threshold = random.nextFloat(); // Random threshold between 0.0 and 1.0
        float chance = random.nextFloat();    // Actual chance roll

        if (chance >= threshold * MoreStuffGeneralConfig.applyArmorDyeingMultiplier) {
            return; // Exit early if the chance does not meet the random threshold
        }

        ItemStack equippedItem = this.getItemBySlot(slot);

        // Only attempt to dye if the item exists and is dyeable
        if (!equippedItem.isEmpty() && isDyeable(equippedItem.getItem())) {
            int r = random.nextInt(MoreStuffGeneralConfig.r);
            int g = random.nextInt(MoreStuffGeneralConfig.g);
            int b = random.nextInt(MoreStuffGeneralConfig.b);
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


    /**
     * @author KitawaDesu
     * @reason Add My More Wolf Armor
     */
    @Overwrite
    public boolean hasArmor() {
        return this.getBodyArmorItem().is(ModItemTags.ABSORBS_DAMAGE);
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
            if (this.getBodyArmorItem().is(ModItems.LEATHER_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.LEATHER_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(Items.WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, Items.WOLF_ARMOR.getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.WOOD_PLATE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.WOOD_PLATE_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.IRON_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.IRON_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.CHAINMAIL_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.CHAINMAIL_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.GOLDEN_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.GOLDEN_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.COPPER_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.COPPER_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.ROSE_GOLDEN_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.ROSE_GOLDEN_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.EMERALD_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.EMERALD_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.DIAMOND_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.DIAMOND_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.NETHERITE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.NETHERITE_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.ROSARITE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.ROSARITE_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (this.getBodyArmorItem().is(ModItems.TURTLE_SCUTE_WOLF_ARMOR)) {
                if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                    this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                    if (this.level() instanceof ServerLevel serverlevel) {
                        serverlevel.sendParticles(
                                new ItemParticleOption(ParticleTypes.ITEM, ModItems.TURTLE_SCUTE_WOLF_ARMOR.get().getDefaultInstance()),
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
            } else if (ModList.get().isLoaded("create")) {
                if (this.getBodyArmorItem().is(CreateCompatItems.ZINC_WOLF_ARMOR)) {
                    if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                        this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                        if (this.level() instanceof ServerLevel serverlevel) {
                            serverlevel.sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, CreateCompatItems.ZINC_WOLF_ARMOR.get().getDefaultInstance()),
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
                } else if (this.getBodyArmorItem().is(CreateCompatItems.BRASS_WOLF_ARMOR)) {
                    if (Crackiness.WOLF_ARMOR.byDamage(i, j) != Crackiness.WOLF_ARMOR.byDamage(this.getBodyArmorItem())) {
                        this.playSound(SoundEvents.WOLF_ARMOR_CRACK);
                        if (this.level() instanceof ServerLevel serverlevel) {
                            serverlevel.sendParticles(
                                    new ItemParticleOption(ParticleTypes.ITEM, CreateCompatItems.BRASS_WOLF_ARMOR.get().getDefaultInstance()),
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
        }
    }

    @Inject(
            method = {"mobInteract"},
            at = {@At("HEAD")},
            cancellable = true
    )
    public void mobInteract(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(ModItemTags.ABSORBS_DAMAGE) && this.isOwnedBy(player) && this.getBodyArmorItem().isEmpty() && !this.isBaby()) {
            this.setBodyArmorItem(itemstack.copyWithCount(1));
            itemstack.consume(1, player);
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.ARMADILLO.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(Items.WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.ARMADILLO.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && !this.getBodyArmorItem().is(Items.WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            cir.setReturnValue(InteractionResult.FAIL);
        } else if (ArmorMaterials.LEATHER.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.LEATHER_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.WOOD_PLATE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.WOOD_PLATE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.STONE_PLATE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.STONE_PLATE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.IRON.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.IRON_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.GOLD.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.GOLDEN_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.CHAIN.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.CHAINMAIL_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.DIAMOND.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.DIAMOND_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.NETHERITE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.NETHERITE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ArmorMaterials.TURTLE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.TURTLE_SCUTE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.EMERALD.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.EMERALD_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.COPPER.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.COPPER_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.ROSE_GOLDEN.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.ROSE_GOLDEN_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.ROSARITE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.ROSARITE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModArmorMaterials.ROSARITE.value().repairIngredient().get().test(itemstack)
                && this.isInSittingPose()
                && this.hasArmor()
                && this.isOwnedBy(player)
                && this.getBodyArmorItem().is(ModItems.ROSARITE_WOLF_ARMOR)
                && this.getBodyArmorItem().isDamaged()) {
            itemstack.shrink(1);
            this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
            ItemStack itemstack2 = this.getBodyArmorItem();
            int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
            itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else if (ModList.get().isLoaded("create")) {
            if (CreateCompatArmorMaterials.ZINC.value().repairIngredient().get().test(itemstack)
                    && this.isInSittingPose()
                    && this.hasArmor()
                    && this.isOwnedBy(player)
                    && this.getBodyArmorItem().is(CreateCompatItems.ZINC_WOLF_ARMOR)
                    && this.getBodyArmorItem().isDamaged()) {
                itemstack.shrink(1);
                this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
                ItemStack itemstack2 = this.getBodyArmorItem();
                int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
                itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
                cir.setReturnValue(InteractionResult.SUCCESS);
            } else if (CreateCompatArmorMaterials.BRASS.value().repairIngredient().get().test(itemstack)
                    && this.isInSittingPose()
                    && this.hasArmor()
                    && this.isOwnedBy(player)
                    && this.getBodyArmorItem().is(CreateCompatItems.BRASS_WOLF_ARMOR)
                    && this.getBodyArmorItem().isDamaged()) {
                itemstack.shrink(1);
                this.playSound(SoundEvents.WOLF_ARMOR_REPAIR);
                ItemStack itemstack2 = this.getBodyArmorItem();
                int i = (int) ((float) itemstack2.getMaxDamage() * 0.125F);
                itemstack2.setDamageValue(Math.max(0, itemstack2.getDamageValue() - i));
                cir.setReturnValue(InteractionResult.SUCCESS);
            }
        }
    }

    @Unique
    private void more_Stuff_NeoForge_1_21_1$turtleHelmetTick() {
        ItemStack itemstack = this.getItemBySlot(EquipmentSlot.BODY);
        if (itemstack.is(ModItems.TURTLE_SCUTE_WOLF_ARMOR) && !this.isEyeInFluid(FluidTags.WATER)) {
            this.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
        }
    }
}
