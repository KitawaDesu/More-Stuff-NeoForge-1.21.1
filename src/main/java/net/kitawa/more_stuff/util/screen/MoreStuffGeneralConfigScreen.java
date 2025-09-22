package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
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

        Button saveButton = Button.builder(Component.literal("Save"), b -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX, lastRowY, buttonWidth, 20).build();
        widgets.add(new WidgetWrapper(saveButton, buttonsStartX, lastRowY));
        addRenderableWidget(saveButton);

        Button backButton = Button.builder(Component.literal("Back"), b -> {
            reloadConfig();
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
        Path configFile = folder.resolve("general_config.toml");

        try {
            if (!Files.exists(folder)) Files.createDirectories(folder);

            try (FileConfig config = FileConfig.builder(configFile)
                    .writingMode(WritingMode.REPLACE)
                    .build()) {

                // Write cached fields to file
                config.set("armorDyeingMultiplier", MoreStuffGeneralConfig.applyArmorDyeingMultiplier);
                config.set("lootDyeingMultiplier", MoreStuffGeneralConfig.applyLootDyeingMultiplier);
                config.set("logarithmicArmorFactor", MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
                config.set("logarithmicEnchantFactor", MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor);
                config.set("naturalArmorMultiplier", MoreStuffGeneralConfig.naturalArmorMultiplier);
                config.set("villagerArmorMultiplier", MoreStuffGeneralConfig.villagerArmorMultiplier);
                config.set("naturalArmorEnchantChance", MoreStuffGeneralConfig.naturalArmorEnchantChance);
                config.set("naturalWeaponEnchantChance", MoreStuffGeneralConfig.naturalWeaponEnchantChance);
                config.set("colorR", MoreStuffGeneralConfig.r);
                config.set("colorG", MoreStuffGeneralConfig.g);
                config.set("colorB", MoreStuffGeneralConfig.b);
                config.set("allowLogarithmicArmor", MoreStuffGeneralConfig.allowLogarithmicArmor);
                config.set("allowLogarithmicEnchantments", MoreStuffGeneralConfig.allowLogarithmicEnchantments);

                config.save();
            }
        } catch (IOException e) {
            System.err.println("Failed to save MoreStuffGeneralConfig: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void reloadConfig() {
        Path folder = FMLPaths.CONFIGDIR.get().resolve("more_stuff");
        Path configFile = folder.resolve("general_config.toml");

        if (Files.exists(configFile)) {
            try (FileConfig config = FileConfig.builder(configFile).build()) {
                config.load();

                MoreStuffGeneralConfig.applyArmorDyeingMultiplier =
                        config.getOrElse("armorDyeingMultiplier", MoreStuffGeneralConfig.applyArmorDyeingMultiplier);
                MoreStuffGeneralConfig.applyLootDyeingMultiplier =
                        config.getOrElse("lootDyeingMultiplier", MoreStuffGeneralConfig.applyLootDyeingMultiplier);
                MoreStuffGeneralConfig.logarithmicArmorScalingFactor =
                        config.getOrElse("logarithmicArmorFactor", MoreStuffGeneralConfig.logarithmicArmorScalingFactor);
                MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor =
                        config.getOrElse("logarithmicEnchantFactor", MoreStuffGeneralConfig.logarithmicEnchantmentScalingFactor);
                MoreStuffGeneralConfig.naturalArmorMultiplier =
                        config.getOrElse("naturalArmorMultiplier", MoreStuffGeneralConfig.naturalArmorMultiplier);
                MoreStuffGeneralConfig.villagerArmorMultiplier =
                        config.getOrElse("villagerArmorMultiplier", MoreStuffGeneralConfig.villagerArmorMultiplier);
                MoreStuffGeneralConfig.naturalArmorEnchantChance =
                        config.getOrElse("naturalArmorEnchantChance", MoreStuffGeneralConfig.naturalArmorEnchantChance);
                MoreStuffGeneralConfig.naturalWeaponEnchantChance =
                        config.getOrElse("naturalWeaponEnchantChance", MoreStuffGeneralConfig.naturalWeaponEnchantChance);
                MoreStuffGeneralConfig.r =
                        config.getOrElse("colorR", MoreStuffGeneralConfig.r);
                MoreStuffGeneralConfig.g =
                        config.getOrElse("colorG", MoreStuffGeneralConfig.g);
                MoreStuffGeneralConfig.b =
                        config.getOrElse("colorB", MoreStuffGeneralConfig.b);
                MoreStuffGeneralConfig.allowLogarithmicArmor =
                        config.getOrElse("allowLogarithmicArmor", MoreStuffGeneralConfig.allowLogarithmicArmor);
                MoreStuffGeneralConfig.allowLogarithmicEnchantments =
                        config.getOrElse("allowLogarithmicEnchantments", MoreStuffGeneralConfig.allowLogarithmicEnchantments);
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
