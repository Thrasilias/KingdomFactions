package nl.dusdavidgames.kingdomfactions.modules.mine;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class MineTravelEvent extends Event {

    // Static handler list for Spigot event system
    public static final HandlerList list = new HandlerList();

    @Getter
    private final KingdomFactionsPlayer player;

    /**
     * Constructor for creating a MineTravelEvent.
     *
     * @param player The player who is traveling to the mine world.
     */
    public MineTravelEvent(KingdomFactionsPlayer player) {
        this.player = player;
    }

    /**
     * Returns the list of handlers for this event.
     *
     * @return The HandlerList for this event.
     */
    @Override
    public HandlerList getHandlers() {
        return list;
    }

    /**
     * Static method to get the HandlerList for this event.
     *
     * @return The HandlerList for this event.
     */
    public static HandlerList getHandlerList() {
        return list;
    }
}
