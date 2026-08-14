**CHANGELOG**
# [2.5.0] - 14/8/2026

## Added

- **Mechanical Roost** — an RF-powered four-row roost that automates chicken production, with dedicated progress, energy, output, and upgrade slots.
- **Mechanical Nest** — place a Robot Rooster inside to power an RF aura that boosts nearby Roosts and Mechanical Roosts. Jade now reports its range, boost strength, active roosts, conflicts, and energy use.
- **Robot Chicken and Robot Rooster progression**
  - Feed eligible machine upgrades to a Smart Chicken to convert it into a Robot Chicken. Each conversion requires a random 8–48 upgrades.
  - Pair a Robot Chicken with a Rooster in the Overworld to begin a two-minute conversion. Completed Robot Roosters can power a Mechanical Nest.
  - Added dedicated Robot Chicken and Robot Rooster items, spawn eggs, catcher support, JEI recipes, and progress feedback.
- **Lava Chicken** — Vanilla and Smart Chickens killed by lava can drop this rare food. Eating it grants 15 seconds of Regeneration III, Absorption IV, healing, movement speed, fire/lava immunity, and a short-lived harmless fire trail. It has a 30-second cooldown and a JEI entry.
- **Machine upgrades** — craftable Speed, Stack, Storage Capacity, Range, and RF Capacity upgrades are now supported across the relevant chicken machines.
- **Machine Configurator** — configure item, fluid, chemical, and energy automation per face using Disabled, Input, Output, or Input/Output modes.
- **Mega Chicken customization and loot** — rare end-game drops now provide 13 themed skin crates for owned Mega Chickens, including Zombie, Valentine's, Toxic, Reptar, Rambo, Pink, Fox, Duck, Dodo, Deep Dark, Creeper, Big Brain, and Aviator skins. Robot Chicken and Robot Rooster items can apply robotic appearances, while Nether Stars and Dragon's Breath adjust Mega Chicken size.
- **KubeJS extensions** — the registry event now supports teaching and modifying breeds, fluid and chemical lineage overrides, per-breed spawn weights, fluid/chemical/gas egg metadata and hazard overrides, and custom items through the `chickens:nest_seeds` tag.

## Changed

- Supported machines now expose their RF operation cost, progress, capacity, and automation state through their in-game tooltips and integration overlays.
- Nest and Mechanical Nest aura data is available through the regular Jade HUD path, including duration, multiplier, range, and conflicting active nests.

## Fixed

- Dynamic Mekanism chemical chickens now use readable localized names instead of leaking unresolved translation keys.
- Roosters can now correctly breed with custom chicken hens, preserving the hen's lineage and stats for the offspring.

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
