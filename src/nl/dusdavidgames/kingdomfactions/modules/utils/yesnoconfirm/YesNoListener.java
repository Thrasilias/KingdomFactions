package nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class YesNoActionHandler implements YesNoListener {

    @Override
    public void onAgree(KingdomFactionsPlayer player) {
        // Handle player agreeing to the action
        player.sendMessage("You have agreed to the action!");
        // Example: Perform an action like executing a command or making a change
        // performAction(player);
    }

    @Override
    public void onDeny(KingdomFactionsPlayer player) {
        // Handle player denying the action
        player.sendMessage("You have denied the action.");
        // Example: Maybe cancel the action or show a message
        // cancelAction(player);
    }

    @Override
    public void onClose(KingdomFactionsPlayer player) {
        // Handle the situation when the confirmation prompt is closed
        player.sendMessage("The confirmation prompt was closed.");
        // Optionally, take any necessary action when the player closes the prompt without responding
    }
}
