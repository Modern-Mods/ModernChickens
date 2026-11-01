package strhercules.chickens.integration.kubejs;

/** Script global that starts a {@link KubeVariantBuilder}, exposed to KubeJS as {@code variant}. */
public final class KubeVariantBinding {
    public static final KubeVariantBinding INSTANCE = new KubeVariantBinding();

    private KubeVariantBinding() {
    }

    public KubeVariantBuilder kind(String kind) {
        return new KubeVariantBuilder().kind(kind);
    }

    public KubeVariantBuilder chickenid(String chickenId) {
        return new KubeVariantBuilder().chickenid(chickenId);
    }
}
