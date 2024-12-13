package nl.dusdavidgames.kingdomfactions.modules.war;

import org.bukkit.Bukkit;
import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.war.command.OorlogCommand;
import nl.dusdavidgames.kingdomfactions.modules.war.command.WarCommand;
import nl.dusdavidgames.kingdomfactions.modules.war.runnable.WarRunnable;

/**
 * Manages the war functionality within the plugin.
 * Responsible for starting, stopping, and tracking the state of the war.
 */
public class WarModule {

    private static @Getter @Setter WarModule instance;

    public WarModule() {
        setInstance(this);

        // Register the commands
        new WarCommand("war", "kingdomfactions.command.war", "Oorlog commando", "war", true, true).registerCommand();
        new OorlogCommand("oorlog", "kingdomfactions.command.oorlog", "Oorlog commando", "oorlog", true, true);
        
        // Schedule the war task to run every second
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new WarRunnable(), 0, 20);
    }

    // The current war instance
    public @Getter @Setter War war;

    /**
     * Checks if a war is currently active.
     * 
     * @return true if the war is active, false otherwise
     */
    public boolean isWarActive() {
        return war != null && war.isActive();  // assuming War class has an isActive() method
    }

    /**
     * Starts a new war with the specified time duration.
     * 
     * @param time The duration of the war in seconds.
     */
    public void start(int time) {
        if (isWarActive()) {
            // Optionally, send a message if a war is already active
            Bukkit.getServer().broadcastMessage("A war is already ongoing!");
            return;
        }
        
        war = new War(time);  // assuming War constructor takes a time argument
        war.start();
    }

    /**
     * Ends the current war.
     */
    public void end() {
        if (war != null) {
            war.end();  // assuming War class has an end() method
            war = null; // reset the war instance
        }
    }
}
