package strhercules.chickens.integration.mekanism;

import strhercules.chickens.ChemicalEggRegistry;
import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.GasEggRegistry;
import strhercules.chickens.LiquidEggRegistry;
import strhercules.chickens.LiquidEggRegistryItem;
import strhercules.chickens.blockentity.AvianChemicalConverterBlockEntity;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity;
import strhercules.chickens.blockentity.AvianFluidConverterBlockEntity;
import strhercules.chickens.item.ChickenItem;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.item.ChemicalEggItem;
import strhercules.chickens.item.GasEggItem;
import strhercules.chickens.item.LiquidEggItem;
import strhercules.chickens.item.ChickensSpawnEggItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Set;

/** Single source of truth for Modern Chickens radioactive content. */
public final class RadioactiveContentHelper {
    // These legacy definitions produce material items without a shared radiation component.
    private static final Set<String> RADIOACTIVE_CHICKENS = Set.of(
            "radioactivewastechicken",
            "uraniumchicken",
            "yelloriumchicken",
            "blutoniumchicken",
            "uraninitechicken",
            "plutoniumpelletchicken",
            "poloniumpelletchicken",
            "antimatterpelletchicken");

    // 1.20.1 has no public getter for the loot table of a chest, and the field name differs between
    // development and release, so the only ResourceLocation field of the class is looked up once.
    @Nullable
    private static final Field LOOT_TABLE_FIELD = findLootTableField();

    private RadioactiveContentHelper() {
    }

    @Nullable
    private static Field findLootTableField() {
        for (Field field : RandomizableContainerBlockEntity.class.getDeclaredFields()) {
            if (field.getType() == ResourceLocation.class && !Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    private static boolean hasPendingLoot(Container container) {
        if (LOOT_TABLE_FIELD == null || !(container instanceof RandomizableContainerBlockEntity chest)) {
            return false;
        }
        try {
            return LOOT_TABLE_FIELD.get(chest) != null;
        } catch (IllegalAccessException exception) {
            return false;
        }
    }

    public static boolean isRadioactive(@Nullable ChickensRegistryItem chicken) {
        if (chicken == null) {
            return false;
        }
        String name = chicken.getEntityName().toLowerCase(Locale.ROOT);
        return RADIOACTIVE_CHICKENS.contains(name)
                || isRadioactiveEgg(chicken.createLayItem())
                || isRadioactiveEgg(chicken.createDropItem());
    }

    public static boolean isRadioactive(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        return isRadioactiveEgg(stack) || isRadioactiveChickenStack(stack);
    }

    private static boolean isRadioactiveEgg(ItemStack stack) {
        if (stack.getItem() instanceof LiquidEggItem) {
            LiquidEggRegistryItem entry = LiquidEggRegistry.findById(ChickenItemHelper.getChickenType(stack));
            return entry != null && entry.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE);
        }
        if (stack.getItem() instanceof ChemicalEggItem || stack.getItem() instanceof GasEggItem) {
            int id = ChickenItemHelper.getChickenType(stack);
            ChemicalEggRegistryItem entry = stack.getItem() instanceof GasEggItem
                    ? GasEggRegistry.findById(id)
                    : ChemicalEggRegistry.findById(id);
            return entry != null && (entry.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE)
                    || MekanismChemicalHelper.isRadioactive(entry.getChemicalId()));
        }
        return false;
    }

    private static boolean isRadioactiveChickenStack(ItemStack stack) {
        if (stack.getItem() instanceof ChickenItem || stack.getItem() instanceof ChickensSpawnEggItem) {
            return isRadioactive(ChickenItemHelper.resolve(stack));
        }
        return false;
    }

    public static boolean hasRadioactiveItems(Container container) {
        // Reading a slot of a chest that still owns an unrolled loot table rolls that loot immediately.
        // Those chests are empty from the player's point of view, so they cannot be radioactive yet.
        if (hasPendingLoot(container)) {
            return false;
        }
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            if (isRadioactive(container.getItem(slot))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isRadioactiveChemical(@Nullable ResourceLocation chemicalId) {
        if (chemicalId == null) {
            return false;
        }
        ChemicalEggRegistryItem chemicalEgg = ChemicalEggRegistry.findByChemical(chemicalId);
        if (chemicalEgg == null) {
            chemicalEgg = GasEggRegistry.findByChemical(chemicalId);
        }
        return (chemicalEgg != null
                && chemicalEgg.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE))
                || MekanismChemicalHelper.isRadioactive(chemicalId);
    }

    public static boolean isRadioactiveFluid(@Nullable FluidStack fluid) {
        if (fluid == null || fluid.isEmpty()) {
            return false;
        }
        ResourceLocation fluidId = BuiltInRegistries.FLUID.getKey(fluid.getFluid());
        LiquidEggRegistryItem entry = fluidId == null ? null : LiquidEggRegistry.findByFluid(fluidId);
        return entry != null && entry.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE);
    }

    public static boolean hasRadioactiveMachineContents(BlockEntity blockEntity) {
        if (blockEntity instanceof Container container && hasRadioactiveItems(container)) {
            return true;
        }
        if (blockEntity instanceof AvianDousingMachineBlockEntity dousing) {
            ChemicalEggRegistryItem chemical = ChemicalEggRegistry.findById(dousing.getChemicalEntryId());
            if (chemical == null) {
                chemical = GasEggRegistry.findById(dousing.getChemicalEntryId());
            }
            return dousing.getChemicalAmount() > 0
                    && ((chemical != null && chemical.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE))
                    || isRadioactiveChemical(dousing.getChemicalId()))
                    || isRadioactiveFluid(dousing.getFluid());
        }
        if (blockEntity instanceof AvianChemicalConverterBlockEntity converter) {
            ChemicalEggRegistryItem stored = converter.getStoredEntry();
            return (stored != null && stored.hasHazard(LiquidEggRegistryItem.HazardFlag.RADIOACTIVE))
                    || converter.getChemicalAmount() > 0 && isRadioactiveChemical(converter.getChemicalId());
        }
        if (blockEntity instanceof AvianFluidConverterBlockEntity converter) {
            return isRadioactiveFluid(converter.getFluid());
        }
        return false;
    }
}
