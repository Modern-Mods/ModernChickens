package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.MechanicalNestBlock;
import strhercules.chickens.blockentity.MechanicalNestBlockEntity;
import strhercules.chickens.entity.Rooster;
import strhercules.chickens.registry.ModEntityTypes;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public final class MechanicalNestBlockEntityRenderer
        implements BlockEntityRenderer<MechanicalNestBlockEntity, EntityDisplayRenderState> {
    private final EntityRenderDispatcher dispatcher;
    private Rooster preview;

    public MechanicalNestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        dispatcher = context.entityRenderer();
    }

    @Override
    public EntityDisplayRenderState createRenderState() {
        return new EntityDisplayRenderState();
    }

    @Override
    public void extractRenderState(MechanicalNestBlockEntity nest, EntityDisplayRenderState renderState,
            float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(nest, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.entity = null;
        if (nest.getRoosterCount() <= 0 || nest.getLevel() == null) {
            return;
        }
        Level level = nest.getLevel();
        if (preview == null || preview.level() != level) {
            preview = ModEntityTypes.ROOSTER.get().create(level, EntitySpawnReason.LOAD);
        }
        if (preview == null || !(nest.getBlockState().getBlock() instanceof MechanicalNestBlock)) {
            return;
        }
        preview.setRobotRooster(true);
        renderState.facing = nest.getBlockState().getValue(MechanicalNestBlock.FACING);
        renderState.entity = dispatcher.extractEntity(preview, partialTicks);
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
        poseStack.translate(0.5D, -0.08D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(0.0D, 0.0D, 0.10D);
        poseStack.scale(0.78F, 0.78F, 0.78F);
        dispatcher.submit(state.entity, camera, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }
}
