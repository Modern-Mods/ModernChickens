package strhercules.chickens.registry;

import com.mojang.serialization.MapCodec;
import strhercules.chickens.ChickensMod;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegisterEvent.RegisterHelper;
import net.neoforged.neoforge.registries.NeoForgeRegistries.Keys;
import net.minecraft.resources.ResourceLocation;

/**
 * Registers the biome modifier codec that handles natural chicken spawns. The modifier instance itself
 * lives in {@code data/chickens/neoforge/biome_modifier/chickens_spawns.json} because
 * {@code neoforge:biome_modifier} is a datapack registry and cannot be populated from code.
 */
public final class ModBiomeModifiers {
    private static final ResourceLocation SPAWN_ID = ResourceLocation.fromNamespaceAndPath(ChickensMod.MOD_ID, "chickens_spawns");

    private ModBiomeModifiers() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(ModBiomeModifiers::onRegisterSerializers);
    }

    private static void onRegisterSerializers(RegisterEvent event) {
        event.register(Keys.BIOME_MODIFIER_SERIALIZERS, helper -> registerSerializer(helper));
    }

    private static void registerSerializer(RegisterHelper<MapCodec<? extends BiomeModifier>> helper) {
        helper.register(SPAWN_ID, ChickensSpawnBiomeModifier.CODEC);
    }
}
