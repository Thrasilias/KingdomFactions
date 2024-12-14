package nl.dusdavidgames.kingdomfactions.modules.shops;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildLevel;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildingType;
import nl.dusdavidgames.kingdomfactions.modules.shops.commands.ShopCommand;
import nl.dusdavidgames.kingdomfactions.modules.shops.events.BlockPlace;
import nl.dusdavidgames.kingdomfactions.modules.shops.shop.Shop;
import nl.dusdavidgames.kingdomfactions.modules.shops.shop.ShopItem;
import nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger.ShopLogger;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class ShopsModule {

    private static @Getter @Setter ShopsModule instance;

    // Using a Map for quicker lookup
    private @Getter Map<String, Shop> shopsByName = new HashMap<>();
    private @Getter Map<String, Shop> shopsByTypeAndLevel = new HashMap<>();

    public ShopsModule() {
        setInstance(this);

        new ShopLogger();

        // Initialize shops based on building type and level
        for (BuildingType type : BuildingType.values()) {
            for (BuildLevel level : BuildLevel.values()) {
                Shop shop = new Shop(type, level);
                this.shopsByName.put(shop.getTITLE(), shop);
                this.shopsByTypeAndLevel.put(type.name() + "_" + level.name(), shop);
            }
        }

        new ShopCommand("shop", "kingdomfactions.command.shop", "Main command for shops", "", true, true).registerCommand();

        Bukkit.getPluginManager().registerEvents(new BlockPlace(), KingdomFactionsPlugin.getInstance());
    }

    public Shop getShop(BuildingType type, BuildLevel level) {
        return shopsByTypeAndLevel.get(type.name() + "_" + level.name());
    }

    public Shop getShop(String name) {
        return this.shopsByName.get(name);
    }

    public void reload(CommandSender sender) {
        for (Shop shop : this.shopsByName.values()) {
            sender.sendMessage(ChatColor.GOLD + "Herladen van shop " + shop.getBuildingType() + " " + shop.getBuildLevel() + ".");
            shop.reload();
            sender.sendMessage(ChatColor.GOLD + "Shop " + shop.getBuildingType() + " " + shop.getBuildLevel() + " herladen!");
        }
        sender.sendMessage(Messages.getInstance().getPrefix() + "Alle shops herladen!");
    }

    public String getItemID(ItemStack is) {
        return is.getDurability() + "" + is.getType() + "" + is.getAmount();
    }

    public boolean canBuy(ShopItem shopItem, Faction faction) {
        String itemID = shopItem.getItemID();
        Map<String, Integer> shopLimits = faction.getShopLimits();
        
        int current = shopLimits.getOrDefault(itemID, 0);
        int max = shopItem.getLimit();

        if (current < max) {
            shopLimits.put(itemID, current + 1);
            return true;
        }
        
        return false;
    }
}
