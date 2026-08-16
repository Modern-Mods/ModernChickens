package strhercules.chickens.datagen.providers;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import strhercules.chickens.ChickensMod;

public class ChickensItemModelProvider extends ItemModelProvider {

    public ChickensItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ChickensMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // BlockItems
        blockItem("roost");
        blockItem("breeder");
        blockItem("collector");
        blockItem("avian_flux_converter");
        blockItem("avian_fluid_converter");
        blockItem("avian_chemical_converter");
        blockItem("avian_dousing_machine");
        blockItem("incubator");
        blockItem("mechanical_roost");
        blockItem("roost_generator");
        blockItem("mechanical_nest");
        blockItem("henhouse");
        blockItem("henhouse_spruce");
        blockItem("henhouse_birch");
        blockItem("henhouse_jungle");
        blockItem("henhouse_acacia");
        blockItem("henhouse_dark_oak");

        nest();

        // Simply Items
        generatedItem("creative_catcher", "creative_catcher");
        generatedItem("catcher", "catcher");
        generatedItem("flying_egg", "flyingegg");
        generatedItem("lava_chicken", "lava_chicken");
        generatedItem("speedupgrade", "speedupgrade");
        generatedItem("stackupgrade", "stackupgrade");
        generatedItem("storagecapacity", "storagecapacity");
        generatedItem("rangeupgrade", "rangeupgrade");
        generatedItem("rfupgrade", "rfupgrade");
        generatedItem("rf_capacity_upgrade", "rfupgrade");
        generatedItem("rf_efficiency_upgrade", "efficiency_upgrade");
        generatedItem("rf_output_upgrade", "output_upgrade");
        generatedItem("rf_exciter_upgrade", "exciter_upgrade");
        generatedItem("rf_stabilizer_upgrade", "stabilizer_upgrade");
        generatedItem("rf_surge_upgrade", "surge_upgrade");
        generatedItem("rf_governor_upgrade", "governer_upgrade");
        generatedItem("configurator", "configurator");
        generatedItem("gas_egg", "gas_egg");
        generatedItem("chemical_egg", "chemical_egg");
        generatedItem("mega_chicken", "mega_chicken");
        generatedItem("analyzer", "analyzer");
        generatedItem("colored_egg", "colored_egg");
        generatedItem("liquid_egg", "liquid_egg");
        generatedItem("robot_chicken", "chicken/robot_chicken");
        generatedItem("robot_rooster", "robot_rooster");

        // flux_egg
        fluxEgg();

        // Spawn eggs
        spawnEgg();
        withExistingParent("rooster_spawn_egg", mcLoc("item/template_spawn_egg"));
        withExistingParent("robot_chicken_spawn_egg", mcLoc("item/template_spawn_egg"));
        withExistingParent("robot_rooster_spawn_egg", mcLoc("item/template_spawn_egg"));
        megaChickenSpawnEgg();
        for (strhercules.chickens.entity.MegaChickenSkin skin : strhercules.chickens.entity.MegaChickenSkin.values()) {
            generatedSkinItem(skin.id() + "_skin_crate");
        }
    }

   
    private void blockItem(String name) {
        withExistingParent(name, modLoc("block/" + name));
    }

   
    private void generatedItem(String name, String texturePath) {
        ItemModelBuilder builder = withExistingParent(name, mcLoc("item/generated"));
        builder.texture("layer0", modLoc("item/" + texturePath));
    }

    private void generatedSkinItem(String name) {
        ItemModelBuilder builder = withExistingParent(name, mcLoc("item/generated"));
        builder.texture("layer0", modLoc("item/crate"));
    }

    private void fluxEgg() {
        ItemModelBuilder builder = withExistingParent("flux_egg", mcLoc("item/egg"));
        builder.texture("layer0", modLoc("item/flux_egg"));
    }

    private void spawnEgg() {
        ItemModelBuilder builder = withExistingParent("spawn_egg", mcLoc("item/template_spawn_egg"));
        builder.texture("layer0", modLoc("item/spawn_egg"));
        builder.texture("layer1", modLoc("item/spawn_egg_overlay"));
    }

   
    private void megaChickenSpawnEgg() {
        withExistingParent("mega_chicken_spawn_egg", mcLoc("item/template_spawn_egg"));
    }

    private void nest() {
        ItemModelBuilder builder = withExistingParent("nest", modLoc("block/nest"));
        builder
            .transforms()
                .transform(ItemDisplayContext.GUI)
                    .rotation(30.0f, 45.0f, 0.0f)
                    .scale(0.625f, 0.625f, 0.625f)
                    .end()
                .end();
    }

}
