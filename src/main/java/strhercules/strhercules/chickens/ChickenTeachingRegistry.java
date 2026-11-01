package strhercules.chickens;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shared item-to-breed teaching mappings used by both chicken interaction paths.
 * KubeJS adds or replaces entries during the common setup registry event.
 */
public final class ChickenTeachingRegistry {
    public static final List<String> VOID_MINER_TIERS = List.of(
            "amethystine", "aurantium", "azurine", "caerium", "citrinetine", "rosarium", "rubetine");

    private static final Map<Item, String> TARGETS = new HashMap<>();
    private static final Map<ResourceLocation, String> NAMESPACED_TARGETS = new LinkedHashMap<>();

    static {
        TARGETS.put(Items.BOOK, "SmartChicken");
        TARGETS.put(Items.CAKE, "chickenNosto");
        TARGETS.put(Blocks.GRASS_BLOCK.asItem(), "americanChicken");
        TARGETS.put(Blocks.DIRT.asItem(), "dirtChicken");
        for (String tier : VOID_MINER_TIERS) {
            NAMESPACED_TARGETS.put(ResourceLocation.fromNamespaceAndPath("voidminersremastered", tier + "_frame"),
                    tier + "MinerChicken");
        }
    }

    private ChickenTeachingRegistry() {
    }

    public static void register(Item trigger, String chickenName) {
        if (trigger != null && chickenName != null && !chickenName.isBlank()) {
            TARGETS.put(trigger, normaliseName(chickenName));
        }
    }

    public static Map<ResourceLocation, String> namespacedTargets() {
        return Collections.unmodifiableMap(NAMESPACED_TARGETS);
    }

    @Nullable
    public static ChickensRegistryItem resolve(ItemStack held) {
        if (held.isEmpty()) {
            return null;
        }
        String chickenName = TARGETS.get(held.getItem());
        if (chickenName == null) {
            chickenName = NAMESPACED_TARGETS.get(BuiltInRegistries.ITEM.getKey(held.getItem()));
        }
        return chickenName == null ? null : ChickensRegistry.getByEntityName(chickenName);
    }

    private static String normaliseName(String name) {
        String trimmed = name.trim();
        int separator = trimmed.indexOf(':');
        return separator < 0 ? trimmed : trimmed.substring(separator + 1);
    }
}
