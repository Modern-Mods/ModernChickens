package strhercules.chickens.client.render.blockentity;

import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.blockentity.AvianChemicalConverterBlockEntity;
import strhercules.chickens.client.render.ClientFluids;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

/** Renders the active chemical converter's stored chemical as a visible tank fill. */
public final class AvianChemicalConverterBlockEntityRenderer
        extends AvianMachineBlockEntityRenderer<AvianChemicalConverterBlockEntity> {
    public AvianChemicalConverterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void extractStoredContents(AvianChemicalConverterBlockEntity machine, Level level,
            AvianMachineRenderState state) {
        ChemicalEggRegistryItem chemical = machine.getStoredEntry();
        TankContents contents = chemical == null
                ? null
                : new TankContents(ClientFluids.blockSprite(chemical.getTexture()), chemical.getEggColor(),
                        machine.getChemicalAmount(), machine.getTankCapacity());
        addTank(state, contents, TANK_MIN, TANK_MAX);
    }
}
