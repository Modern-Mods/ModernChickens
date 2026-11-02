package strhercules.chickens.screen;

import strhercules.chickens.menu.CollectorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CollectorScreen extends SideConfigurableScreen<CollectorMenu> {
    private static final Identifier NONE_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/gui/collector_none.png");
    private static final Identifier ONE_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/gui/collector_one.png");
    private static final Identifier TWO_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/gui/collector_two.png");

    public CollectorScreen(CollectorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 252, 256);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, textureForCapacity(), x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        graphics.text(this.font, this.title, 8, 6, 0xFF404040, false);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        renderSideConfig(graphics, mouseX, mouseY);
    }

    private Identifier textureForCapacity() {
        return switch (this.menu.getCapacityLevel()) {
            case 1 -> ONE_TEXTURE;
            case 2 -> TWO_TEXTURE;
            default -> NONE_TEXTURE;
        };
    }
}
