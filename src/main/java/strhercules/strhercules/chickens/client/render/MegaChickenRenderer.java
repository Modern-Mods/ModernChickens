package strhercules.chickens.client.render;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.ChickensRegistry;
import strhercules.chickens.ChickensRegistryItem;
import strhercules.chickens.entity.MegaChicken;
import strhercules.chickens.entity.MegaChickenSkin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Renderer for the supplied 3x Mega Chicken model and its tame-state equipment. */
public final class MegaChickenRenderer extends MobRenderer<MegaChicken, MegaChickenRenderState, MegaChickenModel> {
    private static final Identifier UNTAMED_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/entity/megachicken/mega_chicken.png");
    private static final Identifier TAMED_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/entity/megachicken/mega_chicken_tamed.png");
    private static final Identifier ROBOT_CHICKEN_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/entity/megachicken/robot_chicken.png");
    private static final Identifier ROBOT_ROOSTER_TEXTURE = Identifier.fromNamespaceAndPath(
            "chickens", "textures/entity/megachicken/robot_rooster.png");
    private static final Set<Identifier> AVAILABLE_TEXTURES = new HashSet<>();

    public MegaChickenRenderer(EntityRendererProvider.Context context) {
        super(context, new MegaChickenModel(context.bakeLayer(MegaChickenModel.LAYER_LOCATION)), 0.9F);
    }

    @Override
    public MegaChickenRenderState createRenderState() {
        return new MegaChickenRenderState();
    }

    @Override
    public void extractRenderState(MegaChicken chicken, MegaChickenRenderState state, float partialTicks) {
        super.extractRenderState(chicken, state, partialTicks);
        state.texture = resolveTexture(chicken);
        state.tamed = chicken.isTamed();
        state.saddled = chicken.isSaddled();
        state.rightChest = chicken.hasRightChest();
        state.leftChest = chicken.hasLeftChest();
        state.airborne = chicken.isFlightActive() && !chicken.onGround();
        if (state.airborne) {
            float flap = Mth.lerp(partialTicks, chicken.oFlap, chicken.flap);
            float speed = Mth.lerp(partialTicks, chicken.oFlapSpeed, chicken.flapSpeed);
            state.bob = (Mth.sin(flap) + 1.0F) * speed;
        } else {
            state.bob = 0.0F;
        }
    }

    @Override
    public Identifier getTextureLocation(MegaChickenRenderState state) {
        return state.texture;
    }

    @Override
    protected void scale(MegaChickenRenderState state, PoseStack poseStack) {
        if (state.isBaby) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }
    }

    private static Identifier resolveTexture(MegaChicken chicken) {
        if (!chicken.isTamed()) {
            return UNTAMED_TEXTURE;
        }
        Identifier robotTexture = switch (chicken.getRobotSkin()) {
            case MegaChicken.ROBOT_SKIN_CHICKEN -> ROBOT_CHICKEN_TEXTURE;
            case MegaChicken.ROBOT_SKIN_ROOSTER -> ROBOT_ROOSTER_TEXTURE;
            default -> null;
        };
        if (robotTexture != null && hasTexture(robotTexture)) {
            return robotTexture;
        }
        MegaChickenSkin directSkin = MegaChickenSkin.byId(chicken.getSkinId());
        if (directSkin != null && hasTexture(directSkin.texture())) {
            return directSkin.texture();
        }
        Identifier appearance = appearanceTexture(chicken);
        return appearance == null ? TAMED_TEXTURE : appearance;
    }

    private static Identifier appearanceTexture(MegaChicken chicken) {
        ChickensRegistryItem appearance = ChickensRegistry.getByType(chicken.getAppearanceType());
        if (appearance == null) {
            return null;
        }
        String path = appearance.getTexture().getPath();
        int slash = path.lastIndexOf('/');
        String name = slash >= 0 ? path.substring(slash + 1) : path;
        if (name.endsWith(".png")) {
            name = name.substring(0, name.length() - 4);
        }
        Identifier texture = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID,
                "textures/entity/megachicken/" + name.toLowerCase(Locale.ROOT) + ".png");
        return hasTexture(texture) ? texture : null;
    }

    private static boolean hasTexture(Identifier texture) {
        if (AVAILABLE_TEXTURES.contains(texture)) {
            return true;
        }
        if (Minecraft.getInstance().getResourceManager().getResource(texture).isPresent()) {
            AVAILABLE_TEXTURES.add(texture);
            return true;
        }
        return false;
    }
}
