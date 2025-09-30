package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.kitawa.more_stuff.util.configs.MoreStuffGeneralConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MoreStuffGeneralConfigScreen extends Screen {

    private final Screen parent;

    private static final int START_Y = 40;
    private static final int ROW_HEIGHT = 24;
    private static final int COLUMN_WIDTH = 200;
    private static final int COLUMN_GAP = 20;

    private final List<WidgetWrapper> widgets = new ArrayList<>();

    public MoreStuffGeneralConfigScreen(Screen parent) {
        super(Component.literal("General Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        widgets.clear();

        int leftX = this.width / 2 - COLUMN_WIDTH - COLUMN_GAP / 2;
        int rightX = this.width / 2 + COLUMN_GAP / 2;
        int y = START_Y;

        // --- Sliders ---
        ConfigEntry[] sliders = new ConfigEntry[]{
                new ConfigEntry("Armor Dyeing Multiplier",
                        MoreStuffGeneralConfig.applyArmorDyeingMultiplier, 0, 10,
                        v -> { MoreStuffGeneralConfig.APPLY_ARMOR_DYEING_MULTIPLIER.set(v); MoreStuffGeneralConfig.applyArmorDyeingMultiplier = v; }),
                new ConfigEntry("Loot Dyeing Multiplier",
                        MoreStuffGeneralConfig.applyLootDyeingMultiplier, 0, 10,
                        v -> { MoreStuffGeneralConfig.APPLY_LOOT_DYEING_MULTIPLIER.set(v); MoreStuffGeneralConfig.applyLootDyeingMultiplier = v; }),
                new ConfigEntry("Logarithmic Armor Factor",
                        MoreStuffGeneralConfig.logarithmicArmorScalingFactor, 0, 10,
                        v -> { MoreStuffGeneralConfig.LOGARITHMIC_ARMOR_SCALING_FACTOR.set(v); MoreStuffGeneralConfig.logarithmicArmorScalingFactor = v; }),
                new ConfigEntry("Logarithmic Enchant Factor",
                        MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor, 0, 20,
                        v -> { MoreStuffGeneralConfig.LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR.set(v); MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor = v; }),
                new ConfigEntry("Natural Armor Multiplier",
                        MoreStuffGeneralConfig.naturalArmorMultiplier, 0, 10,
                        v -> { MoreStuffGeneralConfig.NATURAL_ARMOR_MULTIPLIER.set((int) v); MoreStuffGeneralConfig.naturalArmorMultiplier = (int) v; }),
                new ConfigEntry("Villager Armor Multiplier",
                        MoreStuffGeneralConfig.villagerArmorMultiplier, 0, 10,
                        v -> { MoreStuffGeneralConfig.VILLAGER_ARMOR_MULTIPLIER.set(v); MoreStuffGeneralConfig.villagerArmorMultiplier = v; }),
                new ConfigEntry("Natural Armor Enchant Chance",
                        MoreStuffGeneralConfig.naturalArmorEnchantChance, 0, 1,
                        v -> { MoreStuffGeneralConfig.NATURAL_ARMOR_ENCHANT_CHANCE.set(v); MoreStuffGeneralConfig.naturalArmorEnchantChance = v; }),
                new ConfigEntry("Natural Weapon Enchant Chance",
                        MoreStuffGeneralConfig.naturalWeaponEnchantChance, 0, 1,
                        v -> { MoreStuffGeneralConfig.NATURAL_WEAPON_ENCHANT_CHANCE.set(v); MoreStuffGeneralConfig.naturalWeaponEnchantChance = v; }),
                new ConfigEntry("Red Color",
                        MoreStuffGeneralConfig.r, 1, 255,
                        v -> { MoreStuffGeneralConfig.R.set((int) v); MoreStuffGeneralConfig.r = (int) v; }),
                new ConfigEntry("Green Color",
                        MoreStuffGeneralConfig.g, 1, 255,
                        v -> { MoreStuffGeneralConfig.G.set((int) v); MoreStuffGeneralConfig.g = (int) v; }),
                new ConfigEntry("Blue Color",
                        MoreStuffGeneralConfig.b, 1, 255,
                        v -> { MoreStuffGeneralConfig.B.set((int) v); MoreStuffGeneralConfig.b = (int) v; })
        };

        for (int i = 0; i < sliders.length; i++) {
            ConfigEntry entry = sliders[i];
            int x = (i % 2 == 0) ? leftX : rightX;
            int sliderY = y + ROW_HEIGHT * (i / 2);

            AbstractSliderButton slider = createSlider(entry, x, sliderY);
            widgets.add(new WidgetWrapper(slider, x, sliderY));
            addRenderableWidget(slider);
        }

        int lastRowY = y + ROW_HEIGHT * ((sliders.length + 1) / 2) + 10;

        // --- Checkboxes ---
        ConfigCheckbox[] checkboxes = new ConfigCheckbox[]{
                new ConfigCheckbox("Allow Logarithmic Armor",
                        MoreStuffGeneralConfig.allowLogarithmicArmor,
                        v -> { MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ARMOR.set(v); MoreStuffGeneralConfig.allowLogarithmicArmor = v; }),
                new ConfigCheckbox("Allow Logarithmic Enchantments",
                        MoreStuffGeneralConfig.allowLogarithmicEnchantments,
                        v -> { MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ENCHANTMENTS.set(v); MoreStuffGeneralConfig.allowLogarithmicEnchantments = v; })
        };

        for (int i = 0; i < checkboxes.length; i++) {
            int x = (i % 2 == 0) ? leftX : rightX;
            int yPos = lastRowY + ROW_HEIGHT * (i / 2);
            Checkbox cb = checkboxes[i].create(this.font, x, yPos);
            widgets.add(new WidgetWrapper(cb, x, yPos));
            addRenderableWidget(cb);
        }

        lastRowY += ROW_HEIGHT * ((checkboxes.length + 1) / 2) + 10;

        // --- Buttons ---
        int buttonWidth = 120;
        int buttonGap = 20;
        int buttonsStartX = this.width / 2 - buttonWidth - buttonGap / 2;

// Save button
        Button saveButton = Button.builder(Component.literal("Save"), b -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX, lastRowY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(saveButton, buttonsStartX, lastRowY));
        addRenderableWidget(saveButton);

// Back button
        Button backButton = Button.builder(Component.literal("Back"), b -> {
            reloadConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX + buttonWidth + buttonGap, lastRowY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(backButton, buttonsStartX + buttonWidth + buttonGap, lastRowY));
        addRenderableWidget(backButton);

// Reset button (below, centered horizontally between Save and Back)
        int resetButtonX = this.width / 2 - buttonWidth / 2; // center align
        int resetButtonY = lastRowY + 25; // one row below save/back
        Button resetButton = Button.builder(Component.literal("Reset"), b -> {
            resetConfig();
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
        MoreStuffGeneralConfig.APPLY_ARMOR_DYEING_MULTIPLIER.set(MoreStuffGeneralConfig.applyArmorDyeingMultiplier);
        MoreStuffGeneralConfig.APPLY_LOOT_DYEING_MULTIPLIER.set(MoreStuffGeneralConfig.applyLootDyeingMultiplier);
        MoreStuffGeneralConfig.LOGARITHMIC_ARMOR_SCALING_FACTOR.set(MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
        MoreStuffGeneralConfig.LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR.set(MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor);
        MoreStuffGeneralConfig.NATURAL_ARMOR_MULTIPLIER.set(MoreStuffGeneralConfig.naturalArmorMultiplier);
        MoreStuffGeneralConfig.VILLAGER_ARMOR_MULTIPLIER.set(MoreStuffGeneralConfig.villagerArmorMultiplier);
        MoreStuffGeneralConfig.NATURAL_ARMOR_ENCHANT_CHANCE.set(MoreStuffGeneralConfig.naturalArmorEnchantChance);
        MoreStuffGeneralConfig.NATURAL_WEAPON_ENCHANT_CHANCE.set(MoreStuffGeneralConfig.naturalWeaponEnchantChance);
        MoreStuffGeneralConfig.R.set(MoreStuffGeneralConfig.r);
        MoreStuffGeneralConfig.G.set(MoreStuffGeneralConfig.g);
        MoreStuffGeneralConfig.B.set(MoreStuffGeneralConfig.b);
        MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ARMOR.set(MoreStuffGeneralConfig.allowLogarithmicArmor);
        MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ENCHANTMENTS.set(MoreStuffGeneralConfig.allowLogarithmicEnchantments);
        MoreStuffGeneralConfig.bake();
        MoreStuffGeneralConfig.SPEC.save();
    }

    private static void reloadConfig() {
        MoreStuffGeneralConfig.applyArmorDyeingMultiplier = MoreStuffGeneralConfig.APPLY_ARMOR_DYEING_MULTIPLIER.get();
        MoreStuffGeneralConfig.applyLootDyeingMultiplier = MoreStuffGeneralConfig.APPLY_LOOT_DYEING_MULTIPLIER.get();
        MoreStuffGeneralConfig.logarithmicArmorScalingFactor = MoreStuffGeneralConfig.LOGARITHMIC_ARMOR_SCALING_FACTOR.get();
        MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor = MoreStuffGeneralConfig.LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR.get();
        MoreStuffGeneralConfig.naturalArmorMultiplier = MoreStuffGeneralConfig.NATURAL_ARMOR_MULTIPLIER.get();
        MoreStuffGeneralConfig.villagerArmorMultiplier = MoreStuffGeneralConfig.VILLAGER_ARMOR_MULTIPLIER.get();
        MoreStuffGeneralConfig.naturalArmorEnchantChance = MoreStuffGeneralConfig.NATURAL_ARMOR_ENCHANT_CHANCE.get();
        MoreStuffGeneralConfig.naturalWeaponEnchantChance = MoreStuffGeneralConfig.NATURAL_WEAPON_ENCHANT_CHANCE.get();
        MoreStuffGeneralConfig.r = MoreStuffGeneralConfig.R.get();
        MoreStuffGeneralConfig.g = MoreStuffGeneralConfig.G.get();
        MoreStuffGeneralConfig.b = MoreStuffGeneralConfig.B.get();
        MoreStuffGeneralConfig.allowLogarithmicArmor = MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ARMOR.get();
        MoreStuffGeneralConfig.allowLogarithmicEnchantments = MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ENCHANTMENTS.get();
        MoreStuffGeneralConfig.bake();
    }

    private static void resetConfig() {
        MoreStuffGeneralConfig.applyArmorDyeingMultiplier = MoreStuffGeneralConfig.APPLY_ARMOR_DYEING_MULTIPLIER.getDefault();
        MoreStuffGeneralConfig.applyLootDyeingMultiplier = MoreStuffGeneralConfig.APPLY_LOOT_DYEING_MULTIPLIER.getDefault();
        MoreStuffGeneralConfig.logarithmicArmorScalingFactor = MoreStuffGeneralConfig.LOGARITHMIC_ARMOR_SCALING_FACTOR.getDefault();
        MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor = MoreStuffGeneralConfig.LOGARITHMIC_ENCHANTMENT_SCALING_FACTOR.getDefault();
        MoreStuffGeneralConfig.naturalArmorMultiplier = MoreStuffGeneralConfig.NATURAL_ARMOR_MULTIPLIER.getDefault();
        MoreStuffGeneralConfig.villagerArmorMultiplier = MoreStuffGeneralConfig.VILLAGER_ARMOR_MULTIPLIER.getDefault();
        MoreStuffGeneralConfig.naturalArmorEnchantChance = MoreStuffGeneralConfig.NATURAL_ARMOR_ENCHANT_CHANCE.getDefault();
        MoreStuffGeneralConfig.naturalWeaponEnchantChance = MoreStuffGeneralConfig.NATURAL_WEAPON_ENCHANT_CHANCE.getDefault();
        MoreStuffGeneralConfig.r = MoreStuffGeneralConfig.R.getDefault();
        MoreStuffGeneralConfig.g = MoreStuffGeneralConfig.G.getDefault();
        MoreStuffGeneralConfig.b = MoreStuffGeneralConfig.B.getDefault();
        MoreStuffGeneralConfig.allowLogarithmicArmor = MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ARMOR.getDefault();
        MoreStuffGeneralConfig.allowLogarithmicEnchantments = MoreStuffGeneralConfig.ALLOW_LOGARITHMIC_ENCHANTMENTS.getDefault();
        MoreStuffGeneralConfig.bake();
        ExperimentalUpdatesConfig.SPEC.save();
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

    private static class ConfigCheckbox {
        final String label;
        final boolean initialValue;
        final java.util.function.Consumer<Boolean> callback;

        ConfigCheckbox(String label, boolean initialValue, java.util.function.Consumer<Boolean> callback) {
            this.label = label;
            this.initialValue = initialValue;
            this.callback = callback;
        }

        Checkbox create(Font font, int x, int y) {
            return Checkbox.builder(Component.literal(label), font)
                    .pos(x, y)
                    .maxWidth(200)
                    .selected(initialValue)
                    .onValueChange((c, value) -> callback.accept(value))
                    .build();
        }
    }
}
