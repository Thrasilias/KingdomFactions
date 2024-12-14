package nl.dusdavidgames.kingdomfactions.modules.mine.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

public class WorldProtection implements Listener {

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        // Check if the explosion occurs in the mining world
        if (event.getLocation().getWorld() == Utils.getInstance().getMiningWorld()) {
            // Cancel the explosion to protect the world
            event.setCancelled(true);
        }
    }
}
