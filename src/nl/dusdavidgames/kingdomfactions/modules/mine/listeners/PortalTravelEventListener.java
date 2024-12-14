package nl.dusdavidgames.kingdomfactions.modules.mine.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.mine.MineTravelEvent;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

public class PortalTravelEventListener implements Listener {

    @EventHandler
    public void onPortal(PlayerMoveEvent e) {
        if (e.getPlayer().getEyeLocation().getBlock().getType() == Material.PORTAL) {
            KingdomFactionsPlayer p = PlayerModule.getInstance().getPlayer(e.getPlayer());
            if (p != null && (p.getKingdomTerritory() == p.getKingdom().getType() || p.getKingdomTerritory() == KingdomType.GEEN)) {
                teleportPlayerToDestination(e.getPlayer(), p);
            }
        }
    }

    private void teleportPlayerToDestination(Player player, KingdomFactionsPlayer p) {
        if (player.getLocation().getWorld() == Utils.getInstance().getMiningWorld()) {
            teleportToKingdomSpawn(player, p, "Ranos");
        } else {
            teleportToKingdomMiningSpawn(player, p, "Mithras");
        }
    }

    private void teleportToKingdomSpawn(Player player, KingdomFactionsPlayer p, String destination) {
        player.sendMessage(ChatColor.DARK_PURPLE + "Woosh.. Je bent geteleporteerd naar " + destination + "!");
        teleportPlayer(player, p.getKingdom().getSpawn());
    }

    private void teleportToKingdomMiningSpawn(Player player, KingdomFactionsPlayer p, String destination) {
        player.sendMessage(ChatColor.DARK_PURPLE + "Woosh.. Je bent geteleporteerd naar " + destination + "!");
        teleportPlayer(player, p.getKingdom().getMiningSpawn());
    }

    private void teleportPlayer(Player player, Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(location);
            }
        }.runTaskLater(KingdomFactionsPlugin.getInstance(), 20L);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (e.getClickedBlock().getType() != Material.OBSIDIAN) return;
        if (e.getPlayer().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) return;

        if (!e.getPlayer().isOp()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPortalDestroy(BlockBreakEvent e) {
        if (e.getBlock() == null) return;
        if (e.getBlock().getType() == Material.PORTAL) {
            if (!e.getPlayer().isOp()) {
                e.setCancelled(true);
            }
        }
    }
}
