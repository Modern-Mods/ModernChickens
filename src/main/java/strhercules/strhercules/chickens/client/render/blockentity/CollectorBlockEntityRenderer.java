package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.blockentity.CollectorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Draws a handful of the collector's stored items as a lazy orbit above the
 * crate. The original Roost mod baked different item meshes into the block
 * model; rendering real item stacks here recreates that presentation while
 * keeping the logic data driven.
 */
public class CollectorBlockEntityRenderer
        implements BlockEntityRenderer<CollectorBlockEntity, CollectorBlockEntityRenderer.CollectorRenderState> {
    private static final int MAX_DISPLAY_ITEMS = 4;
    private static final float ORBIT_RADIUS = 0.25F;
    private final ItemModelResolver itemModelResolver;

    public CollectorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        // Pull the shared item resolver from the context so we reuse the same
        // model cache as vanilla containers.
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public CollectorRenderState createRenderState() {
        return new CollectorRenderState();
    }

    @Override
    public void extractRenderState(CollectorBlockEntity collector, CollectorRenderState renderState,
            float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(collector, renderState, partialTicks, cameraPosition,
                breakProgress);
        renderState.items.clear();
        Level level = collector.getLevel();
        if (level == null) {
            level = Minecraft.getInstance().level;
        }
        renderState.time = getAnimationTime(partialTicks);
        for (ItemStack stack : collectDisplayStacks(collector)) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            itemModelResolver.updateForTopItem(itemState, stack, ItemDisplayContext.GROUND, level, null, 0);
            renderState.items.add(itemState);
        }
    }

    @Override
    public void submit(CollectorRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
            CameraRenderState camera) {
        float time = state.time;
        int total = state.items.size();
        for (int index = 0; index < total; index++) {
            ItemStackRenderState itemState = state.items.get(index);
            poseStack.pushPose();

            float angle = time + (360.0F / total) * index;
            double radians = Math.toRadians(angle);
            double x = ORBIT_RADIUS * Mth.cos((float) radians);
            double z = ORBIT_RADIUS * Mth.sin((float) radians);
            double bob = Mth.sin((time + index * 20.0F) * 0.05F) * 0.05F;

            // Anchor items slightly above the crate so the orbit reads clearly in-game.
            poseStack.translate(0.5D + x, 0.7D + bob, 0.5D + z);
            poseStack.mulPose(Axis.YP.rotationDegrees(angle));
            poseStack.scale(0.35F, 0.35F, 0.35F);

            itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }

    private static List<ItemStack> collectDisplayStacks(CollectorBlockEntity collector) {
        List<ItemStack> result = new ArrayList<>(MAX_DISPLAY_ITEMS);
        int outputEnd = collector.getOutputSlotIndex() + collector.getOutputSlotCount();
        for (int slot = collector.getOutputSlotIndex(); slot < outputEnd; slot++) {
            ItemStack stack = collector.getItem(slot);
            if (stack.isEmpty()) {
                continue;
            }
            boolean duplicate = result.stream().anyMatch(existing -> ItemStack.isSameItemSameComponents(existing, stack));
            if (duplicate) {
                continue;
            }
            // Render a single item so the orbit stays readable regardless of the real stack size.
            ItemStack copy = stack.copy();
            copy.setCount(1);
            result.add(copy);
            if (result.size() >= MAX_DISPLAY_ITEMS) {
                break;
            }
        }
        return result;
    }

    private static float getAnimationTime(float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null) {
            return partialTicks;
        }
        // Convert world time into degrees so each item has a smooth circular path.
        return (minecraft.level.getGameTime() + partialTicks) * 6.0F;
    }

    public static class CollectorRenderState extends BlockEntityRenderState {
        public final List<ItemStackRenderState> items = new ArrayList<>();
        public float time;
    }
}
