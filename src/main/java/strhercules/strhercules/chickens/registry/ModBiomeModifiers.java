package strhercules.chickens.registry;

import com.mojang.serialization.Codec;
import strhercules.chickens.ChickensMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegisterEvent.RegisterHelper;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraft.resources.ResourceLocation;

public final class ModBiomeModifiers {
    private static final ResourceLocation SPAWN_ID = new ResourceLocation(ChickensMod.MOD_ID, "chickens_spawns");

    private ModBiomeModifiers() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(ModBiomeModifiers::onRegisterSerializers);
    }

    private static void onRegisterSerializers(RegisterEvent event) {
        event.register(Keys.BIOME_MODIFIER_SERIALIZERS, helper -> registerSerializer(helper));
    }

    private static void registerSerializer(RegisterHelper<Codec<? extends BiomeModifier>> helper) {
        helper.register(SPAWN_ID, ChickensSpawnBiomeModifier.CODEC);
    }
}
