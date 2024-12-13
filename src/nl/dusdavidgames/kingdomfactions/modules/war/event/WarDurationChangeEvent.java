package nl.dusdavidgames.kingdomfactions.modules.war.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarDurationChangeEvent extends Event {
    public static final HandlerList list = new HandlerList();

    // Constructor - can be expanded in the future to pass relevant duration data
    public WarDurationChangeEvent() {
        // Additional parameters could be added here in the future (e.g., time remaining)
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
