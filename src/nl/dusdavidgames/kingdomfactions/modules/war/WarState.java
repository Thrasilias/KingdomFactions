package nl.dusdavidgames.kingdomfactions.modules.war;

/**
 * Enum representing the different states of a war.
 * - NOWAR: Indicates no war is currently ongoing.
 * - WAR: Indicates that a war is currently active.
 */
public enum WarState {

    /**
     * Represents the state where no war is ongoing.
     */
    NOWAR,

    /**
     * Represents the state where a war is ongoing.
     */
    WAR;

    /**
     * Returns a user-friendly string representation of the war state.
     *
     * @return A string representing the current war state.
     */
    @Override
    public String toString() {
        switch (this) {
            case NOWAR:
                return "No War";
            case WAR:
                return "War Active";
            default:
                return super.toString();
        }
    }
}
