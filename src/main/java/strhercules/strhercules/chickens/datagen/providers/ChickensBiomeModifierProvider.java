package strhercules.chickens.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.registry.ChickensSpawnBiomeModifier;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Emits the {@code neoforge:biome_modifier} datapack entry that installs natural chicken spawns.
 * The modifier lives in a dynamic registry, so it can only reach the game through this JSON.
 */
public class ChickensBiomeModifierProvider extends DatapackBuiltinEntriesProvider {
    private static final ResourceKey<BiomeModifier> CHICKENS_SPAWNS = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            ResourceLocation.fromNamespaceAndPath(ChickensMod.MOD_ID, "chickens_spawns"));

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, context ->
                    context.register(CHICKENS_SPAWNS, ChickensSpawnBiomeModifier.INSTANCE));

    public ChickensBiomeModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, BUILDER, Set.of(ChickensMod.MOD_ID));
    }

    @Override
    public String getName() {
        return "Chickens Biome Modifiers";
    }
}
