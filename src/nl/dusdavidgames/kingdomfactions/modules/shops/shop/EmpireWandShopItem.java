package nl.dusdavidgames.kingdomfactions.modules.shops.shop;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.empirewand.EmpireWandModule;

/**
 * Represents the Empire Wand item for sale in the shop.
 */
public class EmpireWandShopItem extends ShopItem {

    /**
     * Creates an instance of the Empire Wand Shop item.
     * Sets the price, display name, and basic details.
     */
    public EmpireWandShopItem() {
        super(Material.BLAZE_ROD, (short) 0, 1, 100000, 0, ChatColor.RED + "Empire Wand", new ArrayList<String>(), true, "", "", -1, "");
        // You may want to adjust the price based on game conditions or server economy
    }

    /**
     * Returns the Empire Wand item for purchase.
     * 
     * @return The Empire Wand as an ItemStack.
     */
    @Override
    public ItemStack getItem() {
        return EmpireWandModule.getInstance().getEmpireWand();
    }

    /**
     * Optionally add lore or description to enhance the item’s usability.
     * This could include functionality descriptions or lore for the player.
     * @return The lore for the Empire Wand item.
     */
    public ArrayList<String> getLore() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "A magical wand to control your empire.");
        lore.add(ChatColor.GRAY + "Right-click to activate special powers.");
        return lore;
    }
}
