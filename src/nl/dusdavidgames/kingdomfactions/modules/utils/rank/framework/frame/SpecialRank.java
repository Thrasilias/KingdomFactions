package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.frame;

/**
 * Represents a special rank derived from donator ranks.
 * This class is intended for unique or elevated donator roles
 * with additional properties or permissions.
 */
public abstract class SpecialRank extends Donator {

    /**
     * Returns the name of the special rank.
     * This method must be implemented by all subclasses.
     *
     * @return The name of the special rank.
     */
    @Override
    public abstract String getName();

    /**
     * Indicates whether the special rank has any exclusive benefits.
     * Subclasses can override this to specify unique behavior.
     *
     * @return True if the special rank has exclusive benefits, false otherwise.
     */
    public boolean hasExclusiveBenefits() {
        return true; // Default assumption for special ranks.
    }
}
