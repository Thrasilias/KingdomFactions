package nl.dusdavidgames.kingdomfactions.modules.permission;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

import java.util.ArrayList;

public class StaffList extends ArrayList<KingdomFactionsPlayer> {

    private static final long serialVersionUID = 7549445362771048417L;

    /**
     * Broadcast a message to all staff members in the list.
     * @param message The message to broadcast.
     */
    public void broadcast(String message) {
        for (KingdomFactionsPlayer player : this) {
            player.sendMessage(message);
        }
    }
}
