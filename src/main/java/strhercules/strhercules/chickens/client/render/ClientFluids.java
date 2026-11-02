package strhercules.chickens.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

public final class ClientFluids {
    private ClientFluids() {
    }

    public static TextureAtlasSprite blockSprite(Identifier texture) {
        return blockAtlas().getSprite(texture);
    }

    public static TextureAtlasSprite stillSprite(Fluid fluid) {
        return model(fluid).stillMaterial().sprite();
    }

    public static int tint(FluidStack stack) {
        FluidTintSource source = model(stack.getFluid()).fluidTintSource();
        return source == null ? 0xFFFFFF : source.colorAsStack(stack) & 0xFFFFFF;
    }

    private static FluidModel model(Fluid fluid) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
    }

    private static TextureAtlas blockAtlas() {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.BLOCKS);
    }
}
