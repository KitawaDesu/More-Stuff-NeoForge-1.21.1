package net.kitawa.more_stuff.util.screen;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.kitawa.more_stuff.blocks.custom.end.phantasmic.RevealableBlock;
import net.kitawa.more_stuff.util.configs.ExperimentalUpdatesConfig;
import net.kitawa.more_stuff.util.configs.LifeBitDropsConfig;
import net.kitawa.more_stuff.util.configs.LifeTokensConfig;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
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

    private boolean localEndUpdateEnabled;

    private CycleButton<RevealableBlock.RenderMode> renderTypeButton;
    private CycleButton<Integer> renderDistanceButton;

    private Checkbox hybernatusBiomesCheckbox;
    private Checkbox phantasmicBiomesCheckbox;

    public ExperimentalUpdatesConfigScreen(Screen parent) {
        super(Component.literal("Experimental Updates Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.clearWidgets();

        localEndUpdateEnabled = ExperimentalUpdatesConfig.isEndUpdateAllowed;

        int leftX  = this.width / 2 - COLUMN_WIDTH - COLUMN_GAP / 2;
        int rightX = this.width / 2 + COLUMN_GAP / 2;
        int y = START_Y;

        // --- Top Row ---
        addRenderableWidget(Checkbox.builder(Component.literal("Enable Combat Update"), font)
                .pos(leftX, y)
                .maxWidth(COLUMN_WIDTH)
                .selected(ExperimentalUpdatesConfig.isCombatUpdateAllowed)
                .onValueChange((c, v) -> ExperimentalUpdatesConfig.isCombatUpdateAllowed = v)
                .build());

        addRenderableWidget(Checkbox.builder(Component.literal("Enable End Update"), font)
                .pos(rightX, y)
                .maxWidth(COLUMN_WIDTH)
                .selected(ExperimentalUpdatesConfig.isEndUpdateAllowed)
                .onValueChange((c, v) -> {
                    ExperimentalUpdatesConfig.isEndUpdateAllowed = v;
                    localEndUpdateEnabled = v;
                    updateVisibility();
                })
                .build());

        // --- Sub-options (under End Update) ---
        int subY = y + ROW_HEIGHT;

        hybernatusBiomesCheckbox = Checkbox.builder(Component.literal("Enable Hybernatus Biomes"), font)
                .pos(rightX, subY)
                .maxWidth(COLUMN_WIDTH)
                .selected(ExperimentalUpdatesConfig.isHybernatusBiomesAllowed)
                .onValueChange((c, v) -> ExperimentalUpdatesConfig.isHybernatusBiomesAllowed = v)
                .build();
        addRenderableWidget(hybernatusBiomesCheckbox);

        phantasmicBiomesCheckbox = Checkbox.builder(Component.literal("Enable Phantasmic Biomes"), font)
                .pos(rightX, subY + ROW_HEIGHT)
                .maxWidth(COLUMN_WIDTH)
                .selected(ExperimentalUpdatesConfig.isPhantasmicBiomesAllowed)
                .onValueChange((c, v) -> ExperimentalUpdatesConfig.isPhantasmicBiomesAllowed = v)
                .build();
        addRenderableWidget(phantasmicBiomesCheckbox);

        // --- Dropdowns ---
        int dropdownY = y + ROW_HEIGHT * 3 + 10;

        renderTypeButton = CycleButton.<RevealableBlock.RenderMode>builder(
                        mode -> Component.literal(mode.name()))
                .withValues(RevealableBlock.RenderMode.values())
                .withInitialValue(ExperimentalUpdatesConfig.phantasmicRenderType)
                .create(leftX, dropdownY, COLUMN_WIDTH, 20,
                        Component.literal("Render Type"),
                        (btn, value) -> ExperimentalUpdatesConfig.phantasmicRenderType = value);

        addRenderableWidget(renderTypeButton);

        List<Integer> distances = new ArrayList<>();
        for (int i = 1; i <= 15; i++) distances.add(i);

        renderDistanceButton = CycleButton.<Integer>builder(
                        d -> Component.literal("Distance: " + d))
                .withValues(distances)
                .withInitialValue(ExperimentalUpdatesConfig.phantasmicRenderDistance)
                .create(rightX, dropdownY, COLUMN_WIDTH, 20,
                        Component.literal("Render Distance"),
                        (btn, value) -> ExperimentalUpdatesConfig.phantasmicRenderDistance = value);

        addRenderableWidget(renderDistanceButton);

        updateVisibility();

        // --- Buttons ---
        int buttonWidth = 120;
        int buttonGap = 20;
        int buttonsStartX = this.width / 2 - buttonWidth - buttonGap / 2;
        int lastRowY = dropdownY + ROW_HEIGHT + 10;

        addRenderableWidget(Button.builder(Component.literal("Save"), b -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX, lastRowY, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Cancel"), b -> {
            reloadConfig();
            this.minecraft.setScreen(parent);
        }).bounds(buttonsStartX + buttonWidth + buttonGap, lastRowY, buttonWidth, 20).build());

        addRenderableWidget(Button.builder(Component.literal("Reset"), b -> {
            resetConfig();
            this.minecraft.setScreen(parent);
        }).bounds(this.width / 2 - buttonWidth / 2, lastRowY + 25, buttonWidth, 20).build());
    }

    private void updateVisibility() {
        if (renderTypeButton != null) renderTypeButton.visible = localEndUpdateEnabled;
        if (renderDistanceButton != null) renderDistanceButton.visible = localEndUpdateEnabled;

        if (hybernatusBiomesCheckbox != null) hybernatusBiomesCheckbox.visible = localEndUpdateEnabled;
        if (phantasmicBiomesCheckbox != null) phantasmicBiomesCheckbox.visible = localEndUpdateEnabled;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);

        if (localEndUpdateEnabled) {
            int leftX  = this.width / 2 - COLUMN_WIDTH - COLUMN_GAP / 2;
            int rightX = this.width / 2 + COLUMN_GAP / 2;
            int labelY = START_Y + ROW_HEIGHT * 3;

            guiGraphics.drawString(this.font, "Phantasmic Render Type", leftX, labelY, 0xAAAAAA);
            guiGraphics.drawString(this.font, "Phantasmic Render Distance", rightX, labelY, 0xAAAAAA);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    // --- Config Handling ---

    private static void saveConfig() {
        ExperimentalUpdatesConfig.IS_COMBAT_UPDATE_ALLOWED.set(ExperimentalUpdatesConfig.isCombatUpdateAllowed);
        ExperimentalUpdatesConfig.IS_END_UPDATE_ALLOWED.set(ExperimentalUpdatesConfig.isEndUpdateAllowed);
        ExperimentalUpdatesConfig.IS_HYBERNATUS_BIOMES_ALLOWED.set(ExperimentalUpdatesConfig.isHybernatusBiomesAllowed);
        ExperimentalUpdatesConfig.IS_PHANTASMIC_BIOMES_ALLOWED.set(ExperimentalUpdatesConfig.isPhantasmicBiomesAllowed);
        ExperimentalUpdatesConfig.PHANTASMIC_RENDER_TYPE.set(ExperimentalUpdatesConfig.phantasmicRenderType);
        ExperimentalUpdatesConfig.PHANTASMIC_RENDER_DISTANCE.set(ExperimentalUpdatesConfig.phantasmicRenderDistance);

        ExperimentalUpdatesConfig.bake();
        ExperimentalUpdatesConfig.SPEC.save();
    }

    private static void reloadConfig() {
        ExperimentalUpdatesConfig.bake();
    }

    private static void resetConfig() {
        ExperimentalUpdatesConfig.isCombatUpdateAllowed =
                ExperimentalUpdatesConfig.IS_COMBAT_UPDATE_ALLOWED.getDefault();

        ExperimentalUpdatesConfig.isEndUpdateAllowed =
                ExperimentalUpdatesConfig.IS_END_UPDATE_ALLOWED.getDefault();

        ExperimentalUpdatesConfig.isHybernatusBiomesAllowed =
                ExperimentalUpdatesConfig.IS_HYBERNATUS_BIOMES_ALLOWED.getDefault();

        ExperimentalUpdatesConfig.isPhantasmicBiomesAllowed =
                ExperimentalUpdatesConfig.IS_PHANTASMIC_BIOMES_ALLOWED.getDefault();

        ExperimentalUpdatesConfig.phantasmicRenderType =
                ExperimentalUpdatesConfig.PHANTASMIC_RENDER_TYPE.getDefault();

        ExperimentalUpdatesConfig.phantasmicRenderDistance =
                ExperimentalUpdatesConfig.PHANTASMIC_RENDER_DISTANCE.getDefault();

        ExperimentalUpdatesConfig.bake();
        ExperimentalUpdatesConfig.SPEC.save();
    }
}