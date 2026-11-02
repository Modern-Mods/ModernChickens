package strhercules.chickens.integration.almostunified;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;

import java.util.Optional;

/**
 * Tag resolution for chicken drops. The AlmostUnified lookup is disabled in
 * this version, so the first item found in the tag is used.
 */
public final class AlmostUnifiedCompat {
    private AlmostUnifiedCompat() {}

    /**
     * Resolves the preferred item for the given tag string.
     *
     * @param tagName e.g. {@code "c:ingots/copper"} or {@code "c:silicon"}
     * @return the resolved stack, or {@link Optional#empty()} if the tag has no entries
     */
    public static Optional<ItemStackTemplate> resolvePreferred(String tagName) {
        Identifier id = Identifier.parse(tagName);
        TagKey<Item> tag = TagKey.create(Registries.ITEM, id);
        return resolveFromTag(tag);
    }

    private static Optional<ItemStackTemplate> resolveFromTag(TagKey<Item> tag) {
        Optional<ItemStackTemplate> fromHolder = BuiltInRegistries.ITEM.get(tag)
                .flatMap(set -> set.stream().findFirst())
                .map(holder -> new ItemStackTemplate(holder.value()));
        if (fromHolder.isPresent()) {
            return fromHolder;
        }
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.builtInRegistryHolder().is(tag)) {
                return Optional.of(new ItemStackTemplate(item));
            }
        }
        return Optional.empty();
    }
}
