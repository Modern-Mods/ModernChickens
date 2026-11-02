package strhercules.chickens.registry;

import com.mojang.serialization.MapCodec;
import strhercules.chickens.SpawnType;
import strhercules.chickens.spawn.ChickensSpawnManager;
import strhercules.chickens.spawn.ChickensSpawnManager.SpawnPlan;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.Optional;

public final class ChickensSpawnBiomeModifier implements BiomeModifier {
    public static final ChickensSpawnBiomeModifier INSTANCE = new ChickensSpawnBiomeModifier();
    public static final MapCodec<ChickensSpawnBiomeModifier> CODEC = MapCodec.unit(() -> INSTANCE);

    private ChickensSpawnBiomeModifier() {
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD) {
            return;
        }
        Optional<SpawnPlan> maybePlan = ChickensSpawnManager.planForSpawn(biome);
        if (maybePlan.isEmpty()) {
            return;
        }
        SpawnPlan plan = maybePlan.get();
        boolean vanillaChickenBiome = hasVanillaChickenSpawn(builder);
        if (requiresVanillaChicken(plan.spawnType()) && !vanillaChickenBiome
                && !ChickensSpawnManager.hasRuleCandidate(biome)) {
            return;
        }

        builder.getMobSpawnSettings().addSpawn(plan.category(), plan.spawnWeight(), plan.spawnerData());
        builder.getMobSpawnSettings().addMobCharge(plan.spawnerData().type(), plan.spawnCharge(), plan.energyBudget());

        if (vanillaChickenBiome) {
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, 1,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.MEGA_CHICKEN.get(), 1, 1));
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, 1,
                    new MobSpawnSettings.SpawnerData(ModEntityTypes.ROOSTER.get(), 1, 1));
        }
    }

    private static boolean requiresVanillaChicken(SpawnType spawnType) {
        return spawnType != SpawnType.HELL && spawnType != SpawnType.END;
    }

    private static boolean hasVanillaChickenSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        return builder.getMobSpawnSettings().getSpawner(MobCategory.CREATURE).getList().stream()
                .anyMatch(spawn -> spawn.value().type() == EntityType.CHICKEN);
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
