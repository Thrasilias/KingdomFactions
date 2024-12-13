package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;

public class Item {

    private static @Getter @Setter Item instance;

    public Item() {
        setInstance(this);
    }

    // Generic method to create ItemStack
    public ItemStack getItem(Material material, String name, int amount, List<String> lore, HashMap<Enchantment, Integer> enchantments, boolean hideEnchantments) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();

        if (itemMeta != null) {
            itemMeta.setDisplayName(name);
            if (lore != null) itemMeta.setLore(lore);
            enchantments.forEach((enchantment, level) -> itemMeta.addEnchant(enchantment, level, true));

            if (hideEnchantments) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            item.setItemMeta(itemMeta);
        }

        return item;
    }

    // Overloaded method for convenience
    public ItemStack getItem(Material material, String name, int amount) {
        return getItem(material, name, amount, null, new HashMap<>(), false);
    }

    // Overloaded method for adding enchantments
    public ItemStack getItem(Material material, String name, int amount, Enchantment enchantment, int level) {
        HashMap<Enchantment, Integer> enchantments = new HashMap<>();
        enchantments.put(enchantment, level);
        return getItem(material, name, amount, null, enchantments, false);
    }

    // Special method for KingdomType glass panes
    public void setPane(KingdomType type, int slot, Inventory inventory) {
        Material material = Material.STAINED_GLASS_PANE; // Update to a newer material if needed
        String name = "";
        ChatColor color = ChatColor.GRAY;

        switch (type) {
            case ADAMANTIUM:
                name = ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "damantium";
                color = ChatColor.GRAY;
                break;
            case DOK:
                name = ChatColor.DARK_PURPLE + "Dok";
                color = ChatColor.PURPLE;
                break;
            case EREDON:
                name = ChatColor.AQUA + "Eredon";
                color = ChatColor.LIGHT_BLUE;
                break;
            case HYVAR:
                name = ChatColor.GOLD + "Hyvar";
                color = ChatColor.ORANGE;
                break;
            case MALZAN:
                name = ChatColor.YELLOW + "Malzan";
                color = ChatColor.YELLOW;
                break;
            case TILIFIA:
                name = ChatColor.DARK_GREEN + "Tilifia";
                color = ChatColor.GREEN;
                break;
            case GEEN:
                name = ChatColor.RED + "Random";
                color = ChatColor.RED;
                break;
            default:
                break;
        }

        ItemStack pane = getItem(material, name, 1, color, false); // Color logic can be customized as needed
        inventory.setItem(slot, pane);
    }
}
