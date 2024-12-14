package nl.dusdavidgames.kingdomfactions.modules.shops.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.ShopDatabase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class AddItemMenu implements Listener {

    private String type;
    private String level;
    private int buyPrice;
    private int sellprice;
    private boolean useDisplayname;
    private String playerName;
    private String extraData;
    private int limit;

    public AddItemMenu(KingdomFactionsPlayer player, String type, String level, int buyPrice, int sellprice,
                       boolean useDisplayname, String extraData, int limit) {
        this.type = type;
        this.level = level;
        this.buyPrice = buyPrice;
        this.sellprice = sellprice;
        this.useDisplayname = useDisplayname;
        this.playerName = player.getName();
        this.extraData = extraData;
        this.limit = limit;

        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Zet het item op het EERSTE slot!");
        player.openInventory(Bukkit.createInventory(null, 9, "addItem " + player.getName()));

        // Register the event listener
        Bukkit.getPluginManager().registerEvents(this, KingdomFactionsPlugin.getInstance());
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Check if the inventory title matches
        if (!event.getInventory().getTitle().equalsIgnoreCase("addItem " + this.playerName)) {
            return;
        }

        ItemStack item = event.getInventory().getItem(0);

        // Validate item presence
        if (item == null || item.getType() == Material.AIR) {
            event.getPlayer().sendMessage("Item NIET gemaakt!");
            unregisterListener();
            return;
        }

        // Process enchantments
        String enchantment = processEnchantments(item);

        // Get display name if present
        String displayname = item.hasItemMeta() && item.getItemMeta().hasDisplayName() 
                            ? item.getItemMeta().getDisplayName() 
                            : "";

        // Handle enchanted books
        if (item.getType() == Material.ENCHANTED_BOOK) {
            enchantment = processEnchantedBook(item);
        }

        // Save the item to the shop database
        ShopDatabase.getInstance().save(type, level, item.getType(), item.getDurability(), item.getAmount(),
                                        buyPrice, sellprice, "", displayname, enchantment, "", useDisplayname, extraData, limit);

        // Inform the player
        event.getPlayer().sendMessage("Item GEMAAKT :D");

        // Unregister the listener
        unregisterListener();
    }

    private String processEnchantments(ItemStack item) {
        StringBuilder enchantment = new StringBuilder();
        for (Enchantment ench : item.getEnchantments().keySet()) {
            enchantment.append("!").append(ench.getName().toUpperCase());
        }
        return enchantment.toString();
    }

    private String processEnchantedBook(ItemStack item) {
        StringBuilder enchantment = new StringBuilder();
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        for (Enchantment ench : meta.getStoredEnchants().keySet()) {
            enchantment.append("!").append(ench.getName().toUpperCase());
        }
        return enchantment.toString();
    }

    // Method to unregister the listener
    private void unregisterListener() {
        HandlerList.unregisterAll(this);
    }
}
