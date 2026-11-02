package strhercules.chickens.liquidegg;

import strhercules.chickens.LiquidEggRegistry;
import strhercules.chickens.LiquidEggRegistryItem;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.registry.ModRegistry;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import javax.annotation.Nullable;

public final class LiquidEggFluidWrapper extends ItemAccessResourceHandler<FluidResource> {

    public LiquidEggFluidWrapper(ItemAccess itemAccess) {
        super(itemAccess, 1);
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource accessResource, int index) {
        LiquidEggRegistryItem entry = resolve(accessResource);
        if (entry == null) {
            return FluidResource.EMPTY;
        }
        FluidStack contained = entry.createFluidStack();
        return contained.isEmpty() ? FluidResource.EMPTY : FluidResource.of(contained);
    }

    @Override
    protected int getAmountFrom(ItemResource accessResource, int index) {
        LiquidEggRegistryItem entry = resolve(accessResource);
        return entry != null ? entry.getVolume() : 0;
    }

    @Nullable
    @Override
    protected ItemResource update(ItemResource accessResource, int index, FluidResource newResource, int newAmount) {
        return newAmount == 0 ? null : ItemResource.EMPTY;
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        return getAmountFrom(itemAccess.getResource(), index);
    }

    @Nullable
    private static LiquidEggRegistryItem resolve(ItemResource accessResource) {
        if (!accessResource.is(ModRegistry.LIQUID_EGG.get())) {
            return null;
        }
        return LiquidEggRegistry.findById(ChickenItemHelper.getChickenType(accessResource.toStack()));
    }
}
