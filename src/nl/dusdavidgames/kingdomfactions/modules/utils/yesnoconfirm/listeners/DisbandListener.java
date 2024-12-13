package nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.listeners;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.YesNoListener;

public class DisbandListener implements YesNoListener {

    @Override
    public void onAgree(KingdomFactionsPlayer player) {
        // Ensure that the player is in a faction before proceeding
        if (player.getFaction() != null) {
            // Broadcast the disband message to all faction members
            player.getFaction().broadcast(Messages.getInstance().getPrefix(), player.getName() + " heeft de faction verwijderd!");
            
            // Disband the faction
            player.getFaction().remove();
        } else {
            // Handle case when player doesn't belong to a faction
            player.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je bent niet in een faction.");
        }
    }

    @Override
    public void onDeny(KingdomFactionsPlayer player) {
        // Close the confirmation inventory when the player denies
        player.getPlayer().closeInventory();
    }

    @Override
    public void onClose(KingdomFactionsPlayer player) {
        // You can add any behavior when the inventory is closed without a choice, if necessary
        // For example, notifying the player that they cancelled the action
        player.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je hebt de disband-bevestiging geannuleerd.");
    }
}
