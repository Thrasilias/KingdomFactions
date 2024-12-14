package nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.coins.CoinsModule;
import nl.dusdavidgames.kingdomfactions.modules.exception.value.ValueException;
import nl.dusdavidgames.kingdomfactions.modules.influence.InfluenceModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class StatisticsProfile extends Profile {

    @Getter private int kills;
    @Getter private int deaths;
    @Getter @Setter private long firstjoin;
    @Getter @Setter private int coins;
    @Getter @Setter private int influence;
    @Getter @Setter private long lastUpdate = System.currentTimeMillis();
    @Getter @Setter private long secondsConnected = 0;

    public StatisticsProfile(KingdomFactionsPlayer player, int kills, int deaths, int coins, int influence, long secondsConnected, long firstjoin) {
        super(player);
        this.kills = kills;
        this.deaths = deaths;
        this.secondsConnected = secondsConnected;
        this.influence = influence;
        this.coins = coins;
        this.firstjoin = firstjoin;
    }

    public StatisticsProfile(KingdomFactionsPlayer player) {
        this(player, 0, 0, 0, 0, 0, System.currentTimeMillis());
    }

    public void addKill(int kill) {
        this.kills += kill;
    }

    public void addDeath(int death) {
        this.deaths += death;
    }

    public void addInfluence(int influence) {
        InfluenceModule.getInstance().addInfluence(this, influence);
    }

    public void removeInfluence(int influence, boolean mayBeNegative) {
        InfluenceModule.getInstance().removeInfluence(this, influence, mayBeNegative);
    }

    public boolean canAffordInfluence(int influence) {
        return InfluenceModule.getInstance().canAfford(this, influence);
    }

    public boolean canAffordCoins(int coins) {
        return CoinsModule.getInstance().canAfford(this, coins);
    }

    public void addCoins(int coins) {
        CoinsModule.getInstance().addCoins(this, coins);
    }

    public void removeCoins(int coins, boolean mayBeNegative) throws ValueException {
        CoinsModule.getInstance().removeCoins(this, coins, mayBeNegative);
    }
}
