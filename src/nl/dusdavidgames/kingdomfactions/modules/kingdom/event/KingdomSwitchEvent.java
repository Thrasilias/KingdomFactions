package nl.dusdavidgames.kingdomfactions.modules.kingdom.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

/**
 * Event triggered when a player switches kingdoms.
 */
public class KingdomSwitchEvent extends Event {
    
    public static final HandlerList list = new HandlerList();
    
    // The player who switched kingdoms
    private @Getter KingdomFactionsPlayer player;

    /**
     * Constructor for the KingdomSwitchEvent.
     * 
     * @param p The player who is switching kingdoms.
     */
    public KingdomSwitchEvent(KingdomFactionsPlayer p) {
        this.player = p;
    }

    /**
     * Gets the handler list for the event.
     * 
     * @return The handler list.
     */
    @Override
    public HandlerList getHandlers() {
        return list;
    }

    /**
     * Gets the static handler list for this event type.
     * 
     * @return The static handler list.
     */
    public static HandlerList getHandlerList() {
        return list;
    }
}
