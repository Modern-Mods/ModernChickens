package strhercules.chickens.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;
import strhercules.chickens.ChemicalEggRegistry;
import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.GasEggRegistry;
import strhercules.chickens.LiquidEggRegistry;
import strhercules.chickens.LiquidEggRegistryItem;
import strhercules.chickens.item.ChemicalEggItem;
import strhercules.chickens.item.ChickenItem;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.item.ChickenStats;
import strhercules.chickens.item.ChickensSpawnEggItem;
import strhercules.chickens.item.GasEggItem;
import strhercules.chickens.item.LiquidEggItem;
import strhercules.chickens.registry.ModRecipeTypes;
import strhercules.chickens.registry.ModRegistry;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Ingredient that matches one exact egg or chicken by its readable name instead of by item components.
 * Every liquid egg, chemical egg, gas egg, chicken and spawn egg shares a single item, so recipes and
 * KubeJS scripts cannot tell them apart with a plain item id. Chickens can also ask for minimum stats.
 *
 * <pre>
 * {"type": "chickens:variant", "kind": "liquid_egg", "id": "minecraft:water"}
 * {"type": "chickens:variant", "kind": "chicken", "id": "DiamondChicken", "min_growth": 5, "min_gain": 3}
 * </pre>
 */
public final class VariantIngredient implements ICustomIngredient {
    public enum Kind implements StringRepresentable {
        LIQUID_EGG("liquid_egg"),
        CHEMICAL_EGG("chemical_egg"),
        GAS_EGG("gas_egg"),
        CHICKEN("chicken"),
        SPAWN_EGG("spawn_egg");

        private final String serializedName;

        Kind(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }

    public static final MapCodec<VariantIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            StringRepresentable.fromEnum(Kind::values).fieldOf("kind").forGetter(VariantIngredient::kind),
            Codec.STRING.fieldOf("id").forGetter(VariantIngredient::id),
            Codec.INT.optionalFieldOf("min_growth", 0).forGetter(VariantIngredient::minGrowth),
            Codec.INT.optionalFieldOf("min_gain", 0).forGetter(VariantIngredient::minGain),
            Codec.INT.optionalFieldOf("min_strength", 0).forGetter(VariantIngredient::minStrength)
    ).apply(instance, VariantIngredient::new));

    private final Kind kind;
    private final String id;
    private final int minGrowth;
    private final int minGain;
    private final int minStrength;

    public VariantIngredient(Kind kind, String id, int minGrowth, int minGain, int minStrength) {
        this.kind = kind;
        this.id = id;
        this.minGrowth = minGrowth;
        this.minGain = minGain;
        this.minStrength = minStrength;
    }

    public Kind kind() {
        return kind;
    }

    public String id() {
        return id;
    }

    public int minGrowth() {
        return minGrowth;
    }

    public int minGain() {
        return minGain;
    }

    public int minStrength() {
        return minStrength;
    }

    @Override
    public boolean test(ItemStack stack) {
        ItemStack reference = reference();
        if (reference.isEmpty() || stack.getItem() != reference.getItem()) {
            return false;
        }
        if (ChickenItemHelper.getChickenType(stack) != ChickenItemHelper.getChickenType(reference)) {
            return false;
        }
        if (kind != Kind.CHICKEN) {
            return true;
        }
        ChickenStats stats = ChickenItemHelper.getStats(stack);
        return stats.growth() >= minGrowth && stats.gain() >= minGain && stats.strength() >= minStrength;
    }

    @Override
    public Stream<ItemStack> getItems() {
        ItemStack reference = reference();
        return reference.isEmpty() ? Stream.empty() : Stream.of(reference);
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return ModRecipeTypes.VARIANT_INGREDIENT.get();
    }

    private ItemStack reference() {
        return switch (kind) {
            case LIQUID_EGG -> {
                ResourceLocation fluidId = ResourceLocation.tryParse(id);
                LiquidEggRegistryItem liquid = fluidId == null ? null : LiquidEggRegistry.findByFluid(fluidId);
                yield liquid == null ? ItemStack.EMPTY : LiquidEggItem.createFor(liquid);
            }
            case CHEMICAL_EGG -> {
                ChemicalEggRegistryItem chemical = findChemical(false);
                yield chemical == null ? ItemStack.EMPTY : ChemicalEggItem.createFor(chemical);
            }
            case GAS_EGG -> {
                ChemicalEggRegistryItem gas = findChemical(true);
                yield gas == null ? ItemStack.EMPTY : GasEggItem.createFor(gas);
            }
            case CHICKEN -> {
                ChickensRegistryItem chicken = ChickensRegistry.getByEntityName(id);
                yield chicken == null ? ItemStack.EMPTY : ((ChickenItem) ModRegistry.CHICKEN_ITEM.get()).createFor(chicken);
            }
            case SPAWN_EGG -> {
                ChickensRegistryItem chicken = ChickensRegistry.getByEntityName(id);
                yield chicken == null ? ItemStack.EMPTY : ChickensSpawnEggItem.createFor(chicken);
            }
        };
    }

    @Nullable
    private ChemicalEggRegistryItem findChemical(boolean gas) {
        ResourceLocation chemicalId = ResourceLocation.tryParse(id);
        if (chemicalId == null) {
            return null;
        }
        return gas ? GasEggRegistry.findByChemical(chemicalId) : ChemicalEggRegistry.findByChemical(chemicalId);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof VariantIngredient ingredient
                && kind == ingredient.kind
                && id.equals(ingredient.id)
                && minGrowth == ingredient.minGrowth
                && minGain == ingredient.minGain
                && minStrength == ingredient.minStrength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, id, minGrowth, minGain, minStrength);
    }
}
