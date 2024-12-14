package nl.dusdavidgames.kingdomfactions.modules.kingdom.eventhandlers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.event.KingdomSwitchEvent;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class KingdomSwitchListener implements Listener {

    /**
     * Handles the KingdomSwitchEvent by updating the player's scoreboard
     * and refreshing the tags for all players.
     *
     * @param e The KingdomSwitchEvent triggered when a player switches kingdoms.
     */
    @EventHandler
    public void onSwitch(KingdomSwitchEvent e) {
        KingdomFactionsPlayer player = e.getPlayer();
        
        // Clear the player's current chat channels
        player.getChatProfile().wipeChannels();

        // Update the player's scoreboard line based on their kingdom and faction
        updateScoreboard(player);

        // Refresh the scoreboard tags for all players
        refreshPlayerTags();
    }

    /**
     * Updates the player's scoreboard line with their kingdom and faction info.
     * 
     * @param player The player whose scoreboard line needs to be updated.
     */
    private void updateScoreboard(KingdomFactionsPlayer player) {
        Faction faction = player.getFaction();
        String factionName = (faction != null) ? faction.getName() : "Geen Faction!";
        
        // Ensure the player's kingdom is not null before accessing it
        String kingdomName = (player.getKingdom() != null) ? player.getKingdom().getType().getColor() + player.getKingdom().getType().name() : ChatColor.GRAY + "No Kingdom";

        // Set the scoreboard line for the player
        player.getScoreboard().editLine(8, ChatColor.GRAY + "[" + kingdomName + " " + factionName + ChatColor.GRAY + "]");
    }

    /**
     * Refreshes the scoreboard tags for all players in the system.
     */
    private void refreshPlayerTags() {
        for (KingdomFactionsPlayer t : PlayerModule.getInstance().getPlayers()) {
            t.getScoreboard().refreshTags();
        }
    }
}
