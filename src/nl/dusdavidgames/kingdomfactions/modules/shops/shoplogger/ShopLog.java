package nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import lombok.Data;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.shops.shop.ShopItem;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.ShopAction;

@Data
public class ShopLog {

    private ShopItem shopItem;
    private UUID offlineUUID;
    private String playerName;
    private String date;
    private ShopAction shopAction;
    private int coins = -1;

    public ShopLog(KingdomFactionsPlayer kdfPlayer, ShopItem shopItem, ShopAction shopAction) {
        if (kdfPlayer == null || shopItem == null || shopAction == null) {
            throw new IllegalArgumentException("Player, shopItem, and shopAction cannot be null.");
        }
        
        this.shopItem = shopItem;
        this.shopAction = shopAction;

        this.coins = (shopAction == ShopAction.PURCHASE) ? shopItem.getBuyPrice() : shopItem.getSellPrice();

        OfflinePlayer player = kdfPlayer.getPlayer();
        if (player == null) {
            throw new IllegalStateException("Player is offline or does not exist.");
        }

        this.offlineUUID = player.getUniqueId();
        this.playerName = player.getName();

        // Use Instant and SimpleDateFormat to get a more precise date-time string
        Instant now = Instant.now();
        this.date = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.MEDIUM)
                                    .format(Date.from(now));

        // Add this log to the ShopLogger instance
        ShopLogger.getInstance().getShopLogs().add(this);
    }
}
