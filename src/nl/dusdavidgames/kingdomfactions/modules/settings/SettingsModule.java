package nl.dusdavidgames.kingdomfactions.modules.settings;

import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class SettingsModule {

    public SettingsModule() {
        // Register the SettingCommand only if it is not already registered
        try {
            new SettingCommand("setting", "kingdomfactions.command.setting", "Verzet instellingen", "[args]", true, true).registerCommand();
        } catch (Exception e) {
            Logger.ERROR.log("Error while registering the SettingCommand: " + e.getMessage());
        }
    }
}
