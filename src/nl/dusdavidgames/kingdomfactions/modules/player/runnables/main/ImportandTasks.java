package nl.dusdavidgames.kingdomfactions.modules.player.runnables.main;

import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Cooldown;
import nl.dusdavidgames.kingdomfactions.modules.utils.TeleportationAction;
import nl.dusdavidgames.kingdomfactions.modules.utils.action.IAction;

public class ImportantTasks {

    public ImportantTasks(MainPlayerRunnable r) {

        // Schedule task to reduce cooldowns
        r.scheduleTask(new ScheduledPlayerTask() {

            @Override
            public void run(KingdomFactionsPlayer player) {
                // Iterate through cooldowns using enhanced for-loop for better readability
                for (Cooldown cooldown : player.getCooldowns()) {
                    cooldown.lower(); // Reduce cooldown for each cooldown in the player's list
                }
            }
        });

        // Schedule task to handle teleportation action delays
        r.scheduleTask(new ScheduledPlayerTask() {

            @Override
            public void run(KingdomFactionsPlayer player) {
                // Only proceed if the player has an active action
                if (player.hasAction()) {
                    IAction action = player.getAction();
                    // Check if the action is of type TeleportationAction
                    if (action instanceof TeleportationAction) {
                        TeleportationAction teleportationAction = (TeleportationAction) action;
                        teleportationAction.handleDelayChange(); // Handle teleportation delay change
                    }
                }
            }
        });
    }
}
