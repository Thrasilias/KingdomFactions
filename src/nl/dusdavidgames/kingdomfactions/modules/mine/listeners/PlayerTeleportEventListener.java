package nl.dusdavidgames.kingdomfactions.modules.mine.listeners;

import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.mine.DelayedPlayerTeleportEvent;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Listens for teleportation events and triggers a delayed custom teleport event.
 */
public class PlayerTeleportEventListener implements Listener {

    // Constant for the teleport delay in ticks (40 ticks = 2 seconds)
    private static final long TELEPORT_DELAY_TICKS = 40L;

    @EventHandler
    public void onTeleport(final PlayerTeleportEvent e) {
        // Delaying the teleport event processing by 2 seconds
        Bukkit.getScheduler().runTaskLater(KingdomFactionsPlugin.getInstance(), () -> {
            // Debug log to indicate the event is being handled
            Logger.DEBUG.log("Calling TeleportEvent");
            
            // Triggering the custom delayed teleport event
            callDelayedTeleportEvent(e);
        }, TELEPORT_DELAY_TICKS);
    }

    /**
     * Calls the custom delayed teleport event.
     * 
     * @param originalEvent The original PlayerTeleportEvent that was triggered.
     */
    private void callDelayedTeleportEvent(PlayerTeleportEvent originalEvent) {
        Bukkit.getPluginManager().callEvent(new DelayedPlayerTeleportEvent(originalEvent));
    }
}
