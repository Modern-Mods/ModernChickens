package strhercules.chickens.client.render;

import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.entity.ChickensChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.animal.chicken.AdultChickenModel;
import net.minecraft.client.model.animal.chicken.BabyChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.animal.chicken.Chicken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspecify.annotations.Nullable;
import java.util.HashSet;
import java.util.Set;

/**
 * Renderer that mirrors the vanilla chicken visuals but swaps the texture based
 * on the chicken registry entry. This restores the per-breed skins from the
 * original mod.
 */
public class ChickensChickenRenderer extends ChickenRenderer {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChickensChickenRenderer");
    private static final Identifier ROBOT_CHICKEN_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/entity/robot_chicken.png");
    private static final Set<Identifier> VERIFIED_TEXTURES = new HashSet<>();
    private static final Set<Identifier> LOGGED_MISSING_TEXTURES = new HashSet<>();

    public ChickensChickenRenderer(EntityRendererProvider.Context context) {
        super(context);
        // Mirror the Fluid Cows setup by decorating the base model with a
        // translucent, animated fluid layer for liquid chickens.
        this.addLayer(new LiquidChickenOverlayLayer(this,
                new AdultChickenModel(context.bakeLayer(ModelLayers.CHICKEN)),
                new BabyChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY))));
    }

    @Override
    public ChickenRenderState createRenderState() {
        return new ChickensChickenRenderState();
    }

    @Override
    public void extractRenderState(Chicken entity, ChickenRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        if (state instanceof ChickensChickenRenderState chickenState) {
            chickenState.texture = null;
            chickenState.overlay = null;
            if (entity instanceof ChickensChicken modChicken) {
                chickenState.texture = resolveTexture(modChicken);
                chickenState.overlay = LiquidChickenOverlayLayer.extractOverlay(modChicken);
            }
        }
    }

    @Override
    public Identifier getTextureLocation(ChickenRenderState state) {
        if (state instanceof ChickensChickenRenderState chickenState && chickenState.texture != null) {
            return chickenState.texture;
        }
        return super.getTextureLocation(state);
    }

    @Nullable
    private static Identifier resolveTexture(ChickensChicken modChicken) {
        if (modChicken.isRobotChicken()) {
            return ROBOT_CHICKEN_TEXTURE;
        }
        ChickensRegistryItem description = ChickensRegistry.getByType(modChicken.getChickenType());
        if (description == null) {
            return null;
        }
        if (description.hasGeneratedTexture()) {
            return DynamicChickenTextures.textureFor(description);
        }

        Identifier texture = description.getTexture();
        if (hasTexture(texture)) {
            return texture;
        }

        if (description.hasLayeredResourceTexture()) {
            return DynamicChickenTextures.textureFor(description);
        }

        if (LOGGED_MISSING_TEXTURES.add(texture)) {
            LOGGER.warn(
                    "Falling back to generated texture for chicken {} because {} was unavailable", description.getEntityName(),
                    texture);
        }

        // Fallback to the tint pipeline so players see a coloured chicken instead of
        // the purple/black missing-texture placeholder when a resource pack is absent
        // or a path is mis-typed.
        return DynamicChickenTextures.textureFor(description);
    }

    private static boolean hasTexture(Identifier texture) {
        if (VERIFIED_TEXTURES.contains(texture)) {
            return true;
        }
        boolean present = Minecraft.getInstance().getResourceManager().getResource(texture).isPresent();
        if (present) {
            VERIFIED_TEXTURES.add(texture);
        }
        return present;
    }

    public static SimplePreparableReloadListener<Void> textureAvailabilityReloader() {
        return new SimplePreparableReloadListener<>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
                clearTextureCaches();
            }
        };
    }

    private static void clearTextureCaches() {
        VERIFIED_TEXTURES.clear();
        LOGGED_MISSING_TEXTURES.clear();
    }
}
