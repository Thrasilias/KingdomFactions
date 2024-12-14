package nl.dusdavidgames.kingdomfactions.modules.permission;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.CommandModule;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;

public class PermListCommand extends KingdomFactionsCommand {

    public PermListCommand(String name, String permission, String info, String usage, boolean sub,
                           boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // No initialization needed for now
    }

    @Override
    public void execute() {
        StringBuilder message = new StringBuilder();

        for (KingdomFactionsCommand cmd : CommandModule.getInstance().getCommand()) {
            if (cmd == this) continue;
            // Add the main command
            message.append("/").append(cmd.getName()).append(" - ")
                    .append(cmd.getPermission()).append(" - ")
                    .append(allowConsole(cmd.allowConsole())).append("\n");

            // Add subcommands
            if (!cmd.getSubCommands().isEmpty()) {
                for (SubCommand s : cmd.getSubCommands()) {
                    message.append("/").append(cmd.getName()).append(" ").append(s.getMainCommand()).append(" - ")
                            .append(s.getPermission()).append(" - ")
                            .append(allowConsole(s, cmd)).append("\n");
                }
            }
        }

        // Send all the collected messages at once
        getSender().sendMessage(message.toString());
    }

    private String allowConsole(boolean allow) {
        return allow ? ChatColor.GREEN + "CONSOLE" : ChatColor.RED + "CONSOLE";
    }

    private String allowConsole(SubCommand command, KingdomFactionsCommand supercmd) {
        // Simplified logic, since it's only based on the console allowance of the subcommand and main command
        return (supercmd.allowConsole() && command.allowConsole()) ? ChatColor.GREEN + "CONSOLE" : ChatColor.RED + "CONSOLE";
    }
}
