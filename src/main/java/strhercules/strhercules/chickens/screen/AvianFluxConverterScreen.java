package strhercules.chickens.screen;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.menu.AvianFluxConverterMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;


public class AvianFluxConverterScreen extends SideConfigurableScreen<AvianFluxConverterMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
            "textures/gui/fluxconverter.png");
    private static final int ENERGY_BAR_X = 103;
    private static final int ENERGY_BAR_Y = 14;
    private static final int ENERGY_BAR_WIDTH = 13;
    private static final int ENERGY_BAR_HEIGHT = 58;
    private static final int ENERGY_TEXTURE_X = 195;
    private static final int ENERGY_TEXTURE_Y = 0;

    public AvianFluxConverterScreen(AvianFluxConverterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderEnergyBar(graphics, x, y);
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
        renderEnergyTooltip(graphics, mouseX, mouseY);
    }

    private void renderEnergyBar(GuiGraphicsExtractor graphics, int originX, int originY) {
        int energy = this.menu.getEnergy();
        int capacity = Math.max(this.menu.getCapacity(), 1);
        int offset = ENERGY_BAR_HEIGHT
                - Math.min(ENERGY_BAR_HEIGHT, energy * ENERGY_BAR_HEIGHT / Math.max(capacity, 1));
        if (offset < ENERGY_BAR_HEIGHT) {
            // Lift the gauge upward from the bottom of the slot just like the henhouse hay bar,
            // reusing the authored fluxconverter.png coordinates provided by art.
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, originX + ENERGY_BAR_X, originY + ENERGY_BAR_Y + offset, ENERGY_TEXTURE_X,
                    ENERGY_TEXTURE_Y + offset, ENERGY_BAR_WIDTH, ENERGY_BAR_HEIGHT - offset, 256, 256);
        }
    }

    private void renderEnergyTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!isHoveringEnergy(mouseX, mouseY)) {
            return;
        }
        int energy = this.menu.getEnergy();
        int capacity = Math.max(this.menu.getCapacity(), 1);
        graphics.setTooltipForNextFrame(this.font, Component.translatable("tooltip.chickens.avian_flux_converter.energy", energy, capacity),
                mouseX, mouseY);
    }

    private boolean isHoveringEnergy(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + ENERGY_BAR_X && mouseX <= x + ENERGY_BAR_X + ENERGY_BAR_WIDTH
                && mouseY >= y + ENERGY_BAR_Y && mouseY <= y + ENERGY_BAR_Y + ENERGY_BAR_HEIGHT;
    }
}
