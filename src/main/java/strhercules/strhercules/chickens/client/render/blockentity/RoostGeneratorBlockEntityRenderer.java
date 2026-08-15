package strhercules.chickens.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import strhercules.chickens.block.RoostGeneratorBlock;
import strhercules.chickens.blockentity.AbstractChickenContainerBlockEntity.RenderData;
import strhercules.chickens.blockentity.RoostGeneratorBlockEntity;
import strhercules.chickens.client.render.ChickenRenderHelper;
import strhercules.chickens.entity.ChickensChicken;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

/** Renders the Flux Chicken at the generator's internal anchor. */
public final class RoostGeneratorBlockEntityRenderer implements BlockEntityRenderer<RoostGeneratorBlockEntity> {
    private static final float SCALE = 0.78F;
    private static final double FLOOR_OFFSET = -0.08D;
    private static final double FRONT_OFFSET = 0.08D;

    private final EntityRenderDispatcher dispatcher;

    public RoostGeneratorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = context.getEntityRenderer();
    }

    @Override
    public void render(RoostGeneratorBlockEntity generator, float partialTicks, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, int packedOverlay) {
        RenderData data = generator.getRenderData(RoostGeneratorBlockEntity.CHICKEN_SLOT);
        if (data == null || data.count() <= 0) {
            return;
        }
        ChickensChicken chicken = ChickenRenderHelper.getChicken(data.chicken().getId(), data.stats());
        BlockState state = generator.getBlockState();
        if (chicken == null || !(state.getBlock() instanceof RoostGeneratorBlock)) {
            return;
        }

        Direction facing = state.getValue(RoostGeneratorBlock.FACING);
        chicken.setRobotChicken(data.robotChicken());
        poseStack.pushPose();
        poseStack.translate(0.5D, FLOOR_OFFSET, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(0.0D, 0.0D, FRONT_OFFSET);
        poseStack.scale(SCALE, SCALE, SCALE);
        ChickenRenderHelper.resetPose(chicken);
        dispatcher.render(chicken, 0.0D, 0.0D, 0.0D, 180.0F, 0.0F, poseStack, buffer,
                LightTexture.FULL_BRIGHT);
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(RoostGeneratorBlockEntity blockEntity) {
        return true;
    }
}
