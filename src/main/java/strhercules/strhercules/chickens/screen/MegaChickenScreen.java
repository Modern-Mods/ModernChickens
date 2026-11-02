package strhercules.chickens.screen;

import strhercules.chickens.entity.MegaChicken;
import strhercules.chickens.menu.MegaChickenMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

/** Vanilla horse-style GUI for the mega chicken's saddle, chests, and cargo. */
public final class MegaChickenScreen extends AbstractContainerScreen<MegaChickenMenu> {
    private static final Identifier FULL_CHEST_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/megachicken.png");
    private static final Identifier ONE_CHEST_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/megachicken_onechest.png");
    private static final Identifier NO_CHEST_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/megachicken_nochest.png");
    private static final Identifier SADDLE_OUTLINE_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/saddle_outline.png");
    private static final Identifier CHEST_OUTLINE_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/chest_outline.png");
    private static final Identifier FLYING_EGG_OUTLINE_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/gui/flyingegg_outline.png");

    private final MegaChicken chicken;
    private float xMouse;
    private float yMouse;

    public MegaChickenScreen(MegaChickenMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 256);
        this.chicken = menu.getChicken();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        super.extractBackground(graphics, mouseX, mouseY, partialTicks);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        boolean rightChest = this.chicken.hasRightChest();
        boolean leftChest = this.chicken.hasLeftChest();
        Identifier texture = rightChest && leftChest
                ? FULL_CHEST_TEXTURE
                : rightChest
                ? ONE_CHEST_TEXTURE
                : NO_CHEST_TEXTURE;
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 0.0F, 0.0F,
                this.imageWidth, this.imageHeight, 256, 256);
        if (leftChest && !rightChest) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, ONE_CHEST_TEXTURE, x, y + 62, 0.0F, 116.0F,
                    this.imageWidth, 54, 256, 256);
        }
        if (!this.chicken.isSaddled()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, SADDLE_OUTLINE_TEXTURE, x + 65, y + 10,
                    0.0F, 0.0F, 16, 16, 16, 16, 16, 16);
        }
        if (!this.chicken.hasFlyingEgg()) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, FLYING_EGG_OUTLINE_TEXTURE, x + 86, y + 10,
                    0.0F, 0.0F, 16, 16, 32, 32, 32, 32);
        }
        if (!rightChest) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, CHEST_OUTLINE_TEXTURE, x + 86, y + 40,
                    0.0F, 0.0F, 16, 16, 32, 32, 32, 32);
        }
        if (!leftChest) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, CHEST_OUTLINE_TEXTURE, x + 65, y + 40,
                    0.0F, 0.0F, 16, 16, 32, 32, 32, 32);
        }
        InventoryScreen.extractEntityInInventoryFollowsMouse(graphics,
                x + 8, y + 7, x + 60, y + 59, 17, 0.25F, this.xMouse, this.yMouse, this.chicken);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
        this.xMouse = mouseX;
        this.yMouse = mouseY;
        super.extractRenderState(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
    }
}
