package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.frame;

/**
 * Represents a leadership rank within the staff hierarchy.
 * This class serves as a base for leadership roles such as team leaders
 * or higher-tiered staff ranks with additional permissions.
 */
public abstract class Lead extends StaffRank {

    /**
     * Indicates if the lead rank has decision-making authority.
     * Subclasses can override this to provide specific behavior.
     *
     * @return True if the rank has decision-making authority, false by default.
     */
    public boolean hasDecisionAuthority() {
        return true; // Default assumption for leadership ranks.
    }

    /**
     * Returns the priority level of the leadership rank.
     * Subclasses can implement this to define rank hierarchy.
     *
     * @return The priority level of the lead rank, where a higher value indicates greater authority.
     */
    public abstract int getPriorityLevel();
}
