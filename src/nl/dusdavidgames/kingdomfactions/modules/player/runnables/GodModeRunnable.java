package nl.dusdavidgames.kingdomfactions.modules.player.runnables;

import org.bukkit.Bukkit;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.GodMode;

public class GodModeRunnable {

    public GodModeRunnable() {
        slowGodModeRunnable();
    }

    private void slowGodModeRunnable() {
        Bukkit.getScheduler().runTaskTimer(KingdomFactionsPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (KingdomFactionsPlayer p : PlayerModule.getInstance().getPlayers()) {
                    GodMode currentGodMode = p.getSettingsProfile().getGodMode();

                    if (currentGodMode.equals(GodMode.FAKEDAMAGE) || currentGodMode.equals(GodMode.NODAMAGE)) {
                        // Send action bar only if in relevant GodMode
                        String godModeMessage = ChatColor.RED + "" + ChatColor.BOLD + "Je zit momenteel in GodMode! Type: "
                                + ChatColor.GRAY + "" + ChatColor.BOLD + currentGodMode;

                        p.sendActionbar(godModeMessage);
                    }
                }
            }
        }, 0, 100); // 100 ticks interval
    }
}
