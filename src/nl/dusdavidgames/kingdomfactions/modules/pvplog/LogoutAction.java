package nl.dusdavidgames.kingdomfactions.modules.pvplog;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.action.IAction;

public class LogoutAction implements IAction {

    private final KingdomFactionsPlayer player;
    @Getter private int logoutSeconds;

    public LogoutAction(KingdomFactionsPlayer player) {
        this.player = player;
        this.logoutSeconds = 10; // Initial logout countdown
    }

    @Override
    public KingdomFactionsPlayer getPlayer() {
        return player;
    }

    @Override
    public void execute() throws KingdomFactionsException {
        // Safely disconnect the player from combat and other state-related actions
        if (mayLogOut()) {
            // Disconnect the player from combat tracker
            getPlayer().getCombatTracker().disconnect();

            // Optionally add logic to notify player or broadcast the logout
            getPlayer().sendMessage("Je bent succesvol uit het gevecht gegaan!");
        } else {
            throw new KingdomFactionsException("You cannot log out yet. Please wait.");
        }
    }

    /**
     * Checks if the player is allowed to log out based on the countdown.
     * @return true if the player may log out (seconds <= 1), false otherwise
     */
    public boolean mayLogOut() {
        return logoutSeconds <= 1; // Player can log out if the countdown is 1 second or less
    }

    /**
     * Decreases the logout countdown by 1 second.
     */
    public void decreaseLogoutSeconds() {
        if (logoutSeconds > 0) {
            logoutSeconds--; // Decrease the countdown each time it's called
        }
    }
}
