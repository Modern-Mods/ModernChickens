package strhercules.chickens;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.Tags;

/**
 * Narrows where a chicken may spawn naturally, on top of its spawn type.
 * {@link #TYPE} keeps the original behaviour of following the spawn type alone.
 */
public enum SpawnBiomeRule {
    TYPE,
    LAND,
    SAND,
    ICY,
    NETHER,
    SOUL_SAND_VALLEY,
    END;

    /**
     * Biome-only check used when the biome spawn tables are built. Rules that depend on the exact
     * position, such as nether fortresses, accept every biome that could contain the place.
     */
    public boolean matchesBiome(Holder<Biome> biome) {
        return switch (this) {
            case TYPE -> true;
            case LAND -> biome.is(BiomeTags.IS_OVERWORLD)
                    && !biome.is(Tags.Biomes.IS_DESERT)
                    && !biome.is(BiomeTags.IS_BEACH)
                    && !biome.is(BiomeTags.IS_OCEAN)
                    && !biome.is(Biomes.MUSHROOM_FIELDS);
            case SAND -> biome.is(BiomeTags.IS_BEACH) || biome.is(Tags.Biomes.IS_DESERT);
            case ICY -> biome.is(Tags.Biomes.IS_SNOWY);
            case NETHER, SOUL_SAND_VALLEY -> biome.is(Tags.Biomes.IS_NETHER);
            case END -> biome.is(Tags.Biomes.IS_END);
        };
    }

    /** Exact check used when a chicken is about to spawn at a position. */
    public boolean matches(ServerLevelAccessor level, Holder<Biome> biome, BlockPos pos) {
        if (this == SOUL_SAND_VALLEY) {
            return biome.is(Biomes.SOUL_SAND_VALLEY) || isInsideFortress(level, pos);
        }
        return matchesBiome(biome);
    }

    private static boolean isInsideFortress(ServerLevelAccessor level, BlockPos pos) {
        Structure fortress = level.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.FORTRESS);
        return fortress != null && level.getLevel().structureManager().getStructureWithPieceAt(pos, fortress).isValid();
    }
}
