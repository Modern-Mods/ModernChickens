package strhercules.chickens.integration.jade;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.blockentity.AvianFluxConverterBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

/**
 * Streams the converter's RF buffer to Jade.
 */
enum AvianFluxConverterDataProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    private static final Identifier ID = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "avian_flux_converter");

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (!(accessor.getBlockEntity() instanceof AvianFluxConverterBlockEntity converter)) {
            return;
        }
        HudData.Builder builder = HudData.builder();
        builder.addEnergy(converter.getEnergyStored(), converter.getEnergyCapacity());
        HudData.write(data, builder.build());
    }

    @Override
    public Identifier getUid() {
        return ID;
    }
}
