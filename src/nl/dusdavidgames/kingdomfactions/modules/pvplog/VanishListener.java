package nl.dusdavidgames.kingdomfactions.modules.pvplog;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.permission.PermissionModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.CombatTracker;

public class VanishListener implements Listener {

    @EventHandler
    public void onVanish(VanishStatusChangeEvent e) {
        if (e.getPlayer() == null || !e.getPlayer().isOnline()) {
            return; // If the player is offline or invalid, do nothing
        }

        CombatTracker combatTracker = PlayerModule.getInstance().getPlayer(e.getPlayer()).getCombatTracker();

        if (combatTracker == null || !combatTracker.isInCombat()) {
            return; // Player is not in combat, no need to broadcast
        }

        if (e.isVanishing()) {
            String message = combatTracker.getPlayer().getFormattedName() + ChatColor.YELLOW + " is in vanish gegaan, terwijl hij/zij in gevecht was!";
            PermissionModule.getInstance().getStaffMembers().broadcast(message);
        }
    }
}
