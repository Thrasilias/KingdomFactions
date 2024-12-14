package nl.dusdavidgames.kingdomfactions.modules.memleak.command;

import nl.dusdavidgames.kingdomfactions.modules.command.KingdomFactionsCommand;
import nl.dusdavidgames.kingdomfactions.modules.command.SubCommand;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.memleak.MemLeakMessage;

/**
 * Handles the 'memleak' command and its subcommands.
 */
public class MemLeakCommand extends KingdomFactionsCommand {

    public MemLeakCommand(String name, String permission, String info, String usage, boolean sub,
                          boolean allowConsole) {
        super(name, permission, info, usage, sub, allowConsole);
    }

    @Override
    public void init() {
        // Registering the 'display' sub-command to show memory leak data
        this.registerSub(new SubCommand("display", "kingdomfactions.command.data.get", "get data!") {
            @Override
            public void execute(String[] args) {
                // Send memory leak message data to the sender
                MemLeakMessage.getInstance().sendMessage(getSender());
            }
        });
    }

    @Override
    public void execute() throws KingdomFactionsException {
        // Implement the main command execution logic here, or provide an informative message.
        // For now, we can send a placeholder message that the main command doesn't do anything.
        getSender().sendMessage("The 'memleak' command is a placeholder for future functionality.");
    }
}
