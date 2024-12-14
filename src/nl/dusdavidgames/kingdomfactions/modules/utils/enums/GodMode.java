package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum GodMode {
    FAKEDAMAGE("Fake Damage"),
    NODAMAGE("No Damage"),
    NOGOD("No God Mode");

    private final String displayName;

    GodMode(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Checks if the god mode allows for damage.
     * @return true if damage is allowed in this god mode.
     */
    public boolean allowsDamage() {
        return this == NOGOD;
    }

    /**
     * Checks if the god mode is set to fake damage.
     * @return true if the god mode is FAKEDAMAGE.
     */
    public boolean isFakeDamage() {
        return this == FAKEDAMAGE;
    }

    /**
     * Checks if the god mode is set to no damage.
     * @return true if the god mode is NODAMAGE.
     */
    public boolean isNoDamage() {
        return this == NODAMAGE;
    }
}
