package nl.dusdavidgames.kingdomfactions.modules.time;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class TimeModule {
    private static @Getter @Setter TimeModule instance;

    public TimeModule() {
        setInstance(this);
        new TimeHelper();
        savePlayerTimes();
    }

    /**
     * Schedules a task to update the time of all players every 20 seconds.
     */
    private void savePlayerTimes() {
        Bukkit.getScheduler().runTaskTimer(KingdomFactionsPlugin.getInstance(), () -> {
            for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
                TimeHelper.getInstance().updateTime(player); // Directly update player time
            }
        }, 0L, 20L * 20); // Runs every 20 seconds
    }
}
