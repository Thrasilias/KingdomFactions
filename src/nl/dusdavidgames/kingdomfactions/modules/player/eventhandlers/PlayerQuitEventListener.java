package nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.chat.ChatModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class PlayerQuitEventListener implements Listener {

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		e.setQuitMessage(null);  // Hide quit message

		KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());

		// Handle player combat tracker
		if (player.getCombatTracker().isInCombat()) {
			handleCombatDisconnect(player);
		} else {
			handleRegularDisconnect(player);
		}
	}

	private void handleCombatDisconnect(KingdomFactionsPlayer player) {
		// Handle combat disconnect scenario
		player.getCombatTracker().handleDisconnect();
		Bukkit.getScheduler().runTaskLater(KingdomFactionsPlugin.getInstance(), () -> {
			leaveAllChannels(player);
			player.saveLogOut();
		}, 40L);  // 2-second delay
	}

	private void handleRegularDisconnect(KingdomFactionsPlayer player) {
		// Handle regular disconnect scenario
		leaveAllChannels(player);
		player.saveLogOut();
	}

	private void leaveAllChannels(KingdomFactionsPlayer player) {
		// Make the player leave all chat channels
		ChatModule.getInstance().getChannels().forEach(channel -> channel.leave(player, false));
	}
}
