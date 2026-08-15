## Balance
- None at the moment

## Feature Suggestions
### Flux Roost / Mechanical Generator Roost
The Mechanical Generator Roost would be a block where instead of putting flux eggs into, you can directly put Redstone Flux chickens into to make RF. The problem with Redstone Flux Chickens as of currently is that it produces RF *per operation*, and not per tick, meaning you're technically not generating that much RF, for example, the current Redstone Flux Chicken (at max 10/10/10 and 16 chickens in the slot) produces 64 1K flux eggs per operation, each operation is around 5-6 seconds, meaning for each operation, you are making 64K FE per operation, but when that is translated to RF/t, you're only really producing around 533.33FE per tick (6 x 20 ticks/sec = 120 ticks, 64,000 FE / 120 ticks = 533.33 FE/t)

I propose the Flux Roost / Mechanical Generator Roost. When you put Redstone Flux Chickens into the machine, it produces FE/t instead of the flux eggs, converting them directly into power. Just like a normal roost, you can put 16 redstone flux chickens in one slot. For the formula, here's what I was thinking: FE/t per chicken = 50 x average(Growth, Gain, and Strength) meaning for a 1/1/1 chicken it would produce 50 FE/t but for 16 chickens it would produce 800 FE/t, a 5/5/5 chicken would produce 250 FE/t (50 x 5) and 16 of those 5/5/5 chickens would produce 4,000 FE/t, and then a 10/10/10 chicken would produce 500 FE/t (50 x 10) making 8,000 FE/t. Of course, you can change this how you see fit, the formula is not rigid at all.

This would not be able to be boosted by the Mechanical Nest / Nest.

The Jade would look like this:
- FE Stored
- FE Generation (in total) per tick
- Max Output

The base stats would be:
Base Capacity: 1,000,000 FE
Base Output: 4,000 FE/t (which scales to 50% of its generation rate, so if you're generating like 12,000 FE/t, it would naturally scale to 6,000 FE/t)

It would also have special (or take existing) upgrades such as:
- RF Capacity Upgrade - Increases the amount FE that the block can store
- RF Efficiency Upgrade - Increases the amount of FE the chickens can make by 10% (to a total of 40%)
- RF Output Upgrade - Increases the percentage of the machine's current FE generation that can be output by 12.5% (up to 100%).

Special Upgrades:
- RF Exciter Upgrade - Greatly increases the amount of FE the chickens can make by 25% but makes their generation unstable. Every 5 seconds, the generation multiplier changes randomly between -20% and +20% (for a maximum of 100% and fluctuating between -80% and +80%)
- RF Stabilizer Upgrade - Reduces the generation fluctuation caused by RF Exciter Upgrades by 10% (up to 40%)
- RF Surge Upgrade - When the internal buffer is above 75% capacity, increases maximum FE output by 15% (up to 60%)
- RF Governor Upgrade - Limits the maximum positive Exciter fluctuation by 10% per upgrade, but converts a portion of the removed fluctuation into guaranteed generation. (to a maximum of 40%)
- RF Intelligence Upgrade - Teaches the Redstone Chickens to work smarter, not harder. The first Intelligence Upgrades creates a new slot for a "Robot Smart Chicken" (created the same way a Robot Rooster is, by breeding a robot chicken and a smart chicken). Each upgrade after increases the amount of Robot Smart chickens you can add and that Growth, Gain and Strength have on FE generation, with higher-stat chickens receiving a significantly larger benefit. This will also introduce a new chicken type, Self-Sterile (yes, I know, this a phrase for botany but just give this one to me LOL) chickens, As a result, its Growth, Gain, and Strength cannot be increased through normal same-species breeding. If this is too similiar to Productive Bees, you can make it them be able to breed with themselves and have the Evolutionary Stasis trait, they can breed with themselves HOWEVER they cannot gain further stats from breeding, so if you breed two 1/1/1 chickens, the next chicken will always be 1/1/1, but if you breed 10/10/10 chickens, you will always get a 10/10/10 chicken. If two parents are different stats, the chicken will inherit stats as normal but it cannot exceed the highest corresponding stat of the parent. If you wish to replicate the 10/10/10 stats onto this chicken, you must use the DNA splicer. (whenever that gets added) 
The formula changed by the RF Intelligence chicken is like this, lets assume:

A = average Growth/Gain/Strength of the Redstone Flux Chicken

S = average Growth/Gain/Strength of the Robot Smart Chicken

I = number of RF Intelligence Upgrades installed, from 1–8

$$\text{FE/t per chicken} = 50 \times A \times \left[1 + \left(\frac{I}{8}\right) \times \left(\frac{A}{10}\right) \times \left(\frac{S}{10}\right)\right]$$

Or in simple terms: `FE/t per chicken = 50 × A × [1 + (I / 8) × (A / 10) × (S / 10)]`

Unlike most other machines, the Flux Roost would only have two upgrade slots. However, both slots are unrestricted, so the player can use any combination of compatible upgrades, **including two of the same upgrade.** Each slot can hold 4 of any upgrade.

This can make for some cool combos, for example
- 4x Exciter + 4x Output: You would essentially have chickens (10/10/10 and 16 in the slot) that has the base generation of 16,000 FE/t but fluctates between 3,200 FE/t and 28,800 FE/t. You would also have an output of 28,800 FE/t.
- 4x Efficiency + 4x Output: You would have chickens (10/10/10 and 16 in the slot) that produce 11,200 FE/t and can output 11,200 FE/t.
- 8x Exciter (both slots filled with Exciters): You would have chickens (10/10/10 and 16 in the slot) with the base rate of 24,000 FE/t that can fluctuate between -14,400 FE/t (taking power out of the buffer cause of -160% fluctuation rate) and 62,400 FE/t (again, +160% fluctuation rate) but only being able to output half of that fluctuation power, so you'd only be able to output either 0 FE/t or 31,200 FE/t, but still has a somewhat higher ceiling than 4x Exciter and 4x Output.
- 4x Exciter + 4x Stabilizer: 16,000 FE/t (assuming 10/10/10 and 16 in the slot) and only +40% fluctation instead of -80%, so you could get 9,600-22,400 FE/t but only able to output half of that power.

and so on and so forth...

### Quantum Chicken
#### Obtainment Method
1. Obtain an Ender Pearl Chicken (you could also rename this to an Ender Chicken or an End Chicken)
Currently, the Ender Pearl Chicken has no recipe, so I will propose one, take a normal chicken and bring it to the end, then feed it with:
- Ender Pearls
- Chorus Fruits
- Chorus Flowers

The Ender Pearl chicken can be special too and be an annoying little shit, it can randomly teleport like a normal Enderman.
2. Obtain the Quantum Seed
First, you have to craft the quantum seed. I suggest things like nether stars and all that jazz and snazz, whatever you think might fit the Quantum Seed. When you craft the quantum seed, it actually has a RF battery, and it starts off uncharged, you need to charge it fully to 1/10/100M RF (you can choose how much power depending on how end game you want it to be)

3. Contain the chicken in a small enclosure multiblock
The Ender Chicken needs to be held within a small enclosure multiblock for this process. This multiblock disables the teleportation of the Ender Chicken and once the Ender Chicken is inside, the process can start, I would kind of make it thematic with like Supercritical Phase Shifter (SPS from Mekanism), of course, it doesn't have to be as fancy and as like visually appealing but something that matches the theme of what you're about to do

4. Put the quantum seed into the multiblock GUI and feed it power.
The chicken begins to transform, it probably would be like a transformation that takes 200,000 RF/t for 30 seconds (you can also set this much higher, though all in all, it will use 120,000,000MFE in total, you can also have it use 1,000,000 RF/t if you feel like you need it to be crazy.) If you lose any power during this or aren't able to supply enough, the machine can do one, two, three or all of these things (its up to you):
A. Blow up
B. Kill the chicken
C. Power off
D. Fail the experiment

5. At the end, the machine finishes the stuff and now you have a quantum chicken.
Boom! Now you have a Quantum Chicken, you need to make two and once you do, you can either give them:
A. Self-Sterile (unable to breed with itself)
or
B. Evolution Stasis (unable to obtain stats higher than the parents it was bred with)
#### Use Cases
Note: The Quantum Chicken cannot be put in a roost, instead, it has to be out at all times. 
##### Entangled Eggs
If you feed the Quantum Chicken Quantum Seeds, it will produce two eggs Entangled Egg α and Entangled Egg β. Those two eggs have a unique "quantum-link ID" meaning no other eggs can join that pair. You must make a Quantum Nest, and once you make a Quantum Nest, any RF you feed into Nest A will have that energy extracted and transported to Nest B, you can also have it vice versa if you want to for some reason. Entangled Eggs are different in which, only Strength determines the "quality" of the Entangled Eggs
1-2: Scrambled Quantum Eggs (can only move 1,000 FE/t), has to be in the same chunk, has to be in the same dimension
3-4: Stable Quantum Eggs (can only move 10,000 FE/t), has a 3x3 chunk radius, has to be in the same dimension
5-6: Entangled Quantum Eggs (can only move 100,000 FE/t), has to be in a 5x5 chunk radius, has to be in the same dimension
7-8: Superposed Quantum Eggs (can only move 1,000,000 FE/t), has to be in a 9x9 chunk radius, has to be in the same dimension
9-10: Singularity Quantum Eggs (can move an unlimited amount of FE) has an unlimited radius, can be in any dimension
##### Feather Time
This is Feather Time *(you see what i did there? father time? get it?)* To get a Feather Time, you must feed a Quantum Chicken clocks, when you make it, it has BOTH Self-Sterile AND Evolution Stasis. (Elaboration later, I'm really sick and not feeling good but it basically can speed up (tick accelerate) blocks in its area when put into a special machine.
