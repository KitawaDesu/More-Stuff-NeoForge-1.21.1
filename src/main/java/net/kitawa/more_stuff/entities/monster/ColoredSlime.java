package net.kitawa.more_stuff.entities.monster;

import com.google.common.collect.Maps;
import net.kitawa.more_stuff.entities.ModEntities;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ColoredSlime extends Slime {

    // All 16 Minecraft colors + "normal"
    public static final List<String> COLOR_NAMES = List.of(
            "white",
            "orange",
            "magenta",
            "light_blue",
            "yellow",
            "lime",
            "pink",
            "gray",
            "light_gray",
            "cyan",
            "purple",
            "blue",
            "brown",
            "green",
            "red",
            "black",
            "clear",
            "tinted",
            "null"
    );

    private static final EntityDataAccessor<String> COLOR =
            SynchedEntityData.defineId(ColoredSlime.class, EntityDataSerializers.STRING);

    public ColoredSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, "null");  // default color
    }

    public void setColorName(String name) {
        if (COLOR_NAMES.contains(name)) {
            this.entityData.set(COLOR, name);
        }
    }

    public String getColorName() {
        return this.entityData.get(COLOR);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("ColorName", this.getColorName());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("ColorName")) {
            setColorName(tag.getString("ColorName"));
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    public void remove(RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public Component getDisplayName() {
        // If it has a custom name set by player, use that
        if (this.hasCustomName()) return super.getDisplayName();

        String color = this.getColorName();
        if (color.equals("null") || color.isEmpty()) {
            return Component.translatable("entity.more_stuff.colored_slime");
        }

        // e.g. "entity.more_stuff.colored_slime.white" -> "White Slime"
        return Component.translatable("entity.more_stuff.colored_slime." + color);
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance difficulty,
            MobSpawnType type,
            @Nullable SpawnGroupData data
    ) {
        // --- Never choose "null" when naturally spawned ---
        var validColors = COLOR_NAMES.stream()
                .filter(c -> !c.equals("null"))
                .toList();

        String random = validColors.get(level.getRandom().nextInt(validColors.size()));
        setColorName(random);

        return super.finalizeSpawn(level, difficulty, type, data);
    }

    public void copyFrom(ColoredSlime parent) {
        // Copy base Slime properties if needed
        // For example, position, size, effects, etc.
        // But most of these are handled by the entity system during split.
        this.setColorName(parent.getColorName());
        // Copy other custom data if you have any
    }
}