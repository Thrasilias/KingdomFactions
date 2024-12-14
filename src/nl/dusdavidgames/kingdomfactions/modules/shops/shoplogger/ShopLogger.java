package nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.ShopLogDatabase;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class ShopLogger {

    private static @Getter @Setter ShopLogger instance;

    private @Getter ArrayList<ShopLog> shopLogs = new ArrayList<>();

    public ShopLogger() {
        setInstance(this);

        // Schedule a task to save logs every 3 seconds (20 ticks = 1 second)
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new WriteToDatabaseTask(),
                20 * 3, 20 * 3);
    }

    // Disable method to save remaining logs on shutdown
    public void disable() {
        Logger.DEBUG.log("Force saving...");

        int savedLogs = 0;
        Iterator<ShopLog> iterator = shopLogs.iterator();

        // Save logs in a batch to avoid concurrent modification
        while (iterator.hasNext()) {
            ShopLog log = iterator.next();
            ShopLogDatabase.getInstance().save(log);
            iterator.remove(); // Safe removal using iterator
            savedLogs++;
        }

        Logger.DEBUG.log("Forced save done. Saved " + savedLogs + " logs. Remaining " + shopLogs.size() + " logs.");
    }
}
