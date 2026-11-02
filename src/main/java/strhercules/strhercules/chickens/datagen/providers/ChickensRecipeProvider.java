package strhercules.chickens.datagen.providers;

import strhercules.chickens.recipe.DousingRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.common.conditions.ICondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChickensRecipeProvider extends RecipeProvider {

    private static final String MODID = "chickens";

    public ChickensRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        coloredEgg("white_chicken", Items.WHITE_DYE, 0);
        coloredEgg("yellow_chicken", Items.YELLOW_DYE, 4);
        coloredEgg("blue_chicken", Items.BLUE_DYE, 11);
        coloredEgg("green_chicken", Items.GREEN_DYE, 13);
        coloredEgg("red_chicken", Items.RED_DYE, 14);
        coloredEgg("black_chicken", Items.BLACK_DYE, 15);

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, item("chickens:flying_egg"))
                .requires(Items.EGG)
                .requires(Items.ELYTRA)
                .unlockedBy("has_elytra", has(Items.ELYTRA))
                .save(output, id("flying_egg"));

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, item("chickens:analyzer"))
                .requires(Items.COMPASS)
                .requires(Items.EGG)
                .unlockedBy("has_compass", has(Items.COMPASS))
                .save(output, id("analyzer"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:configurator"))
                .pattern("II")
                .pattern(" R")
                .pattern(" S")
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .define('S', Items.STICK)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, id("configurator"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:catcher"))
                .pattern("E")
                .pattern("S")
                .pattern("F")
                .define('E', Items.EGG)
                .define('S', Items.STICK)
                .define('F', Items.FEATHER)
                .unlockedBy("has_egg", has(Items.EGG))
                .save(output, id("catcher"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:roost"))
                .pattern("PPP")
                .pattern("PHP")
                .pattern("PPP")
                .define('P', ItemTags.PLANKS)
                .define('H', Items.HAY_BLOCK)
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(output, id("roost"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:breeder"))
                .pattern("PPP")
                .pattern("PSP")
                .pattern("HHH")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.WHEAT_SEEDS)
                .define('H', Items.HAY_BLOCK)
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(output, id("breeder"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:nest"))
                .pattern("SPS")
                .pattern("RHR")
                .pattern("PPP")
                .define('S', Items.WHEAT_SEEDS)
                .define('P', ItemTags.PLANKS)
                .define('R', item("chickens:roost"))
                .define('H', Items.HAY_BLOCK)
                .unlockedBy("has_roost", has(item("chickens:roost")))
                .save(output, id("nest"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:collector"))
                .pattern("PRP")
                .pattern("PHP")
                .pattern("PCP")
                .define('P', ItemTags.PLANKS)
                .define('R', item("chickens:roost"))
                .define('H', Items.HOPPER)
                .define('C', Items.CHEST)
                .unlockedBy("has_roost", has(item("chickens:roost")))
                .save(output, id("collector"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:incubator"))
                .pattern("IFI")
                .pattern("GBG")
                .pattern("IRI")
                .define('I', Items.IRON_INGOT)
                .define('F', item("chickens:flux_egg"))
                .define('G', Items.GLASS)
                .define('B', item("chickens:breeder"))
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_breeder", has(item("chickens:breeder")))
                .save(output, id("incubator"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:mechanical_roost"))
                .pattern("IFI")
                .pattern("FRF")
                .pattern("IFI")
                .define('I', Items.IRON_INGOT)
                .define('F', item("chickens:flux_egg"))
                .define('R', item("chickens:roost"))
                .unlockedBy("has_roost", has(item("chickens:roost")))
                .save(output, id("mechanical_roost"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:roost_generator"))
                .pattern("IFI")
                .pattern("RMR")
                .pattern("IFI")
                .define('I', Items.IRON_INGOT)
                .define('F', item("chickens:flux_egg"))
                .define('R', Items.REDSTONE_BLOCK)
                .define('M', item("chickens:mechanical_roost"))
                .unlockedBy("has_mechanical_roost", has(item("chickens:mechanical_roost")))
                .save(output, id("roost_generator"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:mechanical_nest"))
                .pattern("IFI")
                .pattern("FRF")
                .pattern("IFI")
                .define('I', Items.IRON_INGOT)
                .define('F', item("chickens:flux_egg"))
                .define('R', item("chickens:nest"))
                .unlockedBy("has_nest", has(item("chickens:nest")))
                .save(output, id("mechanical_nest"));

        henhouse("henhouse", Items.OAK_PLANKS);
        henhouse("henhouse_acacia", Items.ACACIA_PLANKS);
        henhouse("henhouse_birch", Items.BIRCH_PLANKS);
        henhouse("henhouse_dark_oak", Items.DARK_OAK_PLANKS);
        henhouse("henhouse_jungle", Items.JUNGLE_PLANKS);
        henhouse("henhouse_spruce", Items.SPRUCE_PLANKS);
        henhouse("henhouse_mangrove", Items.MANGROVE_PLANKS);
        henhouse("henhouse_cherry", Items.CHERRY_PLANKS);
        henhouse("henhouse_pale_oak", Items.PALE_OAK_PLANKS);
        henhouse("henhouse_bamboo", Items.BAMBOO_PLANKS);
        henhouse("henhouse_crimson", Items.CRIMSON_PLANKS);
        henhouse("henhouse_warped", Items.WARPED_PLANKS);

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:avian_fluid_converter"))
                .pattern("GWG")
                .pattern("BCB")
                .pattern("ILI")
                .define('G', Items.GLASS)
                .define('W', Items.WATER_BUCKET)
                .define('B', Items.BUCKET)
                .define('C', item("chickens:collector"))
                .define('I', Items.IRON_BLOCK)
                .define('L', Items.LAVA_BUCKET)
                .unlockedBy("has_collector", has(item("chickens:collector")))
                .save(output, id("avian_fluid_converter"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:avian_chemical_converter"))
                .pattern("ITI")
                .pattern("BCB")
                .pattern("ITI")
                .define('I', Items.IRON_BLOCK)
                .define('T', item("mekanism:basic_chemical_tank"))
                .define('B', Items.BUCKET)
                .define('C', item("chickens:collector"))
                .unlockedBy("has_collector", has(item("chickens:collector")))
                .save(output, id("avian_chemical_converter"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:avian_flux_converter"))
                .pattern("RGR")
                .pattern("GCG")
                .pattern("RIR")
                .define('R', Items.REDSTONE)
                .define('G', Items.GLOWSTONE_DUST)
                .define('C', item("chickens:collector"))
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("has_collector", has(item("chickens:collector")))
                .save(output, id("avian_flux_converter"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:avian_dousing_machine"))
                .pattern("FBF")
                .pattern("RCR")
                .pattern("IGI")
                .define('F', item("chickens:avian_fluid_converter"))
                .define('B', Items.BUCKET)
                .define('R', Items.REDSTONE_BLOCK)
                .define('C', item("chickens:collector"))
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_collector", has(item("chickens:collector")))
                .save(output, id("avian_dousing_machine"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:speedupgrade"))
                .pattern("NEN")
                .pattern("EDE")
                .pattern("NEN")
                .define('N', Items.NETHERITE_INGOT)
                .define('E', Items.ENDER_EYE)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT))
                .save(output, id("speedupgrade"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:stackupgrade"))
                .pattern("NSN")
                .pattern("SDS")
                .pattern("NSN")
                .define('N', Items.NETHERITE_INGOT)
                .define('S', Items.SHULKER_SHELL)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_shulker_shell", has(Items.SHULKER_SHELL))
                .save(output, id("stackupgrade"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:storagecapacity"))
                .pattern("NEN")
                .pattern("ECE")
                .pattern("NEN")
                .define('N', Items.NETHERITE_INGOT)
                .define('E', Items.ENDER_PEARL)
                .define('C', Items.CHEST)
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT))
                .save(output, id("storagecapacity"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:rangeupgrade"))
                .pattern("ENE")
                .pattern("NBN")
                .pattern("ENE")
                .define('E', Items.ENDER_EYE)
                .define('N', Items.NETHERITE_INGOT)
                .define('B', Items.BEACON)
                .unlockedBy("has_beacon", has(Items.BEACON))
                .save(output, id("rangeupgrade"));

        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:rfupgrade"))
                .pattern("NEN")
                .pattern("RDR")
                .pattern("NEN")
                .define('N', Items.NETHERITE_INGOT)
                .define('E', Items.ENDER_EYE)
                .define('R', Items.REDSTONE_BLOCK)
                .define('D', Items.DIAMOND)
                .unlockedBy("has_netherite", has(Items.NETHERITE_INGOT))
                .save(output, id("rfupgrade"));

        rfGeneratorUpgrade("rf_capacity_upgrade", item("chickens:rf_capacity_upgrade"),
                Items.IRON_BLOCK);
        rfGeneratorUpgrade("rf_efficiency_upgrade", item("chickens:rf_efficiency_upgrade"),
                Items.GLOWSTONE_DUST);
        rfGeneratorUpgrade("rf_output_upgrade", item("chickens:rf_output_upgrade"),
                Items.REDSTONE_BLOCK);
        rfGeneratorUpgrade("rf_exciter_upgrade", item("chickens:rf_exciter_upgrade"),
                Items.BLAZE_ROD);
        rfGeneratorUpgrade("rf_stabilizer_upgrade", item("chickens:rf_stabilizer_upgrade"),
                Items.QUARTZ_BLOCK);
        rfGeneratorUpgrade("rf_surge_upgrade", item("chickens:rf_surge_upgrade"),
                Items.GOLD_BLOCK);
        rfGeneratorUpgrade("rf_governor_upgrade", item("chickens:rf_governor_upgrade"),
                Items.AMETHYST_BLOCK);

        dousingRecipe("avian_dousing_dragon", "obsidianChicken", "dragonChicken",
                "minecraft:dragon_breath", 10);
        dousingRecipe("avian_dousing_wither", "soulSandChicken", "witherChicken",
                "minecraft:nether_star", 10);
    }

    private void henhouse(String recipeName, Item planks) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, item("chickens:" + recipeName))
                .pattern("WWW")
                .pattern("WHW")
                .pattern("YYY")
                .define('W', planks)
                .define('H', Items.HOPPER)
                .define('Y', Items.HAY_BLOCK)
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(output, id(recipeName));
    }

    private void rfGeneratorUpgrade(String recipeName, Item result, Item component) {
        ShapedRecipeBuilder.shaped(items, RecipeCategory.MISC, result)
                .pattern("NIN")
                .pattern("ICI")
                .pattern("NIN")
                .define('N', Items.NETHERITE_INGOT)
                .define('I', component)
                .define('C', item("chickens:rfupgrade"))
                .unlockedBy("has_rfupgrade", has(item("chickens:rfupgrade")))
                .save(output, id(recipeName));
    }

    private void dousingRecipe(String recipeName, String inputChicken,
            String resultChicken, String reagentId, int amount) {
        output.accept(id(recipeName), new DousingRecipe(
                inputChicken,
                resultChicken,
                new DousingRecipe.Reagent(DousingRecipe.ReagentType.ITEM, Identifier.parse(reagentId), amount),
                10_000), null);
    }

    private void coloredEgg(String recipeName, Item dye, int chickenType) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("ChickenType", chickenType);
        DataComponentPatch components = DataComponentPatch.builder()
                .set(DataComponents.CUSTOM_DATA, CustomData.of(tag))
                .set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of((float) chickenType), List.of(), List.of(), List.of()))
                .build();

        ShapelessRecipeBuilder.shapeless(items, RecipeCategory.MISC, new ItemStackTemplate(item("chickens:colored_egg"), components))
                .requires(Items.EGG)
                .requires(dye)
                .unlockedBy("has_egg", has(Items.EGG))
                .save(output, id(recipeName));
    }

    private static Item item(String id) {
        Identifier location = Identifier.parse(id);
        Item result = BuiltInRegistries.ITEM.getValue(location);
        if (result == Items.AIR && !location.equals(Identifier.withDefaultNamespace("air"))) {
            throw new IllegalStateException("Item not registered at datagen time: " + id);
        }
        return result;
    }

    private static ResourceKey<Recipe<?>> id(String path) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(MODID, path));
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ChickensRecipeProvider(registries, new RecipeOutput() {
                @Override
                public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancement, ICondition... conditions) {
                    output.accept(id, recipe, null, conditions);
                }

                @Override
                public Advancement.Builder advancement() {
                    return output.advancement();
                }

                @Override
                public void includeRootAdvancement() {
                    output.includeRootAdvancement();
                }
            });
        }

        @Override
        public String getName() {
            return "Chickens Recipes";
        }
    }
}
