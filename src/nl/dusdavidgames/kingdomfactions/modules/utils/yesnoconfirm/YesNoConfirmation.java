package nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

public class YesNoConfirmation {

    private static final int INVENTORY_SIZE = 27; // Defined constant for inventory size
    private static final int TITLE_SLOT = 4; // Position of the title item
    private static final int YES_SLOT = 11;  // Position for "Yes" option
    private static final int NO_SLOT = 15;   // Position for "No" option

    private @Getter KingdomFactionsPlayer player;
    private @Getter String title;
    private @Getter ArrayList<String> yesLore;
    private @Getter ArrayList<String> noLore;
    private YesNoListener listener;

    // Constructor to initialize YesNoConfirmation with ArrayLists for yes and no lore
    public YesNoConfirmation(KingdomFactionsPlayer player, String title, ArrayList<String> yesLore, ArrayList<String> noLore, YesNoListener listener) {
        this.player = player;
        this.title = title;
        this.yesLore = yesLore != null ? yesLore : new ArrayList<>();  // Prevent null lore
        this.noLore = noLore != null ? noLore : new ArrayList<>();
        this.listener = listener;

        this.player.setYesNoConfirmation(this);
        this.player.openInventory(getMenu());
    }

    // Constructor for String lore, which converts it into ArrayLists
    public YesNoConfirmation(KingdomFactionsPlayer player, String title, String yesLore, String noLore, YesNoListener listener) {
        this(player, title, Utils.getInstance().getLore(yesLore), Utils.getInstance().getLore(noLore), listener);
    }

    // Method called when the user agrees
    public void callYes() {
        remove();
        listener.onAgree(player);
    }

    // Method called when the user denies
    public void callNo() {
        remove();
        listener.onDeny(player);
    }

    // Method called when the inventory is closed
    public void callClose() {
        remove();
        listener.onClose(player);
    }

    // Remove the confirmation menu and reset the player's state
    public void remove() {
        this.player.setYesNoConfirmation(null);
        this.player.getPlayer().closeInventory();
    }

    // Create and return the Yes/No confirmation menu inventory
    public Inventory getMenu() {
        Inventory inventory = Bukkit.createInventory(null, INVENTORY_SIZE, ChatColor.RED + "Weet je het zeker?");
        
        // Fill the inventory with glass panes as the background
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            inventory.setItem(i, Item.getInstance().getItem(Material.STAINED_GLASS_PANE, " ", 1, 0));
        }

        // Set title in the center of the inventory
        inventory.setItem(TITLE_SLOT, Item.getInstance().getItem(Material.NETHER_STAR, title, 1));

        // Set the "Yes" and "No" buttons with corresponding lore
        inventory.setItem(YES_SLOT, Item.getInstance().getItem(Material.EMERALD_BLOCK, ChatColor.GREEN + "Ja.", 1, yesLore));
        inventory.setItem(NO_SLOT, Item.getInstance().getItem(Material.REDSTONE_BLOCK, ChatColor.RED + "Nee.", 1, noLore));

        return inventory;
    }
}
