package strhercules.chickens;

import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.biome.Biome;
import strhercules.chickens.config.ChickensConfigHolder;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Central registry that keeps track of every chicken descriptor. Mirrors
 * the responsibilities of the legacy implementation but upgrades the
 * biome logic to make use of modern tag helpers.
 */
public final class ChickensRegistry {
    private static final Map<Integer, ChickensRegistryItem> ITEMS = new HashMap<>();
    public static final int SMART_CHICKEN_ID = 50;
    private static final Random RAND = new Random();

    private ChickensRegistry() {
    }

    public static void register(ChickensRegistryItem entity) {
        validate(entity);
        ITEMS.put(entity.getId(), entity);
    }

    private static void validate(ChickensRegistryItem entity) {
        for (ChickensRegistryItem item : ITEMS.values()) {
            if (entity.getId() == item.getId()) {
                throw new IllegalStateException("Duplicate chicken id " + entity.getId());
            }
            if (entity.getEntityName().equalsIgnoreCase(item.getEntityName())) {
                throw new IllegalStateException("Duplicate chicken name " + entity.getEntityName());
            }
        }
    }

    public static ChickensRegistryItem getByType(int type) {
        return ITEMS.get(type);
    }

    @Nullable
    public static ChickensRegistryItem getByEntityName(String name) {
        if (name == null) {
            return null;
        }
        for (ChickensRegistryItem item : ITEMS.values()) {
            if (item.getEntityName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static Collection<ChickensRegistryItem> getItems() {
        List<ChickensRegistryItem> result = new ArrayList<>();
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (chicken.isEnabled()) {
                result.add(chicken);
            }
        }
        return result;
    }

    public static Collection<ChickensRegistryItem> getDisabledItems() {
        List<ChickensRegistryItem> result = new ArrayList<>();
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (!chicken.isEnabled()) {
                result.add(chicken);
            }
        }
        return result;
    }

    private static List<ChickensRegistryItem> getChildren(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        List<ChickensRegistryItem> result = new ArrayList<>();
        if (parent1.isEnabled()) {
            result.add(parent1);
        }
        if (parent2.isEnabled()) {
            result.add(parent2);
        }
        for (ChickensRegistryItem item : ITEMS.values()) {
            if (item.isEnabled() && item.isChildOf(parent1, parent2)) {
                result.add(item);
            }
        }
        return result;
    }

    @Nullable
    public static ChickensRegistryItem findDyeChicken(net.minecraft.world.item.crafting.Ingredient colour) {
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (chicken.isDye(colour)) {
                return chicken;
            }
        }
        return null;
    }

    @Nullable
    public static ChickensRegistryItem findDyeChicken(DyeColor colour) {
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (chicken.isDye(colour)) {
                return chicken;
            }
        }
        return null;
    }

    public static List<ChickensRegistryItem> getPossibleChickensToSpawn(SpawnType spawnType) {
        List<ChickensRegistryItem> result = new ArrayList<>();
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (chicken.canSpawn() && chicken.getSpawnType() == spawnType && chicken.isEnabled()) {
                result.add(chicken);
            }
        }
        return result;
    }

    public static SpawnType getSpawnType(Holder<Biome> biomeHolder) {
        if (biomeHolder.is(Tags.Biomes.IS_NETHER)) {
            return SpawnType.HELL;
        }
        if (biomeHolder.is(Tags.Biomes.IS_END)) {
            return SpawnType.END;
        }
        if (biomeHolder.is(Tags.Biomes.IS_SNOWY)) {
            return SpawnType.SNOW;
        }
        return SpawnType.NORMAL;
    }

    public static float getChildChance(ChickensRegistryItem child) {
        if (child.getTier() <= 1) {
            return 0;
        }
        ChickensRegistryItem parent1 = child.getParent1();
        ChickensRegistryItem parent2 = child.getParent2();
        List<ChickensRegistryItem> possibleChildren = getChildren(parent1, parent2);
        int maxChance = getMaxChance(possibleChildren);
        double multiplier = ChickensConfigHolder.get().getBreedingChanceMultiplier();
        double totalWeight = getTotalWeight(possibleChildren, maxChance, parent1, parent2, multiplier);
        if (totalWeight <= 0.0D) {
            return 0;
        }
        return (float) (getWeight(child, maxChance, parent1, parent2, multiplier) * 100.0D / totalWeight);
    }

    @Nullable
    public static ChickensRegistryItem getRandomChild(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        List<ChickensRegistryItem> possibleChildren = getChildren(parent1, parent2);
        if (possibleChildren.isEmpty()) {
            return null;
        }
        int maxChance = getMaxChance(possibleChildren);
        double multiplier = ChickensConfigHolder.get().getBreedingChanceMultiplier();
        double totalWeight = getTotalWeight(possibleChildren, maxChance, parent1, parent2, multiplier);
        if (totalWeight <= 0.0D) {
            return null;
        }
        return getChickenToBeBorn(possibleChildren, maxChance, parent1, parent2, multiplier,
                RAND.nextDouble() * totalWeight);
    }

    @Nullable
    private static ChickensRegistryItem getChickenToBeBorn(List<ChickensRegistryItem> possibleChildren, int maxChance,
            ChickensRegistryItem parent1, ChickensRegistryItem parent2, double multiplier, double diceValue) {
        double currentValue = 0.0D;
        ChickensRegistryItem lastWeighted = null;
        for (ChickensRegistryItem child : possibleChildren) {
            double weight = getWeight(child, maxChance, parent1, parent2, multiplier);
            if (weight <= 0.0D) {
                continue;
            }
            currentValue += weight;
            lastWeighted = child;
            if (diceValue < currentValue) {
                return child;
            }
        }
        return lastWeighted;
    }

    private static double getWeight(ChickensRegistryItem chicken, int maxChance, ChickensRegistryItem parent1,
            ChickensRegistryItem parent2, double multiplier) {
        double weight = maxChance - chicken.getTier();
        if (chicken != parent1 && chicken != parent2) {
            weight *= multiplier;
        }
        return weight;
    }

    private static double getTotalWeight(List<ChickensRegistryItem> possibleChildren, int maxChance,
            ChickensRegistryItem parent1, ChickensRegistryItem parent2, double multiplier) {
        double totalWeight = 0.0D;
        for (ChickensRegistryItem child : possibleChildren) {
            totalWeight += getWeight(child, maxChance, parent1, parent2, multiplier);
        }
        return totalWeight;
    }

    private static int getMaxChance(List<ChickensRegistryItem> possibleChildren) {
        int maxChance = 0;
        for (ChickensRegistryItem child : possibleChildren) {
            maxChance = Math.max(maxChance, child.getTier());
        }
        return maxChance + 1;
    }

    public static boolean isAnyIn(SpawnType spawnType) {
        for (ChickensRegistryItem chicken : ITEMS.values()) {
            if (chicken.canSpawn() && chicken.isEnabled() && chicken.getSpawnType() == spawnType) {
                return true;
            }
        }
        return false;
    }

    @Nullable
    public static ChickensRegistryItem getSmartChicken() {
        return ITEMS.get(SMART_CHICKEN_ID);
    }
}