package nl.dusdavidgames.kingdomfactions.modules.player;

import java.util.ArrayList;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class PlayerList extends ArrayList<KingdomFactionsPlayer> {

    private static final long serialVersionUID = -4017062724445242490L;

    /**
     * Broadcast a message to all players in the list.
     * @param message The message to send.
     */
    public void broadcast(String message) {
        // Null check for message to prevent sending null messages
        if (message == null || message.isEmpty()) {
            return; // Do nothing if the message is null or empty
        }

        // Using enhanced for loop for better readability and avoiding any possible NullPointerException
        for (KingdomFactionsPlayer player : this) {
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Find a player by their name.
     * @param name The name of the player.
     * @return The player if found, null otherwise.
     */
    public KingdomFactionsPlayer findPlayerByName(String name) {
        for (KingdomFactionsPlayer player : this) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null; // Return null if no player was found with the given name
    }

    /**
     * Remove a player by their unique ID.
     * @param uuid The UUID of the player to remove.
     * @return True if the player was removed, false otherwise.
     */
    public boolean removePlayerByUuid(UUID uuid) {
        for (KingdomFactionsPlayer player : this) {
            if (player.getUuid().equals(uuid)) {
                return this.remove(player);
            }
        }
        return false; // Return false if no player was found with the given UUID
    }
}
