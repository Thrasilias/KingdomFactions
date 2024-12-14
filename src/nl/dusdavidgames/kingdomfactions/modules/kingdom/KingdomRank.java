package nl.dusdavidgames.kingdomfactions.modules.kingdom;

import org.bukkit.ChatColor;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.utils.IRank;

public enum KingdomRank implements IRank {

    SPELER(""), // Default rank with no prefix
    WACHTER(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Wachter" + ChatColor.GRAY + "] "), // Guard rank with prefix
    KONING(ChatColor.GRAY + "[" + ChatColor.GOLD + "Koning" + ChatColor.GRAY + "] "); // King rank with prefix

    private @Getter String prefix;

    KingdomRank(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get the string representation of a rank.
     * @param rank The KingdomRank enum value.
     * @return The lowercase string representation of the rank.
     */
    public static String getRank(KingdomRank rank) {
        if (rank == null) {
            return SPELER.toString().toLowerCase(); // Default to "SPELER" if rank is null
        }
        return rank.toString().toLowerCase();
    }

    /**
     * Get the corresponding KingdomRank from a string.
     * @param rank The string representing the rank.
     * @return The matching KingdomRank enum, or the default rank "SPELER" if no match is found.
     */
    public static KingdomRank getRank(String rank) {
        if (rank == null || rank.isEmpty()) {
            return SPELER; // Return default rank if the string is null or empty
        }
        for (KingdomRank k : KingdomRank.values()) {
            if (k.toString().equalsIgnoreCase(rank)) {
                return k; // Return the matched rank
            }
        }
        return SPELER; // Return default rank if no match found
    }
}
