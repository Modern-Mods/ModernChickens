package strhercules.chickens.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.registry.ChickensSpawnBiomeModifier;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ChickensBiomeModifierProvider extends DatapackBuiltinEntriesProvider {
    private static final ResourceKey<BiomeModifier> CHICKENS_SPAWNS = ResourceKey.create(
            ForgeRegistries.Keys.BIOME_MODIFIERS,
            new ResourceLocation(ChickensMod.MOD_ID, "chickens_spawns"));

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, context ->
                    context.register(CHICKENS_SPAWNS, ChickensSpawnBiomeModifier.INSTANCE));

    public ChickensBiomeModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(ChickensMod.MOD_ID));
    }

    @Override
    public String getName() {
        return "Chickens Biome Modifiers";
    }
}
