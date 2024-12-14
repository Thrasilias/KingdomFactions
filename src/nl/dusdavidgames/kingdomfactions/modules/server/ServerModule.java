package nl.dusdavidgames.kingdomfactions.modules.server;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.configuration.ConfigModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class ServerModule {

    private static @Getter @Setter ServerModule instance;

    private @Getter @Setter ServerMode serverMode;

    public ServerModule() {
        // Fetch the server mode from the configuration
        String serverModeConfig = ConfigModule.getInstance()
                .getFile(ConfigModule.CONFIG)
                .getConfig()
                .getString("Servermode");

        // Check if the configuration is valid
        if (serverModeConfig != null) {
            ServerMode mode = ServerMode.getMode(serverModeConfig);
            if (mode != null) {
                this.serverMode = mode;
                Logger.DEBUG.log("Server mode set to: " + serverMode);
            } else {
                Logger.DEBUG.log("Invalid server mode in config, defaulting to 'RELEASED'");
                this.serverMode = ServerMode.RELEASED; // Fallback to default
            }
        } else {
            Logger.DEBUG.log("No server mode defined in config, defaulting to 'RELEASED'");
            this.serverMode = ServerMode.RELEASED; // Fallback to default
        }
    }
}
