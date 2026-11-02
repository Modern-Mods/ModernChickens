package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.NestBlock;
import strhercules.chickens.blockentity.NestBlockEntity;
import strhercules.chickens.entity.Rooster;
import strhercules.chickens.item.ChickenItemHelper;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Renders a rooster entity sitting inside the Nest. The pose and scaling are
 * tuned to match the Roost renderer so the two machines feel visually related.
 */
public class NestBlockEntityRenderer implements BlockEntityRenderer<NestBlockEntity, EntityDisplayRenderState> {
    private static final float BASE_SCALE = 0.9F;
    private static final float SCALE_PER_ROOSTER = 0.015F;
    private static final double FRONT_OFFSET = 0.04D;
    private static final double FLOOR_OFFSET = -0.11D;

    private final EntityRenderDispatcher dispatcher;
    private Rooster roosterPreview;

    public NestBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.entityRenderer();
    }

    @Override
    public EntityDisplayRenderState createRenderState() {
        return new EntityDisplayRenderState();
    }

    @Override
    public void extractRenderState(NestBlockEntity nest, EntityDisplayRenderState renderState, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(nest, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.entity = null;
        int count = nest.getRoosterCount();
        if (count <= 0) {
            return;
        }
        Level level = nest.getLevel();
        if (level == null) {
            return;
        }
        if (roosterPreview == null || roosterPreview.level() != level) {
            roosterPreview = ModEntityTypes.ROOSTER.get().create(level, EntitySpawnReason.LOAD);
        }
        if (roosterPreview == null) {
            return;
        }
        roosterPreview.setRobotRooster(ChickenItemHelper.isRobotRooster(
                nest.getItem(NestBlockEntity.ROOSTER_SLOT)));

        BlockState state = nest.getBlockState();
        if (!(state.getBlock() instanceof NestBlock)) {
            return;
        }
        // Treat the nest FACING as the direction the opening points toward.
        // The renderer works with the opposite (back) direction so we can
        // reuse the same transform pattern as the Roost renderer.
        renderState.facing = state.getValue(NestBlock.FACING).getOpposite();
        renderState.scale = Math.min(BASE_SCALE, BASE_SCALE + (count - 1) * SCALE_PER_ROOSTER);
        renderState.entity = dispatcher.extractEntity(roosterPreview, partialTicks);
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
        poseStack.scale(state.scale, state.scale, state.scale);
        dispatcher.submit(state.entity, camera, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }
}
