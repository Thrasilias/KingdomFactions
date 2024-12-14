package nl.dusdavidgames.kingdomfactions.modules.shops.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Data;
import nl.dusdavidgames.kingdomfactions.modules.shops.ShopsModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

@Data
public class ShopItem {

    private ItemStack item;
    private ItemStack displayItem;

    private int buyPrice;
    private int sellPrice;

    private int limit = -1;

    private String itemID;

    private int amount;

    public ShopItem(Material material, short value, int amount, int buyPrice, int sellPrice, String displayName,
            List<String> lore, boolean useDisplayName, String enchantments, String enchantmentLevels, int limit,
            String extraData) {
        this.amount = amount;
        this.item = createItem(material, value, displayName, lore, useDisplayName, enchantments, enchantmentLevels, extraData);
        this.displayItem = createDisplayItem(material, value, displayName, lore, enchantments, enchantmentLevels, extraData);

        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.itemID = ShopsModule.getInstance().getItemID(item);
        this.limit = limit;
    }

    private ItemStack createItem(Material material, short value, String displayName, List<String> lore,
            boolean useDisplayName, String enchantments, String enchantmentLevels, String extraData) {
        ItemStack item = new ItemStack(material, amount, value);
        ItemMeta itemMeta = item.getItemMeta();

        if (useDisplayName) {
            itemMeta.setDisplayName(displayName);
        }

        if (!extraData.trim().isEmpty() && material == Material.MOB_SPAWNER) {
            itemMeta.setLore(Arrays.asList("Spawner: " + extraData));
        }

        item.setItemMeta(itemMeta);
        addEnchantments(enchantments, enchantmentLevels, item);
        return item;
    }

    private ItemStack createDisplayItem(Material material, short value, String displayName, List<String> lore,
            String enchantments, String enchantmentLevels, String extraData) {
        lore.add("Linker muis knop - kopen " + buyPrice + " coins");
        if (sellPrice > 0)
            lore.add("Rechter muis knop - verkopen " + sellPrice + " coins");

        if (!extraData.trim().isEmpty() && material == Material.MOB_SPAWNER) {
            lore.add(" ", "Spawner: " + extraData);
        }

        if (isLimited()) {
            lore.add("Jouw faction kan dit item maximaal " + limit + "X kopen");
        }

        ItemStack shopItem = new ItemStack(material, amount, value);
        ItemMeta shopItemMeta = shopItem.getItemMeta();
        shopItemMeta.setDisplayName(displayName);
        shopItemMeta.setLore(lore);
        shopItem.setItemMeta(shopItemMeta);

        addEnchantments(enchantments, enchantmentLevels, shopItem);
        return shopItem;
    }

    private void addEnchantments(String enchantments, String levels, ItemStack item) {
        if (enchantments == null || levels == null || enchantments.trim().isEmpty())
            return;

        try {
            String[] enchantmentList = enchantments.split("!");
            String[] levelList = levels.split("!");

            for (int i = 0; i < enchantmentList.length; i++) {
                String enchantmentName = enchantmentList[i].trim();
                String levelStr = levelList[i].trim();

                if (!enchantmentName.isEmpty() && !levelStr.isEmpty()) {
                    Enchantment enchantment = Enchantment.getByName(enchantmentName);
                    if (enchantment == null) {
                        Logger.ERROR.log("Invalid enchantment: " + enchantmentName);
                        continue;
                    }
                    int level = Integer.parseInt(levelStr);
                    if (item.getType() == Material.ENCHANTED_BOOK) {
                        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
                        meta.addStoredEnchant(enchantment, level, true);
                        item.setItemMeta(meta);
                    } else {
                        item.addEnchantment(enchantment, level);
                    }
                }
            }
        } catch (Exception e) {
            Logger.ERROR.log("Error adding enchantments to item " + item.getType() + ": " + e.getMessage());
        }
    }

    public boolean isLimited() {
        return this.limit != -1;
    }
}
