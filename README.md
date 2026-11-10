# Modern Chickens

Modern Chickens is a NeoForge 26.1.2 port of the classic **Chickens** and **Roost** mods. Rebuild the original breeding-driven resource automation loop with a large chicken roster, configurable breeding trees, automated coops, dynamic fluid and chemical content, RF generation, and optional Mekanism radiation integration.

### Mod Pages

- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/modern-chickens)
- [Modrinth](https://modrinth.com/mod/modern-chickens)
- [Wiki](https://github.com/Modern-Mods/ModernChickens/wiki)

## Requirements and optional integrations

- **Minecraft:** 26.1.2
- **Loader:** NeoForge 26.1.x
- **Mekanism:** Optional. Required for Mekanism chemical/gas chickens and the Mekanism radiation bridge.
- **JEI:** Optional. Adds breeding, chicken-stat, converter, and dousing recipe categories.
- **Jade/WTHIT:** Optional. Adds chicken, machine, tank, energy, and progress overlays.

Mod-specific chickens and dynamic content are registered only when the relevant parent mod or resource exists. The core breeding and Roost gameplay does not require every integration listed on this page.

## Gameplay overview

- **Collect basic chickens.** Use natural spawns, spawn eggs, and the early Tier 1 roster to start your collection. Throw coloured eggs to obtain dyed variants.
- **Analyse and breed.** Right-click chickens with the **Analyzer** to view Growth, Gain, and Strength. Combine chickens in the **Breeder** to unlock higher tiers.
- **Automate production.** Put chickens in **Roosts** and use **Collectors**, hoppers, and item pipes to move their drops into storage.
- **Scale the coop.** Use **Henhouses**, **Nests**, **Roosters**, the **Incubator**, and the Avian converters to build hands-free production lines.
- **Tune the pack.** Edit `config/chickens.toml` for global options and `config/custom_chickens.toml` for stock/custom chicken tables, lay rates, drops, breeding, and spawn behavior.

The roster, drops, parents, spawn types, textures, and lay coefficients are data-driven. Higher-tier chickens normally do not spawn naturally, so breeding remains the intended progression path.

## Feature highlights

- A large legacy-inspired chicken catalogue with stats, drops, breeding trees, spawn eggs, coloured eggs, Chicken Items, and Chicken Catchers.
- Configurable Roosts, Breeders, Henhouses, Collectors, Nests, Roosters, and an RF-powered **Incubator**.
- An **Avian Fluid Converter** for cracking liquid eggs into fluids and feeding adjacent tanks or pipes.
- An **Avian Chemical Converter** for cracking Mekanism chemical and gas eggs into buffered chemical storage.
- An **Avian Dousing Machine** for imprinting Smart Chickens with a fluid or Mekanism chemical and creating the matching chicken spawn egg.
- An **Avian Flux Converter** and **Redstone Flux Chickens** for converting Flux Eggs into stored and exportable RF.
- Dynamic liquid, chemical, and gas eggs for registered fluids, Mekanism chemicals, and Mekanism gases.
- JEI recipe categories, item subtypes, and Jade/WTHIT overlays for chickens, stats, breeding, tanks, machines, and progress.
- Custom chicken definitions through `config/custom_chickens.toml`, without recompiling the mod.
- Datapack spawn-plan overrides and commands for inspecting or testing spawn behavior.

## Progression and natural spawning

Modern Chickens adds modded birds to biomes that already have a vanilla chicken spawn. It uses the shared `CREATURE` mob cap rather than a separate population system. The default natural-spawn broods are 1-2 birds; roosters spawn alone at low weight. Most parent-based chickens remain breeding-only unless their configuration allows natural spawning.

Mega Chickens use their own rare spawn rule. See [Mega Chickens](#mega-chickens) below.

### Where the basic chickens spawn

Some basic chickens appear where they make sense, even in biomes without vanilla chickens:

- **Sand Chicken:** beaches and deserts.
- **Snowball Chicken:** frozen biomes with ice or snow.
- **White, Log and Flint chickens:** any land biome except deserts, beaches, oceans and mushroom islands.
- **Quartz Chicken:** every Nether biome, fortresses included.
- **Soul Sand Chicken:** Soul Sand Valleys and Nether fortresses.
- **Ender Chicken:** the End, End cities included.
- **Ice Chicken:** it does not spawn naturally. You get it by breeding.

### Spawn-plan datapacks

Spawn overrides live under:

```text
data/<namespace>/chickens/spawn_plans/
```

Spawn plans can override these fields:

- `spawn_type` - `normal`, `snow`, `end`, or `hell`.
- `spawn_weight` - Absolute spawn weight. Use this or `weight_multiplier`, not both.
- `weight_multiplier` - Multiplies the configured base weight.
- `min_brood_size` and `max_brood_size` - Spawn group size bounds.
- `spawn_charge` and `energy_budget` - Mob-charge values used by the biome spawn settings.

### Useful commands

```text
/chickens spawn multiplier <value>
/chickens spawn debug <true|false>
/chickens spawn summon <chickenNameOrId>
/chickens spawn summon_random [normal|snow|end|hell]
/chickens custom list
/chickens export breeding
/chickens export breeding weights
```

## Integrated mods and unlockable chickens

Prebuilt integrations register only when their parent mod is loaded.

- **Applied Energistics 2:** Certus Quartz, Charged Certus, Silicon, Fluix, Sky Stone.
- **Extended AE / Advanced AE:** Entro Alloy, Quantum Alloy, Entro Crystal.
- **Applied Fluix / Applied Generators:** Applied Fluix Crystal, Ember Crystal.
- **Mekanism:** Fluorite, HDPE Pellet, Plutonium Pellet, Polonium Pellet, Antimatter Pellet.
- **Mekanism Extras:** Naquadah ingots, fluids, and chemicals.
- **EvilCraft:** Blood.
- **Just Dire Things:** Celestigem, Eclipse Alloy, Time Crystal.
- **Industrial Foregoing:** Plastic, Rubber.
- **Avaritia:** Neutron Pile, Infinity Ingot.
- **RFTools Base:** Dimensional Shard.
- **Beyond Dimensions:** Shattered Space-Time Crystal.
- **Applied Flux:** Redstone Crystal.
- **SpectreThings / Irregular Implements:** Ectoplasm, using one consolidated chicken when either mod is present.
- **Draconic Evolution:** Small Chaos Fragment.
- **Mystical Agriculture:** Inferium, Prudentium, Tertium, Imperium, Supremium, and Insanium essences.
- **Powah:** Uraninite.
- **Flux Networks:** Flux Dust.
- **Actually Additions:** Black Quartz, Restonia, Diamatine, Emeradic, Enori, Palis, and Void Crystals.
- **Extreme Reactors:** Yellorium, Graphite, Cyanite, and Blutonium through the normal material integration.
- **Ender IO:** Electrical Steel, Energetic Alloy, Vibrant Alloy, Redstone Alloy, Conductive Iron, Pulsating Iron, Dark Steel, and Soularium.
- **Immersive Engineering:** Registered ingot and fluid resources, including uranium-related content when present.
- **Vanilla:** Amethyst Shards, Nether Stars, Dragon Eggs, Breeze Rods, and the Door Chicken with one variant for every wood type (Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson and Warped).
- **Thaumaturge:** Thaumium, Void Metal, Alchemical Brass, Quicksilver, Amber, Alumentum, Salis Mundus, Cinnabar, the seven mine crystals and the native clusters (Iron, Copper, Gold, Tin, Lead, Silver, Cinnabar and Quartz).
- **Astral Sorcery:** Stardust, Starmetal, Rock Crystal, Celestial Crystal, Aquamarine, Resonating Gem and Lumen Crystal.
- **Ultimate Armoury: Tools And Weapons:** JoanFoite, Mushashite, Nadienite and Steel.
- **Allthemodium:** Allthemodium, Vibranium, Unobtainium and the three alloys that mix them.
- **Ender IO Evolution:** Construction Alloy, Crude Steel, Crystalline Alloy, Crystalline Pink Slime, Energetic Silver, Melodic Alloy, Stellar Alloy and Vivid Alloy.
- **Mystical Agriculture addons:** one chicken for every tier essence of the addons you have (Soulium, Awakened Supremium, Awakened Draconium, Lightium, Ornium, Ouranium, Tornium) plus a Soulstone Cobble chicken.
- **Applied Energistics 2 circuits:** Printed Silicon, the Logic, Calculation and Engineering Processors and their Printed Circuits, plus the processors of Advanced AE, ExtendedAE and MEGA Cells.
- **Void Miners Remastered:** one Miner Chicken per tier (see [Void Miner Chickens](#void-miner-chickens)).
- **More metals in the Bronze Chicken style:** Energized Power, Ad Astra, Oritech, Twilight Forest, Mystical Extended Tier, Extended Crafting, Create Encased, Masterful Botany and Extra Botany.

The exact roster depends on the mods and registered resources in your instance. JEI and the Analyzer are the authoritative in-game references.

![Chickens available in ATM10](https://i.imgur.com/YxIPuRC.gif)

![Look at all these chickens](https://media1.tenor.com/m/hRVtp7V06GQAAAAd/look-at.gif)

## Dynamic eggs, fluids, chemicals, and gases

Modern Chickens generates a dedicated egg variant for supported registered fluids, Mekanism chemicals, and Mekanism gases during setup. Generated eggs inherit the resource's name and colour.

The complete automation chain is:

**Resource chicken -> liquid/chemical/gas egg -> converter buffer -> pipes, tanks, or machines**

![Dynamic fluid eggs](https://i.imgur.com/37OGGEr.png)

![Dynamic chemical eggs](https://i.imgur.com/jG71Xpp.png)

### Avian Fluid and Chemical Converters

Liquid and chemical eggs no longer need to be hand-placed to deploy their contents. Put a liquid egg into the **Avian Fluid Converter**, or a chemical/gas egg into the **Avian Chemical Converter**. The converter cracks the shell, stores the contents in an internal buffer, and feeds adjacent tanks, pipes, tubes, or machines each tick.

- **JEI discovery:** JEI lists every supported egg and the fluid or chemical volume it produces. The converter block is registered as a JEI catalyst.
- **Runtime coverage:** A new registered fluid or Mekanism chemical automatically gets a matching egg without a hardcoded page entry or manual JSON edit.
- **Monitoring:** Jade and WTHIT show the resource name, stored amount, tank capacity, and machine state without opening the GUI.
- **Automation:** Pair the converter with normal fluid or chemical transport to move experience, biofuel, radioactive waste, and other technology resources directly from the coop.

Useful server configuration keys are:

- `general.avianFluidConverterCapacity` - Fluid buffer capacity; default `8000` mB.
- `general.avianFluidConverterTransferRate` - Fluid transfer rate; default `2000` mB/t.
- `general.avianChemicalConverterCapacity` - Chemical buffer capacity; default `8000` units.
- `general.avianChemicalConverterTransferRate` - Chemical transfer rate; default `2000` units/t.
- `general.liquidEggHazardsEnabled` - Enables liquid-egg hazard tooltips and placement effects; default `true`.

![Avian Fluid and Chemical Converters](https://i.imgur.com/kokLuyI.jpeg)

## Avian Dousing Machine

The **Avian Dousing Machine** connects the Smart Chicken lineage to the dynamic fluid and chemical chicken generators. It consumes a Smart Chicken, a stored reagent, and RF to create the matching chicken spawn egg, letting you unlock generated breeds in survival without commands or manual registry edits.

- **Input:** Place a Smart Chicken spawn egg or captured Smart Chicken in the input slot. Only Smart Chickens are accepted as blank templates.
- **Fluid reagent:** Pipe a fluid into the built-in tank, often from an Avian Fluid Converter.
- **Chemical reagent:** Feed a Mekanism chemical or gas into the internal buffer directly or through an Avian Chemical Converter.
- **Priority:** When both reagent types are available, the machine prefers the chemical buffer.
- **Output:** One Smart Chicken and the required reagent are consumed to create the corresponding liquid or chemical chicken spawn egg. That chicken lays the matching liquid or chemical egg.
- **Adjacent transport:** The machine can pull compatible fluids and Mekanism chemicals from adjacent tanks, pipes, tubes, or converters.
- **JEI:** The Avian Dousing Machine category lists supported reagents, costs, RF requirements, and output spawn eggs.
- **Monitoring:** Jade and WTHIT show stored reagent, energy, and infusion progress.

![Converting chemicals](https://i.imgur.com/fwZnEtI.jpeg)

![Converting liquids](https://i.imgur.com/Ge7zx4b.jpeg)

![Avian Dousing Machine process](https://i.imgur.com/kSI861D.gif)

The resulting progression is:

**Liquid or Mekanism chemical -> egg -> buffered converter -> Avian Dousing Machine -> dedicated chicken**

## Henhouse

The **Henhouse** collects nearby chicken drops into its internal inventory. It accepts either hay bales or FE/RF from a standard energy provider.

- One charge collects one item.
- A hay bale provides the internal charge and spent hay becomes dirt.
- Outputs can be extracted from below by hoppers or item pipes.
- It is useful for Dragon Eggs, Nether Stars, and other drops that should be collected without placing chickens in a Roost.
- It comes in every wood type: Oak, Acacia, Birch, Dark Oak, Jungle, Spruce, Mangrove, Cherry, Bamboo, Crimson and Warped. The recipe is the same, with the matching planks.

## Boss Chickens via Avian Dousing

Dragon and Wither Chickens cannot be bred. They must be infused in the **Avian Dousing Machine**.

### 1. Add the base chicken

- **Dragon Chicken:** Place an **Obsidian Chicken** in the input slot.
- **Wither Chicken:** Place a **Soul Sand Chicken** in the input slot.

### 2. Fill the special buffer

- **Dragon Chicken:** Insert Dragon's Breath Bottles.
- **Wither Chicken:** Insert Nether Stars.
- Each special item adds `100 mB`.
- The special buffer requires `1000 mB` total, or `10` items.

### 3. Supply power

Ensure the Avian Dousing Machine is powered. It consumes the configured RF during infusion.

### 4. Start the infusion

When the special buffer is full and enough RF is available, the base chicken is consumed and replaced by the appropriate boss chicken spawn egg.

- **Dragon Chicken -> lays Dragon Eggs**
- **Wither Chicken -> lays Nether Stars**

### 5. Automate production

Place the resulting chicken in a Roost or Henhouse to farm Dragon Eggs or Nether Stars hands-free.

## Void Miner Chickens

Void Miners Remastered adds one chicken per tier: Amethystine, Aurantium, Azurine, Caerium, Citrinetine, Rosarium and Rubetine. They cannot be bred.

- Right-click a normal chicken with the Frame of the tier you want and it becomes that Miner Chicken.
- A Miner Chicken always lays 24 crystals of its tier, whatever its stats are, even at 10/10/10.
- They are very slow, so keep a few in Roosts and be patient.

## Roosters

![Rooster](https://i.imgur.com/d49iLC3.png)

Roosters are utility birds inspired by Hatchery's rooster. They never lay eggs themselves; their job is to support breeding and Roost production.

- **Normal chicken AI:** Roosters wander, follow chicken food, panic, and seek nearby adult hens.
- **Seed charge:** Right-click a rooster with an empty hand or chicken food to open its small inventory. Items in `#minecraft:chicken_food` are accepted. Every pair of seeds becomes two points of internal seed charge.
- **Fertilization:** A charged adult rooster seeks a nearby adult non-rooster chicken, spends two seed-charge points, and triggers breeding. The hen determines the offspring type and genetics.
- **Item form:** The Chicken Catcher turns a mature rooster into a Chicken Item marked as a rooster. The item can be placed back into the world or inserted into a Nest.
- **Roost synergy:** Roosts scan within the configured rooster-aura range for active Nests. Roosters in an active Nest add a production bonus on top of the base Roost speed.

Default rooster-related settings:

| Key | Default | Purpose |
| --- | ---: | --- |
| `general.roosterAuraMultiplier` | `1.25` | Roost production multiplier while a rooster aura applies. |
| `general.roosterAuraRange` | `4` | Range used when Roosts look for active Nests. |
| `general.roostSpeed` | `1.0` | Base Roost speed multiplier. |

## Nest

![Nest](https://i.imgur.com/0Pqh6ng.png)

The **Nest** turns captured roosters and seeds into an aura that boosts nearby Roosts. It does not produce items by itself.

- **Two slots:** The GUI has a seed slot on the left and a rooster slot on the right.
- **Accepted seeds:** Wheat, beetroot, melon, and pumpkin seeds.
- **Rooster slot:** Accepts only rooster-marked Chicken Items. Regular Chicken Items belong in Roosts or Breeders.
- **Automation:** The Nest exposes sided inventory access for hoppers and item pipes.
- **Aura fuel:** When at least one rooster is present, the Nest consumes one seed and turns it into aura time. It pauses seed consumption when no rooster is present.
- **Rooster cap:** Only up to the configured number of roosters contributes to the aura. Extra roosters do not increase its strength.

Default Nest settings:

| Key | Default | Purpose |
| --- | ---: | --- |
| `general.nestMaxRoosters` | `1` | Maximum roosters counted by one Nest. |
| `general.nestSeedDurationTicks` | `1200` ticks | Aura time provided by one seed; 60 seconds. Set to `0` to disable aura production. |

## Incubator

![Incubator](https://i.imgur.com/5pxcUQB.png)

![Incubator GUI](https://i.imgur.com/a7w4Cdl.png)

The **Incubator** is an RF-powered machine that turns mod chicken spawn eggs into portable Chicken Items with default `1/1/1` stats. These items can be inserted into Roosts or Breeders, or placed back into the world as entities.

- **Input:** The left slot accepts Modern Chickens spawn eggs.
- **Output:** The right slot produces the matching portable Chicken Item.
- **Energy:** The Incubator pulls RF from adjacent blocks through the NeoForge energy capability and displays its stored energy in the GUI.
- **Processing:** Each operation uses a 200-tick progress bar by default and reserves the configured RF cost as it advances.
- **Automation:** Eggs can be inserted from the top or sides; Chicken Items can be extracted from the bottom.

Default Incubator settings:

| Key | Default | Purpose |
| --- | ---: | --- |
| `general.incubatorEnergyCost` | `10000` RF | Energy cost per Chicken Item. |
| `general.incubatorCapacity` | `100000` RF | Internal energy capacity. |
| `general.incubatorMaxReceive` | `4000` RF/t | Maximum energy received from adjacent blocks per tick. |

## Mega Chickens

![Mega Chicken](https://i.imgur.com/2lmsG6r.png)

![Mega Chicken with equipment](https://i.imgur.com/iq5UhW9.png)

The entity is named **Mega Chicken** in-game. It is a rare, oversized, rideable utility chicken with taming, saddles, two-chest cargo, breed-based appearances, and recoverable portable storage.

### Finding and taming one

- **Rare natural spawn:** Mega Chickens are added to biomes that already spawn vanilla chickens. A normal animal spawn must pass, followed by a `1-in-128` Mega Chicken roll.
- **Spawn Egg:** The **Mega Chicken Spawn Egg** is available for controlled spawning and testing.
- **No breeding:** Mega Chickens cannot breed and are not part of the normal resource-chicken breeding tree.
- **Food-based taming:** Feed a wild Mega Chicken items in the `#minecraft:chicken_food` tag. The required amount is randomized between `16` and `64` items and is influenced by the individual chicken's health, movement speed, and jump strength. The remaining amount is shown to the player while taming.

### Riding and cargo

- A tamed Mega Chicken can be ridden with a normal saddle.
- It accepts up to two vanilla chests, with `27` cargo slots per chest and `54` total cargo slots.
- The inventory screen is a horse-style equipment and cargo screen.
- A chest cannot be removed while that chest's cargo is still occupied. Empty the relevant cargo section first.
- The owner can open the inventory and manage the saddle, chests, and cargo.

### Breed-based appearances

Mega Chickens can be skinned using a captured breed:

1. Capture a non-rooster chicken into a **Chicken Item**. The item must have exactly `10/10/10` Growth, Gain, and Strength.
2. The Mega Chicken's owner right-clicks the tamed Mega Chicken with that item.
3. The Chicken Item is consumed and the Mega Chicken stores that breed as its appearance.
4. If `textures/entity/megachicken/<breed>.png` exists, that texture is used. If it does not, the normal tamed Mega Chicken texture is used.

![Mega Chicken with a saddle and chests](https://i.imgur.com/T5yOSsl.png)

![Mega Chicken appearance examples](https://i.imgur.com/6imh12c.png)

### Capture, storage, and revival

- The Chicken Catcher can capture only a tamed, owned Mega Chicken that is not currently being ridden.
- A captured Mega Chicken becomes a stack-size-one **Mega Chicken** item containing its entity data: owner, appearance, health and attributes, saddle, chests, cargo, and other saved data.
- A live captured Mega Chicken item can be placed directly on a block or by using it while looking at a block.
- If a Mega Chicken dies, it creates a protected revival item instead of ordinary death loot. The item preserves the Mega Chicken's data and restores it at full health.
- To activate a death/revival item, hold a Nether Star in the player's offhand and right-click the item. The Nether Star is consumed.
- After activation, right-click a block with the item to restore the Mega Chicken.
- Death/revival item entities are fire-resistant, invulnerable, no-gravity, and have unlimited lifetime, so the recovery item is not destroyed by fire, despawning, or normal item damage.
- Live captured items do not need Nether Star activation; only the death/revival form does.

## Redstone Flux generation

![Redstone Flux Eggs](https://i.imgur.com/WklUeVL.png)

The **Redstone Flux Chicken** is a Tier 3 breed made from Redstone and Glowstone Chickens. It lays **Flux Eggs** that contain RF.

- A base Flux Egg stores `1000 RF`.
- Each Growth, Gain, or Strength point above `1` adds `100 RF` to the egg's stored energy.
- A `10/10/10` chicken produces up to `3700 RF` per egg.
- Gain also controls the normal drop stack scaling, so a `10/10/10` chicken can produce three Flux Eggs per cycle.
- The **Avian Flux Converter** processes one Flux Egg at a time, stores its payload in an internal battery, and exports RF to adjacent machines or batteries.
- The converter has `50000 RF` of storage by default, discards empty shells, and retains stored RF when broken.

Useful RF settings:

- `general.fluxEggCapacityMultiplier` - Scales stored RF in Flux Eggs.
- `general.avianFluxCapacity` - Avian Flux Converter capacity; default `50000 RF`.
- `general.avianFluxMaxReceive` - Maximum RF received per tick; default `4000`.
- `general.avianFluxMaxExtract` - Maximum RF exported per tick; default `4000`.
- `general.avianFluxEffectsEnabled` - Enables or disables the converter's light and particle effects without disabling energy transfer.

## Radioactive chickens and items

![Redstone Flux Eggs](https://i.imgur.com/Fj9whGz.png)

Radioactive content is an optional integration with **Mekanism's radiation system**. Modern Chickens does not add a separate radiation mechanic and does not require Mekanism for the core breeding mod.

### Radioactive breeds

The built-in radioactive resource breeds include:

- **Radioactive Waste Chicken** - produces Mekanism radioactive waste eggs.
- **Uranium Chicken**
- **Yellorium Chicken**
- **Blutonium Chicken**
- **Uraninite Chicken**
- **Plutonium Pellet Chicken**
- **Polonium Pellet Chicken**
- **Antimatter Pellet Chicken**

Mekanism chemicals and gases are inspected at runtime. If Mekanism reports a chemical as radioactive, its generated Chemical Egg or Gas Egg and matching chicken are treated as radioactive automatically. This also covers radioactive chemical content supplied by compatible integrations without a hardcoded list in this page or a manual config edit.

### What is radioactive?

The radiation bridge recognizes radioactive:

- Liquid, chemical, and gas eggs containing radioactive content.
- Spawn eggs and captured Chicken Items for radioactive breeds.
- Resource items produced by radioactive breeds.
- Radioactive fluids or chemicals stored in the Avian Fluid Converter, Avian Chemical Converter, or Avian Dousing Machine.

### Safety behavior

- **Living resource chickens are localized.** Radioactive resource-chicken entities show Mekanism radiation particles and are protected from Mekanism radiation damage. They do not continuously create an atmospheric radiation source simply by standing nearby.
- **Dropped radioactive items emit a small source.** Dropped radioactive eggs, Chicken Items, spawn eggs, and radioactive resource outputs create a low-level Mekanism radiation source and show warning particles.
- **Carried items can irradiate the player.** Radioactive items in the inventory, armor slots, or offhand contribute a small, capped amount of Mekanism radiation.
- **Stored contents can warn and radiate.** Supported Modern Chickens machines and loaded inventories containing radioactive chicken content emit a small source and warning particles while the content remains inside. Removing the content stops the warning.
- **Breaking a loaded Dousing Machine matters.** Radioactive chemical contents are spilled through Mekanism's radiation API when the machine is removed, so dismantle an active radioactive setup carefully.

Radioactive liquid eggs display a hazard tooltip and can apply Wither briefly when placed by a non-creative player while `general.liquidEggHazardsEnabled` is enabled. Chemical and gas eggs display their radioactive hazard in their tooltip. Disabling that setting removes the liquid-egg hazard tooltip and placement effects; it does not disable Mekanism radiation emitted by radioactive items or stored contents.

## Recipes that ask for an exact egg or chicken

Every liquid, chemical and gas egg is the same item, and so is every chicken, so a normal recipe cannot tell them apart. The `chickens:variant` ingredient picks one exact variant. It works in datapack recipes and in KubeJS recipes.

`kind` and `chickenid` are both required. A recipe that leaves either out fails with a message naming the missing field:

- `kind` - `liquid_egg`, `chemical_egg`, `gas_egg`, `chicken` or `spawn_egg`.
- `chickenid` - The fluid or chemical id for eggs (for example `minecraft:water`), or the chicken name for chickens and spawn eggs (for example `DiamondChicken`). The older field name `id` is still accepted; if both are present, `chickenid` wins.
- `min_growth`, `min_gain` and `min_strength` - Optional minimum stats, only used when `kind` is `chicken`.

```json
{"type": "chickens:variant", "kind": "liquid_egg", "chickenid": "minecraft:water"}
{"type": "chickens:variant", "kind": "chicken", "chickenid": "DiamondChicken", "min_growth": 5, "min_gain": 3}
```

In KubeJS you can chain the same fields with `variant` anywhere an ingredient is expected (shaped keys, shapeless lists, other mods' recipes). `kind(...)` and `chickenid(...)` are both mandatory, in any order. `ChickenVariant` is an identical alias if a script already has its own `variant` variable. Chickens also accept `.minGrowth(n)`, `.minGain(n)` and `.minStrength(n)`:

```js
event.shapeless('minecraft:diamond', [
  variant.kind('liquid_egg').chickenid('minecraft:water'),
  variant.kind('chicken').chickenid('IronChicken').minGrowth(5).minGain(3).minStrength(1),
  'minecraft:coal'
])
```

JEI also tells the variants apart: pressing R or U on an egg shows only the recipes of that egg.

## Custom chicken creation

After the first run, Modern Chickens generates `config/custom_chickens.toml`. It contains every stock `[chickens.<name>]` table and is also where bespoke chickens are defined. Global options remain in `config/chickens.toml` under `[general]`.

Each entry can define:

- The quoted table name - Unique registry name; use `[chickens."Steel Chicken"]` for names containing spaces.
- `id` - Optional positive numeric id; omitted values use the next free id.
- `texture` - In-world texture resource location.
- `itemTexture` - Optional Chicken Item and JEI sprite resource location.
- `layItemName`, `layItemAmount`, and `layItemMeta` - Resource item id, count, and liquid-egg variant.
- `dropItemName`, `dropItemAmount`, and `dropItemMeta` - Death-drop resource item id, count, and variant.
- `backgroundColor` and `foregroundColor` - Hex or decimal colours for generated/tinted textures.
- `parent1` and `parent2` - Existing chicken names; both must resolve for breeding to remain configured.
- `spawnType` - `NORMAL`, `SNOW`, `END`, `HELL`, or `NONE`.
- `layCoefficient` - Lay-time multiplier; defaults to `1.0`.
- `displayName` - Optional in-game display name.
- `generatedTexture` - Whether the configured texture should be colour-tinted.
- `enabled` - Whether the chicken participates in registries and breeding; defaults to `true`.
- `allowNaturalSpawn` - Allows a parent-based chicken into natural spawn tables.
- `allowDousing` - Allows the Avian Dousing Machine to create this chicken from its registered fluid or chemical. Keep it limited to intentional progression starters.

Missing fields use the mod defaults. Resource locations are normalized to lowercase; omitting a texture falls back to the bone-chicken sprite when `generatedTexture = true`. Existing `chickens_custom.json` files are still read for migration compatibility, but new definitions must use TOML.

## Configuration

The global server configuration is generated at `config/chickens.toml`; stock and custom chicken tables are generated at `config/custom_chickens.toml`. Stop the game before editing either file, then restart the client/server to apply changes.

Common settings include:

- `general.enableFluidChickens`
- `general.autoRegisterFluidChickens`
- `general.enableChemicalChickens`
- `general.enableGasChickens`
- `general.disableEggLaying`
- `general.alwaysShowStats`
- `general.roostSpeed`
- `general.breederSpeed`
- `general.breedingChanceMultiplier`
- `general.scalingDrops`
- `general.collectorScanRange`
- `general.liquidEggHazardsEnabled`
- `general.avianFluidConverterCapacity`
- `general.avianFluidConverterTransferRate`
- `general.avianChemicalConverterCapacity`
- `general.avianChemicalConverterTransferRate`
- `general.fluxEggCapacityMultiplier`
- `general.avianFluxCapacity`
- `general.incubatorEnergyCost`
- `general.incubatorCapacity`
- `general.incubatorMaxReceive`

The generated file is the source of truth for the available values and comments in your installed version. Existing legacy `chickens.properties` files are read once for migration and are no longer written by current versions.

See [Explanation.md](Explanation.md) for the complete TOML option reference and migration instructions.

## Links

- [Modern Chickens on GitHub](https://github.com/STRHercules/ModernChickens)
- [Chickens](https://www.curseforge.com/minecraft/mc-mods/chickens)
- [Roost](https://www.curseforge.com/minecraft/mc-mods/roost)

## Credits

- [setycz](https://www.curseforge.com/members/setycz/projects) - original [Chickens](https://www.curseforge.com/minecraft/mc-mods/chickens)
- [Timrwood](https://www.curseforge.com/members/timrwood/projects) - original [Roost](https://www.curseforge.com/minecraft/mc-mods/roost)

---

## Reference

### New Egg Assets

_Dynamic Fluid Eggs!_

![Dynamic Fluid Eggs](https://i.imgur.com/37OGGEr.png)

_Dynamic Chemical Eggs!_

![Dynamic Chemical Eggs](https://i.imgur.com/jG71Xpp.png)

_New Redstone Flux Egg Asset!_

![Redstone Flux Eggs](https://i.imgur.com/WklUeVL.png)

> Dynamic Fluid and Chemical Eggs will mimic their respective resource's color.
	

#### Dousing recipes and KubeJS

The machine registers the `chickens:avian_dousing` recipe type. It is a normal server recipe, so datapacks and KubeJS can add or replace dousing recipes without a KubeJS dependency in the mod. Chicken names are the configured registry names (case-insensitive), not item IDs.

```js
ServerEvents.recipes(event => {
  event.custom({
    type: 'chickens:avian_dousing',
    input: 'IronChicken',
    result: 'GoldChicken',
    reagent: {
      type: 'item',
      id: 'minecraft:gold_ingot',
      amount: 4
    },
    energy: 10000
  }).id('example:iron_to_gold_chicken')
})
```

`reagent.type` accepts `item`, `fluid`, or `chemical`; `reagent.id` is the normal namespaced registry ID and `amount` is the consumed item count or stored mB. Chemical recipes require Mekanism at runtime, but the recipe type itself remains safe to load without Mekanism. Right-click the machine with item reagents after placing the base chicken in the left slot. The built-in Dragon/Wither recipes use the same data path and can be replaced by removing `chickens:avian_dousing_dragon` or `chickens:avian_dousing_wither` first.

With KubeJS installed the mod also registers a recipe schema, so the same recipe can be written in builder form:

```js
ServerEvents.recipes(event => {
  event.recipes.chickens.avian_dousing(
    'GoldChicken',                                            // result
    'IronChicken',                                            // input
    { type: 'item', id: 'minecraft:gold_ingot', amount: 4 }   // reagent
  ).energy(10000)
})
```

Ready-to-use scripts and a full field reference live in [`Examples/KubeJS/`](Examples/KubeJS/README.md), which also documents the `ChickensEvents.registry` startup event for defining chickens from scripts.

Combining the Dousing Machine with the Fluid and Chemical Converters lets you go from liquids or Mekanism chemicals → eggs → buffered tanks → dedicated chickens entirely inside the Modern Chickens ecosystem.

![Dousing Process](https://i.imgur.com/kSI861D.gif)

![Dousing WTHIT](https://i.imgur.com/V25C4td.png)


### Redstone Flux

![RF Chicken Generator!](https://i.imgur.com/Djoeb5P.jpeg)


Modern Chickens introduces power generation through a dedicated Redstone Flux progression line:

- **Redstone Flux Chicken** – A tier 3 breed unlocked by pairing Redstone and Glowstone chickens. Every drop is a charged Flux Egg so energy farms can start as soon as the bird hatches.
- **Flux Egg** – Stores Redstone Flux directly on the item stack. Freshly laid eggs hold 1,000 RF and gain another 100 RF for every growth, gain, or strength point above 1 (maxing out at 3,700 RF per egg from a 10/10/10 chicken).
- **Avian Flux Converter** – A single-slot machine that drains Flux Eggs into a 50,000 RF internal buffer while exporting energy to adjacent consumers. Empty shells are discarded automatically once their payload is exhausted.

#### Flux Egg charge scaling

Flux Eggs inherit the stats of the chicken that laid them, so every breed and breeding investment matters:

| Chicken stats | Stored RF per egg | Notes |
| --- | --- | --- |
| 1/1/1 (base) | 1,000 RF | Entry-level output straight from newly bred birds. |
| 10/10/10 (max) | 3,700 RF | Gains also triple the stack size, dropping **three** eggs per cycle. |

The Avian Flux Converter converts each egg’s stored energy on a one-to-one basis, so farms can bank or route the full payload without transmission loss.

#### Roost throughput examples

Roost production scales with both chicken stats and stack size. Each cycle takes roughly 27,000 server ticks (the midpoint between the Redstone Flux Chicken’s 18,000–36,000 tick lay window) divided by the number of working chickens times their growth stat. The figures below assume default config values, infinite storage, and continuous feeding so every roost stays active.

| Installation | RF/t per roost | 10 roosts | 20 roosts | 30 roosts |
| --- | ---: | ---: | ---: | ---: |
| 1× base Redstone Flux Chicken (stats 1/1/1) | ≈0.04 | ≈0.37 | ≈0.74 | ≈1.11 |
| 1× max-stat Redstone Flux Chicken (stats 10/10/10) | ≈4.11 | ≈41.11 | ≈82.22 | ≈123.33 |
| 16× max-stat Redstone Flux Chickens (full roost of 10/10/10) | ≈65.78 | ≈657.78 | ≈1,315.56 | ≈1,973.33 |

> **Assumptions:** RF/t values use the average lay time and treat each Flux Egg as delivering its entire stored energy (1,000 RF for base birds, 3,700 RF ×3 eggs for maxed birds). Actual outputs fluctuate slightly with the random lay timer and any custom `roostSpeed` tweaks in [chickens.toml](https://github.com/STRHercules/ModernChickens/blob/main/Examples/Config/chickens.toml).

#### Same graph in RF/s:

| Installation | RF/s per roost | 10 roosts | 20 roosts | 30 roosts |
|---|---:|---:|---:|---:|
| 1× base Redstone Flux Chicken (1/1/1) | ≈0.8 | ≈7.4 | ≈14.8 | ≈22.2 |
| 1× max-stat Redstone Flux Chicken (10/10/10) | ≈82.2 | ≈822.2 | ≈1,644.4 | ≈2,466.6 |
| 16× max-stat Redstone Flux Chickens (full roost of 10/10/10) | ≈1,315.6 | ≈13,155.6 | ≈26,311.2 | ≈39,466.6 |

	
### Custom Chicken Definitions

![Custom Chickens](https://i.imgur.com/3jxZfxf.gif)

- Modern Chickens creates `config/custom_chickens.toml`. It contains every stock `[chickens.<name>]` table and is also where new chicken definitions are added.
- `config/chickens.toml` contains only global `[general]` options. Edit either file while the game is stopped, then restart to apply changes.
- Stock tables can be enabled or disabled, reparented, retuned, or given different lay/drop items. New tables use the same shape and are loaded into the game during startup.

Older installations may still have `config/chickens_custom.json`; it remains read for migration compatibility, but new work belongs in `custom_chickens.toml`. See the [TOML example](Examples/Custom%20Chickens/custom_chickens.toml); the JSON block below is legacy syntax only:


```json
{
  "name": "SteelChicken",
  "id": 425,
  "texture": "chickens:textures/entity/steelchicken.png",
  "item_texture": "chickens:textures/item/chicken/steelchicken.png",
  "lay_item": {
    "item": "examplemod:steel_ingot"
  },
  "drop_item": {
    "item": "examplemod:steel_ingot",
    "count": 2
  },
  "background_color": "#4a4a4a",
  "foreground_color": "#b7b7b7",
  "parents": ["IronChicken", "CoalChicken"],
  "spawn_type": "hell",
  "lay_coefficient": 1.25,
  "display_name": "Steel Chicken",
  "generated_texture": false,
  "enabled": true
}
```

```json
{
  "name": "WhippedCreamChicken",
  "id": 425,
  "item_texture": "chickens:textures/item/chicken/steelchicken.png",
  "lay_item": {
    "item": "minecraft:cake"
  },
  "drop_item": {
    "item": "minecraft:cake",
    "count": 1
  },
  "background_color": "#ef30f2",
  "foreground_color": "#472447",
  "parents": ["SlimeChicken", "SnowballChicken"],
  "spawn_type": "none",
  "lay_coefficient": 0.25,
  "display_name": "Whipped Cream Chicken",
  "generated_texture": true,
  "enabled": true
}
```

> **Note:** Minecraft resource locations are lowercase by convention. The loader automatically normalises uppercase letters or
> Windows-style backslash (`\`) separators, but shipping textures in lowercase avoids surprises on dedicated servers.
>
> Omitting the `texture` field will force the chicken to default to bone chicken sprite.
>
> When `generated_texture` is enabled, the renderer tints the referenced texture so in-world chickens match the colours used for the item and spawn egg. If the texture cannot be loaded, the base bone chicken sprite is used instead, and tinted.
>
> If `item_texture` is supplied the chicken item and JEI icons prefer that sprite. When the referenced texture cannot be loaded a warning is logged and Minecraft’s missing-texture placeholder is shown until the asset becomes available; otherwise tinting stays disabled so the art is rendered exactly as authored. The runtime now bakes a vanilla `minecraft:item/generated` model around the sprite so even textures that only exist in your datapack (and therefore lack a prebuilt model) display correctly.
>
> When `generated_texture` is disabled the renderer expects the texture to exist in a resource pack or datapack. Missing assets trigger a warning and fall back to the tinted bone chicken variant so players never see the purple-and-black placeholder in game.

#### Spawn Plan Overrides

Natural spawning now reads tuning data from `ChickensSpawnManager`, and datapacks can override those values without editing configs or code. Place JSON files inside `data/<namespace>/chickens/spawn_plans/` with the following structure:

| Field | Type | Behaviour |
| --- | --- | --- |
| `spawn_type` | String (required) | One of `normal`, `snow`, or `hell`. Determines which biome bucket the override applies to. |
| `spawn_weight` | Integer | Absolute weight for the biome bucket. When omitted, the value derived from [chickens.toml](https://github.com/STRHercules/ModernChickens/blob/main/Examples/Config/chickens.toml) is used. |
| `weight_multiplier` | Float | Multiplies the config-derived weight instead of overriding it. Cannot be combined with `spawn_weight`. |
| `min_brood_size` | Integer | Overrides the minimum flock size inserted into the biome modifier. |
| `max_brood_size` | Integer | Overrides the maximum flock size. Automatically clamps to the configured minimum when smaller. |
| `spawn_charge` | Float | Replaces the mob charge applied to the biome modifier (vanilla uses this to cap spawn density). |
| `energy_budget` | Float | Overrides the spawn energy budget passed to the biome modifier alongside the mob charge. |

Example datapack snippet that boosts overworld chicken density while limiting flocks to two birds:

```json
{
  "spawn_type": "normal",
  "weight_multiplier": 1.5,
  "min_brood_size": 1,
  "max_brood_size": 2
}
```

Reloading datapacks (or restarting the server) automatically reapplies these overrides; removing the JSON restores the configuration defaults.

Natural spawning uses each biome's normal creature pool and shared creature cap. Modern Chickens are added only to biomes that already spawn vanilla chickens; vanilla chicken entries are left unchanged for compatibility with biome and creature-spawn mods. Modern birds use smaller default broods (1-2), while roosters spawn alone at low weight.

The legacy `overworldSpawnChance`, `netherSpawnChance`, and `endSpawnChance` configuration keys are retained so existing configs still load, but no longer force player-adjacent spawns.

For on-the-fly testing, `/chickens spawn multiplier <value>` multiplies every biome weight (set back to `1` to restore defaults) and `/chickens spawn debug <true|false>` toggles chat spam that reports each natural chicken spawn with its breed and coordinates.
When you need an immediate test subject, `/chickens spawn summon <chickenNameOrId>` spawns that breed at your feet and `/chickens spawn summon_random [normal|snow|end|hell]` picks a random chicken from the requested biome bucket.

#### Legacy `chickens_custom.json` Field Reference:

This table documents the backward-compatible JSON reader only. Current TOML field names and stock-table behavior are documented in [Explanation.md](Explanation.md).

| Field | Required | Type | Accepted values and behaviour |
|-------|----------|------|-------------------------------|
| `name` | Yes | String | Unique registry name for the chicken (matches the entity id fragment used elsewhere in the mod). |
| `id` | No | Integer | Positive numeric id. Omit to let the loader pick the next free id automatically. |
| `texture` | No | Resource location | Namespaced path (`namespace:path/to.png`) to the chicken texture. Paths are normalised to lowercase automatically; provide the lowercase form used inside resource packs/datapacks. When the texture is missing, a warning is logged and the renderer falls back to the tinted bone chicken template. |
| `lay_item.item` | Yes | Resource location | Namespaced item id that the chicken lays. Must exist in the item registry. |
| `lay_item.count` | No | Integer | Stack size to lay each cycle. Defaults to `1`; values below `1` are clamped up. |
| `lay_item.type` | No | Integer | Only used with the liquid egg item to select the chicken variant encoded in the stack. |
| `drop_item` | No | Object | Same shape as `lay_item`. When omitted the chicken drops its lay item when killed. |
| `background_color` | No | String/Integer | Hex string (`#RRGGBB` or `RRGGBB`) or decimal value between `0` and `16777215`. Defaults to white (`0xFFFFFF`). |
| `foreground_color` | No | String/Integer | Hex string (`#RRGGBB` or `RRGGBB`) or decimal value between `0` and `16777215`. Defaults to yellow (`0xFFFF00`). |
| `parents` | No | Array[String] | Up to two chicken names that must already exist. Leave empty or omit for Tier 1 chickens. Only the first two entries are used. |
| `spawn_type` | No | String | Case-insensitive values drawn from `normal`, `snow`, `end`, `hell`, or `none`. Defaults to `normal`. |
| `lay_coefficient` | No | Float | Multiplier applied to lay times. Values below `0` are clamped to `0`. Defaults to `1.0`. |
| `display_name` | No | String | Overrides the in-game display name. Defaults to the translated name derived from `name`. |
| `generated_texture` | No | Boolean | Set to `true` to tint the configured texture (or the base white chicken if the texture is missing) using the `background_color`/`foreground_color` pair. When `false`, the renderer uses the texture as-is and only falls back to tinting if that texture cannot be loaded. Defaults to `false`. |
| `enabled` | No | Boolean | Toggles whether the chicken participates in registries and breeding. Defaults to `true` and cascades with parent availability. |
| `item_texture` | No | Resource location | Optional namespaced path pointing at the item sprite (`namespace:textures/item/...png`). When omitted, the loader assumes a sprite lives at `chickens:textures/item/chicken/<lowercase name>.png`. Custom sprites supplied through the JSON file remain authoritative; missing resources log a warning and display Minecraft’s purple-and-black placeholder instead of swapping back to the tinted fallback. When the referenced art already ships with a baked model (for example, reusing an existing Modern Chickens texture), the runtime reuses that model directly; otherwise it now generates a vanilla `minecraft:item/generated` quad on the fly so datapack-only textures render as expected. |
| *(config only)* `allowNaturalSpawn` | No | Boolean | When `true`, higher-tier chickens are allowed to join natural spawn tables even if they have parents. It lives in the matching `[chickens.<name>]` table in `custom_chickens.toml`; defaults to `false` for breeds with parents. |
| *(config only)* `allowDousing` | No | Boolean | When `true`, the Avian Dousing Machine and its JEI recipe may create this chicken from its registered fluid or chemical. Defaults to `false`; use it only for intentional progression starters. |

## Project layout

```
ModernChickens/
├─ src/main/java               # Gameplay code and integrations
├─ src/main/resources          # Pack metadata; runtime assets merged from OriginalChickens
├─ OriginalChickens/           # Legacy assets copied during resource processing (read-only)
├─ roost/                      # Legacy Roost textures used when available (read-only)
├─ ModDevGradle-main/gradle/   # Wrapper and plugin bootstrap
└─ build.gradle                # NeoForge configuration and custom asset generation tasks
```

Only files under `ModernChickens/src` and root documentation (like this README) should be edited; the legacy projects remain read-only snapshots.

## Build Prerequisites

- Java Development Kit 25 (the build uses Gradle toolchains to target Java 25 automatically)
- Git and a 64-bit operating system capable of running Minecraft 26.1.2
- (Optional) A local installation of [JEI](https://www.curseforge.com/minecraft/mc-mods/jei) or [Jade](https://www.curseforge.com/minecraft/mc-mods/jade) for integration testing

The project uses the bundled Gradle wrapper; no global Gradle installation is required.

## Building the mod

```bash
# Clone and enter the project
git clone https://example.com/ModernChickens.git
cd ModernChickens

# Run a clean build
./gradlew clean build
```

The build copies legacy resources from `OriginalChickens`, optionally mirrors Roost textures from `roost`, and produces a distributable JAR in `build/libs/`.

> **Tip:** On first launch the build may download NeoForge dependencies; subsequent runs complete much faster. Use `./gradlew --info build` if you need detailed logging while debugging build issues.


## Support and Contributions

- File gameplay bugs or crash reports through the project issue tracker.
- Keep pull requests focused and ensure `./gradlew build` succeeds before submitting.

*Modern Chickens inherits the MIT license from the original project. See `LICENSE` (when present) or the mod metadata for details.*

Happy hatching!
