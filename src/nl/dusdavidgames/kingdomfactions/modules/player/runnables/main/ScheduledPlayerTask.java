package nl.dusdavidgames.kingdomfactions.modules.player.runnables.main;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

/**
 * Represents a task that will be run for a player during scheduled intervals.
 * Tasks must implement the run method to define the logic that should be executed for each player.
 */
@FunctionalInterface
public interface ScheduledPlayerTask {

    /**
     * Executes the task for a given player.
     * 
     * @param player The player for whom the task is executed.
     */
    void run(KingdomFactionsPlayer player);

    /**
     * Optional: Determines if the task can be canceled before it runs.
     * 
     * @return true if the task can be canceled, false otherwise.
     */
    default boolean isCancelable() {
        return false; // Default behavior: tasks cannot be canceled
    }

    /**
     * Optional: Retrieves the priority of the task for scheduling.
     * 
     * @return the priority of the task.
     */
    default int getPriority() {
        return 0; // Default priority: normal priority
    }
}
