package nl.dusdavidgames.kingdomfactions.modules.player.deathban;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class DeathBan {

    private @Getter UUID uuid;
    private @Getter String name;
    private @Getter long unban;  // Store unban time in milliseconds

    private @Getter int minutes; 

    public DeathBan(String name, UUID uuid, int minutes) {
        this.uuid = uuid;
        this.name = name;
        this.minutes = minutes;
        this.unban = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes);  // Directly calculate unban time in milliseconds
    }

    public void unban() {
        DeathBanModule.getInstance().getBans().remove(this);
    }

    // Helper method to calculate the remaining ban time
    private long getRemainingBanTime() {
        return unban - System.currentTimeMillis();
    }

    public String getMessage() {
        long banLeftMillis = getRemainingBanTime();
        if (banLeftMillis <= 0) return null;  // No remaining ban time

        long seconds = TimeUnit.MILLISECONDS.toSeconds(banLeftMillis);
        long minutesLeft = TimeUnit.SECONDS.toMinutes(seconds);
        long secondsLeft = seconds - TimeUnit.MINUTES.toSeconds(minutesLeft);

        return ChatColor.RED + "Je bent dood gegaan! Je moet nog " + minutesLeft + " minuten en " + secondsLeft
                + " seconden wachten!";
    }

    public boolean handleBan() {
        if (getRemainingBanTime() <= 0) {
            DeathBanModule.getInstance().unRegister(this);
            return false;
        }
        return true;
    }

    public String getTime() {
        long banLeftMillis = getRemainingBanTime();
        if (banLeftMillis <= 0) return "Ban is over.";

        long seconds = TimeUnit.MILLISECONDS.toSeconds(banLeftMillis);
        long minutesLeft = TimeUnit.SECONDS.toMinutes(seconds);
        long secondsLeft = seconds - TimeUnit.MINUTES.toSeconds(minutesLeft);

        return minutesLeft + " minuten, " + secondsLeft + " seconden";
    }

    public boolean isActive() {
        return getRemainingBanTime() > 0;
    }
}
