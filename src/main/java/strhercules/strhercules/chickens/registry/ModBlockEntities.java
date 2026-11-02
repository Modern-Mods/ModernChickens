package strhercules.chickens.registry;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.blockentity.AvianChemicalConverterBlockEntity;
import strhercules.chickens.blockentity.AvianFluxConverterBlockEntity;
import strhercules.chickens.blockentity.AvianDousingMachineBlockEntity;
import strhercules.chickens.blockentity.AvianFluidConverterBlockEntity;
import strhercules.chickens.blockentity.BreederBlockEntity;
import strhercules.chickens.blockentity.CollectorBlockEntity;
import strhercules.chickens.blockentity.IncubatorBlockEntity;
import strhercules.chickens.blockentity.HenhouseBlockEntity;
import strhercules.chickens.blockentity.RoostBlockEntity;
import strhercules.chickens.blockentity.MechanicalRoostBlockEntity;
import strhercules.chickens.blockentity.RoostGeneratorBlockEntity;
import strhercules.chickens.blockentity.MechanicalNestBlockEntity;
import strhercules.chickens.blockentity.NestBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Houses all block entity registrations for Modern Chickens. Keeping the logic
 * here avoids cluttering {@link ModRegistry} with type builders.
 */
public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ChickensMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HenhouseBlockEntity>> HENHOUSE = BLOCK_ENTITIES
            .register("henhouse", () -> new BlockEntityType<>(HenhouseBlockEntity::new, henhouseBlocks()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RoostBlockEntity>> ROOST = BLOCK_ENTITIES
            .register("roost", () -> new BlockEntityType<>(RoostBlockEntity::new, ModRegistry.ROOST.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MechanicalRoostBlockEntity>> MECHANICAL_ROOST = BLOCK_ENTITIES
            .register("mechanical_roost", () -> new BlockEntityType<>(MechanicalRoostBlockEntity::new, ModRegistry.MECHANICAL_ROOST.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RoostGeneratorBlockEntity>> ROOST_GENERATOR = BLOCK_ENTITIES
            .register("roost_generator", () -> new BlockEntityType<>(RoostGeneratorBlockEntity::new, ModRegistry.ROOST_GENERATOR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MechanicalNestBlockEntity>> MECHANICAL_NEST = BLOCK_ENTITIES
            .register("mechanical_nest", () -> new BlockEntityType<>(MechanicalNestBlockEntity::new, ModRegistry.MECHANICAL_NEST.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NestBlockEntity>> NEST = BLOCK_ENTITIES
            .register("nest", () -> new BlockEntityType<>(NestBlockEntity::new, ModRegistry.NEST.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BreederBlockEntity>> BREEDER = BLOCK_ENTITIES
            .register("breeder", () -> new BlockEntityType<>(BreederBlockEntity::new, ModRegistry.BREEDER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CollectorBlockEntity>> COLLECTOR = BLOCK_ENTITIES
            .register("collector", () -> new BlockEntityType<>(CollectorBlockEntity::new, ModRegistry.COLLECTOR.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AvianFluxConverterBlockEntity>> AVIAN_FLUX_CONVERTER = BLOCK_ENTITIES
            .register("avian_flux_converter", () -> new BlockEntityType<>(AvianFluxConverterBlockEntity::new, ModRegistry.AVIAN_FLUX_CONVERTER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AvianFluidConverterBlockEntity>> AVIAN_FLUID_CONVERTER = BLOCK_ENTITIES
            .register("avian_fluid_converter", () -> new BlockEntityType<>(AvianFluidConverterBlockEntity::new, ModRegistry.AVIAN_FLUID_CONVERTER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AvianChemicalConverterBlockEntity>> AVIAN_CHEMICAL_CONVERTER = BLOCK_ENTITIES
            .register("avian_chemical_converter", () -> new BlockEntityType<>(AvianChemicalConverterBlockEntity::new, ModRegistry.AVIAN_CHEMICAL_CONVERTER.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AvianDousingMachineBlockEntity>> AVIAN_DOUSING_MACHINE = BLOCK_ENTITIES
            .register("avian_dousing_machine", () -> new BlockEntityType<>(AvianDousingMachineBlockEntity::new, ModRegistry.AVIAN_DOUSING_MACHINE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IncubatorBlockEntity>> INCUBATOR = BLOCK_ENTITIES
            .register("incubator", () -> new BlockEntityType<>(IncubatorBlockEntity::new, ModRegistry.INCUBATOR.get()));

    private ModBlockEntities() {
    }

    public static void init(IEventBus modBus) {
        BLOCK_ENTITIES.register(modBus);
    }

    private static Block[] henhouseBlocks() {
        // Expand the type to recognise every wood variant instead of only the oak block.
        return ModRegistry.HENHOUSE_BLOCKS.stream().map(DeferredBlock::get).toArray(Block[]::new);
    }
}
