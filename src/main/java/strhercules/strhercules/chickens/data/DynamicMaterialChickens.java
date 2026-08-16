package strhercules.chickens.data;

import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.ChickensMod;
import strhercules.chickens.SpawnType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * Registers stock material chickens for optional material mods with matching
 * Modern Chickens textures.
 */
final class DynamicMaterialChickens {
    private static final String MODERN_FOUNDRY = "modernfoundry";

    private DynamicMaterialChickens() {
    }

    static void register(List<ChickensRegistryItem> chickens, Map<String, ChickensRegistryItem> byName) {
        if (!ModList.get().isLoaded(MODERN_FOUNDRY)) {
            return;
        }

        register(chickens, byName, 633, "CobaltChicken", "cobalt_chicken", "cobalt_ingot",
                0x2376dd, 0x83b5ff, "IronChicken", "QuartzChicken");
        register(chickens, byName, 634, "PigIronChicken", "pigiron_chicken", "pig_iron_ingot",
                0xf0a8a4, 0xffd2cf, "IronChicken", "PinkChicken");
        register(chickens, byName, 635, "ManyullynChicken", "manyullyn_chicken", "manyullyn_ingot",
                0x9261cc, 0xd1a8ff, "CobaltChicken", "NetheriteChicken");
        register(chickens, byName, 636, "KnightslimeChicken", "knight_slime_chicken", "knightslime_ingot",
                0xb771ff, 0xe8ccff, "SlimeChicken", "IronChicken");
    }

    static void refresh() {
        // Modern Foundry's items are registered before the common setup bootstrap.
    }

    private static void register(List<ChickensRegistryItem> chickens,
            Map<String, ChickensRegistryItem> byName, int id, String entityName,
            String texturePath, String itemPath, int bgColor, int fgColor,
            String parent1Name, String parent2Name) {
        String nameKey = entityName.toLowerCase(Locale.ROOT);
        if (ChickensRegistry.getByType(id) != null || byName.containsKey(nameKey)) {
            return;
        }

        Optional<ItemStack> layItem = BuiltInRegistries.ITEM.getOptional(
                ResourceLocation.fromNamespaceAndPath(MODERN_FOUNDRY, itemPath))
                .map(ItemStack::new);
        if (layItem.isEmpty()) {
            return;
        }

        ItemStack layStack = layItem.get();
        for (ChickensRegistryItem existing : byName.values()) {
            if (ItemStack.isSameItemSameComponents(existing.createLayItem(), layStack)) {
                return;
            }
        }

        ChickensRegistryItem parent1 = byName.get(parent1Name.toLowerCase(Locale.ROOT));
        ChickensRegistryItem parent2 = byName.get(parent2Name.toLowerCase(Locale.ROOT));
        if (parent1 == null || parent2 == null) {
            return;
        }

        ChickensRegistryItem chicken = new ChickensRegistryItem(
                id, entityName, texture(texturePath), layStack, bgColor, fgColor,
                parent1, parent2).setSpawnType(SpawnType.NONE);
        chickens.add(chicken);
        byName.put(nameKey, chicken);
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(ChickensMod.MOD_ID, "textures/entity/" + path + ".png");
    }
}
