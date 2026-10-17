**CHANGELOG**
# [2.7.8] - 17/10/2026

## New

- **Thaumaturge chickens.** Thaumium, Void Metal, Alchemical Brass, Quicksilver, Amber, Alumentum, Salis Mundus and Cinnabar chickens, plus one chicken for each of the seven crystals you find in the mines: Air, Water, Fire, Earth, Order, Entropy and Flux. They only appear if Thaumaturge is installed.
- **Astral Sorcery chickens.** Stardust, Starmetal, Rock Crystal, Celestial Crystal, Aquamarine, Resonating Gem and Lumen Crystal chickens. They only appear if Astral Sorcery is installed.
- **Ultimate Armoury chickens.** JoanFoite and Mushashite chickens, with their own textures. They appear if Ultimate Armoury: Tools And Weapons is installed.
- **Allthemodium chickens.** Allthemodium, Vibranium and Unobtainium, plus the three alloys that mix them.
- **Ender IO Evolution chickens.** Construction Alloy, Crude Steel, Crystalline Alloy, Crystalline Pink Slime, Energetic Silver, Melodic Alloy, Stellar Alloy and Vivid Alloy.
- **Five new Henhouses:** Mangrove, Cherry, Bamboo, Crimson and Warped. Same recipe as the others, just with the matching planks.

## Improved

- **All metal chickens now look like the Bronze Chicken**, with the stacked-ingot body, each in its own colours. This covers Copper, Tin, Steel, Aluminium, Uranium, Iridium, Draconium, Manasteel, Elementium and the rest, and the new Thaumaturge and Astral Sorcery metals.
- **The vanilla metal chickens too.** The Iron, Gold and Netherite chickens use the same Bronze Chicken style, coloured like their ingots. The Netherite Chicken finally has its own texture.
- **The item looks like the chicken you place.** The chicken in your inventory now matches the chicken in the world for every metal.
- Many modded chickens now have their own texture instead of a coloured copy of the vanilla chicken. If you want to repaint one, its PNG is in `textures/entity`.

## Changed

- **Nadienite and Steel chickens now use the Ultimate Armoury ingots.** Those ores moved from Ultimate Foods to Ultimate Armoury: Tools And Weapons, and the two chickens follow them, with colours taken from the new ingots.

## Fixed

- **Henhouse recipes.** Every wooden Henhouse recipe used to give you the plain oak Henhouse, so Spruce, Birch, Jungle, Acacia and Dark Oak could not be crafted. Each recipe now gives the Henhouse of its own wood.
- **Andesite Alloy Chicken** never appeared when Create and Industrial Foregoing were installed together. They were fighting over the same chicken id, and the Andesite Alloy Chicken now has its own. Worlds that only had Create and already owned Andesite Alloy Chickens will lose them.
- Texture file names are all lowercase now. This avoids missing textures on Linux servers.

## Removed

- **Chickens created automatically from other mods.** The mod no longer invents chickens for the mods you have installed: the Cobalt, Pig Iron, Manyullyn and Knightslime chickens, one chicken per Mekanism chemical, and one chicken per liquid are gone. Only chickens that are written out in the mod, in `custom_chickens.toml` or in KubeJS exist now. Chickens of those kinds disappear from existing worlds.

# [2.7.1] - 17/10/2026

## Fixed

- **Radiation from ordinary uranium items.** Holding any uranium ingot or material irradiated the player, because every item a radioactive chicken lays or drops was treated as radioactive. Only the radioactive chicken items, their spawn eggs, their radioactive eggs and live radioactive chickens irradiate now.
- **Radiation lingering after moving away.** The radiation sources left behind by chickens, dropped items and containers are removed a couple of seconds after the emitter is gone, and the dose a player picked up from them is cleared once they carry nothing radioactive and stand outside every radiation zone. Radiation from real Mekanism sources is left untouched.

## Changed

- Live radioactive chickens now emit radiation around them, with the same intensity as a radioactive egg. The chicken itself stays immune.

## CI

- The build workflow gained a "Publish to CurseForge" step. It runs only when the commit message contains `[curseforge` (optionally `[curseforge beta]` or `[curseforge alpha]`), reads this changelog for the current version, uploads the jar for NeoForge 1.21.1 with Patchouli as a required dependency, and tags the upload so it is never repeated. It needs the `CURSEFORGE_TOKEN` secret and the `CURSEFORGE_PROJECT_ID` variable.

# [2.7.0] - 14/9/2026

## Added

- **Patchouli guidebook** — Patchouli is now a required dependency and the mod ships an in-game guide covering every registered block, machine, item and mechanic. Five categories (Getting Started, Production, Breeding, Egg Conversion, Upgrades and Tools) across 25 entries, translated into `en_us`, `es_es`, `es_mx` and `es_ar`. The book is available from the Modern Chickens creative tab.
- **Custom book artwork** — the guide no longer uses Patchouli's default look. It ships its own weathered-oak-and-parchment book sheet (background, page arrows, bookmarks, config/eye/resize buttons, nameplate, separators, lock and entry markings), a page filler, a crafting grid overlay, and a dedicated `guide_book` item texture.

## Fixed

- **Chickens never spawned naturally.** Four separate bugs stacked on top of each other, each one hiding the next:
  - The biome modifier instance was registered through `RegisterEvent` on `NeoForgeRegistries.Keys.BIOME_MODIFIERS`. That is a dynamic datapack registry, so the event never fires for it and the call was a silent no-op. No JSON entry existed either, so `modify()` never ran and no biome ever received a chicken, rooster or Mega Chicken spawn entry. The modifier is now generated as a proper datapack entry through a new `DatapackBuiltinEntriesProvider`.
  - `ChickensChicken.finalizeSpawn` was declared with `ServerLevel` instead of `ServerLevelAccessor`, so it never overrode the vanilla method and was never called. Every naturally spawned chicken kept the default type `0` — the White Chicken — which is why only bone-laying chickens appeared.
  - `ChickensChicken.GroupData` implemented `SpawnGroupData` directly, but `AgeableMob.finalizeSpawn` casts the incoming group data to `AgeableMobGroupData`. The second chicken of any naturally spawned group crashed chunk generation with a `ClassCastException`.
  - The `hasVanillaChickenSpawn` gate was applied to every spawn type. No Nether or End biome contains a vanilla chicken, so `SpawnType.HELL` and `SpawnType.END` could never be reached even though their spawn tables were built. The gate now applies only to overworld types; roosters and Mega Chickens still require a vanilla-chicken biome.
- Jade crashed the client on the title screen in development because eight `config.jade.plugin_chickens.*` entries had no translation. All nine plugin UIDs are now translated in every supported language.

## Changed

- Natural spawn weight now honours the `spawnProbability` config value directly instead of scaling it to a tenth. Modern Chickens sit at weight 10, the same as the vanilla chicken, instead of weight 1. Lower `spawnProbability` to make them rarer.

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
