package nl.dusdavidgames.kingdomfactions.modules.player.runnables.main;

import java.util.ArrayList;

/**
 * PlayerTaskList is an extension of ArrayList that holds tasks to be run for each player.
 * It allows for scheduling and managing tasks that are executed periodically for each player.
 */
public class PlayerTaskList extends ArrayList<ScheduledPlayerTask> {

    private static final long serialVersionUID = 8342232943565369494L;

    /**
     * Adds a new task to the list if it's not already scheduled.
     *
     * @param task The task to add.
     * @return true if the task was added, false if it was already in the list.
     */
    public boolean addTask(ScheduledPlayerTask task) {
        if (!contains(task)) {
            return super.add(task); // Adds the task only if it doesn't already exist
        }
        return false; // Task already exists, don't add it again
    }

    /**
     * Removes a task from the list.
     *
     * @param task The task to remove.
     * @return true if the task was removed, false if it wasn't found.
     */
    public boolean removeTask(ScheduledPlayerTask task) {
        return super.remove(task); // Removes the task if it exists
    }

    /**
     * Checks if a task is scheduled.
     *
     * @param task The task to check.
     * @return true if the task exists in the list, false otherwise.
     */
    public boolean containsTask(ScheduledPlayerTask task) {
        return contains(task); // Checks if the task exists in the list
    }
}
