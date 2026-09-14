**CHANGELOG**
# [2.7.0] - 14/9/2026

## Added

- **Patchouli guidebook** — Patchouli is now a required dependency and the mod ships an in-game guide covering every registered block, machine, item and mechanic. Five categories (Getting Started, Production, Breeding, Egg Conversion, Upgrades and Tools) across 23 entries, translated into `en_us`, `es_es`, `es_mx` and `es_ar`. The book is available from the Modern Chickens creative tab.
- **Custom book artwork** — the guide no longer uses Patchouli's default look. It ships its own weathered-oak-and-parchment book sheet (background, page arrows, bookmarks, config/eye/resize buttons, nameplate, separators, lock and entry markings), a page filler, a crafting grid overlay, and a dedicated `guide_book` item texture.

## Fixed

- **Chickens never spawned naturally.** Three separate bugs stacked on top of each other, each one hiding the next:
  - The biome modifier instance was registered through `RegisterEvent` on `ForgeRegistries.Keys.BIOME_MODIFIERS`. That is a dynamic datapack registry, so the event never fires for it and the call was a silent no-op. No JSON entry existed either, so `modify()` never ran and no biome ever received a chicken, rooster or Mega Chicken spawn entry. The modifier is now generated as a proper datapack entry through a new `DatapackBuiltinEntriesProvider`.
  - `ChickensChicken.GroupData` implemented `SpawnGroupData` directly, but `AgeableMob.finalizeSpawn` casts the incoming group data to `AgeableMobGroupData`. The second chicken of any naturally spawned group crashed chunk generation with a `ClassCastException`.
  - The `hasVanillaChickenSpawn` gate was applied to every spawn type. No Nether or End biome contains a vanilla chicken, so `SpawnType.HELL` and `SpawnType.END` could never be reached even though their spawn tables were built. The gate now applies only to overworld types; roosters and Mega Chickens still require a vanilla-chicken biome.
- Eight `config.jade.plugin_chickens.*` entries had no translation, which crashes Jade on the title screen in development. All nine plugin UIDs are now translated in every supported language.

## Changed

- Natural spawn weight now honours the `spawnProbability` config value directly instead of scaling it to a tenth. Modern Chickens sit at weight 10, the same as the vanilla chicken, instead of weight 1. Lower `spawnProbability` to make them rarer.

# [2.4.0] - 11/8/2026

## Added

- **KubeJS integration** (optional; tested with `kubejs-neoforge-2101.7.2-build.368` / `rhino-2101.2.8-build.91`). The plugin ships inside the mod jar and only loads when KubeJS is installed, so nothing changes for packs without it.
  - **`ChickensEvents.registry`** — startup event for defining chickens from scripts:

    ```js
    ChickensEvents.registry(event => {
        event.create('dirt_chicken')
            .displayName('Dirt Chicken')
            .layItem('minecraft:dirt')
            .dropItem('minecraft:dirt', 2)
            .spawnType('NORMAL')
            .primaryColor(0x8B4513)
            .secondaryColor(0x654321)
    })
    ```

    Supports `displayName`, `layItem`, `dropItem`, `parent1`/`parent2`/`parents`, `tier`, `spawnType`, `allowNaturalSpawn`, `primaryColor`, `secondaryColor`, `layCoefficient`, `generatedTexture`, `texturePath`, `itemTexture`, `allowDousing`, `liquidDousingCost`, `enabled` and `id`. Registry ids are derived from the chicken name (6,000,000+ span) so they stay stable across script edits and load order changes.
  - The same startup event now supports `teach`, `modify`, fluid/chemical lineage overrides, per-breed `spawnWeight`, and fluid/chemical/gas egg metadata and hazard overrides. Rooster nest inputs use the extensible `chickens:nest_seeds` item tag.
  - **Recipe schema for `chickens:avian_dousing`** — `event.recipes.chickens.avian_dousing(result, input, reagent).energy(rf)`. The raw `event.custom({ type: 'chickens:avian_dousing', ... })` form keeps working and produces identical JSON.
  - Full reference and runnable examples in `wiki.md` and `Examples/KubeJS/`.
- **Avian Dousing recipes now accept item and block IDs** in `input` and `result`, not just chicken names, so the machine can convert items as well as chickens. Fields resolve as a chicken name first and an item ID second, which keeps every existing recipe working unchanged.

  ```js
  event.recipes.chickens.avian_dousing('minecraft:grass_block', 'minecraft:dirt',
    { type: 'fluid', id: 'minecraft:lava', amount: 1000 }).energy(2000).id('nadiendev:test_dousing_recipe')
  ```

## Fixed

- **Data-driven Avian Dousing recipes were invisible in JEI.** The category was built only from the chicken registry plus two hardcoded entries, so recipes added by datapacks or KubeJS never showed up. It now reads them from the recipe manager.
- **The Avian Dousing input slot rejected anything that was not a chicken item**, which blocked recipes that take an item as input even when the recipe itself was valid.

## Changed

- Chicken tiers can now be pinned explicitly instead of always being derived from the parent chain (`max(parentTier) + 1` remains the default).
- The built-in Dragon and Wither dousing entries in JEI are now generated from their recipe files instead of being hardcoded; costs are unchanged (10 items, 10,000 RF).
- The default dousing energy cost is exposed as `DousingRecipe.DEFAULT_ENERGY` so the recipe codec and the KubeJS schema cannot drift apart.

# [2.0.7] - 12/4/2025 - 18:30 (ARG)

## Added

- **Andesite Alloy Chicken** (`andesiteAlloyChicken`)
- **Chocolate Chicken** (`chocolatchicken`)
- **Ether Gas Chicken** (`ethergasChicken`)
- **Crystal Matrix Chicken** (`crystalmatrixChicken`)

## Changed

- Extended **Almost Unified** support to the drop of the following ingots: copper, tin, zinc, lead, nickel, silver, platinum, invar, bronze, steel, cupronickel, electrum, aluminum/aluminium, osmium, uranium, constantan, yellorium, graphite, cyanite, blutonium, electrical steel, energetic alloy, vibrant alloy, redstone alloy, conductive iron, pulsating iron, dark steel, soularium, signalum, enderium, iridium, lumium, mithril, entro, quantum alloy, black iron, draconium, awakened draconium, manasteel, terrasteel, elementium.
