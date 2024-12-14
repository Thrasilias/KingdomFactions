package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework;

import nl.dusdavidgames.kingdomfactions.modules.utils.rank.Rank;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages the registration and retrieval of ranks within the system.
 */
public class RankManager {

    // A map to store ranks by their names
    private final Map<String, Rank> ranks = new HashMap<>();

    /**
     * Registers a new rank into the system.
     *
     * @param rank The rank to register.
     * @throws IllegalArgumentException if a rank with the same name already exists.
     */
    public void registerRank(Rank rank) {
        if (ranks.containsKey(rank.getName().toLowerCase())) {
            throw new IllegalArgumentException("A rank with the name \"" + rank.getName() + "\" already exists.");
        }
        ranks.put(rank.getName().toLowerCase(), rank);
    }

    /**
     * Retrieves a rank by its name.
     *
     * @param name The name of the rank.
     * @return The rank associated with the given name, or null if no rank exists.
     */
    public Rank getRank(String name) {
        return ranks.get(name.toLowerCase());
    }

    /**
     * Checks if a rank exists by its name.
     *
     * @param name The name of the rank to check.
     * @return True if the rank exists, false otherwise.
     */
    public boolean rankExists(String name) {
        return ranks.containsKey(name.toLowerCase());
    }

    /**
     * Removes a rank from the system.
     *
     * @param name The name of the rank to remove.
     * @throws IllegalArgumentException if the rank does not exist.
     */
    public void removeRank(String name) {
        if (!ranks.containsKey(name.toLowerCase())) {
            throw new IllegalArgumentException("No rank with the name \"" + name + "\" exists.");
        }
        ranks.remove(name.toLowerCase());
    }

    /**
     * Lists all registered ranks.
     *
     * @return A map of rank names to their associated Rank objects.
     */
    public Map<String, Rank> getAllRanks() {
        return new HashMap<>(ranks);
    }
}
