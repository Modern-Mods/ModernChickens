package strhercules.chickens;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Immutable record describing a chemical or gas egg entry. The structure mirrors the fluid registry
 * but captures Mekanism chemical metadata instead of direct fluid objects so the mod can function
 * without a compile-time dependency on Mekanism.
 */
public final class ChemicalEggRegistryItem {
    private final int id;
    private final Identifier chemicalId;
    private final Identifier texture;
    private final Component displayName;
    private int eggColor;
    private int volume;
    private final EnumSet<LiquidEggRegistryItem.HazardFlag> hazards;
    private final boolean gaseous;

    public ChemicalEggRegistryItem(int id,
                                   Identifier chemicalId,
                                   Identifier texture,
                                   Component displayName,
                                   int eggColor,
                                   int volume,
                                   Set<LiquidEggRegistryItem.HazardFlag> hazards,
                                   boolean gaseous) {
        this.id = id;
        this.chemicalId = chemicalId;
        this.texture = texture;
        this.displayName = displayName;
        this.eggColor = eggColor;
        this.volume = volume;
        this.hazards = hazards.isEmpty()
                ? EnumSet.noneOf(LiquidEggRegistryItem.HazardFlag.class)
                : EnumSet.copyOf(hazards);
        this.gaseous = gaseous;
    }

    public int getId() {
        return id;
    }

    public Identifier getChemicalId() {
        return chemicalId;
    }

    public Identifier getTexture() {
        return texture;
    }

    public Component getDisplayName() {
        return displayName.copy();
    }

    public int getEggColor() {
        return eggColor;
    }

    public void setEggColor(int color) {
        if (color >= 0 && color <= 0xFFFFFF) {
            eggColor = color;
        }
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int amount) {
        volume = Math.max(0, amount);
    }

    public Set<LiquidEggRegistryItem.HazardFlag> getHazards() {
        return Collections.unmodifiableSet(hazards);
    }

    public void setHazards(Set<LiquidEggRegistryItem.HazardFlag> values) {
        hazards.clear();
        if (values != null) {
            hazards.addAll(values);
        }
    }

    public boolean isGaseous() {
        return gaseous;
    }

    public boolean hasHazard(LiquidEggRegistryItem.HazardFlag hazard) {
        return hazards.contains(hazard);
    }
}
