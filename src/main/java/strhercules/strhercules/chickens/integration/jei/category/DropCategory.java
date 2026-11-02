package strhercules.chickens.integration.jei.category;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.integration.jei.ChickensJeiRecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * Displays the passive drops produced by each chicken, keeping the same layout
 * as the original mod to preserve the nostalgic UI.
 */
public class DropCategory implements IRecipeCategory<ChickensJeiRecipeTypes.DropRecipe> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "textures/gui/drops.png");
    private static final Identifier ICON_TEXTURE = Identifier.fromNamespaceAndPath(ChickensMod.MOD_ID, "textures/gui/drops_icon.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public DropCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 0, 0, 82, 54);
        this.icon = guiHelper.createDrawable(ICON_TEXTURE, 0, 0, 16, 16);
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 82, 0, 13, 10);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public RecipeType<ChickensJeiRecipeTypes.DropRecipe> getRecipeType() {
        return ChickensJeiRecipeTypes.DROPS;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.drops");
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChickensJeiRecipeTypes.DropRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 13, 15)
                .addItemStack(recipe.chicken());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 15)
                .addItemStack(recipe.drop());
    }

    @Override
    public void draw(ChickensJeiRecipeTypes.DropRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor graphics,
            double mouseX, double mouseY) {
        background.draw(graphics);
        arrow.draw(graphics, 40, 21);
    }
}
