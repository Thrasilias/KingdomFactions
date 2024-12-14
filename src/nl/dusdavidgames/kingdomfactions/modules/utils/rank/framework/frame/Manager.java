package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.frame;

/**
 * Represents a higher-level staff rank with managerial responsibilities.
 * This class serves as a base for all ranks categorized as "Manager."
 */
public abstract class Manager extends StaffRank {

    /**
     * Returns the name of the managerial rank.
     * This method must be implemented by all subclasses.
     *
     * @return The name of the manager rank.
     */
    @Override
    public abstract String getName();
}
