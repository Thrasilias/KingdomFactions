package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum NexusType {
    FACTION("Faction Nexus"),
    CAPITAL("Capital Nexus");

    private final String displayName;

    NexusType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Check if the NexusType is FACTION.
     * @return true if the type is FACTION.
     */
    public boolean isFaction() {
        return this == FACTION;
    }

    /**
     * Check if the NexusType is CAPITAL.
     * @return true if the type is CAPITAL.
     */
    public boolean isCapital() {
        return this == CAPITAL;
    }
}
