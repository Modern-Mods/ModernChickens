package strhercules.chickens;

import strhercules.chickens.client.ChickensConfigScreen;
import strhercules.chickens.entity.MegaChicken;
import strhercules.chickens.client.render.ChickenItemTintSource;
import strhercules.chickens.client.render.ChickenModelProperty;
import strhercules.chickens.client.render.ChickensChickenRenderer;
import strhercules.chickens.client.render.DynamicChickenTextures;
import strhercules.chickens.client.render.LiquidChickenOverlayLayer;
import strhercules.chickens.client.render.MegaChickenModel;
import strhercules.chickens.client.render.MegaChickenRenderer;
import strhercules.chickens.client.render.RoosterModel;
import strhercules.chickens.client.render.RoosterRenderer;
import strhercules.chickens.client.render.blockentity.BreederBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.CollectorBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.AvianChemicalConverterBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.AvianDousingMachineBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.AvianFluxConverterBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.AvianFluidConverterBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.RoostBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.NestBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.MechanicalNestBlockEntityRenderer;
import strhercules.chickens.client.render.blockentity.RoostGeneratorBlockEntityRenderer;
import strhercules.chickens.network.MegaChickenFlightPayload;
import strhercules.chickens.registry.ModBlockEntities;
import strhercules.chickens.registry.ModEntityTypes;
import strhercules.chickens.registry.ModMenuTypes;
import strhercules.chickens.registry.ModRegistry;
import strhercules.chickens.screen.AvianChemicalConverterScreen;
import strhercules.chickens.screen.AvianDousingMachineScreen;
import strhercules.chickens.screen.AvianFluxConverterScreen;
import strhercules.chickens.screen.AvianFluidConverterScreen;
import strhercules.chickens.screen.BreederScreen;
import strhercules.chickens.screen.CollectorScreen;
import strhercules.chickens.screen.IncubatorScreen;
import strhercules.chickens.screen.HenhouseScreen;
import strhercules.chickens.screen.MegaChickenScreen;
import strhercules.chickens.screen.RoostScreen;
import strhercules.chickens.screen.MechanicalRoostScreen;
import strhercules.chickens.screen.RoostGeneratorScreen;
import strhercules.chickens.screen.NestScreen;
import strhercules.chickens.screen.MechanicalNestScreen;
import strhercules.chickens.screen.RoosterScreen;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterSelectItemModelPropertyEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

/**
 * Client-only hooks for renderer and colour registration. Static event
 * subscribers keep server environments free from accidental client class loads.
 */
@EventBusSubscriber(modid = ChickensMod.MOD_ID, value = Dist.CLIENT)
public final class ChickensClient {
    private static final KeyMapping MEGA_CHICKEN_DIVE = new KeyMapping(
            "key.chickens.mega_chicken_dive", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT,
            KeyMapping.Category.MOVEMENT);
    private static final KeyMapping MEGA_CHICKEN_AIRBRAKE = new KeyMapping(
            "key.chickens.mega_chicken_airbrake", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL,
            KeyMapping.Category.MOVEMENT);
    private static boolean lastJumpDown;
    private static boolean lastDiveDown;
    private static boolean lastAirbrakeDown;

    private ChickensClient() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.COLORED_EGG.get(), context -> new ThrownItemRenderer<>(context, 1.0F, true));
        event.registerEntityRenderer(ModEntityTypes.CHICKENS_CHICKEN.get(), ChickensChickenRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ROOSTER.get(), RoosterRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MEGA_CHICKEN.get(), MegaChickenRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ROOST.get(), RoostBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.NEST.get(), NestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MECHANICAL_NEST.get(), MechanicalNestBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ROOST_GENERATOR.get(), RoostGeneratorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BREEDER.get(), BreederBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COLLECTOR.get(), CollectorBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AVIAN_FLUX_CONVERTER.get(),
                AvianFluxConverterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AVIAN_FLUID_CONVERTER.get(),
                AvianFluidConverterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AVIAN_CHEMICAL_CONVERTER.get(),
                AvianChemicalConverterBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.AVIAN_DOUSING_MACHINE.get(),
                AvianDousingMachineBlockEntityRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(RoosterModel.LAYER_LOCATION, RoosterModel::createBodyLayer);
        event.registerLayerDefinition(MegaChickenModel.LAYER_LOCATION, MegaChickenModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class,
                (container, parent) -> new ChickensConfigScreen(parent));
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(MEGA_CHICKEN_DIVE);
        event.register(MEGA_CHICKEN_AIRBRAKE);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.getConnection() == null) {
            lastJumpDown = false;
            lastDiveDown = false;
            lastAirbrakeDown = false;
            return;
        }

        boolean jumping = minecraft.options.keyJump.isDown()
                && minecraft.player.getVehicle() instanceof MegaChicken chicken
                && chicken.hasFlyingEgg();
        boolean diving = MEGA_CHICKEN_DIVE.isDown()
                && minecraft.player.getVehicle() instanceof MegaChicken chicken
                && chicken.hasFlyingEgg()
                && !chicken.onGround();
        boolean airbraking = MEGA_CHICKEN_AIRBRAKE.isDown()
                && minecraft.player.getVehicle() instanceof MegaChicken chicken
                && chicken.hasFlyingEgg()
                && !chicken.onGround();
        if (minecraft.player.getVehicle() instanceof MegaChicken chicken) {
            chicken.setJumpRequested(jumping);
            chicken.setDiveRequested(diving);
            chicken.setAirbrakeRequested(airbraking);
        }
        if (jumping != lastJumpDown || diving != lastDiveDown || airbraking != lastAirbrakeDown) {
            ClientPacketDistributor.sendToServer(new MegaChickenFlightPayload(jumping, diving, airbraking));
            lastJumpDown = jumping;
            lastDiveDown = diving;
            lastAirbrakeDown = airbraking;
        }
    }

    @SubscribeEvent
    public static void onRegisterItemTintSources(RegisterColorHandlersEvent.ItemTintSources event) {
        event.register(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "chicken"), ChickenItemTintSource.MAP_CODEC);
    }

    @SubscribeEvent
    public static void onRegisterSelectItemModelProperties(RegisterSelectItemModelPropertyEvent event) {
        event.register(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "chicken_model"), ChickenModelProperty.TYPE);
    }

    @SubscribeEvent
    public static void onRegisterItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(ModRegistry.FLUX_EGG.get(), (graphics, font, stack, x, y) -> {
            int barX = x + 2;
            int barY = y + 1;
            int barWidth = stack.getBarWidth();
            graphics.fill(barX, barY, barX + 13, barY + 2, 0xFF000000);
            graphics.fill(barX, barY, barX + barWidth, barY + 1, stack.getBarColor() | 0xFF000000);
            return false;
        });
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        // Bind the container to its screen so the henhouse GUI renders correctly on the client.
        event.register(ModMenuTypes.HENHOUSE.get(), HenhouseScreen::new);
        event.register(ModMenuTypes.ROOST.get(), RoostScreen::new);
        event.register(ModMenuTypes.MECHANICAL_ROOST.get(), MechanicalRoostScreen::new);
        event.register(ModMenuTypes.ROOST_GENERATOR.get(), RoostGeneratorScreen::new);
        event.register(ModMenuTypes.NEST.get(), NestScreen::new);
        event.register(ModMenuTypes.MECHANICAL_NEST.get(), MechanicalNestScreen::new);
        event.register(ModMenuTypes.ROOSTER.get(), RoosterScreen::new);
        event.register(ModMenuTypes.BREEDER.get(), BreederScreen::new);
        event.register(ModMenuTypes.COLLECTOR.get(), CollectorScreen::new);
        event.register(ModMenuTypes.AVIAN_FLUX_CONVERTER.get(), AvianFluxConverterScreen::new);
        event.register(ModMenuTypes.AVIAN_FLUID_CONVERTER.get(), AvianFluidConverterScreen::new);
        event.register(ModMenuTypes.AVIAN_CHEMICAL_CONVERTER.get(), AvianChemicalConverterScreen::new);
        event.register(ModMenuTypes.AVIAN_DOUSING_MACHINE.get(), AvianDousingMachineScreen::new);
        event.register(ModMenuTypes.INCUBATOR.get(), IncubatorScreen::new);
        event.register(ModMenuTypes.MEGA_CHICKEN.get(), MegaChickenScreen::new);
    }

    @SubscribeEvent
    public static void onAddReloadListeners(AddClientReloadListenersEvent event) {
        event.addListener(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "dynamic_chicken_textures"),
                DynamicChickenTextures.reloadListener());
        event.addListener(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "chicken_texture_availability"),
                ChickensChickenRenderer.textureAvailabilityReloader());
        event.addListener(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "chicken_item_models"),
                ChickenModelProperty.reloadListener());
        event.addListener(Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "liquid_chicken_overlay"),
                LiquidChickenOverlayLayer.reloadListener());
    }
}
