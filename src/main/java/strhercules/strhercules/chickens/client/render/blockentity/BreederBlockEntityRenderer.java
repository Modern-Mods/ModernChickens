package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.BreederBlock;
import strhercules.chickens.blockentity.AbstractChickenContainerBlockEntity.RenderData;
import strhercules.chickens.blockentity.BreederBlockEntity;
import strhercules.chickens.client.render.ChickenRenderHelper;
import strhercules.chickens.entity.ChickensChicken;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * Visualises the breeder's parent chickens and seed pile. Rendering uses the
 * same animated chickens as the roost renderer so both blocks retain the
 * distinct look of the original Roost mod.
 */
public class BreederBlockEntityRenderer
        implements BlockEntityRenderer<BreederBlockEntity, BreederBlockEntityRenderer.BreederRenderState> {
    private final EntityRenderDispatcher dispatcher;
    private final ItemModelResolver itemModelResolver;

    public BreederBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.entityRenderer();
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BreederRenderState createRenderState() {
        return new BreederRenderState();
    }

    @Override
    public void extractRenderState(BreederBlockEntity breeder, BreederRenderState renderState, float partialTicks,
            Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(breeder, renderState, partialTicks, cameraPosition, breakProgress);
        renderState.valid = false;
        renderState.leftChicken = null;
        renderState.rightChicken = null;
        renderState.seeds.clear();
        BlockState state = breeder.getBlockState();
        if (!(state.getBlock() instanceof BreederBlock)) {
            return;
        }
        renderState.valid = true;
        renderState.facing = state.getValue(BreederBlock.FACING);
        renderState.leftChicken = extractChicken(breeder, BreederBlockEntity.LEFT_CHICKEN_SLOT, partialTicks,
                renderState.lightCoords);
        renderState.rightChicken = extractChicken(breeder, BreederBlockEntity.RIGHT_CHICKEN_SLOT, partialTicks,
                renderState.lightCoords);
        ItemStack seeds = breeder.getItem(BreederBlockEntity.SEED_SLOT);
        if (!seeds.isEmpty()) {
            itemModelResolver.updateForTopItem(renderState.seeds, seeds, ItemDisplayContext.GROUND,
                    breeder.getLevel(), null, 0);
        }
    }

    @Nullable
    private EntityRenderState extractChicken(BreederBlockEntity breeder, int slot, float partialTicks,
            int packedLight) {
        RenderData data = breeder.getRenderData(slot);
        if (data == null) {
            return null;
        }
        ChickensChicken chicken = ChickenRenderHelper.getChicken(data.chicken().getId(), data.stats());
        if (chicken == null) {
            return null;
        }
        chicken.setRobotChicken(data.robotChicken());
        ChickenRenderHelper.resetPose(chicken);
        EntityRenderState entityState = dispatcher.extractEntity(chicken, partialTicks);
        entityState.lightCoords = packedLight;
        return entityState;
    }

    @Override
    public void submit(BreederRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera) {
        if (!state.valid) {
            return;
        }
        Direction facing = state.facing;
        submitChicken(state.leftChicken, -0.25D, facing, poseStack, submitNodeCollector, camera);
        submitChicken(state.rightChicken, 0.25D, facing, poseStack, submitNodeCollector, camera);
        if (!state.seeds.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5D, 0.2D, 0.5D);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
            poseStack.translate(0.0D, 0.1D, -0.1D);
            poseStack.scale(0.5F, 0.5F, 0.5F);
            state.seeds.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    private void submitChicken(@Nullable EntityRenderState chicken, double xOffset, Direction facing,
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (chicken == null) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.25D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - facing.toYRot()));
        poseStack.translate(xOffset, 0.0D, 0.1D);
        poseStack.scale(0.35F, 0.35F, 0.35F);
        dispatcher.submit(chicken, camera, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

    public static class BreederRenderState extends BlockEntityRenderState {
        public boolean valid;
        public Direction facing = Direction.NORTH;
        public @Nullable EntityRenderState leftChicken;
        public @Nullable EntityRenderState rightChicken;
        public final ItemStackRenderState seeds = new ItemStackRenderState();
    }
}
