package strhercules.chickens.integration.jade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import snownee.jade.api.ui.Element;

/**
 * Minimal Jade element that mirrors the custom WTHIT bar renderer so the Jade
 * overlay presents the same fluid/chemical/energy bars.
 */
final class HudBarElement extends Element {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 13;
    private static final int BORDER_COLOR = 0xFF000000;
    private static final int BACKGROUND_COLOR = 0xFF1B1B1B;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private final Component label;
    private final TextureAtlasSprite sprite;
    private final int tint;
    private final float ratio;

    HudBarElement(Component label, TextureAtlasSprite sprite, int tint, float ratio) {
        this.label = label;
        this.sprite = sprite;
        this.tint = tint;
        this.ratio = Math.max(0.0F, Math.min(1.0F, ratio));
        this.width = WIDTH;
        this.height = HEIGHT;
    }

    @Override
    public Component getNarration() {
        return label;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        int intX = getX();
        int intY = getY();
        graphics.fill(intX, intY, intX + WIDTH, intY + HEIGHT, BORDER_COLOR);
        graphics.fill(intX + 1, intY + 1, intX + WIDTH - 1, intY + HEIGHT - 1, BACKGROUND_COLOR);

        if (sprite != null && ratio > 0.0F) {
            int fillWidth = (int) ((WIDTH - 2) * ratio);
            int drawn = 0;
            while (drawn < fillWidth) {
                int segment = Math.min(sprite.contents().width(), fillWidth - drawn);
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, intX + 1 + drawn, intY + 1, segment,
                        HEIGHT - 2, tint);
                drawn += segment;
            }
        }
        renderScrollingText(graphics, intX, intY);
    }

    private void renderScrollingText(GuiGraphicsExtractor graphics, int x, int y) {
        Font font = Minecraft.getInstance().font;
        int areaWidth = WIDTH - 6;
        int baseX = x + 3;
        int textY = y + (HEIGHT - font.lineHeight) / 2;
        int textWidth = font.width(label);
        if (textWidth <= areaWidth) {
            graphics.text(font, label, baseX + (areaWidth - textWidth) / 2, textY, TEXT_COLOR, false);
            return;
        }
        long elapsed = Util.getMillis();
        int travel = textWidth - areaWidth;
        int period = travel * 2 + areaWidth;
        int offset = (int) (elapsed / 20 % period);
        if (offset > travel + areaWidth) {
            offset = period - offset;
        } else if (offset > travel) {
            offset = travel;
        }
        int drawX = baseX - offset;
        graphics.enableScissor(baseX, y + 1, baseX + areaWidth, y + HEIGHT - 1);
        graphics.text(font, label, drawX, textY, TEXT_COLOR, false);
        graphics.disableScissor();
    }
}
