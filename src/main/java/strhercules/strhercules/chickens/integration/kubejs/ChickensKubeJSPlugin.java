package strhercules.chickens.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import strhercules.chickens.registry.ModRecipeTypes;

public class ChickensKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ChickensKubeEvents.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("variant", KubeVariantBinding.INSTANCE);
        registry.add("ChickenVariant", KubeVariantBinding.INSTANCE);
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.register(ModRecipeTypes.AVIAN_DOUSING.getId(), DousingRecipeSchema.SCHEMA);
    }
}
