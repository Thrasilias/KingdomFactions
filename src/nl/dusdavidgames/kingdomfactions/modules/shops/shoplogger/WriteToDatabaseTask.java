package nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger;

import org.bukkit.Bukkit;

import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.ShopLogDatabase;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class WriteToDatabaseTask implements Runnable {

    @Override
    public void run() {
        // Check if there are any shop logs to save
        if (ShopLogger.getInstance().getShopLogs().isEmpty()) {
            return; // No logs to save, exit early
        }

        // Attempt to save the first log to the database
        Logger.DEBUG.log("Attempting to save one shop log to database");

        ShopLog log = ShopLogger.getInstance().getShopLogs().poll(); // Retrieve and remove the first log

        // Save the log to the database
        ShopLogDatabase.getInstance().save(log);

        Logger.DEBUG.log("Saved one shop log to database. " +
                ShopLogger.getInstance().getShopLogs().size() + " shop logs left.");

        // Optionally, if you want to keep the task running every 3 seconds:
        // This can be done by scheduling the next run.
        if (!ShopLogger.getInstance().getShopLogs().isEmpty()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(KingdomFactionsPlugin.getInstance(), this, 20 * 3);
        }
    }
}
