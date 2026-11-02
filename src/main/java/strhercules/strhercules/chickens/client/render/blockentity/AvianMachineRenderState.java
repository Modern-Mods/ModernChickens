package strhercules.chickens.client.render.blockentity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

public class AvianMachineRenderState extends BlockEntityRenderState {
    public boolean lit;
    public boolean divider;
    public float rotation;
    public TextureAtlasSprite interior;
    public final List<Tank> tanks = new ArrayList<>();

    public record Tank(AvianMachineBlockEntityRenderer.TankContents contents, float x0, float x1) {
    }
}
