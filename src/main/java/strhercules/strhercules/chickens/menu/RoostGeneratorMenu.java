package strhercules.chickens.menu;

import strhercules.chickens.blockentity.AbstractChickenContainerBlockEntity;
import strhercules.chickens.blockentity.RoostGeneratorBlockEntity;
import strhercules.chickens.registry.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

/** Container layout for the supplied Roost Generator GUI. */
public class RoostGeneratorMenu extends AbstractContainerMenu implements SideConfigMenu {
    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_COLUMNS = 9;

    private final RoostGeneratorBlockEntity generator;
    private final ContainerLevelAccess access;
    private final int machineSlotCount;

    public RoostGeneratorMenu(int id, Inventory playerInventory, RegistryFriendlyByteBuf buffer) {
        this(id, playerInventory, resolveBlockEntity(playerInventory, buffer));
    }

    public RoostGeneratorMenu(int id, Inventory playerInventory, RoostGeneratorBlockEntity generator) {
        this(id, playerInventory, generator, generator.getDataAccess());
    }

    public RoostGeneratorMenu(int id, Inventory playerInventory, RoostGeneratorBlockEntity generator,
            ContainerData data) {
        super(ModMenuTypes.ROOST_GENERATOR.get(), id);
        this.generator = generator;
        this.machineSlotCount = generator.getContainerSize();
        Level level = generator.getLevel();
        this.access = level != null
                ? ContainerLevelAccess.create(level, generator.getBlockPos())
                : ContainerLevelAccess.NULL;

        this.addSlot(new ChickenSlot(generator, RoostGeneratorBlockEntity.CHICKEN_SLOT, 80, 19));
        this.addSlot(new MachineUpgradeSlot(generator, RoostGeneratorBlockEntity.UPGRADE_SLOT_ONE, 12, 42));
        this.addSlot(new MachineUpgradeSlot(generator, RoostGeneratorBlockEntity.UPGRADE_SLOT_TWO, 12, 61));

        for (int row = 0; row < PLAYER_INVENTORY_ROWS; row++) {
            for (int column = 0; column < PLAYER_COLUMNS; column++) {
                this.addSlot(new Slot(playerInventory, column + row * PLAYER_COLUMNS + PLAYER_COLUMNS,
                        8 + column * 18, 84 + row * 18));
            }
        }
        for (int hotbar = 0; hotbar < PLAYER_COLUMNS; hotbar++) {
            this.addSlot(new Slot(playerInventory, hotbar, 8 + hotbar * 18, 142));
        }

        this.addDataSlots(data);
    }

    private static RoostGeneratorBlockEntity resolveBlockEntity(Inventory inventory, RegistryFriendlyByteBuf buffer) {
        Objects.requireNonNull(inventory, "playerInventory");
        Objects.requireNonNull(buffer, "buffer");
        BlockPos pos = buffer.readBlockPos();
        Level level = inventory.player.level();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof RoostGeneratorBlockEntity generator) {
            return generator;
        }
        throw new IllegalStateException("Roost Generator not found at " + pos);
    }

    @Override
    public boolean stillValid(Player player) {
        return generator.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack original = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack current = slot.getItem();
            original = current.copy();
            if (index < machineSlotCount) {
                if (!moveItemStackTo(current, machineSlotCount, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(current, 0, machineSlotCount, false)) {
                return ItemStack.EMPTY;
            }
            if (current.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            slot.onTake(player, current);
        }
        return original;
    }

    public ContainerLevelAccess getAccess() {
        return access;
    }

    @Override
    public RoostGeneratorBlockEntity getSideConfigurable() {
        return generator;
    }

    public int getEnergy() {
        return generator.getEnergyStored();
    }

    public int getCapacity() {
        return generator.getEnergyCapacity();
    }

    public int getGeneration() {
        return generator.getGenerationPerTick();
    }

    public int getNominalGeneration() {
        return generator.getNominalGenerationPerTick();
    }

    public int getMaxOutput() {
        return generator.getMaxOutputPerTick();
    }

    public int getOutputRatioPercent() {
        return generator.getOutputRatioPercent();
    }

    private static final class ChickenSlot extends Slot {
        private final RoostGeneratorBlockEntity generator;

        private ChickenSlot(RoostGeneratorBlockEntity generator, int index, int x, int y) {
            super(generator, index, x, y);
            this.generator = generator;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return RoostGeneratorBlockEntity.isRedstoneFluxChicken(stack);
        }

        @Override
        public int getMaxStackSize(ItemStack stack) {
            return generator.getMaxStackSizeForSlot(getContainerSlot(), stack);
        }

        @Override
        public ItemStack remove(int amount) {
            return super.remove(Math.min(amount,
                    AbstractChickenContainerBlockEntity.getLegalExternalStackSize(getItem())));
        }
    }
}
