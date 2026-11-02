package strhercules.chickens.registry;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.block.AvianChemicalConverterBlock;
import strhercules.chickens.block.AvianFluxConverterBlock;
import strhercules.chickens.block.AvianDousingMachineBlock;
import strhercules.chickens.block.AvianFluidConverterBlock;
import strhercules.chickens.block.BreederBlock;
import strhercules.chickens.block.CollectorBlock;
import strhercules.chickens.block.IncubatorBlock;
import strhercules.chickens.block.MechanicalRoostBlock;
import strhercules.chickens.block.RoostGeneratorBlock;
import strhercules.chickens.block.MechanicalNestBlock;
import strhercules.chickens.block.HenhouseBlock;
import strhercules.chickens.block.LavaChickenFireBlock;
import strhercules.chickens.block.RoostBlock;
import strhercules.chickens.block.NestBlock;
import strhercules.chickens.item.AnalyzerItem;
import strhercules.chickens.item.ChickensSpawnEggItem;
import strhercules.chickens.item.ColoredEggItem;
import strhercules.chickens.item.FluxEggItem;
import strhercules.chickens.item.FlyingEggItem;
import strhercules.chickens.item.ChickenItem;
import strhercules.chickens.item.ChickenCatcherItem;
import strhercules.chickens.item.CreativeCatcherItem;
import strhercules.chickens.item.LiquidEggItem;
import strhercules.chickens.item.LavaChickenItem;
import strhercules.chickens.item.ChemicalEggItem;
import strhercules.chickens.item.GasEggItem;
import strhercules.chickens.item.MegaChickenItem;
import strhercules.chickens.item.MegaChickenSkinCrateItem;
import strhercules.chickens.item.MachineConfiguratorItem;
import strhercules.chickens.entity.MegaChickenSkin;
import strhercules.chickens.item.UpgradeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Unit;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.item.SpawnEggItem;
import strhercules.chickens.item.TooltipBlockItem;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredRegister.Blocks;
import net.neoforged.neoforge.registries.DeferredRegister.Items;
import net.neoforged.bus.api.IEventBus;

import java.util.Collections;
import java.util.List;

public final class ModRegistry {
    public static final Items ITEMS = DeferredRegister.createItems(ChickensMod.MOD_ID);
    public static final Blocks BLOCKS = DeferredRegister.createBlocks(ChickensMod.MOD_ID);

    private ModRegistry() {
    }

    public static void init(IEventBus modBus) {
        ModEffects.init(modBus);
        ITEMS.register(modBus);
        BLOCKS.register(modBus);
        ModEntityTypes.init(modBus);
        ModBlockEntities.init(modBus);
        ModMenuTypes.init(modBus);
        ModRecipeTypes.init(modBus);
        ModSpawns.init(modBus);
        ModBiomeModifiers.init(modBus);
        ModCapabilities.init(modBus);
        ModCreativeTabs.init(modBus);
    }

    public static final DeferredItem<ChickensSpawnEggItem> SPAWN_EGG = ITEMS.registerItem("spawn_egg",
            ChickensSpawnEggItem::new,
            props -> props.stacksTo(64).spawnEgg(ModEntityTypes.CHICKENS_CHICKEN.get()));
    public static final DeferredItem<SpawnEggItem> ROOSTER_SPAWN_EGG = ITEMS.registerItem("rooster_spawn_egg",
            SpawnEggItem::new,
            props -> props.stacksTo(64).spawnEgg(ModEntityTypes.ROOSTER.get()));
    public static final DeferredItem<SpawnEggItem> MEGA_CHICKEN_SPAWN_EGG = ITEMS.registerItem("mega_chicken_spawn_egg",
            SpawnEggItem::new,
            props -> props.stacksTo(64).spawnEgg(ModEntityTypes.MEGA_CHICKEN.get()));
    public static final DeferredItem<SpawnEggItem> ROBOT_CHICKEN_SPAWN_EGG = ITEMS.registerItem("robot_chicken_spawn_egg",
            SpawnEggItem::new,
            ModRegistry::robotChickenSpawnEggProperties);
    public static final DeferredItem<SpawnEggItem> ROBOT_ROOSTER_SPAWN_EGG = ITEMS.registerItem("robot_rooster_spawn_egg",
            SpawnEggItem::new,
            ModRegistry::robotRoosterSpawnEggProperties);
    public static final DeferredItem<ColoredEggItem> COLORED_EGG = ITEMS.registerItem("colored_egg", ColoredEggItem::new, props -> props.stacksTo(64));
    public static final DeferredItem<LiquidEggItem> LIQUID_EGG = ITEMS.registerItem("liquid_egg", LiquidEggItem::new, props -> props.stacksTo(64));
    public static final DeferredItem<ChemicalEggItem> CHEMICAL_EGG = ITEMS.registerItem("chemical_egg", ChemicalEggItem::new, props -> props.stacksTo(64));
    public static final DeferredItem<GasEggItem> GAS_EGG = ITEMS.registerItem("gas_egg", GasEggItem::new, props -> props.stacksTo(64));
    public static final DeferredItem<FluxEggItem> FLUX_EGG = ITEMS.registerItem("flux_egg", FluxEggItem::new, props -> props.stacksTo(64));
    public static final DeferredItem<FlyingEggItem> FLYING_EGG = ITEMS.registerItem("flying_egg", FlyingEggItem::new, props -> props.stacksTo(1));
    public static final DeferredItem<LavaChickenItem> LAVA_CHICKEN = ITEMS.registerItem("lava_chicken", LavaChickenItem::new,
            props -> props.stacksTo(16).fireResistant());
    public static final DeferredItem<UpgradeItem> SPEED_UPGRADE = ITEMS.registerItem("speedupgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.SPEED), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> STACK_UPGRADE = ITEMS.registerItem("stackupgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.STACK), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> STORAGE_CAPACITY_UPGRADE = ITEMS.registerItem("storagecapacity", props -> new UpgradeItem(props, UpgradeItem.Kind.STORAGE), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RANGE_UPGRADE = ITEMS.registerItem("rangeupgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RANGE), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_UPGRADE = ITEMS.registerItem("rfupgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_CAPACITY_UPGRADE = ITEMS.registerItem("rf_capacity_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_CAPACITY, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_EFFICIENCY_UPGRADE = ITEMS.registerItem("rf_efficiency_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_EFFICIENCY, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_OUTPUT_UPGRADE = ITEMS.registerItem("rf_output_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_OUTPUT, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_EXCITER_UPGRADE = ITEMS.registerItem("rf_exciter_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_EXCITER, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_STABILIZER_UPGRADE = ITEMS.registerItem("rf_stabilizer_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_STABILIZER, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_SURGE_UPGRADE = ITEMS.registerItem("rf_surge_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_SURGE, false), props -> props.stacksTo(64));
    public static final DeferredItem<UpgradeItem> RF_GOVERNOR_UPGRADE = ITEMS.registerItem("rf_governor_upgrade", props -> new UpgradeItem(props, UpgradeItem.Kind.RF_GOVERNOR, false), props -> props.stacksTo(64));
    public static final DeferredItem<MachineConfiguratorItem> CONFIGURATOR = ITEMS.registerItem("configurator", MachineConfiguratorItem::new);
    public static final DeferredItem<AnalyzerItem> ANALYZER = ITEMS.registerItem("analyzer", AnalyzerItem::new, props -> props.durability(238));
    public static final DeferredBlock<RoostBlock> ROOST = BLOCKS.registerBlock("roost", RoostBlock::new, RoostBlock::defaultProperties);
    public static final DeferredBlock<NestBlock> NEST = BLOCKS.registerBlock("nest", NestBlock::new, NestBlock::defaultProperties);
    public static final DeferredBlock<BreederBlock> BREEDER = BLOCKS.registerBlock("breeder", BreederBlock::new, BreederBlock::defaultProperties);
    public static final DeferredBlock<CollectorBlock> COLLECTOR = BLOCKS.registerBlock("collector", CollectorBlock::new, CollectorBlock::defaultProperties);
    public static final DeferredBlock<LavaChickenFireBlock> LAVA_CHICKEN_FIRE = BLOCKS.registerBlock("lava_chicken_fire", LavaChickenFireBlock::new, LavaChickenFireBlock::defaultProperties);
    public static final DeferredBlock<AvianFluxConverterBlock> AVIAN_FLUX_CONVERTER = BLOCKS.registerBlock("avian_flux_converter", AvianFluxConverterBlock::new, AvianFluxConverterBlock::defaultProperties);
    public static final DeferredBlock<AvianFluidConverterBlock> AVIAN_FLUID_CONVERTER = BLOCKS.registerBlock("avian_fluid_converter", AvianFluidConverterBlock::new, AvianFluidConverterBlock::defaultProperties);
    public static final DeferredBlock<AvianChemicalConverterBlock> AVIAN_CHEMICAL_CONVERTER = BLOCKS.registerBlock("avian_chemical_converter", AvianChemicalConverterBlock::new, AvianChemicalConverterBlock::defaultProperties);
    public static final DeferredBlock<AvianDousingMachineBlock> AVIAN_DOUSING_MACHINE = BLOCKS.registerBlock("avian_dousing_machine", AvianDousingMachineBlock::new, AvianDousingMachineBlock::defaultProperties);
    public static final DeferredBlock<IncubatorBlock> INCUBATOR = BLOCKS.registerBlock("incubator", IncubatorBlock::new, IncubatorBlock::defaultProperties);
    public static final DeferredBlock<MechanicalRoostBlock> MECHANICAL_ROOST = BLOCKS.registerBlock("mechanical_roost", MechanicalRoostBlock::new, MechanicalRoostBlock::defaultProperties);
    public static final DeferredBlock<RoostGeneratorBlock> ROOST_GENERATOR = BLOCKS.registerBlock("roost_generator", RoostGeneratorBlock::new, RoostGeneratorBlock::defaultProperties);
    public static final DeferredBlock<MechanicalNestBlock> MECHANICAL_NEST = BLOCKS.registerBlock("mechanical_nest", MechanicalNestBlock::new, MechanicalNestBlock::defaultProperties);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE = registerHenhouse("henhouse", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_SPRUCE = registerHenhouse("henhouse_spruce", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_BIRCH = registerHenhouse("henhouse_birch", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_JUNGLE = registerHenhouse("henhouse_jungle", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_ACACIA = registerHenhouse("henhouse_acacia", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_DARK_OAK = registerHenhouse("henhouse_dark_oak", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_MANGROVE = registerHenhouse("henhouse_mangrove", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_CHERRY = registerHenhouse("henhouse_cherry", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_PALE_OAK = registerHenhouse("henhouse_pale_oak", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_BAMBOO = registerHenhouse("henhouse_bamboo", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_CRIMSON = registerHenhouse("henhouse_crimson", MapColor.COLOR_BROWN);
    public static final DeferredBlock<HenhouseBlock> HENHOUSE_WARPED = registerHenhouse("henhouse_warped", MapColor.COLOR_BROWN);

    public static final List<DeferredBlock<HenhouseBlock>> HENHOUSE_BLOCKS = List.of(
            HENHOUSE, HENHOUSE_SPRUCE, HENHOUSE_BIRCH, HENHOUSE_JUNGLE, HENHOUSE_ACACIA, HENHOUSE_DARK_OAK,
            HENHOUSE_MANGROVE, HENHOUSE_CHERRY, HENHOUSE_PALE_OAK, HENHOUSE_BAMBOO, HENHOUSE_CRIMSON, HENHOUSE_WARPED
    );

    public static final DeferredItem<BlockItem> HENHOUSE_ITEM = registerHenhouseItem("henhouse", HENHOUSE);
    public static final DeferredItem<BlockItem> HENHOUSE_SPRUCE_ITEM = registerHenhouseItem("henhouse_spruce", HENHOUSE_SPRUCE);
    public static final DeferredItem<BlockItem> HENHOUSE_BIRCH_ITEM = registerHenhouseItem("henhouse_birch", HENHOUSE_BIRCH);
    public static final DeferredItem<BlockItem> HENHOUSE_JUNGLE_ITEM = registerHenhouseItem("henhouse_jungle", HENHOUSE_JUNGLE);
    public static final DeferredItem<BlockItem> HENHOUSE_ACACIA_ITEM = registerHenhouseItem("henhouse_acacia", HENHOUSE_ACACIA);
    public static final DeferredItem<BlockItem> HENHOUSE_DARK_OAK_ITEM = registerHenhouseItem("henhouse_dark_oak", HENHOUSE_DARK_OAK);
    public static final DeferredItem<BlockItem> HENHOUSE_MANGROVE_ITEM = registerHenhouseItem("henhouse_mangrove", HENHOUSE_MANGROVE);
    public static final DeferredItem<BlockItem> HENHOUSE_CHERRY_ITEM = registerHenhouseItem("henhouse_cherry", HENHOUSE_CHERRY);
    public static final DeferredItem<BlockItem> HENHOUSE_PALE_OAK_ITEM = registerHenhouseItem("henhouse_pale_oak", HENHOUSE_PALE_OAK);
    public static final DeferredItem<BlockItem> HENHOUSE_BAMBOO_ITEM = registerHenhouseItem("henhouse_bamboo", HENHOUSE_BAMBOO);
    public static final DeferredItem<BlockItem> HENHOUSE_CRIMSON_ITEM = registerHenhouseItem("henhouse_crimson", HENHOUSE_CRIMSON);
    public static final DeferredItem<BlockItem> HENHOUSE_WARPED_ITEM = registerHenhouseItem("henhouse_warped", HENHOUSE_WARPED);
    public static final DeferredItem<BlockItem> ROOST_ITEM = ITEMS.registerSimpleBlockItem(ROOST);
    public static final DeferredItem<BlockItem> NEST_ITEM = ITEMS.registerSimpleBlockItem(NEST);
    public static final DeferredItem<BlockItem> BREEDER_ITEM = ITEMS.registerSimpleBlockItem(BREEDER);
    public static final DeferredItem<BlockItem> COLLECTOR_ITEM = ITEMS.registerSimpleBlockItem(COLLECTOR);
    public static final DeferredItem<BlockItem> AVIAN_FLUX_CONVERTER_ITEM = registerTooltipBlockItem("avian_flux_converter", AVIAN_FLUX_CONVERTER);
    public static final DeferredItem<BlockItem> AVIAN_FLUID_CONVERTER_ITEM = registerTooltipBlockItem("avian_fluid_converter", AVIAN_FLUID_CONVERTER);
    public static final DeferredItem<BlockItem> AVIAN_CHEMICAL_CONVERTER_ITEM = registerTooltipBlockItem("avian_chemical_converter", AVIAN_CHEMICAL_CONVERTER);
    public static final DeferredItem<BlockItem> AVIAN_DOUSING_MACHINE_ITEM = registerTooltipBlockItem("avian_dousing_machine", AVIAN_DOUSING_MACHINE);
    public static final DeferredItem<BlockItem> INCUBATOR_ITEM = registerTooltipBlockItem("incubator", INCUBATOR);
    public static final DeferredItem<BlockItem> MECHANICAL_ROOST_ITEM = ITEMS.registerSimpleBlockItem(MECHANICAL_ROOST);
    public static final DeferredItem<BlockItem> ROOST_GENERATOR_ITEM = ITEMS.registerSimpleBlockItem(ROOST_GENERATOR);
    public static final DeferredItem<BlockItem> MECHANICAL_NEST_ITEM = ITEMS.registerSimpleBlockItem(MECHANICAL_NEST);

    private static final List<DeferredItem<BlockItem>> HENHOUSE_ITEMS = List.of(
            HENHOUSE_ITEM, HENHOUSE_SPRUCE_ITEM, HENHOUSE_BIRCH_ITEM,
            HENHOUSE_JUNGLE_ITEM, HENHOUSE_ACACIA_ITEM, HENHOUSE_DARK_OAK_ITEM,
            HENHOUSE_MANGROVE_ITEM, HENHOUSE_CHERRY_ITEM, HENHOUSE_PALE_OAK_ITEM, HENHOUSE_BAMBOO_ITEM,
            HENHOUSE_CRIMSON_ITEM, HENHOUSE_WARPED_ITEM
    );
    public static final DeferredItem<ChickenItem> CHICKEN_ITEM = ITEMS.registerItem("chicken", ChickenItem::new, props -> props.stacksTo(16));
    public static final DeferredItem<ChickenItem> ROBOT_CHICKEN_ITEM = ITEMS.registerItem("robot_chicken", ChickenItem::new, ModRegistry::robotChickenItemProperties);
    public static final DeferredItem<ChickenItem> ROBOT_ROOSTER_ITEM = ITEMS.registerItem("robot_rooster", ChickenItem::new, ModRegistry::robotRoosterItemProperties);
    public static final DeferredItem<ChickenCatcherItem> CATCHER = ITEMS.registerItem("catcher", ChickenCatcherItem::new, props -> props.stacksTo(1).durability(64));
    public static final DeferredItem<MegaChickenItem> MEGA_CHICKEN_ITEM = ITEMS.registerItem("mega_chicken", MegaChickenItem::new);
    public static final DeferredItem<MegaChickenSkinCrateItem> ZOMBIE_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.ZOMBIE);
    public static final DeferredItem<MegaChickenSkinCrateItem> VALENTINES_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.VALENTINES);
    public static final DeferredItem<MegaChickenSkinCrateItem> TOXIC_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.TOXIC);
    public static final DeferredItem<MegaChickenSkinCrateItem> REPTAR_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.REPTAR);
    public static final DeferredItem<MegaChickenSkinCrateItem> RAMBO_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.RAMBO);
    public static final DeferredItem<MegaChickenSkinCrateItem> PINK_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.PINK);
    public static final DeferredItem<MegaChickenSkinCrateItem> FOX_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.FOX);
    public static final DeferredItem<MegaChickenSkinCrateItem> DUCK_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.DUCK);
    public static final DeferredItem<MegaChickenSkinCrateItem> DODO_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.DODO);
    public static final DeferredItem<MegaChickenSkinCrateItem> DEEPDARK_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.DEEPDARK);
    public static final DeferredItem<MegaChickenSkinCrateItem> CREEPER_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.CREEPER);
    public static final DeferredItem<MegaChickenSkinCrateItem> BIGBRAIN_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.BIGBRAIN);
    public static final DeferredItem<MegaChickenSkinCrateItem> AVIATOR_SKIN_CRATE = registerSkinCrate(MegaChickenSkin.AVIATOR);
    public static final DeferredItem<CreativeCatcherItem> CREATIVE_CATCHER = ITEMS.registerItem("creative_catcher", CreativeCatcherItem::new,
            props -> props.stacksTo(1).component(DataComponents.UNBREAKABLE, Unit.INSTANCE));

    public static List<DeferredItem<BlockItem>> getHenhouseItems() {
        return Collections.unmodifiableList(HENHOUSE_ITEMS);
    }

    public static DeferredItem<MegaChickenSkinCrateItem> skinCrate(MegaChickenSkin skin) {
        return switch (skin) {
            case ZOMBIE -> ZOMBIE_SKIN_CRATE;
            case VALENTINES -> VALENTINES_SKIN_CRATE;
            case TOXIC -> TOXIC_SKIN_CRATE;
            case REPTAR -> REPTAR_SKIN_CRATE;
            case RAMBO -> RAMBO_SKIN_CRATE;
            case PINK -> PINK_SKIN_CRATE;
            case FOX -> FOX_SKIN_CRATE;
            case DUCK -> DUCK_SKIN_CRATE;
            case DODO -> DODO_SKIN_CRATE;
            case DEEPDARK -> DEEPDARK_SKIN_CRATE;
            case CREEPER -> CREEPER_SKIN_CRATE;
            case BIGBRAIN -> BIGBRAIN_SKIN_CRATE;
            case AVIATOR -> AVIATOR_SKIN_CRATE;
        };
    }

    private static DeferredItem<MegaChickenSkinCrateItem> registerSkinCrate(MegaChickenSkin skin) {
        return ITEMS.registerItem(skin.id() + "_skin_crate", props -> new MegaChickenSkinCrateItem(props, skin));
    }

    private static Item.Properties robotChickenItemProperties() {
        CompoundTag data = new CompoundTag();
        data.putInt("ChickenType", ChickensRegistry.SMART_CHICKEN_ID);
        data.putBoolean("RobotChicken", true);
        return new Item.Properties()
                .stacksTo(16)
                .component(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    private static Item.Properties robotRoosterItemProperties() {
        CompoundTag data = new CompoundTag();
        data.putBoolean("IsRooster", true);
        data.putBoolean("IsRobotRooster", true);
        return new Item.Properties()
                .stacksTo(16)
                .component(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    private static Item.Properties robotChickenSpawnEggProperties() {
        CompoundTag data = new CompoundTag();
        data.putInt("Type", ChickensRegistry.SMART_CHICKEN_ID);
        data.putBoolean("RobotChicken", true);
        return new Item.Properties()
                .stacksTo(64)
                .component(DataComponents.ENTITY_DATA,
                        TypedEntityData.of(ModEntityTypes.CHICKENS_CHICKEN.get(), data));
    }

    private static Item.Properties robotRoosterSpawnEggProperties() {
        CompoundTag data = new CompoundTag();
        data.putBoolean("RobotRooster", true);
        return new Item.Properties()
                .stacksTo(64)
                .component(DataComponents.ENTITY_DATA,
                        TypedEntityData.of(ModEntityTypes.ROOSTER.get(), data));
    }

    private static DeferredBlock<HenhouseBlock> registerHenhouse(String name, MapColor color) {
        return BLOCKS.registerBlock(name, HenhouseBlock::new, () -> HenhouseBlock.defaultProperties(color));
    }

    private static DeferredItem<BlockItem> registerHenhouseItem(String name, DeferredBlock<HenhouseBlock> block) {
        return registerTooltipBlockItem(name, block);
    }

    private static DeferredItem<BlockItem> registerTooltipBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.registerItem(name, props -> new TooltipBlockItem(block.get(), props),
                () -> new Item.Properties().useBlockDescriptionPrefix());
    }
}
