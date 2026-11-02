package strhercules.chickens.client.render.blockentity;

import strhercules.chickens.ChemicalEggRegistry;
import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.GasEggRegistry;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity;
import strhercules.chickens.client.render.ClientFluids;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

/** Renders the active dousing machine's stored reagent as two visible tanks. */
public final class AvianDousingMachineBlockEntityRenderer
        extends AvianMachineBlockEntityRenderer<AvianDousingMachineBlockEntity> {
    private static final float TANK_MID = 8.0F / 16.0F;

    public AvianDousingMachineBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected boolean hasDivider() {
        return true;
    }

    @Override
    protected void extractStoredContents(AvianDousingMachineBlockEntity machine, Level level,
            AvianMachineRenderState state) {
        TankContents chemical = resolveChemicalContents(machine);
        TankContents fluid = resolveFluidContents(machine.getFluid(), machine.getLiquidAmount(),
                machine.getLiquidCapacity());
        addTank(state, chemical, TANK_MIN, TANK_MID);
        addTank(state, fluid, TANK_MID, TANK_MAX);
    }

    @Nullable
    private static TankContents resolveChemicalContents(AvianDousingMachineBlockEntity machine) {
        Identifier chemicalId = machine.getChemicalId();
        if (chemicalId == null || machine.getChemicalAmount() <= 0) {
            return null;
        }
        ChemicalEggRegistryItem chemical = ChemicalEggRegistry.findByChemical(chemicalId);
        if (chemical == null) {
            chemical = GasEggRegistry.findByChemical(chemicalId);
        }
        return chemical == null
                ? null
                : new TankContents(ClientFluids.blockSprite(chemical.getTexture()), chemical.getEggColor(),
                        machine.getChemicalAmount(), machine.getChemicalCapacity());
    }
}
