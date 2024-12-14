package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

import net.md_5.bungee.api.ChatColor;

public enum MessagePrefix {
    KINGDOMFACTIONS("Kingdom Factions"),
    KDFERROR("Error", ChatColor.RED),
    DEBUG("Debug", ChatColor.GRAY);

    private final String displayName;
    private final ChatColor color;

    MessagePrefix(String displayName) {
        this(displayName, ChatColor.WHITE); // Default to white if no color is provided
    }

    MessagePrefix(String displayName, ChatColor color) {
        this.displayName = displayName;
        this.color = color;
    }

    @Override
    public String toString() {
        return color + "[" + displayName + "]" + ChatColor.RESET;
    }

    /**
     * Gets the formatted message prefix.
     * @return the formatted message prefix with color.
     */
    public String getFormattedPrefix() {
        return color + "[" + displayName + "]" + ChatColor.RESET;
    }
}
