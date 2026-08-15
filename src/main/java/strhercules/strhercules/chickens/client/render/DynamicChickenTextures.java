package strhercules.chickens.client.render;

import com.mojang.blaze3d.platform.NativeImage;
import strhercules.chickens.ChickensMod;
import strhercules.chickens.ChickensRegistryItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public final class DynamicChickenTextures {
    private static final Logger LOGGER = LoggerFactory.getLogger("ChickensDynamicTextures");
    private static final ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/entity/chicken.png");
    private static final ResourceLocation LAYERED_BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChickensMod.MOD_ID, "textures/entity/chicken_base_two.png");
    private static final ResourceLocation LAYERED_PRIMARY_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChickensMod.MOD_ID, "textures/entity/chicken_resource.png");
    private static final ResourceLocation LAYERED_FOREGROUND_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChickensMod.MOD_ID, "textures/entity/chicken_resource_two.png");
    private static final ResourceLocation LAYERED_PARTS_TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ChickensMod.MOD_ID, "textures/entity/chicken_parts.png");
    private static final int LAYERED_TEXTURE_WIDTH = 64;
    private static final int LAYERED_TEXTURE_HEIGHT = 32;
    private static final Map<Integer, ResourceLocation> CACHE = new HashMap<>();
    private static NativeImage baseImageCache;
    private static NativeImage layeredBaseImageCache;
    private static NativeImage layeredPrimaryImageCache;
    private static NativeImage layeredForegroundImageCache;
    private static NativeImage layeredPartsImageCache;

    private DynamicChickenTextures() {
    }

    public static ResourceLocation textureFor(ChickensRegistryItem chicken) {
        return CACHE.computeIfAbsent(chicken.getId(), id -> generateTexture(chicken));
    }

    private static ResourceLocation generateTexture(ChickensRegistryItem chicken) {
        if (chicken.hasLayeredResourceTexture()) {
            NativeImage layered = generateLayeredImage(chicken);
            if (layered != null) {
                DynamicTexture texture = new DynamicTexture(layered);
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                        ChickensMod.MOD_ID, "dynamic/chicken_" + chicken.getId());
                Minecraft.getInstance().getTextureManager().register(id, texture);
                return id;
            }
        }

        return generateLegacyTexture(chicken);
    }

    private static ResourceLocation generateLegacyTexture(ChickensRegistryItem chicken) {
        NativeImage base = getBaseImage();
        if (base == null) {
            return BASE_TEXTURE;
        }

        NativeImage image = new NativeImage(base.getWidth(), base.getHeight(), false);
        int primary = chicken.getBgColor();
        int accent = chicken.getFgColor();
        for (int y = 0; y < base.getHeight(); y++) {
            for (int x = 0; x < base.getWidth(); x++) {
                int rgba = base.getPixelRGBA(x, y);
                int alpha = (rgba >>> 24) & 0xFF;
                if (alpha == 0) {
                    image.setPixelRGBA(x, y, 0);
                    continue;
                }
                int r = rgba & 0xFF;
                int g = (rgba >> 8) & 0xFF;
                int b = (rgba >> 16) & 0xFF;
                float max = Math.max(Math.max(r, g), b) / 255.0f;
                float min = Math.min(Math.min(r, g), b) / 255.0f;
                float saturation = max == 0.0f ? 0.0f : (max - min) / max;

                int resultRgb;
                if (saturation < 0.25f) {
                    float brightness = max;
                    resultRgb = lerpColor(accent, primary, brightness);
                } else {
                    resultRgb = (r << 16) | (g << 8) | b;
                }
                int finalColor = toAbgr(alpha, resultRgb);
                image.setPixelRGBA(x, y, finalColor);
            }
        }

        DynamicTexture texture = new DynamicTexture(image);
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(
                ChickensMod.MOD_ID, "dynamic/chicken_" + chicken.getId());
        Minecraft.getInstance().getTextureManager().register(id, texture);
        return id;
    }

    private static NativeImage generateLayeredImage(ChickensRegistryItem chicken) {
        NativeImage base = getLayeredBaseImage();
        NativeImage primary = getLayeredPrimaryImage();
        NativeImage foreground = getLayeredForegroundImage();
        NativeImage parts = getLayeredPartsImage();
        if (!hasDimensions(base, LAYERED_TEXTURE_WIDTH, LAYERED_TEXTURE_HEIGHT)
                || !hasDimensions(primary, LAYERED_TEXTURE_WIDTH, LAYERED_TEXTURE_HEIGHT)
                || !hasDimensions(parts, LAYERED_TEXTURE_WIDTH, LAYERED_TEXTURE_HEIGHT)
                || !hasDimensions(foreground, LAYERED_TEXTURE_WIDTH, LAYERED_TEXTURE_HEIGHT)) {
            LOGGER.warn("Unable to build layered resource texture for chicken {} because one or more layer dimensions are invalid",
                    chicken.getEntityName());
            return null;
        }

        NativeImage image = new NativeImage(LAYERED_TEXTURE_WIDTH, LAYERED_TEXTURE_HEIGHT, false);
        int primaryColor = chicken.getBgColor();
        int foregroundColor = chicken.getFgColor();
        tintLayer(image, base, 0, 0, primaryColor);
        tintLayer(image, primary, 0, 0, primaryColor);
        tintLayer(image, foreground, 0, 0, foregroundColor);
        copyLayer(image, parts, 0, 0);
        return image;
    }

    private static boolean hasDimensions(NativeImage image, int width, int height) {
        return image != null && image.getWidth() == width && image.getHeight() == height;
    }

    private static void tintLayer(NativeImage target, NativeImage source, int offsetX, int offsetY, int color) {
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int sourcePixel = source.getPixelRGBA(x, y);
                int alpha = (sourcePixel >>> 24) & 0xFF;
                if (alpha == 0) {
                    continue;
                }
                int shade = sourcePixel & 0xFF;
                int tintedRed = red * shade / 255;
                int tintedGreen = green * shade / 255;
                int tintedBlue = blue * shade / 255;
                target.setPixelRGBA(offsetX + x, offsetY + y,
                        toAbgr(alpha, (tintedRed << 16) | (tintedGreen << 8) | tintedBlue));
            }
        }
    }

    private static void copyLayer(NativeImage target, NativeImage source, int offsetX, int offsetY) {
        for (int y = 0; y < source.getHeight(); y++) {
            for (int x = 0; x < source.getWidth(); x++) {
                int pixel = source.getPixelRGBA(x, y);
                if (((pixel >>> 24) & 0xFF) != 0) {
                    target.setPixelRGBA(offsetX + x, offsetY + y, pixel);
                }
            }
        }
    }

    private static int lerpColor(int start, int end, float amount) {
        float clamped = Math.max(0.0f, Math.min(1.0f, amount));
        int sr = (start >> 16) & 0xFF;
        int sg = (start >> 8) & 0xFF;
        int sb = start & 0xFF;

        int er = (end >> 16) & 0xFF;
        int eg = (end >> 8) & 0xFF;
        int eb = end & 0xFF;

        int r = (int) (sr + (er - sr) * clamped);
        int g = (int) (sg + (eg - sg) * clamped);
        int b = (int) (sb + (eb - sb) * clamped);
        return (r << 16) | (g << 8) | b;
    }

    private static int toAbgr(int alpha, int rgb) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;
        return (alpha << 24) | (b << 16) | (g << 8) | r;
    }

    private static NativeImage getBaseImage() {
        if (baseImageCache != null) {
            return baseImageCache;
        }
        baseImageCache = loadImage(BASE_TEXTURE);
        return baseImageCache;
    }

    private static NativeImage getLayeredBaseImage() {
        if (layeredBaseImageCache == null) {
            layeredBaseImageCache = loadImage(LAYERED_BASE_TEXTURE);
        }
        return layeredBaseImageCache;
    }

    private static NativeImage getLayeredPrimaryImage() {
        if (layeredPrimaryImageCache == null) {
            layeredPrimaryImageCache = loadImage(LAYERED_PRIMARY_TEXTURE);
        }
        return layeredPrimaryImageCache;
    }

    private static NativeImage getLayeredForegroundImage() {
        if (layeredForegroundImageCache == null) {
            layeredForegroundImageCache = loadImage(LAYERED_FOREGROUND_TEXTURE);
        }
        return layeredForegroundImageCache;
    }

    private static NativeImage getLayeredPartsImage() {
        if (layeredPartsImageCache == null) {
            layeredPartsImageCache = loadImage(LAYERED_PARTS_TEXTURE);
        }
        return layeredPartsImageCache;
    }

    private static NativeImage loadImage(ResourceLocation location) {
        Optional<Resource> resource = Minecraft.getInstance().getResourceManager().getResource(location);
        if (resource.isEmpty()) {
            LOGGER.warn("Unable to load chicken texture {}", location);
            return null;
        }
        try (InputStream stream = resource.get().open()) {
            return NativeImage.read(stream);
        } catch (IOException e) {
            LOGGER.warn("Failed to read chicken texture {}", location, e);
            return null;
        }
    }

    private static void closeImage(NativeImage image) {
        if (image != null) {
            image.close();
        }
    }

    public static void clear() {
        CACHE.clear();
        closeImage(baseImageCache);
        closeImage(layeredBaseImageCache);
        closeImage(layeredPrimaryImageCache);
        closeImage(layeredForegroundImageCache);
        closeImage(layeredPartsImageCache);
        baseImageCache = null;
        layeredBaseImageCache = null;
        layeredPrimaryImageCache = null;
        layeredForegroundImageCache = null;
        layeredPartsImageCache = null;
    }

    public static SimplePreparableReloadListener<Void> reloadListener() {
        return new SimplePreparableReloadListener<>() {
            @Override
            protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
                return null;
            }

            @Override
            protected void apply(Void object, ResourceManager resourceManager, ProfilerFiller profiler) {
                clear();
            }
        };
    }
}
