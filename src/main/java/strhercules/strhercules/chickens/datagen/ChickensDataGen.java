package strhercules.chickens.datagen;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.datagen.providers.ChickensBiomeModifierProvider;
import strhercules.chickens.datagen.providers.ChickensItemTagProvider;
import strhercules.chickens.datagen.providers.ChickensLootTableProvider;
import strhercules.chickens.datagen.providers.ChickensRecipeProvider;

@EventBusSubscriber(modid = ChickensMod.MOD_ID)
public class ChickensDataGen {

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider(ChickensRecipeProvider.Runner::new);
        event.createProvider(ChickensLootTableProvider::new);
        event.createProvider(ChickensItemTagProvider::new);
        event.createProvider(ChickensBiomeModifierProvider::new);
    }
}
