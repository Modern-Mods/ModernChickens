package strhercules.chickens.blockentity;

import net.minecraft.util.Mth;

/** Machine energy handler that keeps its own buffer, capacity and transfer limits. */
public abstract class StoredEnergyHandler extends MachineEnergyHandler {
    protected int energy;
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    protected StoredEnergyHandler(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    protected int storedEnergy() {
        return energy;
    }

    @Override
    protected void storeEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    protected int energyCapacity() {
        return capacity;
    }

    @Override
    protected int maxInsert() {
        return maxReceive;
    }

    @Override
    protected int maxExtract() {
        return maxExtract;
    }

    public int getEnergyStored() {
        return energy;
    }

    public int getMaxEnergyStored() {
        return capacity;
    }

    void setEnergy(int energy) {
        this.energy = Mth.clamp(energy, 0, capacity);
    }
}
