package nl.dusdavidgames.kingdomfactions.modules.war.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WarStartEvent extends Event {
    public static final HandlerList list = new HandlerList();

    // Constructor - can be extended to carry additional data if needed in the future
    public WarStartEvent() {
        // You can add data parameters in the future, such as the kingdoms involved
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }
}
