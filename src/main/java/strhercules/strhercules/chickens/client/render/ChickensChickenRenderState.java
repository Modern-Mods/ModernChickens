package strhercules.chickens.client.render;

import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

public class ChickensChickenRenderState extends ChickenRenderState {
    public @Nullable Identifier texture;
    public @Nullable OverlayInfo overlay;

    public record OverlayInfo(TextureAtlasSprite sprite, int tint) {
    }
}
