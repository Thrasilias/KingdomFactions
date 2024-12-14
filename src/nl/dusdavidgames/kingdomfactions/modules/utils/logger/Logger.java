package nl.dusdavidgames.kingdomfactions.modules.utils.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static final Logger DEBUG = new Debugger();
    public static final Logger INFO = new Logger("[INFO]");
    public static final Logger ERROR = new Logger("[ERROR]");
    public static final Logger WARNING = new Logger("[WARNING]");
    public static final Logger CRITICAL = new Logger("[CRITICAL]");
    public static final Logger MEMLEAK = new MemLeakHunter();
    public static final Logger BROADCAST = new Logger("[BROADCAST]");

    private final String prefix;

    // The constructor now accepts the prefix for the log level (e.g., INFO, ERROR)
    public Logger(String prefix) {
        this.prefix = prefix;
    }

    // Log method with a timestamp and log message
    public void log(String message) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println("[KingdomFactions] " + timestamp + " " + prefix + " " + message);
    }

    // Optionally, add more control over logging level if needed
    public static boolean isLoggingEnabled(String level) {
        // Here you could add logic to control which levels are enabled based on the config
        return true; // Always enabled, for now
    }
}
