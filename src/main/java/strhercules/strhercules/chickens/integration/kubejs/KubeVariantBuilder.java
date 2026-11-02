package strhercules.chickens.integration.kubejs;

import dev.latvian.mods.kubejs.core.IngredientSupplierKJS;
import net.minecraft.world.item.crafting.Ingredient;
import strhercules.chickens.recipe.VariantIngredient;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Fluent KubeJS builder for the {@code chickens:variant} ingredient. KubeJS accepts it anywhere an
 * ingredient is expected, so {@code variant.kind('liquid_egg').chickenid('minecraft:water')} can be used
 * directly in shaped, shapeless and custom recipes.
 */
public final class KubeVariantBuilder implements IngredientSupplierKJS {
    @Nullable
    private String kind;
    @Nullable
    private String chickenId;
    private int minGrowth;
    private int minGain;
    private int minStrength;
    private boolean minimumStatsSet;

    public KubeVariantBuilder kind(String kind) {
        this.kind = kind;
        return this;
    }

    public KubeVariantBuilder chickenid(String chickenId) {
        this.chickenId = chickenId;
        return this;
    }

    public KubeVariantBuilder minGrowth(int minGrowth) {
        this.minGrowth = minGrowth;
        this.minimumStatsSet = true;
        return this;
    }

    public KubeVariantBuilder minGain(int minGain) {
        this.minGain = minGain;
        this.minimumStatsSet = true;
        return this;
    }

    public KubeVariantBuilder minStrength(int minStrength) {
        this.minStrength = minStrength;
        this.minimumStatsSet = true;
        return this;
    }

    public VariantIngredient build() {
        if (kind == null) {
            throw new IllegalStateException("variant ingredient is missing the required call .kind(...), valid kinds: " + validKinds());
        }
        if (chickenId == null || chickenId.isBlank()) {
            throw new IllegalStateException("variant ingredient is missing the required call .chickenid(...)");
        }
        VariantIngredient.Kind resolvedKind = VariantIngredient.Kind.byName(kind);
        if (resolvedKind == null) {
            throw new IllegalStateException("variant ingredient has unknown kind '" + kind + "', valid kinds: " + validKinds());
        }
        if (minimumStatsSet && resolvedKind != VariantIngredient.Kind.CHICKEN) {
            throw new IllegalStateException("minGrowth, minGain and minStrength only apply to kind 'chicken', not '" + kind + "'");
        }
        return new VariantIngredient(resolvedKind, chickenId.trim(), minGrowth, minGain, minStrength);
    }

    @Override
    public Ingredient kjs$asIngredient() {
        return build().toVanilla();
    }

    private static String validKinds() {
        return Arrays.stream(VariantIngredient.Kind.values())
                .map(VariantIngredient.Kind::getSerializedName)
                .collect(Collectors.joining(", "));
    }
}
