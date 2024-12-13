package nl.dusdavidgames.kingdomfactions.modules.viewdistance.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.viewdistance.ViewDistanceModule;

public class PlayerJoinEvent implements Listener {
    
    @EventHandler
    public void onPlayerJoinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        // Retrieve the player who joined the server
        PlayerModule playerModule = PlayerModule.getInstance();
        KingdomFactionsPlayer player = playerModule.getPlayer(event.getPlayer());
        
        // Delegate view distance handling to ViewDistanceModule
        ViewDistanceModule.getInstance().setViewDistance(player);
    }
}
