package nl.dusdavidgames.kingdomfactions.modules.player.deathban;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class DeathBanModule {

    public DeathBanModule() {
        setInstance(this);
        KingdomFactionsPlugin.getInstance().registerListener(new DeathBanLoginEventListener());
        this.initDeathbanCleaner();
    }

    private static @Getter @Setter DeathBanModule instance;

    // Use a map for faster lookups
    private @Getter Map<UUID, DeathBan> bans = new HashMap<>();

    public DeathBan getBan(UUID uuid) {
        return bans.get(uuid);
    }

    public void ban(KingdomFactionsPlayer p) {
        DeathBan ban = new DeathBan(p.getName(), p.getUuid(), 5);
        bans.put(p.getUuid(), ban);
        p.kick(ChatColor.RED + "Je bent dood gegaan! Wacht " + ban.getMinutes() + " minuten.");
    }

    public void unRegister(DeathBan ban) {
        bans.remove(ban.getUuid());
    }

    public DeathBan getBan(String name) {
        // Iterate through the bans to find by name
        for (DeathBan ban : bans.values()) {
            if (ban.getName().equalsIgnoreCase(name)) {
                return ban;
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    private void initDeathbanCleaner() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new Runnable() {

            @Override
            public void run() {
                // Safely remove inactive bans
                bans.entrySet().removeIf(entry -> !entry.getValue().isActive());
            }
        }, 0, 20 * 60 * 5); // run every 5 minutes
    }
}
