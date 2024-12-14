package nl.dusdavidgames.kingdomfactions.modules.mine.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class PortalProtection implements Listener {

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!canPerformAction(event.getPlayer(), event.getBlockClicked().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!canPerformAction(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!canPerformAction(event.getPlayer(), event.getBlock().getLocation())) {
            event.setCancelled(true);
        }
    }

    private boolean canPerformAction(Player player, Location loc) {
        if (PlayerModule.getInstance().getPlayer(player).getSettingsProfile().hasAdminMode()) {
            return true; // Admin mode bypasses protection
        }

        // Create a list of surrounding locations to check
        List<Location> locations = Arrays.asList(
                loc,
                loc.clone().add(0, 1, 0),   // Above
                loc.clone().subtract(0, 1, 0),  // Below
                loc.clone().add(1, 0, 0),   // Side
                loc.clone().add(0, 0, 1),   // Side
                loc.clone().subtract(1, 0, 0), // Side
                loc.clone().subtract(0, 0, 1)  // Side
        );

        // Check if any of the surrounding locations contain a portal
        for (Location location : locations) {
            if (location.getBlock().getType().equals(Material.PORTAL) ||
                location.getBlock().getType().equals(Material.END_PORTAL)) {  // Consider all portal types
                player.sendMessage(ChatColor.RED + "Je hebt geen toestemming om deze actie uit te voeren!");
                return false; // Block action if near portal
            }
        }

        return true; // Allow action if not near a portal
    }
}
