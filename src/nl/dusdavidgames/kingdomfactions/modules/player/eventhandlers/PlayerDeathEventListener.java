package nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBanModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.settings.Setting;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;

public class PlayerDeathEventListener implements Listener {

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getEntity());
		e.setKeepInventory(false);

		// Handle players without kingdom (KingdomType.GEEN)
		if (player.getKingdom().getType().equals(KingdomType.GEEN)) {
			player.getInventory().clear();
			player.getInventory().addItem(Item.getInstance().getItem(Material.COMPASS, ChatColor.RED + "Selecteer jouw kingdom", 1));
			player.updateInventory();
			player.teleport(player.getKingdom().getSpawn());
		}

		// Handle killer if present
		if (e.getEntity().getKiller() != null) {
			KingdomFactionsPlayer killer = PlayerModule.getInstance().getPlayer(e.getEntity().getKiller());
			e.setDeathMessage(getFormattedName(player) + " is verwond door " + getFormattedName(killer) + "!");
			if (player.getCombatTracker().isInCombat()) {
				player.getCombatTracker().clearCombat();
			}
			// Handle deathban with a delayed task
			Bukkit.getScheduler().runTaskLater(KingdomFactionsPlugin.getInstance(), () -> {
				if (!player.hasPermission("kingdomfactions.deathban.ignore") && Setting.USE_DEATHBAN.isEnabled()) {
					DeathBanModule.getInstance().ban(player);
				}
			}, 40L); // Delay of 2 seconds before banning the player
		} else {
			e.setDeathMessage(null);
		}
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		KingdomFactionsPlayer p = PlayerModule.getInstance().getPlayer(e.getPlayer());
		Bukkit.getScheduler().runTaskLater(KingdomFactionsPlugin.getInstance(), () -> {
			if (p.getKingdom() != null) {
				if (p.hasFaction()) {
					// Teleport to faction home or kingdom spawn
					if (p.getFaction().hasHome()) {
						p.teleport(p.getFaction().getHome().getLocation());
					} else {
						p.teleport(p.getKingdom().getSpawn());
					}
				} else {
					p.teleport(p.getKingdom().getSpawn());
				}
			}
		}, 20); // Delay of 1 second before teleportation
	}

	// A more descriptive method name and string formatting
	private String getFormattedName(KingdomFactionsPlayer player) {
		StringBuilder builder = new StringBuilder();
		builder.append(player.getKingdom().getType().getPrefix());
		if (player.hasFaction()) {
			builder.append(player.getFaction().getPrefix());
		}
		builder.append(player.getName());
		return builder.toString();
	}
}
