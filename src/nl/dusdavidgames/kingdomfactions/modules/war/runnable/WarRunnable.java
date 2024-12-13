package nl.dusdavidgames.kingdomfactions.modules.war.runnable;

import org.bukkit.Bukkit;

import nl.dusdavidgames.kingdomfactions.modules.war.War;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;
import nl.dusdavidgames.kingdomfactions.modules.war.event.WarDurationChangeEvent;

public class WarRunnable implements Runnable {

    private long lastEventTime = 0;

    @Override
    public void run() {
        if (WarModule.getInstance().isWarActive()) {
            War war = WarModule.getInstance().getWar();

            // Check if at least one minute has passed since the last event call
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastEventTime >= 60000) {
                Bukkit.getPluginManager().callEvent(new WarDurationChangeEvent());
                lastEventTime = currentTime;
            }

            // End the war when the time is up
            if (currentTime >= war.getTimeInMilliSeconds()) {
                war.end();
            }
        }
    }
}
