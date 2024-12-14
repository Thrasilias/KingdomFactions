package nl.dusdavidgames.kingdomfactions.modules.player.runnables.main;

import org.bukkit.Bukkit;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.CombatTracker;

public class MainPlayerRunnable {

    private final PlayerTaskList taskList = new PlayerTaskList();

    /**
     * Initializes the MainPlayerRunnable, sets up repeating tasks, and initializes important tasks and combat tracking.
     */
    public MainPlayerRunnable() {
        this.init();
        new ImportantTasks(this); // Schedule important tasks
        CombatTracker.initCombatNotifier(this); // Initialize combat notifications
    }

    /**
     * Initializes the repeating task to process scheduled tasks for each player.
     */
    private void init() {
        // Schedule a task to run every 20 ticks (1 second)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                // Iterate over all players and run their scheduled tasks
                for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
                    for (ScheduledPlayerTask task : taskList) {
                        task.run(player); // Execute each scheduled task for the player
                    }
                }
            }
        }, 0L, 20L);
    }

    /**
     * Schedules a new task to be run for every player.
     *
     * @param task The task to be scheduled
     */
    public void scheduleTask(ScheduledPlayerTask task) {
        this.taskList.add(task); // Add task to the list for future execution
    }
}
