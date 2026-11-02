package strhercules.chickens.screen;

import strhercules.chickens.menu.RoostGeneratorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import java.util.Locale;
import java.util.List;

/** Client screen for the one-slot Roost Generator. */
public class RoostGeneratorScreen extends SideConfigurableScreen<RoostGeneratorMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/gui/roost_generator.png");
    private static final int GUI_WIDTH = 176;
    private static final int ENERGY_BAR_X = 149;
    private static final int ENERGY_BAR_Y = 14;
    private static final int ENERGY_BAR_WIDTH = 10;
    private static final int ENERGY_BAR_HEIGHT = 56;
    private static final int ENERGY_TEXTURE_X = 179;
    private static final int ENERGY_TEXTURE_Y = 1;
    private static final int INFORMATION_X = 35;
    private static final int INFORMATION_Y = 42;
    private static final int INFORMATION_WIDTH = 107;
    private static final int INFORMATION_HEIGHT = 35;

    public RoostGeneratorScreen(RoostGeneratorMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, GUI_WIDTH, 166);
        this.inventoryLabelY = 72;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, GUI_WIDTH, this.imageHeight, 256, 256);
        renderEnergyBar(graphics, x, y);
        renderInformation(graphics, x, y);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        // The supplied texture reserves its information panel for the machine
        // status rather than a separate title or inventory caption.
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
        renderSideConfig(graphics, mouseX, mouseY);
        renderEnergyTooltip(graphics, mouseX, mouseY);
        renderInformationTooltip(graphics, mouseX, mouseY);
    }

    private void renderEnergyBar(GuiGraphicsExtractor graphics, int originX, int originY) {
        int capacity = Math.max(menu.getCapacity(), 1);
        int energy = Math.max(menu.getEnergy(), 0);
        int filled = Math.min(ENERGY_BAR_HEIGHT, energy * ENERGY_BAR_HEIGHT / capacity);
        int offset = ENERGY_BAR_HEIGHT - filled;
        if (filled > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, originX + ENERGY_BAR_X, originY + ENERGY_BAR_Y + offset,
                    ENERGY_TEXTURE_X, ENERGY_TEXTURE_Y + offset, ENERGY_BAR_WIDTH, filled, 256, 256);
        }
    }

    private void renderInformation(GuiGraphicsExtractor graphics, int originX, int originY) {
        int color = 0xFF404040;
        graphics.text(font, Component.translatable("screen.chickens.roost_generator.stored",
                compact(menu.getEnergy()), compact(menu.getCapacity())), originX + 35, originY + 43, color, false);
        graphics.text(font, Component.translatable("screen.chickens.roost_generator.generation",
                compact(menu.getGeneration())), originX + 35, originY + 51, color, false);
        graphics.text(font, Component.translatable("screen.chickens.roost_generator.output",
                compact(menu.getMaxOutput())), originX + 35, originY + 59, color, false);
        graphics.text(font, Component.translatable("screen.chickens.roost_generator.ratio",
                menu.getOutputRatioPercent()), originX + 35, originY + 67, color, false);
    }

    private void renderEnergyTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2 + ENERGY_BAR_X;
        int y = (this.height - this.imageHeight) / 2 + ENERGY_BAR_Y;
        if (mouseX >= x && mouseX < x + ENERGY_BAR_WIDTH
                && mouseY >= y && mouseY < y + ENERGY_BAR_HEIGHT) {
            graphics.setTooltipForNextFrame(font,
                    Component.translatable("screen.chickens.roost_generator.energy_tooltip",
                            menu.getEnergy(), menu.getCapacity()), mouseX, mouseY);
        }
    }

    private void renderInformationTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2 + INFORMATION_X;
        int y = (this.height - this.imageHeight) / 2 + INFORMATION_Y;
        if (mouseX < x || mouseX >= x + INFORMATION_WIDTH || mouseY < y
                || mouseY >= y + INFORMATION_HEIGHT) {
            return;
        }
        graphics.setComponentTooltipForNextFrame(font, List.of(
                Component.translatable("screen.chickens.roost_generator.info.title"),
                Component.translatable("screen.chickens.roost_generator.info.stored",
                        compact(menu.getEnergy()), compact(menu.getCapacity())),
                Component.translatable("screen.chickens.roost_generator.info.generation",
                        compact(menu.getGeneration())),
                Component.translatable("screen.chickens.roost_generator.info.nominal",
                        compact(menu.getNominalGeneration())),
                Component.translatable("screen.chickens.roost_generator.info.max_output",
                        compact(menu.getMaxOutput())),
                Component.translatable("screen.chickens.roost_generator.info.ratio",
                        menu.getOutputRatioPercent())), mouseX, mouseY);
    }

    private static String compact(int value) {
        if (value == Integer.MIN_VALUE) {
            return "-2.1B";
        }
        if (Math.abs(value) >= 1_000_000) {
            return String.format(Locale.ROOT, "%.1fM", value / 1_000_000.0D);
        }
        if (Math.abs(value) >= 1_000) {
            return String.format(Locale.ROOT, "%.1fk", value / 1_000.0D);
        }
        return Integer.toString(value);
    }
}
