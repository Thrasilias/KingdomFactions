package nl.dusdavidgames.kingdomfactions.modules.utils.action;

import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public interface IAction {

    // Get the player associated with the action
    KingdomFactionsPlayer getPlayer();

    // The execute method is now marked as deprecated and should be used cautiously
    @Deprecated
    void execute() throws KingdomFactionsException;

    // Default cancel method to unset the player's current action
    default void cancel() {
        KingdomFactionsPlayer player = getPlayer();
        if (player != null) {
            player.setAction(null); // Unsets the action associated with the player
        }
    }
}
