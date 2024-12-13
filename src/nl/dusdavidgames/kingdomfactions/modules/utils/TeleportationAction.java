package nl.dusdavidgames.kingdomfactions.modules.utils;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.action.IAction;
import org.bukkit.Location;

public class TeleportationAction implements IAction {

    private Location location;
    private int delay;
    private boolean cancelOnMove;
    private KingdomFactionsPlayer player;

    public TeleportationAction(KingdomFactionsPlayer player, Location location, boolean cancelOnMove, int delay) {
        this.delay = delay;
        this.cancelOnMove = cancelOnMove;
        this.location = location;
        this.player = player;
    }

    public Location getLocation() {
        return location;
    }

    public int getDelay() {
        return delay;
    }

    public boolean shouldCancelOnMove() {
        return cancelOnMove;
    }

    public KingdomFactionsPlayer getPlayer() {
        return player;
    }

    /**
     * Executes the teleportation action, moving the player to the destination location.
     */
    public void execute() throws KingdomFactionsException {
        getPlayer().sendActionbar(ChatColor.RED + "Teleporting...");
        getPlayer().teleport(location);
        cancel(); // Ensure that the teleport action is canceled after execution
    }

    /**
     * Checks if the player moved, and if so, cancels the teleport action.
     */
    public void cancelTeleportIfMoved() {
        if (!shouldCancelOnMove()) {
            return;
        }
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + "You moved! The teleportation has been canceled.");
        cancel();
    }

    /**
     * Updates the player with the remaining time until teleportation.
     */
    public void notifyPlayerOnDelayChange() {
        getPlayer().sendActionbar(ChatColor.RED + "Teleportation in " + this.delay + " second(s)!");
    }

    /**
     * Handles the delay countdown and executes the teleportation when time is up.
     */
    public void handleDelayChange() {
        if (delay <= 1) {
            try {
                execute();
            } catch (KingdomFactionsException e) {
                Logger.ERROR.log("Error executing teleportation for player: " + getPlayer().getName());
                e.printStackTrace();
            }
        } else {
            delay -= 1;
            notifyPlayerOnDelayChange();
        }
    }

    /**
     * Cancels the teleportation action.
     */
    private void cancel() {
        // Any cancel logic, if required, can go here.
    }
}
