package strhercules.chickens.screen;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.blockentity.HenhouseBlockEntity;
import strhercules.chickens.menu.HenhouseMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client-side screen for the henhouse menu. It reuses the legacy texture layout
 * and progress bar math so players see the same UI they expect from the original
 * mod while interacting with the modern container.
 */
public class HenhouseScreen extends SideConfigurableScreen<HenhouseMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
        "textures/gui/henhouse.png");

    public HenhouseScreen(HenhouseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);

        int energy = this.menu.getEnergy();
        final int barHeight = 57;
        int offset = barHeight - Math.min(barHeight,
                energy * barHeight / Math.max(HenhouseBlockEntity.ENERGY_CAPACITY, 1));
        if (offset < barHeight) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x + 75, y + 14 + offset, 195, offset, 12, barHeight - offset, 256, 256);
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, (this.imageWidth - this.font.width(this.title)) / 2, 6, 0xFF404040, false);
        graphics.text(this.font, this.playerInventoryTitle, 8, this.inventoryLabelY, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        renderSideConfig(graphics, mouseX, mouseY);
    }
}
