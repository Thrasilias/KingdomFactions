package nl.dusdavidgames.kingdomfactions.modules.viewdistance;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.viewdistance.events.ChunkChangeEvent;
import nl.dusdavidgames.kingdomfactions.modules.viewdistance.events.PlayerJoinEvent;

public class ViewDistanceModule {

    private static @Getter @Setter ViewDistanceModule instance;
    private final int MAX_DISTANCE = 10;
    private final int MIN_DISTANCE = 4; // 3 was agreed upon, but TPS is rarely exactly 20, hence +1

    public ViewDistanceModule() {
        setInstance(this);

        KingdomFactionsPlugin.getInstance().registerListener(new ChunkChangeEvent());
        KingdomFactionsPlugin.getInstance().registerListener(new PlayerJoinEvent());
    }

    public void setViewDistance(KingdomFactionsPlayer player) {
        if (player.isStaff() || player.getKingdomRank().equals(KingdomRank.KONING)) {
            // If the player is staff or a king, allow the maximum view distance
            player.setViewDistance(MAX_DISTANCE);
            return;
        }
        
        // Set the player's view distance dynamically based on the TPS
        int calculatedDistance = getDynamicViewDistance();
        player.setViewDistance(calculatedDistance);
    }

    private int getDynamicViewDistance() {
        // Get the current TPS (Ticks per second)
        double tps = KingdomFactionsPlugin.getInstance().getServer().spigot().getTPS()[0];
        
        // Calculate view distance based on TPS, scaling between MIN and MAX
        double tpsRate = tps / 20 * 100;  // Normalize to percentage of max TPS (20)
        double viewDistance = tpsRate / 100 * 7 + MIN_DISTANCE;
        
        // Ensure the distance doesn't exceed the max
        if (viewDistance > MAX_DISTANCE) {
            return MAX_DISTANCE;
        }

        return (int) viewDistance;
    }
}
