package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

public class ExperimentalUpdatesConfigScreen extends Screen {

    private final Screen parent;

    private static final int START_Y = 40;
    private static final int ROW_HEIGHT = 24;
    private static final int COLUMN_WIDTH = 200;
    private static final int COLUMN_GAP = 20;

    private final List<WidgetWrapper> widgets = new ArrayList<>();

    public ExperimentalUpdatesConfigScreen(Screen parent) {
        super(Component.literal("Experimental Updates Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        widgets.clear();

        int leftX = this.width / 2 - COLUMN_WIDTH - COLUMN_GAP / 2;
        int rightX = this.width / 2 + COLUMN_GAP / 2;
        int y = START_Y;

        // --- Checkboxes ---
        ConfigCheckbox[] checkboxes = new ConfigCheckbox[]{
                new ConfigCheckbox("Enable Combat Update", ExperimentalUpdatesConfig.isCombatUpdateAllowed,
                        v -> ExperimentalUpdatesConfig.isCombatUpdateAllowed = v)
        };

        for (int i = 0; i < checkboxes.length; i++) {
            int x = (i % 2 == 0) ? leftX : rightX;
            int yPos = y + ROW_HEIGHT * (i / 2);
            Checkbox cb = checkboxes[i].create(this.font, x, yPos);
            widgets.add(new WidgetWrapper(cb, x, yPos));
            addRenderableWidget(cb);
        }

        int lastRowY = y + ROW_HEIGHT * ((checkboxes.length + 1) / 2) + 10;

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
        Path configFile = folder.resolve("experimental_updates.toml");

        try {
            if (!Files.exists(folder)) Files.createDirectories(folder);

            try (FileConfig config = FileConfig.builder(configFile)
                    .writingMode(WritingMode.REPLACE)
                    .build()) {

                config.set("enableCombatUpdate", ExperimentalUpdatesConfig.isCombatUpdateAllowed);

                config.save();
            }
        } catch (IOException e) {
            System.err.println("Failed to save ExperimentalUpdatesConfig: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void reloadConfig() {
        Path folder = FMLPaths.CONFIGDIR.get().resolve("more_stuff");
        Path configFile = folder.resolve("experimental_updates.toml");

        if (Files.exists(configFile)) {
            try (FileConfig config = FileConfig.builder(configFile).build()) {
                config.load();

                ExperimentalUpdatesConfig.isCombatUpdateAllowed =
                        config.getOrElse("enableCombatUpdate", ExperimentalUpdatesConfig.isCombatUpdateAllowed);
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
