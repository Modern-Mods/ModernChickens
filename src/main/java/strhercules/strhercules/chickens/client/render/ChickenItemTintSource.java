package strhercules.chickens.client.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import strhercules.chickens.ChemicalEggRegistry;
import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.GasEggRegistry;
import strhercules.chickens.LiquidEggRegistry;
import strhercules.chickens.LiquidEggRegistryItem;
import strhercules.chickens.item.ChickenItemHelper;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record ChickenItemTintSource(Kind kind) implements ItemTintSource {
    public static final MapCodec<ChickenItemTintSource> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(StringRepresentable.fromEnum(Kind::values).fieldOf("kind")
                    .forGetter(ChickenItemTintSource::kind)).apply(instance, ChickenItemTintSource::new));
    private static final int WHITE = 0xFFFFFFFF;

    @Override
    public int calculate(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner) {
        return switch (kind) {
            case BACKGROUND -> chickenColor(stack, true);
            case FOREGROUND -> chickenColor(stack, false);
            case OVERLAY -> overlayColor(stack);
            case COLORED_EGG -> coloredEggColor(stack);
            case LIQUID_EGG -> liquidEggColor(stack);
            case CHEMICAL_EGG -> chemicalEggColor(stack);
            case GAS_EGG -> gasEggColor(stack);
        };
    }

    @Override
    public MapCodec<ChickenItemTintSource> type() {
        return MAP_CODEC;
    }

    private static int chickenColor(ItemStack stack, boolean background) {
        ChickensRegistryItem chicken = ChickenItemHelper.resolve(stack);
        if (chicken == null) {
            return WHITE;
        }
        return 0xFF000000 | (background ? chicken.getBgColor() : chicken.getFgColor());
    }

    private static int overlayColor(ItemStack stack) {
        ChickensRegistryItem chicken = ChickenItemHelper.resolve(stack);
        if (chicken == null) {
            return WHITE;
        }
        return 0xFF000000 | LiquidChickenOverlayLayer.itemOverlayTint(chicken);
    }

    private static int coloredEggColor(ItemStack stack) {
        ChickensRegistryItem chicken = ChickenItemHelper.resolve(stack);
        if (chicken == null) {
            return WHITE;
        }
        DyeColor dye = chicken.getDyeColor();
        int color = dye != null ? dye.getTextColor() : chicken.getFgColor();
        return 0xFF000000 | color;
    }

    private static int liquidEggColor(ItemStack stack) {
        LiquidEggRegistryItem liquid = LiquidEggRegistry.findById(ChickenItemHelper.getChickenType(stack));
        return 0xFF000000 | (liquid != null ? liquid.getEggColor() : 0xFFFFFF);
    }

    private static int chemicalEggColor(ItemStack stack) {
        ChemicalEggRegistryItem chemical = ChemicalEggRegistry.findById(ChickenItemHelper.getChickenType(stack));
        return 0xFF000000 | (chemical != null ? chemical.getEggColor() : 0xFFFFFF);
    }

    private static int gasEggColor(ItemStack stack) {
        ChemicalEggRegistryItem gas = GasEggRegistry.findById(ChickenItemHelper.getChickenType(stack));
        return 0xFF000000 | (gas != null ? gas.getEggColor() : 0xFFFFFF);
    }

    public enum Kind implements StringRepresentable {
        BACKGROUND("background"),
        FOREGROUND("foreground"),
        OVERLAY("overlay"),
        COLORED_EGG("colored_egg"),
        LIQUID_EGG("liquid_egg"),
        CHEMICAL_EGG("chemical_egg"),
        GAS_EGG("gas_egg");

        private final String serializedName;

        Kind(String serializedName) {
            this.serializedName = serializedName;
        }

        @Override
        public String getSerializedName() {
            return serializedName;
        }
    }
}
