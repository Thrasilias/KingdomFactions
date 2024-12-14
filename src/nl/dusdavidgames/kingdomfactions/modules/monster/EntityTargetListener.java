package nl.dusdavidgames.kingdomfactions.modules.monster;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.CapitalNexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.Nexus;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class EntityTargetListener implements Listener {

    @EventHandler
    public void onTarget(EntityTargetEvent e) {
        // Check if the target is a player
        if (!(e.getTarget() instanceof Player)) {
            return;
        }

        Player targetPlayer = (Player) e.getTarget();
        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(targetPlayer);

        // Check if the entity is a guard, if so skip further checks
        if (MonsterModule.getInstance().isGuard((LivingEntity) e.getEntity())) {
            return;
        }

        // If the target player is a guard, cancel the event
        if (MonsterModule.getInstance().isGuard((LivingEntity) e.getTarget())) {
            e.setCancelled(true);
            return;
        }

        // Get the Nexus of the guard
        INexus guardNexus = MonsterModule.getInstance().getGuard((LivingEntity) e.getEntity()).getNexus();
        if (guardNexus == null) {
            return;
        }

        // Check if the guard belongs to the same kingdom or faction as the player
        switch (guardNexus.getType()) {
            case CAPITAL:
                // Capital nexus check: if the player's kingdom matches the guard's kingdom, cancel the event
                if (player.getKingdom() != null && player.getKingdom().getType() == ((CapitalNexus) guardNexus).getKingdom()) {
                    e.setCancelled(true);
                }
                break;
            case FACTION:
                // Faction nexus check: if the player's faction matches the guard's faction, cancel the event
                if (player.getFaction() != null && player.getFaction().equals(((Nexus) guardNexus).getOwner())) {
                    e.setCancelled(true);
                }
                break;
            default:
                break;
        }
    }
}
