package strhercules.chickens.screen;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity;
import strhercules.chickens.menu.AvianDousingMachineMenu;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity.InfusionMode;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity.SpecialInfusion;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.world.entity.player.Inventory;
import strhercules.chickens.client.render.ClientFluids;
import net.minecraft.util.Mth;

/**
 * Client GUI for the Avian Dousing Machine. Renders the combined energy,
 * chemical, and liquid gauges plus a progress indicator showing when the next
 * Smart Chicken infusion will complete.
 */
public class AvianDousingMachineScreen extends SideConfigurableScreen<AvianDousingMachineMenu> {
    private static final Identifier GUI_TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
            "textures/gui/douser.png");

    private static final int CHEM_BAR_X = 9;
    private static final int CHEM_BAR_Y = 14;
    private static final int LIQUID_BAR_X = 26;
    private static final int LIQUID_BAR_Y = 14;
    private static final int ENERGY_BAR_X = 148;
    private static final int ENERGY_BAR_Y = 13;
    private static final int PROGRESS_X = 82;
    private static final int PROGRESS_Y = 36;
    private static final int BAR_WIDTH = 12;
    private static final int BAR_HEIGHT = 57;
    private static final int PROGRESS_WIDTH = 18;
    private static final int PROGRESS_HEIGHT = 13;
    private static final int BAR_TEXTURE_X = 195;
    private static final int BAR_TEXTURE_Y = 0;
    private static final int ENERGY_TEXTURE_X = 208;
    private static final int ENERGY_TEXTURE_Y = 0;
    private static final int TEXTURE_SIZE = 256;

    public AvianDousingMachineScreen(AvianDousingMachineMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, 176, 166);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight, TEXTURE_SIZE, TEXTURE_SIZE);

        renderChemicalBar(graphics, x, y);
        renderLiquidBar(graphics, x, y);
        renderEnergyBar(graphics, x, y);
        renderProgressArrow(graphics, x, y);
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
        renderTooltips(graphics, mouseX, mouseY);
    }

    private void renderChemicalBar(GuiGraphicsExtractor graphics, int originX, int originY) {
        int amount = menu.getChemicalAmount();
        int capacity = Math.max(menu.getChemicalCapacity(), 1);
        if (amount <= 0) {
            return;
        }
        int filled = Math.min(BAR_HEIGHT, amount * BAR_HEIGHT / capacity);
        if (filled <= 0) {
            return;
        }
        int offset = BAR_HEIGHT - filled;
        int color = 0xFFFFFF;
        var entry = menu.getStoredChemical();
        if (entry != null) {
            color = entry.getEggColor();
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE,
                originX + CHEM_BAR_X,
                originY + CHEM_BAR_Y + offset,
                BAR_TEXTURE_X,
                BAR_TEXTURE_Y + offset,
                BAR_WIDTH,
                filled,
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                0xFF000000 | color);
    }

    private void renderLiquidBar(GuiGraphicsExtractor graphics, int originX, int originY) {
        int amount = menu.getFluidAmount();
        int capacity = Math.max(menu.getFluidCapacity(), 1);
        if (amount <= 0) {
            return;
        }
        int filled = Math.min(BAR_HEIGHT, amount * BAR_HEIGHT / capacity);
        if (filled <= 0) {
            return;
        }
        int offset = BAR_HEIGHT - filled;
        FluidStack stack = menu.getFluid();
        int color = colorForSpecial(menu);
        if (color == -1) {
            color = 0x3FA7FF;
            if (!stack.isEmpty()) {
                color = ClientFluids.tint(stack);
            }
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE,
                originX + LIQUID_BAR_X,
                originY + LIQUID_BAR_Y + offset,
                BAR_TEXTURE_X,
                BAR_TEXTURE_Y + offset,
                BAR_WIDTH,
                filled,
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                0xFF000000 | color);
    }

    private void renderEnergyBar(GuiGraphicsExtractor graphics, int originX, int originY) {
        int energy = getDisplayedEnergy();
        int capacity = Math.max(getDisplayedCapacity(), 1);
        if (energy <= 0) {
            return;
        }
        int filled = energy > 0
                ? Math.max(1, Math.min(BAR_HEIGHT, Mth.ceil(energy * BAR_HEIGHT / (float) capacity)))
                : 0;
        if (filled <= 0) {
            return;
        }
        int offset = BAR_HEIGHT - filled;
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE,
                originX + ENERGY_BAR_X,
                originY + ENERGY_BAR_Y + offset,
                ENERGY_TEXTURE_X,
                ENERGY_TEXTURE_Y + offset,
                BAR_WIDTH,
                filled,
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                0xFFF2BF33);
    }

    private void renderProgressArrow(GuiGraphicsExtractor graphics, int originX, int originY) {
        int progress = menu.getProgress();
        int max = Math.max(menu.getMaxProgress(), 1);
        if (progress <= 0) {
            return;
        }
        int width = Math.min(PROGRESS_WIDTH, progress * PROGRESS_WIDTH / max);
        if (width <= 0) {
            return;
        }
        graphics.blit(RenderPipelines.GUI_TEXTURED, GUI_TEXTURE,
                originX + PROGRESS_X,
                originY + PROGRESS_Y,
                176,
                0,
                width,
                PROGRESS_HEIGHT,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
    }

    private void renderTooltips(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        if (isHoveringChemical(mouseX, mouseY)) {
            int amount = menu.getChemicalAmount();
            int capacity = Math.max(menu.getChemicalCapacity(), 1);
            Component chemicalName = menu.getStoredChemical() != null
                    ? menu.getStoredChemical().getDisplayName()
                    : Component.translatable("tooltip.chickens.avian_dousing_machine.empty");
            Component tooltip = Component.translatable("tooltip.chickens.avian_dousing_machine.chemical",
                    chemicalName, amount, capacity, AvianDousingMachineBlockEntity.CHEMICAL_COST,
                    menu.getEnergyCostForMode(InfusionMode.CHEMICAL));
            graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
            return;
        }
        if (isHoveringLiquid(mouseX, mouseY)) {
            int amount = menu.getFluidAmount();
            int capacity = Math.max(menu.getFluidCapacity(), 1);
            FluidStack stack = menu.getFluid();
            Component fluidName = stack.isEmpty()
                    ? Component.translatable("tooltip.chickens.avian_dousing_machine.empty")
                    : stack.getHoverName();
            if (menu.getSpecialInfusion() != SpecialInfusion.NONE && menu.getSpecialAmount() > 0) {
                fluidName = Component.literal(menu.getSpecialInfusion().getDisplayName());
            }
            int useCost = menu.getSpecialInfusion() != SpecialInfusion.NONE
                    ? AvianDousingMachineBlockEntity.SPECIAL_LIQUID_CAPACITY
                    : menu.getLiquidCost();
            int energyCost = menu.getSpecialInfusion() != SpecialInfusion.NONE
                    ? menu.getEnergyCostForMode(InfusionMode.SPECIAL)
                    : menu.getEnergyCostForMode(InfusionMode.LIQUID);
            Component tooltip = Component.translatable("tooltip.chickens.avian_dousing_machine.liquid",
                    fluidName, amount, capacity, useCost, energyCost);
            graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
            return;
        }
        if (isHoveringEnergy(mouseX, mouseY)) {
            int energy = getDisplayedEnergy();
            int capacity = Math.max(getDisplayedCapacity(), 1);
            Component tooltip = Component.translatable("tooltip.chickens.avian_dousing_machine.energy", energy, capacity,
                    menu.getEnergyCostForCurrentOperation());
            graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
            return;
        }
        if (isHoveringProgress(mouseX, mouseY)) {
            int progress = menu.getProgress();
            int max = Math.max(menu.getMaxProgress(), 1);
            Component tooltip = Component.translatable("tooltip.chickens.avian_dousing_machine.progress", progress * 100 / max);
            graphics.setTooltipForNextFrame(this.font, tooltip, mouseX, mouseY);
            return;
        }
    }

    private boolean isHoveringChemical(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + CHEM_BAR_X && mouseX <= x + CHEM_BAR_X + BAR_WIDTH
                && mouseY >= y + CHEM_BAR_Y && mouseY <= y + CHEM_BAR_Y + BAR_HEIGHT;
    }

    private boolean isHoveringLiquid(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + LIQUID_BAR_X && mouseX <= x + LIQUID_BAR_X + BAR_WIDTH
                && mouseY >= y + LIQUID_BAR_Y && mouseY <= y + LIQUID_BAR_Y + BAR_HEIGHT;
    }

    private boolean isHoveringEnergy(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + ENERGY_BAR_X && mouseX <= x + ENERGY_BAR_X + BAR_WIDTH
                && mouseY >= y + ENERGY_BAR_Y && mouseY <= y + ENERGY_BAR_Y + BAR_HEIGHT;
    }

    private boolean isHoveringProgress(int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        return mouseX >= x + PROGRESS_X && mouseX <= x + PROGRESS_X + PROGRESS_WIDTH
                && mouseY >= y + PROGRESS_Y && mouseY <= y + PROGRESS_Y + PROGRESS_HEIGHT;
    }

    private int colorForSpecial(AvianDousingMachineMenu menu) {
        SpecialInfusion infusion = menu.getSpecialInfusion();
        if (infusion == SpecialInfusion.NONE || menu.getSpecialAmount() <= 0) {
            return -1;
        }
        return switch (infusion) {
            case DRAGON_BREATH -> 0xA05CFF;
            case NETHER_STAR -> 0xE0E0E0;
            default -> -1;
        };
    }

    private int getDisplayedEnergy() {
        return menu.getEnergyStored();
    }

    private int getDisplayedCapacity() {
        return menu.getEnergyCapacity();
    }
}
