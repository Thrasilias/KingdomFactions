package nl.dusdavidgames.kingdomfactions.modules.settings;

import lombok.Getter;
import lombok.Setter;

public enum Setting {

    BURGER_OORLOG(true), // Enables the Burger War feature
    ALLOW_HOME(true),     // Allows players to set home
    ALLOW_SPAWN(true),    // Allows players to spawn
    USE_DEATHBAN(true);   // Enables death ban feature

    private @Setter @Getter boolean enabled;

    private Setting(boolean defaultState) {
        this.enabled = defaultState;
    }

    /**
     * Toggles the setting between true and false.
     */
    public void toggle() {
        this.enabled = !this.enabled;
    }

    /**
     * Returns a string representation of the setting state.
     */
    public String getStateAsString() {
        return this.enabled ? "Enabled" : "Disabled";
    }
}
