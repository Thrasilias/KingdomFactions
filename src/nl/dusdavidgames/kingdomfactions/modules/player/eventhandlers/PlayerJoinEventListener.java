package nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.exception.chat.ChannelNotFoundException;
import nl.dusdavidgames.kingdomfactions.modules.exception.chat.ChannelRankException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.exception.value.ValueException;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionMember;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.nexus.protection.ProtectionModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class PlayerJoinEventListener implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());

		// Attempt to load player data and handle exceptions
		try {
			e.setJoinMessage(null); // Hide join message
			loadPlayerData(player);
			preparePlayerInventoryAndTeleport(player);
			handlePlayerSwitch(player);
		} catch (Exception ex) {
			handleJoinError(e, ex);
		}
	}

	private void loadPlayerData(KingdomFactionsPlayer player) throws ChannelNotFoundException, ChannelRankException {
		player.restoreOldPvP();
		player.sendTitle(ChatColor.RED + "The Kingdom Factions", ChatColor.RED + "We laden jouw gegevens..", 20, 40, 20);
		player.setTerritoryId(ProtectionModule.getInstance().getTerritoryId(player));
		player.setKingdomTerritory(ProtectionModule.getInstance().getKingdomTerritory(player));

		if (player.hasFaction()) {
			FactionMember member = player.getFaction().getFactionMember(player.getUuid());
			member.setRank(player.getFactionRank());
			member.updateName();
		}

		player.loadScoreboard();
		ProtectionModule.getInstance().updateTerritory(player);

		// Prepare chat system (assuming it's necessary later)
		prepareChat(player);
	}

	private void preparePlayerInventoryAndTeleport(KingdomFactionsPlayer player) {
		// Handle Kingdom-type GEEN (no kingdom assigned)
		if (player.getKingdom().getType().equals(KingdomType.GEEN)) {
			player.getInventory().clear();
			player.getInventory().addItem(Item.getInstance().getItem(Material.COMPASS, ChatColor.RED + "Selecteer jouw kingdom", 1));
			player.updateInventory();
			player.teleport(player.getKingdom().getSpawn());
		}
	}

	private void handlePlayerSwitch(KingdomFactionsPlayer player) {
		// Handle switch if applicable
		if (player.hasSwitch()) {
			try {
				player.executeSwitch();
			} catch (PlayerException | ValueException e1) {
				Logger.ERROR.log("Error during player switch for " + player.getName());
				e1.printStackTrace();
			}
		}
	}

	private void prepareChat(KingdomFactionsPlayer player) throws ChannelNotFoundException, ChannelRankException {
		// The commented-out chat code can be added when necessary
		// For now, it's left as a placeholder.
	}

	private void handleJoinError(PlayerJoinEvent e, Exception ex) {
		// Log the error and kick the player with a user-friendly message
		Logger.ERROR.log("Exception during player join for " + e.getPlayer().getName());
		ex.printStackTrace();
		e.getPlayer().kickPlayer(ChatColor.RED + "" + ChatColor.BOLD + "The Kingdom Factions \n"
				+ "We konden jouw spelerdata niet laden! Ons excuses. \nProbeer later nog eens te joinen!");
	}
}
