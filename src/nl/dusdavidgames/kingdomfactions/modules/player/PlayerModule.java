package nl.dusdavidgames.kingdomfactions.modules.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.projectiles.ProjectileSource;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.PlayerDatabase;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.UnkownPlayerException;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBanModule;
import nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers.GodModeListener;
import nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers.PlayerDeathEventListener;
import nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers.PlayerJoinEventListener;
import nl.dusdavidgames.kingdomfactions.modules.player.eventhandlers.PlayerQuitEventListener;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.offline.OfflineKingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.StatisticsProfile;
import nl.dusdavidgames.kingdomfactions.modules.player.pvp.PvPManager;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.GodModeRunnable;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.SaveRunnable;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.ScheduledSaveRunnable;

public class PlayerModule {

    private @Getter PlayerList players = new PlayerList();
    private static @Getter @Setter PlayerModule instance;
    private @Getter Queue<KingdomFactionsPlayer> queue = new LinkedList<>();
    
    public boolean saving = false;

    @SuppressWarnings("deprecation")
    public PlayerModule() {
        setInstance(this);
        
        new DeathBanModule();
        
        new GodModeRunnable();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(KingdomFactionsPlugin.getInstance(),
                new ScheduledSaveRunnable(), 0, 300L * 20);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new SaveRunnable(), 0, 20 * 2);

        KingdomFactionsPlugin.getInstance().registerListener(new PlayerJoinEventListener());
        KingdomFactionsPlugin.getInstance().registerListener(new PlayerQuitEventListener());
        KingdomFactionsPlugin.getInstance().registerListener(new PlayerDeathEventListener());
        KingdomFactionsPlugin.getInstance().registerListener(new GodModeListener());
        KingdomFactionsPlugin.getInstance().registerListener(new PvPManager());
    }

    /**
     * General method to get player by name or UUID, reduces code duplication.
     * @param predicate Predicate to apply for comparison.
     * @return KingdomFactionsPlayer or null if not found.
     */
    private KingdomFactionsPlayer getPlayerByPredicate(java.util.function.Predicate<KingdomFactionsPlayer> predicate) {
        for (KingdomFactionsPlayer player : players) {
            if (predicate.test(player)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Get KingdomFactionsPlayer by player name.
     * @param name Name of the player.
     * @return KingdomFactionsPlayer
     * @throws UnkownPlayerException If the player is not found.
     */
    public KingdomFactionsPlayer getPlayer(String name) throws UnkownPlayerException {
        KingdomFactionsPlayer player = getPlayerByPredicate(p -> p.getName().equalsIgnoreCase(name));
        if (player == null) {
            throw new UnkownPlayerException("Player is not online.");
        }
        return player;
    }

    /**
     * Get KingdomFactionsPlayer by UUID.
     * @param uuid UUID of the player.
     * @return KingdomFactionsPlayer or null if not found.
     */
    public KingdomFactionsPlayer getPlayer(UUID uuid) {
        return getPlayerByPredicate(p -> p.getUuid().equals(uuid));
    }

    /**
     * Get KingdomFactionsPlayer from CommandSender.
     * @param sender CommandSender (usually Player).
     * @return KingdomFactionsPlayer
     */
    public KingdomFactionsPlayer getPlayer(CommandSender sender) {
        return getPlayer(sender.getName());
    }

    /**
     * Get player by entity.
     * @param entity Entity to convert to player.
     * @return KingdomFactionsPlayer
     * @throws PlayerException If the entity is not a player.
     */
    public KingdomFactionsPlayer getPlayer(Entity entity) throws PlayerException {
        if (entity instanceof Player) {
            return getPlayer(((Player) entity).getName());
        } else {
            throw new PlayerException("Could not convert this entity to KingdomFactionsPlayer");
        }
    }

    /**
     * Get player by HumanEntity.
     * @param entity HumanEntity to convert to player.
     * @return KingdomFactionsPlayer
     */
    public KingdomFactionsPlayer getPlayer(HumanEntity entity) {
        try {
            return getPlayer(entity.getName());
        } catch (UnkownPlayerException e) {
            return null; // This should not happen
        }
    }

    /**
     * Converts KingdomFactionsPlayer to OfflineKingdomFactionsPlayer.
     * @param player KingdomFactionsPlayer.
     * @return OfflineKingdomFactionsPlayer
     */
    public OfflineKingdomFactionsPlayer convert(KingdomFactionsPlayer player) {
        return new OfflineKingdomFactionsPlayer(player.getName(), player.getUuid(), player.getIpAdres(), 
                player.getKingdom(), player.getFaction(), player.getFactionRank(), player.getKingdomRank(),
                player.getCoins(), player.getStatisticsProfile().getDeaths(), player.getStatisticsProfile().getKills(),
                player.getStatisticsProfile().getFirstjoin(), player.getStatisticsProfile().getSecondsConnected(), 
                player.getInfluence(), player.getLocation());
    }

    /**
     * Converts OfflineKingdomFactionsPlayer to KingdomFactionsPlayer.
     * @param player OfflineKingdomFactionsPlayer.
     * @return KingdomFactionsPlayer or null if not found.
     */
    public KingdomFactionsPlayer convert(OfflineKingdomFactionsPlayer player) {
        try {
            return getPlayer(player.getName());
        } catch (UnkownPlayerException e) {
            return null; // This should not happen
        }
    }

    /**
     * Save all players asynchronously.
     */
    public void saveAsync() {
        for (KingdomFactionsPlayer player : players) {
            if (!queue.contains(player)) { // Avoid redundant queue entries
                queue.offer(player);
            }
        }
    }

    /**
     * Retrieves a player from PlayerDatabase by UUID and name.
     * @param player Player object.
     * @return KingdomFactionsPlayer
     */
    public KingdomFactionsPlayer getPlayer(Player player) {
        KingdomFactionsPlayer p = getPlayerByPredicate(pl -> pl.getUuid().equals(player.getUniqueId()));
        if (p == null) {
            try {
                p = PlayerDatabase.getInstance().loadPlayer(player.getUniqueId());
            } catch (UnkownPlayerException e) {
                KingdomFactionsPlugin.getInstance().getLogger().warning("Failed to load player with UUID: " + player.getUniqueId());
                PlayerDatabase.getInstance().createPlayer(player.getUniqueId() + "", player.getName(), player.getAddress().getHostString());
                p = new KingdomFactionsPlayer(FactionRank.SPELER, KingdomRank.SPELER,
                        KingdomModule.getInstance().getKingdom(KingdomType.GEEN), null, player.getName());
                p.setStatisticsProfile(new StatisticsProfile(p, 0, 0, 0, 0, 0, System.currentTimeMillis()));
            }
            this.players.add(p);
        }
        return p;
    }

    /**
     * Get IPlayerBase by name.
     * @param name Player's name.
     * @return IPlayerBase
     * @throws UnkownPlayerException If the player is not found.
     */
    public IPlayerBase getPlayerBase(String name) throws UnkownPlayerException {
        KingdomFactionsPlayer player = getPlayer(name);
        if (player != null) {
            return player;
        } else {
            return PlayerDatabase.getInstance().loadOfflinePlayer(name);
        }
    }

    /**
     * Get IPlayerBase by UUID.
     * @param uuid Player's UUID.
     * @return IPlayerBase
     * @throws UnkownPlayerException If the player is not found.
     */
    public IPlayerBase getPlayerBase(UUID uuid) throws UnkownPlayerException {
        KingdomFactionsPlayer player = getPlayer(uuid);
        if (player != null) {
            return player;
        } else {
            return PlayerDatabase.getInstance().loadOfflinePlayer(uuid);
        }
    }
}
