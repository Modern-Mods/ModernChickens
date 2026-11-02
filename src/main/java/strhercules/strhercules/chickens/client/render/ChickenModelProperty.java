package strhercules.chickens.client.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import strhercules.chickens.ChickensMod;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.item.ChemicalEggItem;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.item.GasEggItem;
import strhercules.chickens.item.LiquidEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class ChickenModelProperty implements SelectItemModelProperty<String> {
    public static final ChickenModelProperty INSTANCE = new ChickenModelProperty();
    public static final SelectItemModelProperty.Type<ChickenModelProperty, String> TYPE = SelectItemModelProperty.Type
            .create(MapCodec.unit(INSTANCE), Codec.STRING);
    public static final String ROOSTER = "#rooster";
    public static final String ROBOT_CHICKEN = "#robot_chicken";
    public static final String LAYERED = "#layered";
    public static final String BONE_OVERLAY = "#bone";
    public static final String DEFAULT = "#default";

    private static final Map<ChickensRegistryItem, String> CACHE = new WeakHashMap<>();

    private ChickenModelProperty() {
    }

    @Override
    public @Nullable String get(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed,
            ItemDisplayContext displayContext) {
        if (ChickenItemHelper.isRooster(stack)) {
            return ROOSTER;
        }
        if (ChickenItemHelper.isRobotChicken(stack)) {
            return ROBOT_CHICKEN;
        }
        ChickensRegistryItem chicken = ChickenItemHelper.resolve(stack);
        if (chicken == null) {
            return DEFAULT;
        }
        return CACHE.computeIfAbsent(chicken, ChickenModelProperty::select);
    }

    @Override
    public Codec<String> valueCodec() {
        return Codec.STRING;
    }

    @Override
    public SelectItemModelProperty.Type<ChickenModelProperty, String> type() {
        return TYPE;
    }

    public static boolean usesBoneOverlay(ChickensRegistryItem chicken) {
        if (chicken.isCustom()) {
            return false;
        }
        Object layItem = chicken.createLayItem().getItem();
        return layItem instanceof LiquidEggItem
                || layItem instanceof ChemicalEggItem
                || layItem instanceof GasEggItem;
    }

    public static SimplePreparableReloadListener<Void> reloadListener() {
        return new SimplePreparableReloadListener<>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
                CACHE.clear();
            }
        };
    }

    private static String select(ChickensRegistryItem chicken) {
        if (usesBoneOverlay(chicken)) {
            return BONE_OVERLAY;
        }
        boolean explicitTexture = chicken.getItemTexture() != null;
        if (hasTexture(spriteTexture(chicken))) {
            return chicken.getEntityName().toLowerCase(Locale.ROOT);
        }
        if (chicken.hasLayeredResourceTexture() && !explicitTexture) {
            return LAYERED;
        }
        return DEFAULT;
    }

    private static Identifier spriteTexture(ChickensRegistryItem chicken) {
        if (chicken.getItemTexture() != null) {
            return chicken.getItemTexture();
        }
        return Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
                "textures/item/chicken/" + chicken.getEntityName().toLowerCase(Locale.ROOT) + ".png");
    }

    private static boolean hasTexture(Identifier texture) {
        return Minecraft.getInstance().getResourceManager().getResource(texture).isPresent();
    }
}
