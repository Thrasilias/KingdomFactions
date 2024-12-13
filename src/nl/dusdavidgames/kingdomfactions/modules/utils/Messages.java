package nl.dusdavidgames.kingdomfactions.modules.utils;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;

public class Messages {

    private static @Getter @Setter Messages instance;

    // Prefix for the plugin
    private @Getter String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "KingdomFactions" + ChatColor.GRAY + "] " + ChatColor.WHITE;

    // Common message templates
    private @Getter String noPerm = getPrefix() + ChatColor.RED + "Je hebt onvoldoende rechten voor deze actie!";
    private @Getter String warning = ChatColor.DARK_RED + "KINGDOMFACTIONS" + ChatColor.RED + "" + ChatColor.BOLD + ">> " + ChatColor.RED;

    // Additional messages
    private @Getter String success = getPrefix() + ChatColor.GREEN + "De actie is succesvol uitgevoerd!";
    private @Getter String error = getPrefix() + ChatColor.RED + "Er is een fout opgetreden tijdens de actie.";
    private @Getter String info = getPrefix() + ChatColor.YELLOW + "Informatie: ";

    // Singleton initialization
    public Messages() {
        setInstance(this);
    }

    // Method to get the instance (singleton pattern)
    public static Messages getInstance() {
        return instance;
    }

    // Example method to send a formatted message to a player (or log)
    public String formatMessage(String message, ChatColor color) {
        return getPrefix() + color + message;
    }

    // Example of a dynamic message method
    public String formatWithPlayer(String message, String playerName) {
        return getPrefix() + ChatColor.WHITE + message + " " + ChatColor.GRAY + "(" + playerName + ")";
    }
}
