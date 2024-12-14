package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.ranks;

import nl.dusdavidgames.kingdomfactions.modules.utils.rank.Rank;

/**
 * Represents the default player rank in the system.
 */
public class Speler extends Rank {

    /**
     * Returns the name of this rank.
     *
     * @return The name "Speler".
     */
    @Override
    public String getName() {
        return "Speler";
    }

    /**
     * Indicates whether this rank is a staff rank.
     *
     * @return False, as "Speler" is not a staff rank.
     */
    @Override
    public boolean isStaff() {
        return false;
    }
}
