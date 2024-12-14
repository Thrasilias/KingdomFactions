package nl.dusdavidgames.kingdomfactions.modules.scoreboard;

import org.bukkit.World;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.Nexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.InhabitableType;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;

public class ScoreboardModule {

	private static @Getter @Setter ScoreboardModule instance;

	public ScoreboardModule() {
		setInstance(this);
		KingdomFactionsPlugin.getInstance().registerListener(new ScoreboardListeners());
	}
	
	// Sets up the scoreboard for a player
	public void setupScoreboard(KingdomFactionsPlayer player) {
		ScoreBoard board = new ScoreBoard("The Kingdom Factions", player);
		board.addBlankLine(13);
		board.addLine(ChatColor.GRAY + "Oorlog: ", 12);
		board.addLine(getWarState(), 11);
		board.addBlankLine(10);
		board.addLine(ChatColor.GRAY + "Faction:", 9);
		board.addLine(getFaction(player.getKingdom().getType(), player.getFaction()), 8);
		board.addBlankLine(7);
		board.addLine(ChatColor.GRAY + "Locatie:", 6);
		board.addLine(ChatColor.GRAY + "Wereld: " + getWorld(player.getLocation().getWorld()), 5);
		board.addLine(ChatColor.GRAY + getTerritory(player.getTerritoryId(), player.getKingdomTerritory()), 4);
		board.addBlankLine(3);
		board.addLine(ChatColor.GRAY + "Coins: " + ChatColor.RED + player.getStatisticsProfile().getCoins(), 2);
		board.addLine(ChatColor.GRAY + "Influence: " + ChatColor.RED + player.getStatisticsProfile().getInfluence(), 1);
		player.setScoreBoard(board);
		player.getScoreboard().refreshTags();
	}

	// Returns the player's faction display string
	private String getFaction(KingdomType k, Faction f) {
		if (f == null) {
			return k.getColor() + "Geen Faction!";
		} else {
			return ChatColor.GRAY + "[" + k.getColor() + f.getName() + ChatColor.GRAY + "]";
		}
	}

	// Returns the territory display string based on the player's location and territory ID
	public String getTerritory(String id, KingdomType k) {
		if (id.equalsIgnoreCase("~MINING~") || k == KingdomType.GEEN || k == KingdomType.ERROR) {
			return ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "Wildernis" + ChatColor.GRAY + "]";
		}

		INexus n = NexusModule.getInstance().getINexus(id);
		if (n == null) {
			return k.getPrefix();
		}

		if (n instanceof Nexus) {
			if (((Nexus) n).getOwnerId().equalsIgnoreCase("Wilderness")) {
				return k.getPrefix().replace("]", "") + ChatColor.GRAY + "/ Onbekend" + ChatColor.GRAY + "]";
			}
		}

		if (n.getOwner() == null) {
			return k.getPrefix();
		}

		if (n.getOwner().getInhabitableType() == InhabitableType.FACTION) {
			Faction f = (Faction) n.getOwner();
			return k.getPrefix().replace("]", "") + ChatColor.GRAY + "/ " + f.getStyle().getColor() + f.getName() + ChatColor.GRAY + "]";
		} else if (n.getOwner().getInhabitableType() == InhabitableType.KINGDOM) {
			Kingdom kingdom = (Kingdom) n.getOwner();
			return kingdom.getType().getPrefix().replace("]", "") + ChatColor.GRAY + "/ " + ChatColor.RED + "Hoofdstad" + ChatColor.GRAY + "]";
		}

		return k.getPrefix();
	}

	// Returns the current war state or "Geen Oorlog" if no war is active
	public String getWarState() {
		if (WarModule.getInstance().isWarActive()) {
			return ChatColor.GRAY + "Resterende Minuten: " + WarModule.getInstance().getWar().getTime();
		}
		return ChatColor.GRAY + "Geen Oorlog";
	}

	// Returns the world display string for the player's current world
	public String getWorld(World w) {
		if (w == Utils.getInstance().getOverWorld()) {
			return ChatColor.GRAY + "[" + ChatColor.GREEN + "Ranos" + ChatColor.GRAY + "]";
		} else {
			return ChatColor.GRAY + "[" + ChatColor.RED + "Mithras" + ChatColor.GRAY + "]";
		}
	}

	// Updates the scoreboard for a player with the latest information
	public void updateScoreboard(KingdomFactionsPlayer p) {
		p.getScoreboard().editLine(2, ChatColor.GRAY + "Coins: " + ChatColor.RED + p.getStatisticsProfile().getCoins());
		p.getScoreboard().editLine(1, ChatColor.GRAY + "Influence: " + ChatColor.RED + p.getStatisticsProfile().getInfluence());
		p.getScoreboard().editLine(5, ChatColor.GRAY + "Wereld: " + getWorld(p.getLocation().getWorld()));
		p.getScoreboard().editLine(4, getTerritory(p.getTerritoryId(), p.getKingdomTerritory()));

		Faction f = p.getFaction();
		String factionLine = (f != null) ? ChatColor.GRAY + "[" + p.getKingdom().getType().getColor() + f.getName() + ChatColor.GRAY + "]" : p.getKingdom().getType().getColor() + "Geen Faction!";
		p.getScoreboard().editLine(8, factionLine);
	}
}
