package nl.dusdavidgames.kingdomfactions.modules.memleak;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.memleak.command.MemLeakCommand;

public class MemLeakModule {

    // Singleton instance of MemLeakModule
    private static @Getter @Setter MemLeakModule instance;

    // Constructor to initialize the module
    public MemLeakModule() {
        setInstance(this);

        // Initialize MemLeakMessage to manage memory leak messages
        new MemLeakMessage();

        // Register the MemLeakCommand with necessary permissions and info
        new MemLeakCommand("data", "kingdomfactions.command.data", 
                "Main command for data management", "", true, true).registerCommand();
    }

    /**
     * Initializes the MemLeakModule, ensuring the instance is set and commands are registered.
     */
    public static void initialize() {
        if (instance == null) {
            new MemLeakModule(); // Ensures initialization of singleton
        }
    }
}
