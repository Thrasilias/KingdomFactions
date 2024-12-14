package nl.dusdavidgames.kingdomfactions.modules.settings;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class SettingCommand extends KingdomFactionsCommand {

    public SettingCommand(String name, String permission, String info, String usage, boolean sub, boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // Register 'set' subcommand to change settings
        this.registerSub(new SubCommand("set", "kingdomfactions.command.setting.set", "Verzet een instelling!") {

            @Override
            public void execute(String[] args) throws KingdomFactionsException {
                if (args.length < 3) {
                    getUser().sendMessage(Messages.getInstance().getPrefix() + "Gebruik: /setting set <setting> <true/false>");
                    return;
                }

                Setting setting;
                try {
                    setting = Setting.valueOf(args[1].toUpperCase());
                } catch (IllegalArgumentException e) {
                    getUser().sendMessage(Messages.getInstance().getPrefix() + "Onbekende optie: " + args[1]);
                    return;
                }

                boolean enabled;
                try {
                    enabled = Boolean.parseBoolean(args[2]);
                } catch (Exception e) {
                    getUser().sendMessage(Messages.getInstance().getPrefix() + "Ongeldige waarde: " + args[2] + ". Gebruik true of false.");
                    return;
                }

                setting.setEnabled(enabled);
                getUser().sendMessage(Messages.getInstance().getPrefix() + "Optie " + setting.toString() + " verzet naar " + (enabled ? "AAN" : "UIT"));
            }
        });

        // Register 'list' subcommand to list all settings
        this.registerSub(new SubCommand("list", "kingdomfactions.command.setting.list", "Verkrijg een lijst met alle instellingen!") {

            @Override
            public void execute(String[] args) throws KingdomFactionsException {
                getUser().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "---------------------------");
                for (Setting setting : Setting.values()) {
                    getUser().sendMessage(ChatColor.BLUE + setting.toString() + ": " + (setting.isEnabled() ? ChatColor.GREEN + "AAN" : ChatColor.RED + "UIT"));
                }
                getUser().sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "---------------------------");
            }
        });
    }

    @Override
    public void execute() throws KingdomFactionsException {
        // You may want to define a default behavior or help message here if no subcommand is passed
    }
}
