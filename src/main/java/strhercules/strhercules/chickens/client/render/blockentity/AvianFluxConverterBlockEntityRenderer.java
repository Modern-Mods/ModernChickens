package strhercules.chickens.client.render.blockentity;

import strhercules.chickens.blockentity.AvianFluxConverterBlockEntity;
import strhercules.chickens.client.render.ClientFluids;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

/** Renders the active flux converter's stored RF as a red energy tank fill. */
public final class AvianFluxConverterBlockEntityRenderer
        extends AvianMachineBlockEntityRenderer<AvianFluxConverterBlockEntity> {
    private static final Identifier ENERGY_TEXTURE =
            Identifier.withDefaultNamespace("block/white_concrete");
    private static final int ENERGY_TINT = 0xFF3C3C;

    public AvianFluxConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void extractStoredContents(AvianFluxConverterBlockEntity machine, Level level,
            AvianMachineRenderState state) {
        TankContents contents = machine.getEnergyStored() <= 0
                ? null
                : new TankContents(ClientFluids.blockSprite(ENERGY_TEXTURE), ENERGY_TINT,
                        machine.getEnergyStored(), machine.getEnergyCapacity());
        addTank(state, contents, TANK_MIN, TANK_MAX);
    }
}
