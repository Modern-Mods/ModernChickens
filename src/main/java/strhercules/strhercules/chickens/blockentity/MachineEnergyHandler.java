package strhercules.chickens.blockentity;

import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/** Energy handler over a buffer owned by a block entity, with transactional and direct access. */
public abstract class MachineEnergyHandler implements EnergyHandler {
    private final EnergyJournal journal = new EnergyJournal();

    protected abstract int storedEnergy();

    protected abstract void storeEnergy(int energy);

    protected abstract int energyCapacity();

    protected abstract int maxInsert();

    protected abstract int maxExtract();

    protected void onEnergyChanged() {
    }

    @Override
    public long getAmountAsLong() {
        return storedEnergy();
    }

    @Override
    public long getCapacityAsLong() {
        return energyCapacity();
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        int accepted = acceptableInsert(amount);
        if (accepted > 0) {
            journal.updateSnapshots(transaction);
            storeEnergy(storedEnergy() + accepted);
        }
        return accepted;
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        int removed = availableExtract(amount);
        if (removed > 0) {
            journal.updateSnapshots(transaction);
            storeEnergy(storedEnergy() - removed);
        }
        return removed;
    }

    public int receiveEnergy(int amount, boolean simulate) {
        int accepted = acceptableInsert(amount);
        if (accepted > 0 && !simulate) {
            storeEnergy(storedEnergy() + accepted);
            onEnergyChanged();
        }
        return accepted;
    }

    public int extractEnergy(int amount, boolean simulate) {
        int removed = availableExtract(amount);
        if (removed > 0 && !simulate) {
            storeEnergy(storedEnergy() - removed);
            onEnergyChanged();
        }
        return removed;
    }

    private int acceptableInsert(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int limit = maxInsert();
        if (limit <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(limit, Math.min(energyCapacity() - storedEnergy(), amount)));
    }

    private int availableExtract(int amount) {
        if (amount <= 0) {
            return 0;
        }
        int limit = maxExtract();
        if (limit <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(limit, Math.min(storedEnergy(), amount)));
    }

    private final class EnergyJournal extends SnapshotJournal<Integer> {
        @Override
        protected Integer createSnapshot() {
            return storedEnergy();
        }

        @Override
        protected void revertToSnapshot(Integer snapshot) {
            storeEnergy(snapshot);
        }

        @Override
        protected void onRootCommit(Integer originalState) {
            if (storedEnergy() != originalState) {
                onEnergyChanged();
            }
        }
    }
}
