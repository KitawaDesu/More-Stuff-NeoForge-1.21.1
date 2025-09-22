package net.kitawa.more_stuff.util.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class ModConfigsScreen extends Screen {

    private final Screen parent;

    private final List<WidgetWrapper> widgets = new ArrayList<>();

    public ModConfigsScreen(Screen parent) {
        super(Component.literal("Mod Configs"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        widgets.clear();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = 50;
        int spacing = 10;
        int currentY = startY;

        // --- Config Buttons ---
        Button lifeBitDropsButton = Button.builder(Component.literal("Life Bit Drops Config"), b ->
                        this.minecraft.setScreen(new LifeBitDropsConfigScreen(this)))
                .bounds(this.width / 2 - buttonWidth / 2, currentY, buttonWidth, buttonHeight)
                .build();
        widgets.add(new WidgetWrapper(lifeBitDropsButton, this.width / 2 - buttonWidth / 2, currentY));
        addRenderableWidget(lifeBitDropsButton);

        currentY += buttonHeight + spacing;

        Button lifeTokensButton = Button.builder(Component.literal("Life Tokens Config"), b ->
                        this.minecraft.setScreen(new LifeTokensConfigScreen(this)))
                .bounds(this.width / 2 - buttonWidth / 2, currentY, buttonWidth, buttonHeight)
                .build();
        widgets.add(new WidgetWrapper(lifeTokensButton, this.width / 2 - buttonWidth / 2, currentY));
        addRenderableWidget(lifeTokensButton);

        currentY += buttonHeight + spacing;

        Button generalConfigButton = Button.builder(Component.literal("General Config"), b ->
                        this.minecraft.setScreen(new MoreStuffGeneralConfigScreen(this)))
                .bounds(this.width / 2 - buttonWidth / 2, currentY, buttonWidth, buttonHeight)
                .build();
        widgets.add(new WidgetWrapper(generalConfigButton, this.width / 2 - buttonWidth / 2, currentY));
        addRenderableWidget(generalConfigButton);

        currentY += buttonHeight + spacing;

        // --- Back Button ---
        Button backButton = Button.builder(Component.literal("Back"), b -> this.minecraft.setScreen(parent))
                .bounds(this.width / 2 - buttonWidth / 2, currentY, buttonWidth, buttonHeight)
                .build();
        widgets.add(new WidgetWrapper(backButton, this.width / 2 - buttonWidth / 2, currentY));
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

    private static class WidgetWrapper {
        final AbstractWidget widget;
        final int baseX, baseY;

        WidgetWrapper(AbstractWidget widget, int x, int y) {
            this.widget = widget;
            this.baseX = x;
            this.baseY = y;
        }
    }
}
