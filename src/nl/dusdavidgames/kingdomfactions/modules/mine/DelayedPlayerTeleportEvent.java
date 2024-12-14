package nl.dusdavidgames.kingdomfactions.modules.mine;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * Event that is triggered when a player is teleported, with additional information
 * to handle delayed teleportation or other custom behaviors.
 */
public class DelayedPlayerTeleportEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    // The player involved in the teleport event
    private @Getter final KingdomFactionsPlayer player;
    
    // The location the player is teleporting from
    private @Getter final Location from;
    
    // The location the player is teleporting to
    private @Getter final Location to;
    
    // The cause of the teleport (e.g., command, plugin, etc.)
    private @Getter final PlayerTeleportEvent.TeleportCause cause;

    /**
     * Constructor to create the event with specific player, from/to locations and cause.
     *
     * @param player The player involved in the teleport.
     * @param from The original location before teleportation.
     * @param to The target location for the teleport.
     * @param cause The cause of the teleportation.
     */
    public DelayedPlayerTeleportEvent(Player player, Location from, Location to, PlayerTeleportEvent.TeleportCause cause) {
        this.player = PlayerModule.getInstance().getPlayer(player); // Fetches the KingdomFactionsPlayer
        this.from = from;
        this.to = to;
        this.cause = cause;
    }

    /**
     * Constructor that creates the event using a standard PlayerTeleportEvent.
     *
     * @param event The original PlayerTeleportEvent to extract details from.
     */
    public DelayedPlayerTeleportEvent(PlayerTeleportEvent event) {
        this(event.getPlayer(), event.getFrom(), event.getTo(), event.getCause());
    }

    /**
     * Gets the handler list for this event type.
     *
     * @return The HandlerList instance.
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * Gets the static handler list for this event type.
     *
     * @return The static HandlerList instance.
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
