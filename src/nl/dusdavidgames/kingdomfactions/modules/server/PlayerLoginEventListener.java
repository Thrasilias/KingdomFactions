package nl.dusdavidgames.kingdomfactions.modules.server;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class PlayerLoginEventListener implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        KingdomFactionsPlayer p = PlayerModule.getInstance().getPlayer(e.getPlayer());
        
        // Ensure that the player is valid before checking permissions
        if (p == null) {
            e.disallow(Result.KICK_OTHER, "An error occurred while processing your login.");
            return;
        }

        // Check if the player has the necessary permissions for the server mode
        if (!p.hasPermission(ServerModule.getInstance().getServerMode().getPermission())) {
            String message = ServerModule.getInstance().getServerMode().getMessage();
            // Ensure color codes are properly formatted
            message = ChatColor.translateAlternateColorCodes('&', message);
            e.disallow(Result.KICK_OTHER, message);
        }
    }
}
