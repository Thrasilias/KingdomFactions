package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.frame;

import nl.dusdavidgames.kingdomfactions.modules.utils.rank.Rank;

/**
 * Represents a donator rank within the system.
 * Donator ranks are special ranks granted to players who support the server
 * through donations, offering perks without staff responsibilities.
 */
public abstract class Donator extends Rank {

    /**
     * Indicates whether the rank is a staff role.
     * Donator ranks are not staff by default.
     *
     * @return False, as donator ranks do not have staff responsibilities.
     */
    @Override
    public boolean isStaff() {
        return false;
    }

    /**
     * Provides the level of support associated with the donator rank.
     * Subclasses should override this method to specify their unique donator level.
     *
     * @return A string representing the donator level.
     */
    public abstract String getSupportLevel();

    /**
     * Checks if the donator rank comes with exclusive perks.
     *
     * @return True if the rank has exclusive perks, false by default.
     */
    public boolean hasExclusivePerks() {
        return true; // Default assumption for donator ranks.
    }
}
