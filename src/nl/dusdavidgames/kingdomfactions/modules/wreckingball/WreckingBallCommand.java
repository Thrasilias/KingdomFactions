package nl.dusdavidgames.kingdomfactions.modules.wreckingball;

import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class WreckingBallCommand extends KingdomFactionsCommand {

    public WreckingBallCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // No initialization required
    }

    @Override
    public void execute() throws KingdomFactionsException {
        // Check if the player has permission to use the command
        if (!getPlayer().hasPermission("kingdomfactions.command.wreckingball")) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Je hebt geen toestemming om deze command uit te voeren.");
            return;
        }

        PlayerInventory inventory = getPlayer().getInventory();

        // Check if the player already has a Wrecking Ball
        if (inventory.contains(WreckingBallModule.getInstance().getWreckingBall())) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Je hebt al een WreckingBall!");
            return;
        }

        // Give the player the Wrecking Ball item
        inventory.addItem(WreckingBallModule.getInstance().getWreckingBall());
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je hebt een WreckingBall gekregen! Dit item is alleen voor staffleden, het is niet toegestaan om dit item te delen met andere spelers.");
    }
}
