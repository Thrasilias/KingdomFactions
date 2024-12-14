package nl.dusdavidgames.kingdomfactions.modules.shops.shop;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.ShopDatabase;
import nl.dusdavidgames.kingdomfactions.modules.empirewand.EmpireWandModule;
import nl.dusdavidgames.kingdomfactions.modules.exception.value.ValueException;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildLevel;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildingType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.shops.ShopsModule;
import nl.dusdavidgames.kingdomfactions.modules.shops.shoplogger.ShopLog;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.ShopAction;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class Shop implements Listener {

    private @Getter BuildingType buildingType;
    private @Getter BuildLevel buildLevel;

    private ArrayList<ShopItem> shopItems = new ArrayList<>();

    private @Getter Inventory shopInventory;
    private int inventorySize = 9;

    private @Getter final String TITLE;

    public Shop(BuildingType type, BuildLevel level) {
        this.buildingType = type;
        this.buildLevel = level;
        this.TITLE = ChatColor.AQUA + "Shop " + type + " " + level.getRoman();

        loadShopItems();

        setupInventory();

        Bukkit.getPluginManager().registerEvents(this, KingdomFactionsPlugin.getInstance());
    }

    private void loadShopItems() {
        if (this.buildingType == BuildingType.NEXUS && this.buildLevel == BuildLevel.LEVEL_8) {
            this.shopItems.add(new EmpireWandShopItem());
        }
        this.shopItems.addAll(ShopDatabase.getInstance().loadShopItems(this.buildingType, this.buildLevel));
    }

    private void setupInventory() {
        this.shopInventory = Bukkit.createInventory(null, this.inventorySize, this.TITLE);
        if (this.shopItems.size() > this.shopInventory.getSize()) {
            this.inventorySize += 9;
            setupInventory();
            return;
        }

        for (ShopItem shopItem : this.shopItems) {
            shopInventory.addItem(shopItem.getDisplayItem());
            Logger.DEBUG.log("new shop item added to inventory for " + TITLE);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event) throws ValueException {
        if (!(event.getWhoClicked() instanceof Player))
            return;

        if (!event.getInventory().getTitle().equalsIgnoreCase(this.TITLE))
            return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null)
            return;

        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(event.getWhoClicked());

        // buy
        if (event.isLeftClick()) {
            ShopItem shopItem = getShopItem(event.getCurrentItem());
            if (shopItem == null)
                return;

            addItems(player, shopItem.getItem().getType(), shopItem.getItem().getAmount(),
                    shopItem.getItem().getDurability(), shopItem.getBuyPrice(), shopItem);
        }

        // sell
        if (event.isRightClick()) {
            ShopItem shopItem = getShopItem(event.getCurrentItem());
            if (shopItem == null)
                return;
            if (shopItem.getSellPrice() <= 0)
                return;
            removeItems(player, shopItem.getItem().getType(), shopItem.getItem().getAmount(),
                    shopItem.getItem().getDurability(), shopItem.getSellPrice(), shopItem);
        }
    }

    private ShopItem getShopItem(ItemStack itemstack) {
        if (itemstack == null)
            return null;
        for (ShopItem shopItem : shopItems) {
            if (shopItem.getDisplayItem().isSimilar(itemstack) || shopItem.getItem().isSimilar(itemstack))
                return shopItem;
        }

        return null;
    }

    public void removeItems(KingdomFactionsPlayer p, Material material, int amount, short data, int coins,
            ShopItem shopItem) {
        Inventory inv = p.getInventory();
        ItemStack itemToRemove = new ItemStack(material, amount, data);

        // Find and remove the item from the inventory
        int removedAmount = findAndRemoveItem(inv, itemToRemove, coins, shopItem);

        if (removedAmount > 0) {
            p.addCoins(coins); // Add coins to player
            new ShopLog(p, shopItem, ShopAction.SELL);
            p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt " + removedAmount + " item(s) verkocht.");
        } else {
            p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt niet genoeg items.");
        }
    }

    private int findAndRemoveItem(Inventory inv, ItemStack itemToRemove, int coins, ShopItem shopItem) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack inventoryItem = inv.getItem(i);
            if (inventoryItem != null && inventoryItem.isSimilar(itemToRemove)) {
                int amountToRemove = Math.min(itemToRemove.getAmount(), inventoryItem.getAmount());
                inventoryItem.setAmount(inventoryItem.getAmount() - amountToRemove);
                return amountToRemove;
            }
        }
        return 0;
    }

    public void addItems(KingdomFactionsPlayer p, Material material, int amount, short data, int coins,
            ShopItem shopItem) throws ValueException {
        if (!p.canAfford(coins)) {
            p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt minimaal " + coins + " coins nodig!");
            return;
        }

        if (shopItem.isLimited() && !canBuyLimitedItem(p, shopItem)) {
            return;
        }

        // Add item to player's inventory
        ItemStack item = new ItemStack(material, amount, data);
        if (!addItemToInventory(p, item)) {
            p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt geen ruimte in je inventaris.");
            p.getPlayer().closeInventory();
            return;
        }

        p.removeCoins(coins, false);
        new ShopLog(p, shopItem, ShopAction.PURCHASE);
        p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt " + amount + " item(s) gekocht.");
    }

    private boolean addItemToInventory(KingdomFactionsPlayer p, ItemStack item) {
        Inventory inv = p.getInventory();
        int freeSlot = inv.firstEmpty();
        if (freeSlot == -1) return false;

        inv.setItem(freeSlot, item);
        return true;
    }

    private boolean canBuyLimitedItem(KingdomFactionsPlayer p, ShopItem shopItem) {
        if (!ShopsModule.getInstance().canBuy(shopItem, p.getFaction())) {
            p.sendMessage(Messages.getInstance().getPrefix()
                    + "Je hebt het maximale inkoop aantal bereikt voor dit item!");
            return false;
        }

        if (p.getFactionRank() != FactionRank.LEADER && p.getFactionRank() != FactionRank.OFFICER) {
            p.sendMessage(Messages.getInstance().getPrefix()
                    + "Alleen een faction Leider of Officier kan een limited items kopen!");
            return false;
        }

        return true;
    }

    public void reload() {
        if (!shopInventory.getViewers().isEmpty()) {
            for (int i = 0; i < shopInventory.getViewers().size(); i++) {
                shopInventory.getViewers().get(i).closeInventory();
            }
        }
        shopItems.clear();
        loadShopItems();
        setupInventory();
    }

    public int getShopItemAmount() {
        return this.shopItems.size();
    }
}
