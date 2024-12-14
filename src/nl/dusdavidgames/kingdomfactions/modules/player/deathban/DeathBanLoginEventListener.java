package nl.dusdavidgames.kingdomfactions.modules.player.deathban;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class DeathBanLoginEventListener implements Listener {

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {
        // Fetch the player's death ban information
        DeathBan ban = DeathBanModule.getInstance().getBan(e.getPlayer().getUniqueId());
        
        // If no ban exists, allow the player to join
        if (ban == null) {
            return;
        }
        
        // Handle the ban
        if (ban.handleBan()) {
            // Ensure a valid ban message is provided
            String banMessage = ban.getMessage();
            if (banMessage == null || banMessage.isEmpty()) {
                banMessage = ChatColor.RED + "You are banned due to death and cannot join at the moment.";
            }
            
            // Disallow player login with the ban message
            e.disallow(Result.KICK_OTHER, banMessage);

            // Optional: Log the ban for debugging purposes
            Logger.INFO.log("Player " + e.getPlayer().getName() + " is banned due to death for " + ban.getTime() + " left.");
        }
    }
}
