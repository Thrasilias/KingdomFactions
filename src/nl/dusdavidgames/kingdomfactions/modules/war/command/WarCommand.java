package nl.dusdavidgames.kingdomfactions.modules.war.command;

import org.bukkit.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;

public class WarCommand extends KingdomFactionsCommand {

    public WarCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // Registering 'start' subcommand
        this.registerSub(new SubCommand("start", "kingdomfactions.command.war.start", "Start de oorlog!") {
            @Override
            public void execute(String[] args) {
                if (args.length < 2) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Gebruik: /war start <tijd>");
                    return;
                }

                try {
                    int time = Integer.parseInt(args[1]);
                    WarModule.getInstance().start(time);
                    broadcast(PlayerModule.getInstance().getPlayer(getSender()).getFormattedName() + ChatColor.YELLOW + " heeft de oorlog begonnen!");
                } catch (NumberFormatException e) {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Gelieve een geldig getal voor de tijd in te voeren!");
                }
            }
        });

        // Registering 'stop' subcommand
        this.registerSub(new SubCommand("stop", "kingdomfactions.command.war.stop", "Stop de oorlog!") {
            @Override
            public void execute(String[] args) {
                if (WarModule.getInstance().isWarActive()) {
                    WarModule.getInstance().getWar().end();
                    broadcast(PlayerModule.getInstance().getPlayer(getSender()).getFormattedName() + ChatColor.YELLOW + " heeft de oorlog gestopt!");
                } else {
                    getSender().sendMessage(Messages.getInstance().getPrefix() + ChatColor.RED + "Er is op dit moment geen oorlog!");
                }
            }
        });
    }

    @Override
    public void execute() {
        // Default execution logic
        return;
    }
}
