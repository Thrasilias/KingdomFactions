package nl.dusdavidgames.kingdomfactions.modules.pvplog;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.exception.KingdomFactionsException;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.main.ScheduledPlayerTask;

public class CombatModule {

    private static @Getter @Setter CombatModule instance;

    public CombatModule() {
        setInstance(this);
        this.initRunnable();
        KingdomFactionsPlugin.getInstance().registerListener(new VanishListener());
        registerCommands();
    }

    private void registerCommands() {
        // Register combat-related commands
        new CombatCommand("combat", "kingdomfactions.command.combat", "Combat gerelateerde commando's", "combat <sub>",
                true, false).registerCommand();

        // Register logout command for players in combat
        new LogoutCommand("logout", "kingdomfactions.command.logout", "Log uit via dit commando als je in gevecht bent!", "logout",
                false, false).registerCommand();
    }

    private void initRunnable() {
        // Schedule a task to update combat status and handle logout actions
        KingdomFactionsPlugin.getInstance().getTaskManager().scheduleTask(new ScheduledPlayerTask() {

            @Override
            public void run(KingdomFactionsPlayer player) {
                if (player.getCombatTracker() == null) return;

                // Update combat timer if the player is in combat
                updateCombatStatus(player);

                // Check if player has an action (e.g., logout action)
                if (player.hasAction()) {
                    handlePlayerAction(player);
                }
            }
        });
    }

    private void updateCombatStatus(KingdomFactionsPlayer player) {
        if (player.getCombatTracker().isInCombat()) {
            if (player.getCombatTracker().getCombatSeconds() <= 0) {
                player.getCombatTracker().setInCombat(false); // Combat ends when the timer reaches 0
            } else {
                int remainingTime = player.getCombatTracker().getCombatSeconds();
                remainingTime--;
                player.getCombatTracker().setCombatSeconds(remainingTime); // Decrease combat time by 1 second
            }
        }
    }

    private void handlePlayerAction(KingdomFactionsPlayer player) {
        // Handle logout action, if any
        if (player.getAction() instanceof LogoutAction) {
            LogoutAction logoutAction = (LogoutAction) player.getAction();

            if (logoutAction.logoutSeconds <= 1) {
                try {
                    logoutAction.execute(); // Execute logout action
                } catch (KingdomFactionsException e) {
                    KingdomFactionsPlugin.getInstance().getLogger().severe("Error during logout execution: " + e.getMessage());
                    e.printStackTrace(); // Log error properly
                }
            } else {
                logoutAction.logoutSeconds--; // Decrease logout countdown by 1 second
            }
        }
    }
}
