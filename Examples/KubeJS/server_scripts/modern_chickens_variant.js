// Modern Chickens - exact egg / chicken ingredients.
// Copy to: kubejs/server_scripts/
//
// Every liquid egg, chemical egg, gas egg, chicken and spawn egg is the same
// item, so a plain item string cannot name one exact variant. The
// `chickens:variant` ingredient picks one by its readable name instead.
//
// KubeJS builder (works anywhere KubeJS expects an ingredient):
//   variant.kind('liquid_egg').chickenid('minecraft:water')
// `ChickenVariant` is an identical alias of `variant`.
//
// kind (mandatory)      liquid_egg | chemical_egg | gas_egg | chicken | spawn_egg
// chickenid (mandatory) fluid id for liquid_egg, chemical id for chemical_egg and
//                       gas_egg, chicken registry name (IronChicken, ...) for
//                       chicken and spawn_egg
// chicken only (optional) .minGrowth(n) .minGain(n) .minStrength(n)
//
// Leaving out kind or chickenid throws an error with the missing field named.

ServerEvents.recipes(event => {

  // ==============================================
  // SHAPELESS: water liquid egg + a strong chicken
  // ==============================================
  event.shapeless('minecraft:diamond', [
    variant.kind('liquid_egg').chickenid('minecraft:water'),
    variant.kind('chicken').chickenid('IronChicken').minGrowth(5).minGain(3),
    'minecraft:coal'
  ]).id('example:variant_shapeless')

  // ==============================================
  // SHAPED: water liquid eggs around a chicken
  // ==============================================
  event.shaped('minecraft:sponge', [
    'EEE',
    'ECE',
    'EEE'
  ], {
    E: variant.kind('liquid_egg').chickenid('minecraft:water'),
    C: variant.kind('chicken').chickenid('IronChicken').minStrength(2)
  }).id('example:variant_shaped')

  // ==============================================
  // PLAIN JSON FORM
  // ==============================================
  // The same ingredient written as raw JSON. `chickenid` is the field name;
  // the older `id` is still read as an alias so existing datapacks keep working.
  event.shapeless('minecraft:iron_ingot', [
    { type: 'chickens:variant', kind: 'liquid_egg', chickenid: 'minecraft:water' },
    { type: 'chickens:variant', kind: 'chicken', chickenid: 'IronChicken', min_growth: 5, min_gain: 3, min_strength: 1 }
  ]).id('example:variant_json')

})
