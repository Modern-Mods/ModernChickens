package strhercules.chickens.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import strhercules.chickens.entity.Rooster;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

/**
 * Renderer for the rooster entity. Uses the ported {@link RoosterModel} so the
 * legacy Hatchery rooster texture maps correctly onto the geometry.
 */
public class RoosterRenderer extends MobRenderer<Rooster, RoosterRenderState, RoosterModel> {
    private static final Identifier ROOSTER_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/entity/rooster.png");
    private static final Identifier ROBOT_ROOSTER_TEXTURE = Identifier.fromNamespaceAndPath("chickens",
            "textures/entity/robot_rooster.png");

    public RoosterRenderer(EntityRendererProvider.Context context) {
        super(context, new RoosterModel(context.bakeLayer(RoosterModel.LAYER_LOCATION)), 0.3F);
    }

    @Override
    public RoosterRenderState createRenderState() {
        return new RoosterRenderState();
    }

    @Override
    public void extractRenderState(Rooster rooster, RoosterRenderState state, float partialTicks) {
        super.extractRenderState(rooster, state, partialTicks);
        // Mirror the vanilla ChickenRenderer flap logic so the rooster's
        // wing animation feels identical to standard chickens.
        state.flap = Mth.lerp(partialTicks, rooster.oFlap, rooster.flap);
        state.flapSpeed = Mth.lerp(partialTicks, rooster.oFlapSpeed, rooster.flapSpeed);
        state.robot = rooster.isRobotRooster();
    }

    @Override
    public Identifier getTextureLocation(RoosterRenderState state) {
        return state.robot ? ROBOT_ROOSTER_TEXTURE : ROOSTER_TEXTURE;
    }

    @Override
    protected void scale(RoosterRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }
}
