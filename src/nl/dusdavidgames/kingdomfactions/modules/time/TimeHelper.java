package nl.dusdavidgames.kingdomfactions.modules.time;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class TimeHelper {
    
    private static @Getter @Setter TimeHelper instance;

    // Constructor to initialize the instance
    public TimeHelper() {
        setInstance(this);
    }

    /**
     * Updates the time for the given player based on the last update time
     * and adds the time difference to the player's seconds connected.
     *
     * @param p The player whose time will be updated.
     */
    public void updateTime(KingdomFactionsPlayer p) {
        long currentTime = System.currentTimeMillis();
        
        // If the player has never been updated before, set the last update time to the current time
        if (p.getStatisticsProfile().getLastUpdate() == 0) {
            p.getStatisticsProfile().setLastUpdate(currentTime);
        }
        
        // Calculate the time difference in seconds
        long timeDiffInMillis = currentTime - p.getStatisticsProfile().getLastUpdate();
        int timeDiffInSeconds = (int) (timeDiffInMillis / 1000);
        
        // Update the player's total connected time
        long newTotalTime = p.getStatisticsProfile().getSecondsConnected() + timeDiffInSeconds;
        p.getStatisticsProfile().setSecondsConnected(newTotalTime);
        
        // Update the last update time to the current time
        p.getStatisticsProfile().setLastUpdate(currentTime);
    }

    /**
     * Translates milliseconds into an array of time units in the format of
     * days, hours, minutes, seconds.
     *
     * @param ms The time in milliseconds.
     * @return An array containing the days, hours, minutes, and seconds.
     */
    public int[] translateTime(long ms) {
        int days = (int) TimeUnit.MILLISECONDS.toDays(ms);
        int hours = (int) (TimeUnit.MILLISECONDS.toHours(ms) - (days * 24));
        int minutes = (int) (TimeUnit.MILLISECONDS.toMinutes(ms) - (TimeUnit.MILLISECONDS.toHours(ms) * 60));
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(ms) - (TimeUnit.MILLISECONDS.toMinutes(ms) * 60));
        
        return new int[] { days, hours, minutes, seconds };
    }
}
