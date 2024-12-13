package nl.dusdavidgames.kingdomfactions.modules.utils;

public enum CooldownType {
    WAND("cooldown.wand"),
    CHAT("cooldown.chat");

    private final String cooldownKey;

    CooldownType(String cooldownKey) {
        this.cooldownKey = cooldownKey;
    }

    public String getCooldownKey() {
        return cooldownKey;
    }
}
