package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum PurchaseSource {
    BUYCRAFT("Buycraft Purchase"),
    MANUAL("Manual Purchase");

    private final String displayName;

    PurchaseSource(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Check if the purchase source is from Buycraft.
     * @return true if the source is Buycraft.
     */
    public boolean isBuycraft() {
        return this == BUYCRAFT;
    }

    /**
     * Check if the purchase source is manual.
     * @return true if the source is manual.
     */
    public boolean isManual() {
        return this == MANUAL;
    }
}
