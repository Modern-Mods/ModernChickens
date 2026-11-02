package strhercules.chickens.integration.jei;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.recipe.DousingRecipe;
import strhercules.chickens.registry.ModRecipeTypes;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.Collection;
import java.util.List;

@EventBusSubscriber(modid = ChickensMod.MOD_ID, value = Dist.CLIENT)
public final class ReceivedDousingRecipes {
    private static volatile Collection<RecipeHolder<DousingRecipe>> recipes = List.of();

    private ReceivedDousingRecipes() {
    }

    public static Collection<RecipeHolder<DousingRecipe>> get() {
        return recipes;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRecipesReceived(RecipesReceivedEvent event) {
        recipes = List.copyOf(event.getRecipeMap().byType(ModRecipeTypes.AVIAN_DOUSING.get()));
    }

    @SubscribeEvent
    public static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        recipes = List.of();
    }
}
