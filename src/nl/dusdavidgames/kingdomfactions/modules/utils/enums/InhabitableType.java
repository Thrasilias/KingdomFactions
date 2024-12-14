package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum InhabitableType {
    FACTION("Faction"),
    KINGDOM("Kingdom");

    private final String displayName;

    InhabitableType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Checks if the type is FACTION.
     * @return true if the type is FACTION.
     */
    public boolean isFaction() {
        return this == FACTION;
    }

    /**
     * Checks if the type is KINGDOM.
     * @return true if the type is KINGDOM.
     */
    public boolean isKingdom() {
        return this == KINGDOM;
    }
}
