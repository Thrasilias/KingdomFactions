package nl.dusdavidgames.kingdomfactions.modules.player.runnables;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class ScheduledSaveRunnable implements Runnable {

    @Override
    public void run() {
        if (PlayerModule.getInstance().getPlayers().isEmpty()) {
            return;
        }

        Logger.INFO.log("Starting auto save...");
        Logger.INFO.log(PlayerModule.getInstance().getPlayers().size() + " players to save..");
        PlayerModule.getInstance().saving = true;

        // Attempt to save asynchronously
        try {
            PlayerModule.getInstance().saveAsync();
        } catch (Exception e) {
            Logger.ERROR.log("Error during auto save: " + e.getMessage());
        }

        // Log completion (could be added inside saveAsync if that function provides feedback when finished)
        Logger.INFO.log("Auto save process triggered.");
    }
}
