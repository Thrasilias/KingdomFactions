package nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.GodMode;

public class GodModeListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        // Ensure the entity is a player
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            KingdomFactionsPlayer p = PlayerModule.getInstance().getPlayer(player);

            // Ensure that KingdomFactionsPlayer and its SettingsProfile are not null
            if (p == null || p.getSettingsProfile() == null) {
                return; // If player or settings profile is null, do nothing
            }

            // Handle GodMode settings
            GodMode godMode = p.getSettingsProfile().getGodMode();

            switch (godMode) {
                case FAKEDAMAGE:
                    e.setDamage(0); // No real damage dealt
                    p.heal(); // Heal the player to counteract damage
                    break;
                case NODAMAGE:
                    e.setCancelled(true); // Cancel the damage event completely
                    break;
                case NOGOD:
                    // No action needed if GodMode is NOGOD
                    break;
                default:
                    // Handle any unforeseen GodMode settings if necessary
                    break;
            }
        }
    }
}
