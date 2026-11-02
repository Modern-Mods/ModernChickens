package strhercules.chickens.screen;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.menu.AvianFluidConverterMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import strhercules.chickens.client.render.ClientFluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client screen for the Avian Fluid Converter. Renders a vertical tank gauge
 * whose grey fill is tinted from the stored fluid.
 */
public class AvianFluidConverterScreen extends SideConfigurableScreen<AvianFluidConverterMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
            "textures/gui/fluidchemicalconverter.png");
    private static final int TANK_X = 104;
    private static final int TANK_Y = 15;
    private static final int TANK_WIDTH = 10;
    private static final int TANK_HEIGHT = 55;
    private static final int TANK_TEXTURE_X = 196;
    private static final int TANK_TEXTURE_Y = 1;

    public AvianFluidConverterScreen(AvianFluidConverterMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
        renderTank(graphics, x, y);
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
        renderTankTooltip(graphics, mouseX, mouseY);
    }

    private void renderTank(GuiGraphicsExtractor graphics, int originX, int originY) {
        FluidStack fluid = this.menu.getFluid();
        int amount = this.menu.getFluidAmount();
        int capacity = Math.max(this.menu.getCapacity(), 1);
        if (amount <= 0) {
            return;
        }
        int filled = Math.min(TANK_HEIGHT, amount * TANK_HEIGHT / capacity);
        if (filled <= 0) {
            return;
        }
        int offset = TANK_HEIGHT - filled;
        int color = ClientFluids.tint(fluid);
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, originX + TANK_X, originY + TANK_Y + offset, TANK_TEXTURE_X,
                TANK_TEXTURE_Y + offset, TANK_WIDTH, filled, 256, 256,
                0xFF000000 | color);
    }

    private void renderTankTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (!isHoveringTank(mouseX, mouseY)) {
            return;
        }
        FluidStack fluid = this.menu.getFluid();
        int amount = this.menu.getFluidAmount();
        int capacity = Math.max(this.menu.getCapacity(), 1);
        Component tooltip = fluid.isEmpty()
                ? Component.translatable("tooltip.chickens.avian_fluid_converter.empty")
                : Component.translatable("tooltip.chickens.avian_fluid_converter.level",
                        fluid.getHoverName(), amount, capacity);
        graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
    }

    private boolean isHoveringTank(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + TANK_X && mouseX <= x + TANK_X + TANK_WIDTH
                && mouseY >= y + TANK_Y && mouseY <= y + TANK_Y + TANK_HEIGHT;
    }
}
