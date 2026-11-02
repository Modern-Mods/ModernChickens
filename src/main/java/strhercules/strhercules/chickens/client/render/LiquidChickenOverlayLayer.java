package strhercules.chickens.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import strhercules.chickens.ChemicalEggRegistry;
import strhercules.chickens.ChemicalEggRegistryItem;
import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.GasEggRegistry;
import strhercules.chickens.LiquidEggRegistry;
import strhercules.chickens.LiquidEggRegistryItem;
import strhercules.chickens.client.render.ChickensChickenRenderState.OverlayInfo;
import strhercules.chickens.entity.ChickensChicken;
import strhercules.chickens.item.ChickenItemHelper;
import strhercules.chickens.item.ChemicalEggItem;
import strhercules.chickens.item.GasEggItem;
import strhercules.chickens.item.LiquidEggItem;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import org.jspecify.annotations.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Render layer that mirrors ModernFluidCows' overlay: it replays the chicken model using the bound
 * fluid's atlas sprite and tint, letting animated textures supply the liquid motion naturally.
 */
public final class LiquidChickenOverlayLayer extends RenderLayer<ChickenRenderState, ChickenModel> {
    private static final Map<Integer, Optional<LiquidEggRegistryItem>> LIQUID_CACHE = new HashMap<>();
    private static final int OVERLAY_ALPHA = Math.round(0.8F * 255.0F);

    private final ChickenModel adultModel;
    private final ChickenModel babyModel;

    public LiquidChickenOverlayLayer(RenderLayerParent<ChickenRenderState, ChickenModel> parent,
                                     ChickenModel adultModel, ChickenModel babyModel) {
        super(parent);
        this.adultModel = adultModel;
        this.babyModel = babyModel;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords,
                       ChickenRenderState state, float yRot, float xRot) {
        if (!(state instanceof ChickensChickenRenderState chickenState) || state.isInvisible
                || chickenState.overlay == null) {
            return;
        }
        OverlayInfo overlay = chickenState.overlay;
        ChickenModel model = state.isBaby ? babyModel : adultModel;
        submitNodeCollector.submitModel(model, state, poseStack,
                RenderTypes.entityTranslucent(TextureAtlas.LOCATION_BLOCKS), lightCoords,
                LivingEntityRenderer.getOverlayCoords(state, 0.0F),
                ARGB.color(OVERLAY_ALPHA, overlay.tint()), overlay.sprite(), state.outlineColor, null);
    }

    @Nullable
    public static OverlayInfo extractOverlay(ChickensChicken chicken) {
        ChickensRegistryItem description = ChickensRegistry.getByType(chicken.getChickenType());
        if (description == null) {
            return null;
        }
        return resolveOverlay(description);
    }

    private static LiquidEggRegistryItem resolveLiquid(ChickensRegistryItem description) {
        Optional<LiquidEggRegistryItem> cached = LIQUID_CACHE.computeIfAbsent(description.getId(), id -> {
            ItemStack layStack = description.createLayItem();
            if (!(layStack.getItem() instanceof LiquidEggItem)) {
                return Optional.empty();
            }
            return Optional.ofNullable(LiquidEggRegistry.findById(ChickenItemHelper.getChickenType(layStack)));
        });
        return cached.orElse(null);
    }

    @Nullable
    private static OverlayInfo resolveOverlay(ChickensRegistryItem description) {
        ItemStack layStack = description.createLayItem();
        if (layStack.isEmpty()) {
            return null;
        }
        if (layStack.getItem() instanceof LiquidEggItem) {
            LiquidEggRegistryItem liquid = resolveLiquid(description);
            if (liquid == null) {
                return null;
            }
            FluidStack fluidStack = liquid.createFluidStack();
            if (fluidStack.isEmpty()) {
                return null;
            }
            return new OverlayInfo(ClientFluids.stillSprite(fluidStack.getFluid()),
                    brightenTint(ClientFluids.tint(fluidStack)));
        }
        int id = ChickenItemHelper.getChickenType(layStack);
        if (layStack.getItem() instanceof ChemicalEggItem) {
            ChemicalEggRegistryItem chemical = ChemicalEggRegistry.findById(id);
            if (chemical != null) {
                return new OverlayInfo(ClientFluids.blockSprite(chemical.getTexture()),
                        brightenTint(chemical.getEggColor()));
            }
        } else if (layStack.getItem() instanceof GasEggItem) {
            ChemicalEggRegistryItem gas = GasEggRegistry.findById(id);
            if (gas != null) {
                return new OverlayInfo(ClientFluids.blockSprite(gas.getTexture()), brightenTint(gas.getEggColor()));
            }
        }
        return null;
    }

    private static int brightenTint(int tint) {
        int red = Mth.clamp((int) (((tint >> 16) & 0xFF) * 1.2F + 24.0F), 0, 255);
        int green = Mth.clamp((int) (((tint >> 8) & 0xFF) * 1.2F + 24.0F), 0, 255);
        int blue = Mth.clamp((int) ((tint & 0xFF) * 1.2F + 24.0F), 0, 255);
        return (red << 16) | (green << 8) | blue;
    }

    public static int itemOverlayTint(ChickensRegistryItem description) {
        ItemStack layStack = description.createLayItem();
        if (layStack.getItem() instanceof LiquidEggItem) {
            LiquidEggRegistryItem liquid = resolveLiquid(description);
            if (liquid != null) {
                FluidStack fluidStack = liquid.createFluidStack();
                if (!fluidStack.isEmpty()) {
                    return brightenTint(ClientFluids.tint(fluidStack));
                }
            }
        } else {
            int id = ChickenItemHelper.getChickenType(layStack);
            if (layStack.getItem() instanceof ChemicalEggItem) {
                ChemicalEggRegistryItem chemical = ChemicalEggRegistry.findById(id);
                if (chemical != null) {
                    return brightenTint(chemical.getEggColor());
                }
            } else if (layStack.getItem() instanceof GasEggItem) {
                ChemicalEggRegistryItem gas = GasEggRegistry.findById(id);
                if (gas != null) {
                    return brightenTint(gas.getEggColor());
                }
            }
        }
        return brightenTint(description.getBgColor());
    }

    public static void clearCaches() {
        LIQUID_CACHE.clear();
    }

    public static SimplePreparableReloadListener<Void> reloadListener() {
        return new SimplePreparableReloadListener<>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
                clearCaches();
            }
        };
    }
}
