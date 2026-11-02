package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.RoostBlock;
import strhercules.chickens.blockentity.AbstractChickenContainerBlockEntity.RenderData;
import strhercules.chickens.blockentity.RoostBlockEntity;
import strhercules.chickens.client.render.ChickenRenderHelper;
import strhercules.chickens.entity.ChickensChicken;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Renders the chicken sprite inside a roost by reusing the animated Chicken
 * entity. The pose and scaling are tuned to match the legacy Roost look.
 */
public class RoostBlockEntityRenderer implements BlockEntityRenderer<RoostBlockEntity, EntityDisplayRenderState> {
    private static final float BASE_SCALE = 0.9F;
    private static final float SCALE_PER_CHICKEN = 0.015F;
    private static final double FRONT_OFFSET = 0.04D;
    private static final double FLOOR_OFFSET = -0.11D;

    private final EntityRenderDispatcher dispatcher;
    private Rooster roosterPreview;

    public RoostBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.entityRenderer();
    }

    @Override
    public EntityDisplayRenderState createRenderState() {
        return new EntityDisplayRenderState();
    }

    @Override
    public void extractRenderState(RoostBlockEntity roost, EntityDisplayRenderState renderState, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(roost, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.entity = null;
        BlockState state = roost.getBlockState();
        if (!(state.getBlock() instanceof RoostBlock)) {
            return;
        }
        ItemStack slotStack = roost.getItem(RoostBlockEntity.CHICKEN_SLOT);
        if (!slotStack.isEmpty() && ChickenItemHelper.isRooster(slotStack)) {
            extractRooster(roost, slotStack, state, renderState, partialTicks);
            return;
        }

        RenderData data = roost.getRenderData(RoostBlockEntity.CHICKEN_SLOT);
        if (data == null || data.count() <= 0) {
            return;
        }
        ChickensChicken chicken = ChickenRenderHelper.getChicken(data.chicken().getId(), data.stats());
        if (chicken == null) {
            return;
        }
        chicken.setRobotChicken(data.robotChicken());

        renderState.facing = state.getValue(RoostBlock.FACING);
        renderState.scale = Math.min(BASE_SCALE, BASE_SCALE + (data.count() - 1) * SCALE_PER_CHICKEN);
        ChickenRenderHelper.resetPose(chicken);
        renderState.entity = dispatcher.extractEntity(chicken, partialTicks);
        renderState.entity.lightCoords = LightCoordsUtil.FULL_BRIGHT;
    }

    private void extractRooster(RoostBlockEntity roost, ItemStack stack, BlockState state,
            EntityDisplayRenderState renderState, float partialTicks) {
        Level level = roost.getLevel();
        if (level == null) {
            return;
        }
        if (roosterPreview == null || roosterPreview.level() != level) {
            roosterPreview = ModEntityTypes.ROOSTER.get().create(level, EntitySpawnReason.LOAD);
        }
        if (roosterPreview == null) {
            return;
        }
        roosterPreview.setRobotRooster(ChickenItemHelper.isRobotRooster(stack));

        renderState.facing = state.getValue(RoostBlock.FACING);
        renderState.scale = Math.min(BASE_SCALE, BASE_SCALE + (stack.getCount() - 1) * SCALE_PER_CHICKEN);
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
