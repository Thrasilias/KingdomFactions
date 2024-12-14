package nl.dusdavidgames.kingdomfactions.modules.influence.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.StatisticsProfile;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.ValueEditAction;

/**
 * This event is triggered when a player's influence value is modified.
 */
public class InfluenceEditEvent extends Event {

    // List of handlers for this event
    public static final HandlerList list = new HandlerList();

    // The profile containing the player's statistics
    private @Getter StatisticsProfile profile;

    // The player associated with this profile
    private @Getter KingdomFactionsPlayer player;

    // The action that was performed on the player's influence
    private @Getter ValueEditAction action;

    /**
     * Constructor to create the event with a player's statistics profile and the action performed.
     * @param profile The statistics profile of the player.
     * @param action The action that was performed on the influence value.
     */
    public InfluenceEditEvent(StatisticsProfile profile, ValueEditAction action) {
        this.profile = profile;
        this.player = profile.getPlayer();
        this.action = action;
    }

    @Override
    public HandlerList getHandlers() {
        return list;
    }

    public static HandlerList getHandlerList() {
        return list;
    }

    @Override
    public String toString() {
        return "InfluenceEditEvent{player=" + player.getName() + ", action=" + action + "}";
    }
}
