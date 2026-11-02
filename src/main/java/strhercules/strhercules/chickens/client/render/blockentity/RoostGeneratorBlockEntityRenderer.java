package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.RoostGeneratorBlock;
import strhercules.chickens.blockentity.AbstractChickenContainerBlockEntity.RenderData;
import strhercules.chickens.blockentity.RoostGeneratorBlockEntity;
import strhercules.chickens.client.render.ChickenRenderHelper;
import strhercules.chickens.entity.ChickensChicken;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/** Renders the Flux Chicken at the generator's internal anchor. */
public final class RoostGeneratorBlockEntityRenderer
        implements BlockEntityRenderer<RoostGeneratorBlockEntity, EntityDisplayRenderState> {
    private static final float SCALE = 0.78F;
    private static final double FLOOR_OFFSET = -0.08D;
    private static final double FRONT_OFFSET = 0.08D;

    private final EntityRenderDispatcher dispatcher;

    public RoostGeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.entityRenderer();
    }

    @Override
    public EntityDisplayRenderState createRenderState() {
        return new EntityDisplayRenderState();
    }

    @Override
    public void extractRenderState(RoostGeneratorBlockEntity generator, EntityDisplayRenderState renderState,
            float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(generator, renderState, partialTicks, cameraPosition,
                breakProgress);
        renderState.entity = null;
        RenderData data = generator.getRenderData(RoostGeneratorBlockEntity.CHICKEN_SLOT);
        if (data == null || data.count() <= 0) {
            return;
        }
        ChickensChicken chicken = ChickenRenderHelper.getChicken(data.chicken().getId(), data.stats());
        BlockState state = generator.getBlockState();
        if (chicken == null || !(state.getBlock() instanceof RoostGeneratorBlock)) {
            return;
        }

        renderState.facing = state.getValue(RoostGeneratorBlock.FACING);
        chicken.setRobotChicken(data.robotChicken());
        ChickenRenderHelper.resetPose(chicken);
        renderState.entity = dispatcher.extractEntity(chicken, partialTicks);
        renderState.entity.lightCoords = LightCoordsUtil.FULL_BRIGHT;
    }

    @Override
    public void submit(EntityDisplayRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera) {
        if (state.entity == null) {
            return;
        }
        Direction facing = state.facing;
        poseStack.pushPose();
        poseStack.translate(0.5D, FLOOR_OFFSET, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(0.0D, 0.0D, FRONT_OFFSET);
        poseStack.scale(SCALE, SCALE, SCALE);
        dispatcher.submit(state.entity, camera, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }
}
