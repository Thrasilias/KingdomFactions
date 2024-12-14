package nl.dusdavidgames.kingdomfactions.modules.pvplog;

import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.CombatTracker;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class LogoutCommand extends KingdomFactionsCommand {

    public LogoutCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // Optional initialization logic can go here if needed
    }

    @Override
    public void execute() throws KingdomFactionsException {
        CombatTracker tracker = getPlayer().getCombatTracker();

        if (!tracker.isInCombat()) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je bent momenteel niet in een gevecht! Je kan zelf uitloggen!");
            return;
        }

        if (getPlayer().hasAction() && getPlayer().getAction() instanceof LogoutAction) {
            getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je bent momenteel al aan het uitloggen!");
            return;
        }

        // Inform the player about the 20-second logout timer
        getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Over 20 seconden, zullen wij veilig de verbinding verbreken. Let op! Blijf stil staan! anders stopt het proces!");
        
        // Set the logout action for the player
        getPlayer().setAction(new LogoutAction(getPlayer()));
    }
}
