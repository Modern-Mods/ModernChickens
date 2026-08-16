package strhercules.chickens.blockentity;

import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.block.RoostGeneratorBlock;
import strhercules.chickens.config.ChickensConfigHolder;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.item.ChickenStats;
import strhercules.chickens.item.UpgradeItem;
import strhercules.chickens.menu.RoostGeneratorMenu;
import strhercules.chickens.registry.ModBlockEntities;
import strhercules.chickens.registry.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EnergyStorage;
import net.neoforged.neoforge.energy.IEnergyStorage;

import javax.annotation.Nullable;
import java.util.List;

/** A one-slot FE generator that converts Redstone Flux Chicken stats directly into FE/t. */
public class RoostGeneratorBlockEntity extends AbstractChickenContainerBlockEntity {
    public static final int CHICKEN_SLOT = 0;
    public static final int UPGRADE_SLOT_COUNT = 2;
    public static final int UPGRADE_SLOT_ONE = 0;
    public static final int UPGRADE_SLOT_TWO = 1;

    private static final int MAX_STANDARD_UPGRADES = 4;
    private static final int MAX_CHICKENS = 16;
    private static final int MAX_EXCITER_UPGRADES = 8;
    private static final int MAX_OUTPUT_UPGRADES = 4;
    private static final int MAX_STABILIZER_UPGRADES = 4;
    private static final int MAX_SURGE_UPGRADES = 4;
    private static final int MAX_GOVERNOR_UPGRADES = 4;
    private static final int DATA_FIELD_COUNT = 4;

    private final MachineEnergyStorage energyStorage = new MachineEnergyStorage();
    private final ContainerData generatorData = new ContainerData() {
        @Override
        public int get(int index) {
            if (index < 0 || index >= DATA_FIELD_COUNT * 2) {
                return 0;
            }
            int value = switch (index / 2) {
                case 0 -> getEnergyStored();
                case 1 -> getEnergyCapacity();
                case 2 -> getGenerationPerTick();
                case 3 -> getMaxOutputPerTick();
                default -> 0;
            };
            return (index & 1) == 0 ? value & 0xFFFF : (value >>> 16) & 0xFFFF;
        }

        @Override
        public void set(int index, int value) {
            if (index < 0 || index >= DATA_FIELD_COUNT * 2) {
                return;
            }
            int field = index / 2;
            int current = switch (field) {
                case 0 -> getEnergyStored();
                case 1 -> getEnergyCapacity();
                case 2 -> getGenerationPerTick();
                case 3 -> getMaxOutputPerTick();
                default -> 0;
            };
            int merged = (index & 1) == 0
                    ? (current & 0xFFFF0000) | (value & 0xFFFF)
                    : (current & 0x0000FFFF) | ((value & 0xFFFF) << 16);
            switch (field) {
                case 0 -> energyStorage.setEnergy(Mth.clamp(merged, 0, getEnergyCapacity()));
                case 1 -> {
                    capacity = Math.max(1, merged);
                    energyStorage.setLimits(capacity);
                }
                case 2 -> clientGeneration = merged;
                case 3 -> clientMaxOutput = Math.max(0, merged);
                default -> {
                }
            }
        }

        @Override
        public int getCount() {
            return DATA_FIELD_COUNT * 2;
        }
    };

    private int capacity;
    private int fluctuationTicks;
    private double fluctuation;
    private boolean hasFluctuationRoll;
    private long outputBudgetTick = Long.MIN_VALUE;
    private int extractedThisTick;
    private int clientGeneration;
    private int clientMaxOutput;
    private boolean cachedActiveState;

    public RoostGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ROOST_GENERATOR.get(), pos, state, 1, 1, UPGRADE_SLOT_COUNT);
        cachedActiveState = state.hasProperty(RoostGeneratorBlock.LIT)
                && state.getValue(RoostGeneratorBlock.LIT);
        syncWithConfig();
    }

    @Override
    protected void runServerTick(Level level) {
        if (level.isClientSide) {
            return;
        }
        syncWithConfig();
        prepareOutputBudget(level.getGameTime());

        if (!isRedstoneFluxChicken(getItem(CHICKEN_SLOT))) {
            fluctuation = 0.0D;
            fluctuationTicks = 0;
            hasFluctuationRoll = false;
            updateActiveState(level, false);
            return;
        }

        rollFluctuationIfNeeded(level.random);
        int generation = getGenerationPerTick();
        applyGeneration(generation);
        pushEnergyToNeighbors(level);
        if (generation != 0 && level instanceof ServerLevel serverLevel
                && serverLevel.getGameTime() % 10L == 0L) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    worldPosition.getX() + 0.5D, worldPosition.getY() + 0.6D,
                    worldPosition.getZ() + 0.5D, 4, 0.15D, 0.12D, 0.15D, 0.03D);
        }
        updateActiveState(level, getGenerationPerTick() != 0 || getEnergyStored() > 0);
        setChanged();
    }

    @Override
    protected boolean spawnChickenItem(RandomSource random) {
        return false;
    }

    @Override
    protected int requiredSeedsForDrop() {
        return 0;
    }

    @Override
    protected double speedMultiplier() {
        return 1.0D;
    }

    @Override
    protected int getMaxStackSizeForSlotWithStackUpgrades(int slot, ItemStack stack, int stackUpgradeCount) {
        if (slot == CHICKEN_SLOT) {
            return Math.min(MAX_CHICKENS, stack.getMaxStackSize());
        }
        return super.getMaxStackSizeForSlotWithStackUpgrades(slot, stack, stackUpgradeCount);
    }

    @Override
    protected int getChickenSlotCount() {
        return 1;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.chickens.roost_generator");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInventory, ContainerData dataAccess) {
        return new RoostGeneratorMenu(id, playerInventory, this, generatorData);
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new RoostGeneratorMenu(id, playerInventory, this, generatorData);
    }

    @Override
    public ContainerData getDataAccess() {
        return generatorData;
    }

    @Override
    protected ChickenContainerEntry createChickenData(int slot, ItemStack stack) {
        if (slot != CHICKEN_SLOT || !isRedstoneFluxChicken(stack)) {
            return null;
        }
        ChickensRegistryItem description = ChickenItemHelper.resolve(stack);
        return description == null
                ? null
                : new ChickenContainerEntry(description, ChickenItemHelper.getStats(stack));
    }

    @Override
    public RenderData getRenderData(int slot) {
        if (slot != CHICKEN_SLOT) {
            return null;
        }
        ItemStack stack = getItem(CHICKEN_SLOT);
        ChickenContainerEntry entry = createChickenData(CHICKEN_SLOT, stack);
        return entry == null ? null : new RenderData(entry.chicken(), entry.stats(), stack.getCount());
    }

    @Override
    public boolean hasRequiredChickens() {
        return isRedstoneFluxChicken(getItem(CHICKEN_SLOT));
    }

    @Override
    public boolean hasRequiredSeeds() {
        return true;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public double getProgressFraction() {
        return 0.0D;
    }

    @Override
    public int getTotalLayTimeTicks() {
        return 0;
    }

    @Override
    public int getRemainingLayTimeTicks() {
        return 0;
    }

    @Override
    public int getProgressIncrementPerTick() {
        return 0;
    }

    @Override
    public void storeTooltipData(CompoundTag tag) {
        tag.putFloat("Progress", 0.0F);
        tag.putBoolean("HasSeeds", true);
        tag.putBoolean("HasChickens", hasRequiredChickens());
        tag.putInt("RequiredSeeds", 0);
    }

    @Override
    public void appendTooltip(List<Component> tooltip, CompoundTag data) {
        if (!hasRequiredChickens()) {
            tooltip.add(Component.translatable("tooltip.chickens.container.empty"));
        }
    }

    @Override
    public boolean canPlaceUpgrade(int slot, ItemStack stack) {
        if (slot < 0 || slot >= UPGRADE_SLOT_COUNT || stack.isEmpty()) {
            return false;
        }
        UpgradeItem.Kind kind = upgradeKind(stack);
        if (kind == null) {
            return false;
        }
        return countUpgradeExcept(kind, slot) < upgradeCap(kind);
    }

    @Override
    public int getUpgradeMaxStackSize(int slot) {
        return MAX_STANDARD_UPGRADES;
    }

    @Override
    public boolean canRemoveUpgrade(int slot, int count) {
        return slot >= 0 && slot < UPGRADE_SLOT_COUNT && count > 0;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        if (index == CHICKEN_SLOT) {
            return sideConfig().allows(direction, MachineSideConfig.Channel.ITEMS, true)
                    && isRedstoneFluxChicken(stack);
        }
        return super.canPlaceItemThroughFace(index, stack, direction);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == CHICKEN_SLOT && !stack.isEmpty() && !isRedstoneFluxChicken(stack)) {
            return;
        }
        if (index >= getUpgradeSlotIndex(UPGRADE_SLOT_ONE) && index < getContainerSize() && !stack.isEmpty()) {
            int slot = index - getUpgradeSlotIndex(UPGRADE_SLOT_ONE);
            UpgradeItem.Kind kind = upgradeKind(stack);
            if (kind == null) {
                return;
            }
            int allowed = upgradeCap(kind) - countUpgradeExcept(kind, slot);
            if (allowed <= 0) {
                return;
            }
            stack = stack.copyWithCount(Math.min(stack.getCount(), Math.min(MAX_STANDARD_UPGRADES, allowed)));
        }
        super.setItem(index, stack);
        if (index >= getUpgradeSlotIndex(UPGRADE_SLOT_ONE)) {
            syncWithConfig();
            hasFluctuationRoll = false;
        }
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack removed = super.removeItem(index, count);
        if (!removed.isEmpty() && index >= getUpgradeSlotIndex(UPGRADE_SLOT_ONE)) {
            syncWithConfig();
            hasFluctuationRoll = false;
        }
        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack removed = super.removeItemNoUpdate(index);
        if (!removed.isEmpty() && index >= getUpgradeSlotIndex(UPGRADE_SLOT_ONE)) {
            syncWithConfig();
            hasFluctuationRoll = false;
        }
        return removed;
    }

    @Override
    public void clearContent() {
        super.clearContent();
        syncWithConfig();
        fluctuation = 0.0D;
        hasFluctuationRoll = false;
    }

    public boolean pullChickenOut(Player player) {
        ItemStack stack = getItem(CHICKEN_SLOT);
        if (stack.isEmpty()) {
            return false;
        }
        setItem(CHICKEN_SLOT, ItemStack.EMPTY);
        int maxExternalStackSize = getLegalExternalStackSize(stack);
        ItemStack remaining = stack.copy();
        while (!remaining.isEmpty()) {
            ItemStack toGive = remaining.split(maxExternalStackSize);
            if (!player.addItem(toGive) && !toGive.isEmpty()) {
                player.drop(toGive, false);
            }
        }
        return true;
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getEnergyCapacity() {
        return capacity;
    }

    /** Returns the signed current FE/t after efficiency, Exciter, Stabilizer, and Governor effects. */
    public int getGenerationPerTick() {
        if (level != null && level.isClientSide) {
            return clientGeneration;
        }
        return safeInt(getNetGenerationPerTick());
    }

    public int getNominalGenerationPerTick() {
        return safeInt(getNominalGeneration());
    }

    /** Returns the non-negative FE/t export ceiling for this tick. */
    public int getMaxOutputPerTick() {
        if (level != null && level.isClientSide) {
            return clientMaxOutput;
        }
        double generation = getNetGenerationPerTick();
        if (generation <= 0.0D) {
            return 0;
        }
        return safeInt(generation * getOutputRatio());
    }

    public int getOutputRatioPercent() {
        return (int) Math.round(getOutputRatio() * 100.0D);
    }

    public double getGovernorGuaranteedBonus() {
        return governorReduction() * ChickensConfigHolder.get().getRoostGeneratorGovernorConversion();
    }

    public IEnergyStorage getEnergyStorage(@Nullable Direction direction) {
        return MachineCapabilityWrappers.energy(energyStorage, sideConfig(), direction);
    }

    public int getComparatorOutput() {
        return capacity <= 0 ? 0 : Math.round(15.0F * getEnergyStored() / (float) capacity);
    }

    public static boolean isRedstoneFluxChicken(ItemStack stack) {
        if (!ChickenItemHelper.isChicken(stack) || ChickenItemHelper.isRooster(stack)) {
            return false;
        }
        ChickensRegistryItem description = ChickenItemHelper.resolve(stack);
        return description != null && (description.getId() == 404
                || "RedstoneFluxChicken".equalsIgnoreCase(description.getEntityName()));
    }

    private void rollFluctuationIfNeeded(RandomSource random) {
        var config = ChickensConfigHolder.get();
        int interval = Math.max(1, config.getRoostGeneratorFluctuationIntervalTicks());
        if (hasFluctuationRoll && ++fluctuationTicks < interval) {
            return;
        }
        double positiveAmplitude = positiveFluctuationAmplitude();
        double negativeAmplitude = negativeFluctuationAmplitude();
        fluctuation = -negativeAmplitude
                + random.nextDouble() * (negativeAmplitude + positiveAmplitude);
        fluctuationTicks = 0;
        hasFluctuationRoll = true;
    }

    private void applyGeneration(double generation) {
        int amount = safeInt(Math.abs(generation));
        if (generation > 0.0D) {
            energyStorage.addEnergy(amount);
        } else if (generation < 0.0D) {
            // Negative generation is an internal-buffer drain only. It never
            // calls an adjacent capability and can never create negative FE.
            energyStorage.removeEnergy(amount);
        }
    }

    private void pushEnergyToNeighbors(Level level) {
        for (Direction direction : Direction.values()) {
            if (!sideConfig().allows(direction, MachineSideConfig.Channel.ENERGY, false)) {
                continue;
            }
            IEnergyStorage target = level.getCapability(Capabilities.EnergyStorage.BLOCK,
                    worldPosition.relative(direction), direction.getOpposite());
            if (target == null) {
                continue;
            }
            int available = energyStorage.extractEnergy(Integer.MAX_VALUE, true);
            if (available <= 0) {
                return;
            }
            int accepted = target.receiveEnergy(available, true);
            if (accepted <= 0) {
                continue;
            }
            int extracted = energyStorage.extractEnergy(accepted, false);
            if (extracted > 0) {
                target.receiveEnergy(extracted, false);
            }
        }
    }

    private double getNetGenerationPerTick() {
        return getNominalGeneration() * (1.0D + getGovernorGuaranteedBonus() + fluctuation);
    }

    private double getNominalGeneration() {
        ChickenContainerEntry entry = createChickenData(CHICKEN_SLOT, getItem(CHICKEN_SLOT));
        if (entry == null) {
            return 0.0D;
        }
        ChickenStats stats = entry.stats();
        double averageStats = (stats.growth() + stats.gain() + stats.strength()) / 3.0D;
        double generation = ChickensConfigHolder.get().getRoostGeneratorBaseGeneration()
                * getItem(CHICKEN_SLOT).getCount() * averageStats;
        int efficiencyUpgrades = countUpgrade(UpgradeItem.Kind.RF_EFFICIENCY);
        int exciters = countUpgrade(UpgradeItem.Kind.RF_EXCITER);
        generation *= 1.0D + efficiencyUpgrades
                * ChickensConfigHolder.get().getRoostGeneratorEfficiencyBonus();
        generation *= 1.0D + exciters
                * ChickensConfigHolder.get().getRoostGeneratorExciterGenerationBonus();
        return generation;
    }

    private double getOutputRatio() {
        var config = ChickensConfigHolder.get();
        double baseRatio = config.getRoostGeneratorBaseOutputRatio();
        int surgeUpgrades = countUpgrade(UpgradeItem.Kind.RF_SURGE);
        if (surgeUpgrades > 0 && getEnergyStored() > Math.round(getEnergyCapacity() * 0.75D)) {
            baseRatio *= 1.0D + surgeUpgrades * config.getRoostGeneratorSurgeBonus();
        }
        double ratio = baseRatio + countUpgrade(UpgradeItem.Kind.RF_OUTPUT)
                * config.getRoostGeneratorOutputBonus();
        return Math.max(0.0D, Math.min(1.0D, ratio));
    }

    private double rawFluctuation() {
        return countUpgrade(UpgradeItem.Kind.RF_EXCITER)
                * ChickensConfigHolder.get().getRoostGeneratorExciterFluctuation();
    }

    private double remainingFluctuation() {
        double stabilizerReduction = Math.min(0.40D,
                countUpgrade(UpgradeItem.Kind.RF_STABILIZER)
                        * ChickensConfigHolder.get().getRoostGeneratorStabilizerReduction());
        return Math.max(0.0D, rawFluctuation() - stabilizerReduction);
    }

    private double governorReduction() {
        return Math.min(remainingFluctuation(), Math.min(0.40D,
                countUpgrade(UpgradeItem.Kind.RF_GOVERNOR)
                        * ChickensConfigHolder.get().getRoostGeneratorGovernorReduction()));
    }

    private double positiveFluctuationAmplitude() {
        return Math.max(0.0D, remainingFluctuation() - governorReduction());
    }

    private double negativeFluctuationAmplitude() {
        return remainingFluctuation();
    }

    private int countUpgrade(UpgradeItem.Kind kind) {
        int count = 0;
        for (int slot = 0; slot < UPGRADE_SLOT_COUNT; slot++) {
            if (upgradeKind(getItem(getUpgradeSlotIndex(slot))) == kind) {
                count += getItem(getUpgradeSlotIndex(slot)).getCount();
            }
        }
        return Math.min(upgradeCap(kind), count);
    }

    private int countUpgradeExcept(UpgradeItem.Kind kind, int excludedSlot) {
        int count = 0;
        for (int slot = 0; slot < UPGRADE_SLOT_COUNT; slot++) {
            if (slot != excludedSlot && upgradeKind(getItem(getUpgradeSlotIndex(slot))) == kind) {
                count += getItem(getUpgradeSlotIndex(slot)).getCount();
            }
        }
        return count;
    }

    @Nullable
    private static UpgradeItem.Kind upgradeKind(ItemStack stack) {
        if (stack.is(ModRegistry.RF_CAPACITY_UPGRADE.get())) return UpgradeItem.Kind.RF_CAPACITY;
        if (stack.is(ModRegistry.RF_EFFICIENCY_UPGRADE.get())) return UpgradeItem.Kind.RF_EFFICIENCY;
        if (stack.is(ModRegistry.RF_OUTPUT_UPGRADE.get())) return UpgradeItem.Kind.RF_OUTPUT;
        if (stack.is(ModRegistry.RF_EXCITER_UPGRADE.get())) return UpgradeItem.Kind.RF_EXCITER;
        if (stack.is(ModRegistry.RF_STABILIZER_UPGRADE.get())) return UpgradeItem.Kind.RF_STABILIZER;
        if (stack.is(ModRegistry.RF_SURGE_UPGRADE.get())) return UpgradeItem.Kind.RF_SURGE;
        if (stack.is(ModRegistry.RF_GOVERNOR_UPGRADE.get())) return UpgradeItem.Kind.RF_GOVERNOR;
        return null;
    }

    private static int upgradeCap(UpgradeItem.Kind kind) {
        return kind == UpgradeItem.Kind.RF_EXCITER ? MAX_EXCITER_UPGRADES
                : kind == UpgradeItem.Kind.RF_OUTPUT ? MAX_OUTPUT_UPGRADES
                : kind == UpgradeItem.Kind.RF_STABILIZER ? MAX_STABILIZER_UPGRADES
                : kind == UpgradeItem.Kind.RF_SURGE ? MAX_SURGE_UPGRADES
                : kind == UpgradeItem.Kind.RF_GOVERNOR ? MAX_GOVERNOR_UPGRADES
                : MAX_STANDARD_UPGRADES;
    }

    private void syncWithConfig() {
        int capacityUpgrades = countUpgrade(UpgradeItem.Kind.RF_CAPACITY);
        long configuredCapacity = Math.max(1, ChickensConfigHolder.get().getRoostGeneratorCapacity());
        long upgradedCapacity = configuredCapacity << Math.min(capacityUpgrades, 30);
        capacity = (int) Math.min(Integer.MAX_VALUE, upgradedCapacity);
        energyStorage.setLimits(capacity);
    }

    private void prepareOutputBudget(long gameTime) {
        if (outputBudgetTick != gameTime) {
            outputBudgetTick = gameTime;
            extractedThisTick = 0;
        }
    }

    private void updateActiveState(Level level, boolean active) {
        if (cachedActiveState == active) {
            return;
        }
        cachedActiveState = active;
        BlockState state = getBlockState();
        if (state.hasProperty(RoostGeneratorBlock.LIT)) {
            level.setBlock(worldPosition, state.setValue(RoostGeneratorBlock.LIT, active), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.putInt("Energy", getEnergyStored());
        tag.putInt("FluctuationTicks", fluctuationTicks);
        tag.putDouble("Fluctuation", fluctuation);
        tag.putBoolean("HasFluctuationRoll", hasFluctuationRoll);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        syncWithConfig();
        energyStorage.setEnergy(Mth.clamp(tag.getInt("Energy"), 0, capacity));
        fluctuationTicks = Math.max(0, tag.getInt("FluctuationTicks"));
        fluctuation = Double.isFinite(tag.getDouble("Fluctuation")) ? tag.getDouble("Fluctuation") : 0.0D;
        hasFluctuationRoll = tag.getBoolean("HasFluctuationRoll");
    }

    private static int safeInt(double value) {
        if (!Double.isFinite(value)) {
            return value < 0.0D ? Integer.MIN_VALUE : 0;
        }
        if (value <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (value >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) Math.round(value);
    }

    private final class MachineEnergyStorage extends EnergyStorage {
        MachineEnergyStorage() {
            super(1_000_000, 0, Integer.MAX_VALUE);
        }

        @Override
        public int receiveEnergy(int amount, boolean simulate) {
            return 0;
        }

        @Override
        public int extractEnergy(int amount, boolean simulate) {
            if (amount <= 0 || energy <= 0) {
                return 0;
            }
            if (level != null) {
                prepareOutputBudget(level.getGameTime());
            }
            int availableThisTick = Math.max(0, getMaxOutputPerTick() - extractedThisTick);
            int extracted = Math.min(Math.min(amount, availableThisTick), energy);
            if (!simulate && extracted > 0) {
                energy -= extracted;
                extractedThisTick += extracted;
                setChanged();
            }
            return extracted;
        }

        @Override
        public boolean canReceive() {
            return false;
        }

        @Override
        public boolean canExtract() {
            return getMaxOutputPerTick() > 0;
        }

        void addEnergy(int amount) {
            if (amount > 0) {
                energy = Math.min(capacity, energy + amount);
            }
        }

        void removeEnergy(int amount) {
            if (amount > 0) {
                energy = Math.max(0, energy - amount);
            }
        }

        void setEnergy(int value) {
            energy = Mth.clamp(value, 0, capacity);
        }

        void setLimits(int newCapacity) {
            capacity = Math.max(1, newCapacity);
            maxReceive = 0;
            maxExtract = Integer.MAX_VALUE;
            energy = Math.min(energy, capacity);
        }
    }
}
