package strhercules.chickens.client.render.blockentity;

import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.Direction;
import org.jspecify.annotations.Nullable;

public class EntityDisplayRenderState extends BlockEntityRenderState {
    public @Nullable EntityRenderState entity;
    public Direction facing = Direction.NORTH;
    public float scale = 1.0F;
}
