package strhercules.chickens.screen;

import strhercules.chickens.blockentity.BreederBlockEntity;
import strhercules.chickens.menu.BreederMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client screen for the breeder container. Restores the legacy Roost layout by
 * drawing the ported texture and heart progress bar from the original mod.
 */
public class BreederScreen extends SideConfigurableScreen<BreederMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/gui/breeder.png");
    private static final int PROGRESS_X = 84;
    private static final int PROGRESS_Y = 22;
    private static final int PROGRESS_HEIGHT = 12;
    private static final int PROGRESS_WIDTH = 25;
    private static final int PROGRESS_OVERLAY_U = 176;

    public BreederScreen(BreederMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 144);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        int width = getProgressWidth();
        if (width > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x + PROGRESS_X, y + PROGRESS_Y, PROGRESS_OVERLAY_U, 0, width, PROGRESS_HEIGHT, 256,
                    256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 8, 6, 0xFF404040, false);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        renderSideConfig(graphics, mouseX, mouseY);
        renderProgressTooltip(graphics, mouseX, mouseY);
    }

    private void renderProgressTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!isHoveringProgress(mouseX, mouseY)) {
            return;
        }
        BreederBlockEntity breeder = this.menu.getBreeder();
        int progress = this.menu.getProgress();
        int percent = Math.min(100, progress * 100 / 1000);
        if (breeder.getItem(BreederBlockEntity.SEED_SLOT).isEmpty()) {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("container.chickens.breeder.no_seeds"), mouseX, mouseY);
        } else {
            graphics.setTooltipForNextFrame(this.font, Component.translatable("tooltip.chickens.container.progress", percent), mouseX,
                    mouseY);
        }
    }

    private boolean isHoveringProgress(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX > x + PROGRESS_X && mouseX < x + PROGRESS_X + PROGRESS_WIDTH
                && mouseY > y + PROGRESS_Y && mouseY < y + PROGRESS_Y + PROGRESS_HEIGHT;
    }

    private int getProgressWidth() {
        int progress = this.menu.getProgress();
        if (progress <= 0) {
            return 0;
        }
        int width = 1 + (progress * PROGRESS_WIDTH) / 1000;
        return Math.min(width, PROGRESS_WIDTH);
    }
}
