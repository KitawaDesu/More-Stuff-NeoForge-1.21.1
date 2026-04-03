package net.kitawa.more_stuff.util.mixins.mobs.entity;

import net.kitawa.more_stuff.compat.create.items.CreateCompatItems;
import net.kitawa.more_stuff.compat.create.items.util.CreateCompatArmorMaterials;
import net.kitawa.more_stuff.items.ModItems;
import net.kitawa.more_stuff.items.util.ModArmorMaterials;
import net.kitawa.more_stuff.util.tags.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.WolfVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Wolf.class)
public abstract class WolfMixin extends TamableAnimal implements NeutralMob, VariantHolder<Holder<WolfVariant>> {

    protected WolfMixin(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    public boolean more_Stuff_NeoForge_1_21_1$canArmorAbsorb(DamageSource damageSource) {
        return this.getBodyArmorItem().is(ModItemTags.WOLF_ARMOR) && !damageSource.is(DamageTypeTags.BYPASSES_WOLF_ARMOR);
    }

    @Inject(
            method = {"tick"},
            at = {@At("TAIL")}
    )
    public void tick(CallbackInfo ci) {
        this.more_Stuff_NeoForge_1_21_1$turtleHelmetTick();
    }


    /**
     * @author KitawaDesu
     * @reason Add My More Wolf Armor
     */
    @Overwrite
    public boolean hasArmor() {
        return this.getBodyArmorItem().is(ModItemTags.WOLF_ARMOR);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSource, float damageAmount) {
        if (!this.more_Stuff_NeoForge_1_21_1$canArmorAbsorb(damageSource)) {
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
        if (itemstack.is(ModItemTags.WOLF_ARMOR) && this.isOwnedBy(player) && this.getBodyArmorItem().isEmpty() && !this.isBaby()) {
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
