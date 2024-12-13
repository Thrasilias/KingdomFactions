package nl.dusdavidgames.kingdomfactions.modules.war.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarStopEvent extends Event {
    public static final HandlerList list = new HandlerList();

    // Constructor - could be extended to carry additional data in the future
    public WarStopEvent() {
        // You can add data parameters in the future, if required.
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
