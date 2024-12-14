package nl.dusdavidgames.kingdomfactions.modules.mine.command;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class MineCommand extends KingdomFactionsCommand {

    public MineCommand(String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super("setmine", permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // Initialization logic if needed in the future.
    }

    @Override
    public void execute() {
        // Check if the player provided a kingdom name
        if (getArgs().length < 1) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Je moet een kingdom naam opgeven!");
            return;
        }

        // Get the kingdom from the argument
        Kingdom k = KingdomModule.getInstance().getKingdom(getArgs()[0]);

        if (k == null) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Ongeldig Kingdom!");
            return;
        }

        // Set the mining spawn location
        k.setMiningSpawn(getPlayer().getLocation());

        // Send confirmation message to the player
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je hebt de Mining Spawn van " + k.getType().getPrefix() + ChatColor.WHITE + " gezet!");
    }
}
