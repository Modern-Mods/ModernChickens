package strhercules.chickens.blockentity;

import strhercules.chickens.integration.mekanism.MekanismChemicalHelper;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/** Direction-aware capability views used by the independent side configurator. */
public final class MachineCapabilityWrappers {
    private MachineCapabilityWrappers() {
    }

    public static EnergyHandler energy(EnergyHandler delegate, MachineSideConfig config,
            @Nullable Direction side) {
        return new EnergyHandler() {
            @Override
            public long getAmountAsLong() {
                return delegate.getAmountAsLong();
            }

            @Override
            public long getCapacityAsLong() {
                return delegate.getCapacityAsLong();
            }

            @Override
            public int insert(int amount, TransactionContext transaction) {
                return config.allows(side, MachineSideConfig.Channel.ENERGY, true)
                        ? delegate.insert(amount, transaction) : 0;
            }

            @Override
            public int extract(int amount, TransactionContext transaction) {
                return config.allows(side, MachineSideConfig.Channel.ENERGY, false)
                        ? delegate.extract(amount, transaction) : 0;
            }
        };
    }

    public static ResourceHandler<FluidResource> fluid(ResourceHandler<FluidResource> delegate,
            MachineSideConfig config, @Nullable Direction side) {
        return new ResourceHandler<>() {
            @Override
            public int size() {
                return delegate.size();
            }

            @Override
            public FluidResource getResource(int index) {
                return delegate.getResource(index);
            }

            @Override
            public long getAmountAsLong(int index) {
                return delegate.getAmountAsLong(index);
            }

            @Override
            public long getCapacityAsLong(int index, FluidResource resource) {
                return delegate.getCapacityAsLong(index, resource);
            }

            @Override
            public boolean isValid(int index, FluidResource resource) {
                return config.allows(side, MachineSideConfig.Channel.FLUIDS, true)
                        && delegate.isValid(index, resource);
            }

            @Override
            public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return config.allows(side, MachineSideConfig.Channel.FLUIDS, true)
                        ? delegate.insert(index, resource, amount, transaction) : 0;
            }

            @Override
            public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
                return config.allows(side, MachineSideConfig.Channel.FLUIDS, false)
                        ? delegate.extract(index, resource, amount, transaction) : 0;
            }
        };
    }

    /** Adds side-aware input/output policy to Mekanism's reflective chemical handler. */
    @Nullable
    public static Object chemical(@Nullable Object delegate, MachineSideConfig config,
            @Nullable Direction side) {
        if (delegate == null) {
            return null;
        }
        if (side == null) {
            return delegate;
        }
        try {
            Class<?> handlerType = Class.forName("mekanism.api.chemical.IChemicalHandler");
            return Proxy.newProxyInstance(handlerType.getClassLoader(), new Class<?>[] { handlerType },
                    (proxy, method, args) -> {
                        String name = method.getName();
                        if ("insertChemical".equals(name)
                                && !config.allows(side, MachineSideConfig.Channel.CHEMICALS, true)) {
                            return firstChemicalStack(args);
                        }
                        if ("extractChemical".equals(name)
                                && !config.allows(side, MachineSideConfig.Channel.CHEMICALS, false)) {
                            return MekanismChemicalHelper.emptyStack();
                        }
                        if ("isValid".equals(name)
                                && !config.allows(side, MachineSideConfig.Channel.CHEMICALS, true)) {
                            return false;
                        }
                        if ("setChemicalInTank".equals(name)
                                && !config.allows(side, MachineSideConfig.Channel.CHEMICALS, true)) {
                            return null;
                        }
                        if ("equals".equals(name)) {
                            return proxy == (args != null && args.length == 1 ? args[0] : null);
                        }
                        if ("hashCode".equals(name)) {
                            return System.identityHashCode(proxy);
                        }
                        if ("toString".equals(name)) {
                            return delegate.toString();
                        }
                        try {
                            return method.invoke(delegate, args);
                        } catch (InvocationTargetException exception) {
                            throw exception.getCause();
                        }
                    });
        } catch (ReflectiveOperationException | LinkageError exception) {
            // The caller only reaches this path when a compatible handler was
            // already created. Keep that handler usable if a future Mekanism
            // API changes its interface name or class loader.
            return delegate;
        }
    }

    private static Object firstChemicalStack(@Nullable Object[] args) {
        if (args != null) {
            for (Object arg : args) {
                if (arg != null && arg.getClass().getName().contains("ChemicalStack")) {
                    return arg;
                }
            }
        }
        return MekanismChemicalHelper.emptyStack();
    }
}
