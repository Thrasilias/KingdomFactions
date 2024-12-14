package nl.dusdavidgames.kingdomfactions.modules.kingdom.eventhandlers.capital;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.SetCapitalAction;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class InteractEventListener implements Listener {

    /**
     * Handles the event when a player interacts with a block.
     * Specifically, checks if a player interacts with a COAL_BLOCK
     * to set the capital of a kingdom.
     *
     * @param e The PlayerInteractEvent triggered by the player.
     */
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        // Ensure the player clicked on a block and it's a COAL_BLOCK
        if (e.getClickedBlock() == null || e.getClickedBlock().getType() != Material.COAL_BLOCK) {
            return;
        }

        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());

        // Check if the player has an action in progress and it's a SetCapitalAction
        if (player.hasAction() && player.getAction() instanceof SetCapitalAction) {
            e.setCancelled(true);  // Cancel the event to prevent unintended behavior

            SetCapitalAction action = (SetCapitalAction) player.getAction();

            // Set the location for the capital and execute the action
            action.setLocation(e.getClickedBlock().getLocation());
            action.execute();

            // Send confirmation message to the player
            player.sendMessage(Messages.getInstance().getPrefix() + 
                               "Je hebt de HoofdStad Nexus succesvol gezet op: " + 
                               e.getClickedBlock().getLocation());
        }
    }
}
