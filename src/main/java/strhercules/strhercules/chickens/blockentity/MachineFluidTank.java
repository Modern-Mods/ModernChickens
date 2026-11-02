package strhercules.chickens.blockentity;

import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;

import java.util.function.Predicate;

/** Single-slot fluid tank with a mutable capacity and a stored-fluid validator. */
public class MachineFluidTank extends FluidStacksResourceHandler {
    private final Predicate<FluidStack> validator;
    private final Runnable changeListener;

    public MachineFluidTank(int capacity, Predicate<FluidStack> validator, Runnable changeListener) {
        super(1, capacity);
        this.validator = validator;
        this.changeListener = changeListener;
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return validator.test(resource.toStack(1));
    }

    @Override
    protected void onContentsChanged(int index, FluidStack previousContents) {
        changeListener.run();
    }

    public FluidStack getFluid() {
        return stacks.get(0);
    }

    public int getFluidAmount() {
        return stacks.get(0).getAmount();
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isEmpty() {
        return stacks.get(0).isEmpty();
    }

    public void setFluid(FluidStack stack) {
        stacks.set(0, stack.copy());
    }

    public int fill(FluidStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return 0;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int inserted = insert(0, FluidResource.of(stack), stack.getAmount(), transaction);
            if (!simulate) {
                transaction.commit();
            }
            return inserted;
        }
    }

    public FluidStack drain(int maxAmount, boolean simulate) {
        FluidResource resource = getResource(0);
        if (resource.isEmpty() || maxAmount <= 0) {
            return FluidStack.EMPTY;
        }
        try (Transaction transaction = Transaction.openRoot()) {
            int extracted = extract(0, resource, maxAmount, transaction);
            if (!simulate) {
                transaction.commit();
            }
            return resource.toStack(extracted);
        }
    }

    public void saveFluid(ValueOutput output) {
        FluidStack fluid = getFluid();
        if (!fluid.isEmpty()) {
            output.store(FluidStack.MAP_CODEC, fluid);
        }
    }

    public void loadFluid(ValueInput input) {
        setFluid(input.read(FluidStack.MAP_CODEC).orElse(FluidStack.EMPTY));
    }
}
