package nl.dusdavidgames.kingdomfactions.modules.kingdom.command;

import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class SpawnCommand extends KingdomFactionsCommand {

    public SpawnCommand(String name, String permission, String info, String usage) {
        super(name, permission, info, usage, false, false);
    }

    @Override
    public void init() {
        // Initialization logic if needed
    }

    @Override
    public void execute() throws KingdomFactionsException {
        if (getPlayer().getKingdom() != null) {
            if (getPlayer().getKingdom().getType() != KingdomType.GEEN) {
                // If the player has a kingdom and it's not the 'GEEN' type
                if (getPlayer().getKingdom().getSpawn() != null) {
                    // Teleport the player to their kingdom's spawn
                    getPlayer().teleport(getPlayer().getKingdom().getSpawn());
                    sendMessage("Je bent geteleporteerd naar de spawn van je Kingdom!");
                } else {
                    // The kingdom does not have a spawn set
                    sendMessage("Dit Kingdom heeft geen spawn ingesteld!");
                }
            } else {
                // If the kingdom is of type 'GEEN'
                sendMessage("Je hebt geen geldig Kingdom om naar te teleporteren!");
            }
        } else {
            // If the player does not belong to any kingdom
            sendMessage("Je behoort tot geen Kingdom!");
        }
    }

    private void sendMessage(String message) {
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + message);
    }
}
