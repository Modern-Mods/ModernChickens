package strhercules.chickens.client.render.blockentity;

import strhercules.chickens.blockentity.AvianFluidConverterBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

/** Renders the active fluid converter's stored fluid as a visible tank fill. */
public final class AvianFluidConverterBlockEntityRenderer
        extends AvianMachineBlockEntityRenderer<AvianFluidConverterBlockEntity> {
    public AvianFluidConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void extractStoredContents(AvianFluidConverterBlockEntity machine, Level level,
            AvianMachineRenderState state) {
        TankContents contents = resolveFluidContents(machine.getFluid(), machine.getFluidAmount(),
                machine.getTankCapacity());
        addTank(state, contents, TANK_MIN, TANK_MAX);
    }
}
