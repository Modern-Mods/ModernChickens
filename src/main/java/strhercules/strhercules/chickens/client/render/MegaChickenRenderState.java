package strhercules.chickens.client.render;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;

public class MegaChickenRenderState extends LivingEntityRenderState {
    public Identifier texture;
    public boolean tamed;
    public boolean saddled;
    public boolean rightChest;
    public boolean leftChest;
    public boolean airborne;
    public float bob;
}
