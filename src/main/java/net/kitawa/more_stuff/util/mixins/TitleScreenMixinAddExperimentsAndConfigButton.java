package net.kitawa.more_stuff.util.mixins;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.util.screen.ExperimentalUpdatesConfigScreen;
import net.kitawa.more_stuff.util.screen.ModConfigsScreen;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixinAddExperimentsAndConfigButton extends Screen {

    protected TitleScreenMixinAddExperimentsAndConfigButton(Component title) {
        super(title);
    }

    @Inject(method = "createNormalMenuOptions", at = @At("TAIL"))
    private void addExperimentalUpdatesAndConfigButtons(int y, int rowHeight, CallbackInfo ci) {
        int buttonY = y + rowHeight / 2;
        int adjustedButtonY = buttonY;

        // --- Experimental Updates Button (left side) ---
        if (ModList.get().isLoaded("create")) {
            adjustedButtonY = buttonY - 12; // adjust the offset as needed
        }

        SpriteIconButton experimentalButton = SpriteIconButton.builder(
                        Component.literal("Experimental Updates"),
                        b -> this.minecraft.setScreen(new ExperimentalUpdatesConfigScreen((TitleScreen)(Object)this)),
                        true // iconOnly
                )
                .width(20) // match CommonButtons style
                .sprite(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "icon/experimental_button"), 16, 16)
                .build();

        experimentalButton.setPosition(this.width / 2 - 124, adjustedButtonY); // left side
        this.addRenderableWidget(experimentalButton);

        // --- Mod Configs Button (right side) ---
        SpriteIconButton modConfigsButton = SpriteIconButton.builder(
                        Component.literal("Mod Configs"),
                        b -> this.minecraft.setScreen(new ModConfigsScreen((TitleScreen)(Object)this)),
                        true // iconOnly
                )
                .width(20)
                .sprite(ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "icon/configs_button"), 16, 16)
                .build();

        modConfigsButton.setPosition(this.width / 2 + 104, buttonY); // right side, mirrored
        this.addRenderableWidget(modConfigsButton);
    }
}