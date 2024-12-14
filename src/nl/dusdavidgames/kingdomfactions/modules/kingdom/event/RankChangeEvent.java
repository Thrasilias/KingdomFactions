package nl.dusdavidgames.kingdomfactions.modules.kingdom.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.utils.IRank;

/**
 * Event triggered when a player's rank is changed.
 */
public class RankChangeEvent extends Event {

    public static final HandlerList list = new HandlerList();

    // The player whose rank has changed
    private @Getter IPlayerBase player;

    // The new rank of the player
    private @Getter IRank rank;

    /**
     * Constructor for the RankChangeEvent.
     * 
     * @param rank The new rank of the player.
     * @param base The player whose rank has changed.
     */
    public RankChangeEvent(IRank rank, IPlayerBase base) {
        this.rank = rank;
        this.player = base;
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
