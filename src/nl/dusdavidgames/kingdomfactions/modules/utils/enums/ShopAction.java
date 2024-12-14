package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum ShopAction {
    SELL("Sell Item"),
    PURCHASE("Purchase Item");

    private final String displayName;

    ShopAction(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Check if the shop action is a SELL action.
     * @return true if the action is SELL.
     */
    public boolean isSell() {
        return this == SELL;
    }

    /**
     * Check if the shop action is a PURCHASE action.
     * @return true if the action is PURCHASE.
     */
    public boolean isPurchase() {
        return this == PURCHASE;
    }
}
