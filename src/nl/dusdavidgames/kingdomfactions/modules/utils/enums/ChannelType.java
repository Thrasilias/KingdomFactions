package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum ChannelType {
    /**
     * Should only be used for Radius and Kingdom related channels
     */
    KINGDOM,
    
    /**
     * Should only be used for Faction related channels
     */
    FACTION,
    
    /**
     * Should only be used for DDG Staff
     */
    ANY,
    
    /**
     * Should only be used for Custom channels
     */
    CUSTOM;

    // Optionally, override the toString() method to provide custom string output
    @Override
    public String toString() {
        switch (this) {
            case KINGDOM:
                return "Kingdom Channel";
            case FACTION:
                return "Faction Channel";
            case ANY:
                return "Any Channel (DDG Staff)";
            case CUSTOM:
                return "Custom Channel";
            default:
                return super.toString();
        }
    }

    // Add additional utility methods if needed
    public boolean isCustom() {
        return this == CUSTOM;
    }

    public boolean isFactionOrKingdom() {
        return this == FACTION || this == KINGDOM;
    }
}
