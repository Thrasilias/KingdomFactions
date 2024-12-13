package nl.dusdavidgames.kingdomfactions.modules.utils;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class Cooldown {

    private @Getter String key;
    private @Getter KingdomFactionsPlayer player;
    private @Getter int cooldown;

    // Constructor with cooldown time
    public Cooldown(String key, KingdomFactionsPlayer player, int cooldown) {
        this.key = key;
        this.player = player;
        this.cooldown = cooldown;
    }

    // Constructor with default 60-second cooldown (e.g., for chat)
    public Cooldown(String key, KingdomFactionsPlayer player) {
        this(key, player, 60);  // Default 60 seconds cooldown
    }

    // Decreases the cooldown by 1. If it reaches 0, it cancels the cooldown.
    public void lower() {
        if (cooldown <= 0) {
            cancel();  // Cooldown expired, cancel it
        } else {
            cooldown--;
            // Optional: You could notify the player about the remaining cooldown here
        }
    }

    // Cancels the cooldown, removing it from the player's active cooldowns
    public void cancel() {
        player.removeCooldown(key);  // Assuming KingdomFactionsPlayer has a removeCooldown method
        // Optional: Notify the player that the cooldown has finished
    }
}
