package strhercules.chickens;

import strhercules.chickens.config.ChickensConfigHolder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;

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
        if (biomeHolder.is(BiomeTags.IS_NETHER)) {
            return SpawnType.HELL;
        }
        if (biomeHolder.is(BiomeTags.IS_END)) {
            return SpawnType.END;
        }
        if (biomeHolder.is(Tags.Biomes.IS_SNOWY)) {
            return SpawnType.SNOW;
        }
        return SpawnType.NORMAL;
    }

    public static float getChildChance(ChickensRegistryItem child) {
        if (child.getTier() <= 1 || !child.isBreedable()) {
            return 0;
        }
        BreedingPool pool = createBreedingPool(child.getParent1(), child.getParent2());
        double totalWeight = pool.totalWeight();
        if (totalWeight <= 0.0D) {
            return 0;
        }
        return (float) (pool.weightOf(child) * 100.0D / totalWeight);
    }

    @Nullable
    public static ChickensRegistryItem getRandomChild(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        BreedingPool pool = createBreedingPool(parent1, parent2);
        if (pool.chickens().isEmpty()) {
            return null;
        }
        double totalWeight = pool.totalWeight();
        if (totalWeight <= 0.0D) {
            return null;
        }
        return getChickenToBeBorn(pool, RAND.nextDouble() * totalWeight);
    }

    private static BreedingPool createBreedingPool(ChickensRegistryItem parent1, ChickensRegistryItem parent2) {
        List<ChickensRegistryItem> possibleChildren = getChildren(parent1, parent2);
        return new BreedingPool(possibleChildren, parent1, parent2, getMaxChance(possibleChildren),
                ChickensConfigHolder.get().getBreedingChanceMultiplier());
    }

    @Nullable
    private static ChickensRegistryItem getChickenToBeBorn(BreedingPool pool, double diceValue) {
        double currentValue = 0.0D;
        ChickensRegistryItem lastWeighted = null;
        for (ChickensRegistryItem child : pool.chickens()) {
            double weight = pool.weightOf(child);
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

    private record BreedingPool(List<ChickensRegistryItem> chickens, ChickensRegistryItem parent1,
                                ChickensRegistryItem parent2, int maxChance, double unlockedMultiplier) {
        double weightOf(ChickensRegistryItem chicken) {
            double weight = maxChance - chicken.getTier();
            return chicken == parent1 || chicken == parent2 ? weight : weight * unlockedMultiplier;
        }

        double totalWeight() {
            double total = 0.0D;
            for (ChickensRegistryItem chicken : chickens) {
                total += weightOf(chicken);
            }
            return total;
        }
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
