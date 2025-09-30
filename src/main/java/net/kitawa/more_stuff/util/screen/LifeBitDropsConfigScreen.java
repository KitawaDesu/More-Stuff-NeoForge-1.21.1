package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LifeBitDropsConfigScreen extends Screen {

    private final Screen parent;

    private static final int START_Y = 40;
    private static final int ROW_HEIGHT = 24;
    private static final int COLUMN_WIDTH = 200;
    private static final int COLUMN_GAP = 20;

    private final List<WidgetWrapper> widgets = new ArrayList<>();

    public LifeBitDropsConfigScreen(Screen parent) {
        super(Component.literal("Life Bit Drops Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        widgets.clear();

        int leftX = this.width / 2 - COLUMN_WIDTH - COLUMN_GAP / 2;
        int rightX = this.width / 2 + COLUMN_GAP / 2;
        int y = START_Y;

        // --- Sliders ---
        ConfigEntry[] entries = new ConfigEntry[]{
                new ConfigEntry("Armor Bonus %", LifeBitDropsConfig.armorBonus, 0, 10, v -> LifeBitDropsConfig.armorBonus = v),
                new ConfigEntry("Toughness Bonus %", LifeBitDropsConfig.toughnessBonus, 0, 10, v -> LifeBitDropsConfig.toughnessBonus = v),
                new ConfigEntry("Minimum Drop %", LifeBitDropsConfig.minimumDrop, 0, 1, v -> LifeBitDropsConfig.minimumDrop = v),
                new ConfigEntry("Biased Towards %", LifeBitDropsConfig.biasedTowards, 0, 1, v -> LifeBitDropsConfig.biasedTowards = v),
                new ConfigEntry("Mob Attack Bonus", LifeBitDropsConfig.mobAttackBonus, 0, 10, v -> LifeBitDropsConfig.mobAttackBonus = v),
                new ConfigEntry("Player Health Penalty", LifeBitDropsConfig.playerHealthPenalty, 0, 10, v -> LifeBitDropsConfig.playerHealthPenalty = v),
                new ConfigEntry("Player Armor Penalty", LifeBitDropsConfig.playerArmorPenalty, 0, 10, v -> LifeBitDropsConfig.playerArmorPenalty = v),
                new ConfigEntry("Player Toughness Penalty", LifeBitDropsConfig.playerToughnessPenalty, 0, 10, v -> LifeBitDropsConfig.playerToughnessPenalty = v),
                new ConfigEntry("Player Attack Damage Penalty", LifeBitDropsConfig.playerAttackDamagePenalty, 0, 10, v -> LifeBitDropsConfig.playerAttackDamagePenalty = v),
                new ConfigEntry("Player Health Threshold", LifeBitDropsConfig.playerHealthThreshold, 0, 1024, v -> LifeBitDropsConfig.playerHealthThreshold = v),
                new ConfigEntry("Player Armor Threshold", LifeBitDropsConfig.playerArmorThreshold, 0, 1024, v -> LifeBitDropsConfig.playerArmorThreshold = v),
                new ConfigEntry("Player Toughness Threshold", LifeBitDropsConfig.playerToughnessThreshold, 0, 1024, v -> LifeBitDropsConfig.playerToughnessThreshold = v),
                new ConfigEntry("Player Attack Damage Threshold", LifeBitDropsConfig.playerAttackDamageThreshold, 0, 1024, v -> LifeBitDropsConfig.playerAttackDamageThreshold = v)
        };

        for (int i = 0; i < entries.length; i++) {
            ConfigEntry entry = entries[i];
            int x = (i % 2 == 0) ? leftX : rightX;
            int sliderY = y + ROW_HEIGHT * (i / 2);

            AbstractSliderButton slider = createSlider(entry, x, sliderY);
            widgets.add(new WidgetWrapper(slider, x, sliderY));
            addRenderableWidget(slider);
        }

        int lastRowY = y + ROW_HEIGHT * ((entries.length + 1) / 2) + 10;

        // --- Buttons ---
        int buttonWidth = 120;
        int buttonGap = 20;
        int buttonsStartX = this.width / 2 - buttonWidth - buttonGap / 2;

        Button saveButton = Button.builder(Component.literal("Save"), b -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX, lastRowY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(saveButton, buttonsStartX, lastRowY));
        addRenderableWidget(saveButton);

        Button backButton = Button.builder(Component.literal("Back"), b -> {
            reloadConfig(); // discard changes
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX + buttonWidth + buttonGap, lastRowY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(backButton, buttonsStartX + buttonWidth + buttonGap, lastRowY));
        addRenderableWidget(backButton);

        int resetButtonX = this.width / 2 - buttonWidth / 2;
        int resetButtonY = lastRowY + 25;
        Button resetButton = Button.builder(Component.literal("Reset"), b -> {
            resetConfig(); // restore defaults
            this.minecraft.setScreen(parent);
        }).bounds(resetButtonX, resetButtonY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(resetButton, resetButtonX, resetButtonY));
        addRenderableWidget(resetButton);
    }

    private AbstractSliderButton createSlider(ConfigEntry entry, int x, int y) {
        return new AbstractSliderButton(x, y, COLUMN_WIDTH, ROW_HEIGHT,
                Component.literal(entry.name + ": " + String.format("%.2f", entry.currentValue)),
                (entry.currentValue - entry.min) / (entry.max - entry.min)) {
            @Override
            public void updateMessage() {
                double value = entry.min + (entry.max - entry.min) * this.value;
                setMessage(Component.literal(entry.name + ": " + String.format("%.2f", value)));
            }

            @Override
            public void applyValue() {
                double value = entry.min + (entry.max - entry.min) * this.value;
                entry.callback.apply(value);
            }
        };
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        for (WidgetWrapper wrapper : widgets) {
            wrapper.widget.setY(wrapper.baseY);
            wrapper.widget.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    private static void saveConfig() {
        LifeBitDropsConfig.ARMOR_BONUS_PERCENTAGE.set(LifeBitDropsConfig.armorBonus);
        LifeBitDropsConfig.TOUGHNESS_BONUS_PERCENTAGE.set(LifeBitDropsConfig.toughnessBonus);
        LifeBitDropsConfig.MINIMUM_DROP_PERCENTAGE.set(LifeBitDropsConfig.minimumDrop);
        LifeBitDropsConfig.BIASED_TOWARDS_PERCENTAGE.set(LifeBitDropsConfig.biasedTowards);
        LifeBitDropsConfig.MOB_ATTACK_DAMAGE_BONUS.set(LifeBitDropsConfig.mobAttackBonus);
        LifeBitDropsConfig.PLAYER_HEALTH_PENALTY.set(LifeBitDropsConfig.playerHealthPenalty);
        LifeBitDropsConfig.PLAYER_ARMOR_PENALTY.set(LifeBitDropsConfig.playerArmorPenalty);
        LifeBitDropsConfig.PLAYER_TOUGHNESS_PENALTY.set(LifeBitDropsConfig.playerToughnessPenalty);
        LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_PENALTY.set(LifeBitDropsConfig.playerAttackDamagePenalty);
        LifeBitDropsConfig.PLAYER_HEALTH_THRESHOLD.set(LifeBitDropsConfig.playerHealthThreshold);
        LifeBitDropsConfig.PLAYER_ARMOR_THRESHOLD.set(LifeBitDropsConfig.playerArmorThreshold);
        LifeBitDropsConfig.PLAYER_TOUGHNESS_THRESHOLD.set(LifeBitDropsConfig.playerToughnessThreshold);
        LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_THRESHOLD.set(LifeBitDropsConfig.playerAttackDamageThreshold);
        LifeBitDropsConfig.bake();
        LifeBitDropsConfig.SPEC.save();
    }

    private static void reloadConfig() {
        LifeBitDropsConfig.armorBonus = LifeBitDropsConfig.ARMOR_BONUS_PERCENTAGE.get();
        LifeBitDropsConfig.toughnessBonus = LifeBitDropsConfig.TOUGHNESS_BONUS_PERCENTAGE.get();
        LifeBitDropsConfig.minimumDrop = LifeBitDropsConfig.MINIMUM_DROP_PERCENTAGE.get();
        LifeBitDropsConfig.biasedTowards = LifeBitDropsConfig.BIASED_TOWARDS_PERCENTAGE.get();
        LifeBitDropsConfig.mobAttackBonus = LifeBitDropsConfig.MOB_ATTACK_DAMAGE_BONUS.get();
        LifeBitDropsConfig.playerHealthPenalty = LifeBitDropsConfig.PLAYER_HEALTH_PENALTY.get();
        LifeBitDropsConfig.playerArmorPenalty = LifeBitDropsConfig.PLAYER_ARMOR_PENALTY.get();
        LifeBitDropsConfig.playerToughnessPenalty = LifeBitDropsConfig.PLAYER_TOUGHNESS_PENALTY.get();
        LifeBitDropsConfig.playerAttackDamagePenalty = LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_PENALTY.get();
        LifeBitDropsConfig.playerHealthThreshold = LifeBitDropsConfig.PLAYER_HEALTH_THRESHOLD.get();
        LifeBitDropsConfig.playerArmorThreshold = LifeBitDropsConfig.PLAYER_ARMOR_THRESHOLD.get();
        LifeBitDropsConfig.playerToughnessThreshold = LifeBitDropsConfig.PLAYER_TOUGHNESS_THRESHOLD.get();
        LifeBitDropsConfig.playerAttackDamageThreshold = LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_THRESHOLD.get();
        LifeBitDropsConfig.bake(); // <- make sure to reapply into any computed values
    }

    private static void resetConfig() {
        LifeBitDropsConfig.armorBonus = LifeBitDropsConfig.ARMOR_BONUS_PERCENTAGE.getDefault();
        LifeBitDropsConfig.toughnessBonus = LifeBitDropsConfig.TOUGHNESS_BONUS_PERCENTAGE.getDefault();
        LifeBitDropsConfig.minimumDrop = LifeBitDropsConfig.MINIMUM_DROP_PERCENTAGE.getDefault();
        LifeBitDropsConfig.biasedTowards = LifeBitDropsConfig.BIASED_TOWARDS_PERCENTAGE.getDefault();
        LifeBitDropsConfig.mobAttackBonus = LifeBitDropsConfig.MOB_ATTACK_DAMAGE_BONUS.getDefault();
        LifeBitDropsConfig.playerHealthPenalty = LifeBitDropsConfig.PLAYER_HEALTH_PENALTY.getDefault();
        LifeBitDropsConfig.playerArmorPenalty = LifeBitDropsConfig.PLAYER_ARMOR_PENALTY.getDefault();
        LifeBitDropsConfig.playerToughnessPenalty = LifeBitDropsConfig.PLAYER_TOUGHNESS_PENALTY.getDefault();
        LifeBitDropsConfig.playerAttackDamagePenalty = LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_PENALTY.getDefault();
        LifeBitDropsConfig.playerHealthThreshold = LifeBitDropsConfig.PLAYER_HEALTH_THRESHOLD.getDefault();
        LifeBitDropsConfig.playerArmorThreshold = LifeBitDropsConfig.PLAYER_ARMOR_THRESHOLD.getDefault();
        LifeBitDropsConfig.playerToughnessThreshold = LifeBitDropsConfig.PLAYER_TOUGHNESS_THRESHOLD.getDefault();
        LifeBitDropsConfig.playerAttackDamageThreshold = LifeBitDropsConfig.PLAYER_ATTACK_DAMAGE_THRESHOLD.getDefault();
        LifeBitDropsConfig.bake();
        ExperimentalUpdatesConfig.SPEC.save(); // <- make sure to reapply into any computed values
    }

    private static class WidgetWrapper {
        final AbstractWidget widget;
        final int baseX, baseY;

        WidgetWrapper(AbstractWidget widget, int x, int y) {
            this.widget = widget;
            this.baseX = x;
            this.baseY = y;
        }
    }

    @FunctionalInterface
    private interface SliderCallback {
        void apply(double value);
    }

    private static class ConfigEntry {
        final String name;
        double currentValue;
        final double min, max;
        final SliderCallback callback;

        ConfigEntry(String name, double currentValue, double min, double max, SliderCallback callback) {
            this.name = name;
            this.currentValue = currentValue;
            this.min = min;
            this.max = max;
            this.callback = callback;
        }
    }
}
