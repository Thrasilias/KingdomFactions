package nl.dusdavidgames.kingdomfactions.modules.player.player.console;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;

public class CommandUser {

    private final CommandSender sender;

    public CommandUser(CommandSender sender) {
        this.sender = sender;
    }

    /**
     * Returns the formatted name of the user, prefixed with the appropriate color.
     * If the sender is the console, it returns "CONSOLE" in dark red.
     * Otherwise, it fetches the formatted name of the player.
     *
     * @return the formatted name.
     */
    public String getFormattedName() {
        if (isConsole()) {
            return ChatColor.DARK_RED + "CONSOLE";
        }
        return PlayerModule.getInstance().getPlayer(sender).getFormattedName();
    }

    /**
     * Checks if the sender is the console.
     *
     * @return true if sender is console, false otherwise.
     */
    public boolean isConsole() {
        return sender instanceof ConsoleCommandSender;
    }

    /**
     * Gets the CommandSender object associated with this CommandUser.
     *
     * @return the CommandSender.
     */
    public CommandSender getSender() {
        return sender;
    }

    /**
     * Sends a message to the sender.
     *
     * @param message the message to send.
     */
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}
