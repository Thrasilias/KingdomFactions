package nl.dusdavidgames.kingdomfactions.modules.utils.logger;

import nl.dusdavidgames.kingdomfactions.modules.configuration.ConfigModule;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemLeakHunter extends Logger {

    public MemLeakHunter() {
        super("Memory Leak Hunter ");
    }

    @Override
    public void log(String message) {
        boolean shouldLogMemLeak = ConfigModule.getInstance()
                .getFile(ConfigModule.CONFIG)
                .getConfig()
                .getBoolean("log.memleak_hunter");

        if (shouldLogMemLeak) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            System.out.println("[KingdomFactions] " + timestamp + " " + prefix + message);
        }
    }
}
