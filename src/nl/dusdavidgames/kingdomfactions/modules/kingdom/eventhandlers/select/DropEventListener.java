package nl.dusdavidgames.kingdomfactions.modules.kingdom.eventhandlers.select;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class DropEventListener implements Listener {
    
    // Maximum food level a player can have
    private static final int MAX_FOOD_LEVEL = 20;
    
    /**
     * Handles the item drop event.
     * Cancels the event if the player belongs to the kingdom 'GEEN'.
     *
     * @param e The PlayerDropItemEvent triggered when a player drops an item.
     */
    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());
        
        // Check if the player has a kingdom and it's 'GEEN'
        if (player != null && player.getKingdom() != null && player.getKingdom().getType() == KingdomType.GEEN) {
            e.setCancelled(true);  // Cancel the drop event
        }
    }
    
    /**
     * Handles the food level change event.
     * Resets the food level to the maximum if the player belongs to the kingdom 'GEEN'.
     *
     * @param e The FoodLevelChangeEvent triggered when a player's food level changes.
     */
    @EventHandler
    public void onFood(FoodLevelChangeEvent e) {
        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getEntity());
        
        if (player != null && player.getKingdom() != null) {
            // If the player belongs to the 'GEEN' kingdom, set their food level to MAX_FOOD_LEVEL
            if (player.getKingdom().getType() == KingdomType.GEEN) {
                e.setFoodLevel(MAX_FOOD_LEVEL);
            }
        }
    }
}
