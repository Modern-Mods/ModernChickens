package strhercules.chickens.integration.jade;

import snownee.jade.api.JadeIds;
import snownee.jade.api.callback.JadeTooltipCollectedCallback;
import snownee.jade.api.ui.BoxElement;

/**
 * Strips Jade's built-in universal bars after all providers have run so the
 * custom Chickens HUD bars remain the sole fluid/energy display.
 */
final class JadeOverlaySanitiser implements JadeTooltipCollectedCallback {
    static final JadeOverlaySanitiser INSTANCE = new JadeOverlaySanitiser();

    private JadeOverlaySanitiser() {
    }

    @Override
    public void onTooltipCollected(BoxElement rootElement, snownee.jade.api.Accessor<?> accessor) {
        // Remove universal FE/energy lines
        purge(rootElement, JadeIds.UNIVERSAL_ENERGY_STORAGE);
        purge(rootElement, JadeIds.UNIVERSAL_ENERGY_STORAGE_DEFAULT);
        purge(rootElement, JadeIds.UNIVERSAL_ENERGY_STORAGE_DETAILED);
        // Remove universal fluid lines
        purge(rootElement, JadeIds.UNIVERSAL_FLUID_STORAGE);
        purge(rootElement, JadeIds.UNIVERSAL_FLUID_STORAGE_DEFAULT);
        purge(rootElement, JadeIds.UNIVERSAL_FLUID_STORAGE_DETAILED);
    }

    private static void purge(BoxElement root, net.minecraft.resources.Identifier tag) {
        root.getTooltip().remove(tag);
    }
}
