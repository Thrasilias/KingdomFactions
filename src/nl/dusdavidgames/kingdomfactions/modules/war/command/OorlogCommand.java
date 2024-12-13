package nl.dusdavidgames.kingdomfactions.modules.war.command;

import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;

public class OorlogCommand extends KingdomFactionsCommand {

    public OorlogCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        this.registerSub(new SubCommand("info", "kingdomfactions.command.oorlog.info", "Verkrijg informatie over de oorlog!") {
            @Override
            public void execute(String[] args) {
                if (!WarModule.getInstance().isWarActive()) {
                    getPlayer().sendMessage("Er is momenteel geen oorlog actief.");
                    return;
                }

                // Iterating through all kingdoms and displaying their war info
                for (Kingdom k : KingdomModule.getInstance().getKingdoms()) {
                    // Skip error or 'geen' kingdom types
                    if (k.getType() == KingdomType.ERROR || k.getType() == KingdomType.GEEN)
                        continue;

                    // Get soldiers and display kingdom info
                    String soldiersInfo = WarModule.getInstance().getWar().getSoldiers(k).toString();
                    String kingdomInfo = displayKingdomInfo(k, getPlayer());
                    getPlayer().sendMessage(k.getType().getPrefix() + k.getType().getColor() + " " + soldiersInfo + kingdomInfo);
                }
            }
        });
    }

    @Override
    public void execute() {
        // Default execute method (no direct implementation needed)
    }

    // Method to display additional kingdom information if player has permission
    private String displayKingdomInfo(Kingdom k, KingdomFactionsPlayer p) {
        if (p.hasPermission("kingdomfactions.command.oorlog.info.extra")) {
            // If player has permission, display additional info like members count
            return " /" + k.getMembers().size() + " leden"; // Example of members count
        }
        return "";
    }
}
