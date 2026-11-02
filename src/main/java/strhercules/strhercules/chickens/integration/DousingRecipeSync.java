package strhercules.chickens.integration;

import strhercules.chickens.ChickensMod;
import strhercules.chickens.registry.ModRecipeTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = ChickensMod.MOD_ID)
public final class DousingRecipeSync {
    private DousingRecipeSync() {
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        event.sendRecipes(ModRecipeTypes.AVIAN_DOUSING.get());
    }
}
