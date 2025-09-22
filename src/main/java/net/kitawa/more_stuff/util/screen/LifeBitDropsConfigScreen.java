package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
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
    }

    private AbstractSliderButton createSlider(ConfigEntry entry, int x, int y) {
        return new AbstractSliderButton(x, y, COLUMN_WIDTH, ROW_HEIGHT,
                Component.literal(entry.name + ": " + String.format("%.2f", entry.currentValue)),
                (entry.currentValue - entry.min) / (entry.max - entry.min)) {
            @Override
            protected void updateMessage() {
                double value = entry.min + (entry.max - entry.min) * this.value;
                setMessage(Component.literal(entry.name + ": " + String.format("%.2f", value)));
            }

            @Override
            protected void applyValue() {
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
        Path folder = FMLPaths.CONFIGDIR.get().resolve("more_stuff");
        Path configFile = folder.resolve("life_bit_drops.toml");

        try {
            if (!Files.exists(folder)) Files.createDirectories(folder);

            try (CommentedFileConfig config = CommentedFileConfig.builder(configFile)
                    .writingMode(WritingMode.REPLACE)
                    .build()) {

                config.set("life_bit_drops.armorBonusPercentage", LifeBitDropsConfig.armorBonus);
                config.set("life_bit_drops.toughnessBonusPercentage", LifeBitDropsConfig.toughnessBonus);
                config.set("life_bit_drops.minimumDropPercentage", LifeBitDropsConfig.minimumDrop);
                config.set("life_bit_drops.biasedTowardsPercentage", LifeBitDropsConfig.biasedTowards);
                config.set("life_bit_drops.mobAttackDamageBonus", LifeBitDropsConfig.mobAttackBonus);
                config.set("life_bit_drops.playerHealthPenalty", LifeBitDropsConfig.playerHealthPenalty);
                config.set("life_bit_drops.playerArmorPenalty", LifeBitDropsConfig.playerArmorPenalty);
                config.set("life_bit_drops.playerToughnessPenalty", LifeBitDropsConfig.playerToughnessPenalty);
                config.set("life_bit_drops.playerAttackDamagePenalty", LifeBitDropsConfig.playerAttackDamagePenalty);
                config.set("life_bit_drops.playerHealthThreshold", LifeBitDropsConfig.playerHealthThreshold);
                config.set("life_bit_drops.playerArmorThreshold", LifeBitDropsConfig.playerArmorThreshold);
                config.set("life_bit_drops.playerToughnessThreshold", LifeBitDropsConfig.playerToughnessThreshold);
                config.set("life_bit_drops.playerAttackDamageThreshold", LifeBitDropsConfig.playerAttackDamageThreshold);

                config.save();
            }
        } catch (IOException e) {
            System.err.println("Failed to save LifeBitDropsConfig: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void reloadConfig() {
        Path folder = FMLPaths.CONFIGDIR.get().resolve("more_stuff");
        Path configFile = folder.resolve("life_bit_drops.toml");

        if (Files.exists(configFile)) {
            try (CommentedFileConfig config = CommentedFileConfig.builder(configFile).build()) {
                config.load();

                LifeBitDropsConfig.armorBonus = config.getOrElse("life_bit_drops.armorBonusPercentage", LifeBitDropsConfig.armorBonus);
                LifeBitDropsConfig.toughnessBonus = config.getOrElse("life_bit_drops.toughnessBonusPercentage", LifeBitDropsConfig.toughnessBonus);
                LifeBitDropsConfig.minimumDrop = config.getOrElse("life_bit_drops.minimumDropPercentage", LifeBitDropsConfig.minimumDrop);
                LifeBitDropsConfig.biasedTowards = config.getOrElse("life_bit_drops.biasedTowardsPercentage", LifeBitDropsConfig.biasedTowards);
                LifeBitDropsConfig.mobAttackBonus = config.getOrElse("life_bit_drops.mobAttackDamageBonus", LifeBitDropsConfig.mobAttackBonus);
                LifeBitDropsConfig.playerHealthPenalty = config.getOrElse("life_bit_drops.playerHealthPenalty", LifeBitDropsConfig.playerHealthPenalty);
                LifeBitDropsConfig.playerArmorPenalty = config.getOrElse("life_bit_drops.playerArmorPenalty", LifeBitDropsConfig.playerArmorPenalty);
                LifeBitDropsConfig.playerToughnessPenalty = config.getOrElse("life_bit_drops.playerToughnessPenalty", LifeBitDropsConfig.playerToughnessPenalty);
                LifeBitDropsConfig.playerAttackDamagePenalty = config.getOrElse("life_bit_drops.playerAttackDamagePenalty", LifeBitDropsConfig.playerAttackDamagePenalty);
                LifeBitDropsConfig.playerHealthThreshold = config.getOrElse("life_bit_drops.playerHealthThreshold", LifeBitDropsConfig.playerHealthThreshold);
                LifeBitDropsConfig.playerArmorThreshold = config.getOrElse("life_bit_drops.playerArmorThreshold", LifeBitDropsConfig.playerArmorThreshold);
                LifeBitDropsConfig.playerToughnessThreshold = config.getOrElse("life_bit_drops.playerToughnessThreshold", LifeBitDropsConfig.playerToughnessThreshold);
                LifeBitDropsConfig.playerAttackDamageThreshold = config.getOrElse("life_bit_drops.playerAttackDamageThreshold", LifeBitDropsConfig.playerAttackDamageThreshold);
            }
        }
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
