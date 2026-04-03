package net.kitawa.more_stuff.util.screen;

import net.kitawa.more_stuff.MoreStuff;
import net.kitawa.more_stuff.experimentals.blocks.util.FletchingTableMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class FletchingTableScreen extends AbstractContainerScreen<FletchingTableMenu> {
    private static final ResourceLocation BG_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "textures/gui/containers/fletching_table.png");
    private static final ResourceLocation FEATHER_SPRITE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "container/slot/feather");
    private static final ResourceLocation STICK_SPRITE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "container/slot/stick");
    private static final ResourceLocation FLINT_SPRITE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "container/slot/flint");
    private static final ResourceLocation POTION_SPRITE =
            ResourceLocation.fromNamespaceAndPath(MoreStuff.MOD_ID, "container/slot/potion");

    public FletchingTableScreen(FletchingTableMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
        this.titleLabelY = 6;
    }

    @Override
    protected void renderBg(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        gfx.blit(BG_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);

        for (Slot slot : this.menu.slots) {
            if (slot instanceof FletchingTableMenu.TypeRestrictedSlot restrictedSlot && !slot.hasItem()) {
                ResourceLocation sprite = switch (restrictedSlot.requiredType) {
                    case "more_stuff:fletching" -> FEATHER_SPRITE;
                    case "more_stuff:shaft" -> STICK_SPRITE;
                    case "more_stuff:tip" -> FLINT_SPRITE;
                    case "more_stuff:modifier" -> POTION_SPRITE;
                    default -> null;
                };

                if (sprite != null) {
                    gfx.blitSprite(sprite, x + slot.x, y + slot.y, 16, 16);
                }
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics gfx, int mouseX, int mouseY) {
        gfx.drawString(this.font, this.title, 8, 6, 0x404040, false);
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gfx, mouseX, mouseY, partialTicks);
        super.render(gfx, mouseX, mouseY, partialTicks);
        this.renderTooltip(gfx, mouseX, mouseY);
    }
}
