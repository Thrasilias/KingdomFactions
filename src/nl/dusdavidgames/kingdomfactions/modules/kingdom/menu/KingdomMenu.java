package nl.dusdavidgames.kingdomfactions.modules.kingdom.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.exception.kingdom.KingdomException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.GlassColor;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.utils.Randomizer;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.Title;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.TitleDuration;

public class KingdomMenu implements Listener {

    private static @Getter @Setter KingdomMenu instance;

    public KingdomMenu() {
        setInstance(this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.PHYSICAL || e.getPlayer().getItemInHand() == null || 
            !e.getPlayer().getItemInHand().getType().equals(Material.COMPASS) || 
            !e.getPlayer().getItemInHand().hasItemMeta() || 
            !e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.RED + "Selecteer jouw kingdom")) {
            return;
        }
        setKingdomMenu(PlayerModule.getInstance().getPlayer(e.getPlayer()));
        e.setCancelled(true);
    }

    public void setKingdomMenu(KingdomFactionsPlayer p) {
        Inventory in = Bukkit.createInventory(null, 45, ChatColor.BLUE + "Kies jouw Kingdom!");
        for (int i = 0; i < 45; i++) {
            in.setItem(i, Item.getInstance().getItem(Material.STAINED_GLASS_PANE, " ", 1, GlassColor.WHITE));
        }

        // Set items for each kingdom in specific slots
        for (KingdomType type : KingdomType.values()) {
            if (type == KingdomType.GEEN || type == KingdomType.ERROR) continue; // Skip invalid kingdoms
            int slot = getKingdomSlot(type);
            Item.getInstance().setPane(type, slot, in);
        }
        p.openInventory(in);
    }

    private int getKingdomSlot(KingdomType type) {
        switch (type) {
            case EREDON: return 2;
            case DOK: return 4;
            case TILIFIA: return 6;
            case ADAMANTIUM: return 20;
            case MALZAN: return 22;
            case HYVAR: return 24;
            case GEEN: return 40; // Random kingdom
            default: return -1; // Invalid kingdom
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!ChatColor.stripColor(e.getInventory().getName()).equalsIgnoreCase("Kies jouw Kingdom!")) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        KingdomFactionsPlayer p = PlayerModule.getInstance().getPlayer(player);
        e.setCancelled(true);

        if (e.getCurrentItem() == null) {
            player.closeInventory();
            return;
        }

        switch (e.getRawSlot()) {
            case 2: selectKingdom(p, KingdomType.EREDON); break;
            case 4: selectKingdom(p, KingdomType.DOK); break;
            case 6: selectKingdom(p, KingdomType.TILIFIA); break;
            case 20: selectKingdom(p, KingdomType.ADAMANTIUM); break;
            case 22: selectKingdom(p, KingdomType.MALZAN); break;
            case 24: selectKingdom(p, KingdomType.HYVAR); break;
            case 40: selectKingdom(p, getRandom()); break;
            default: break;
        }
    }

    private String kingdomString(Kingdom k) {
        return k.getType().getPrefix() + k.getType().name();
    }

    private Kingdom getRandom() {
        ArrayList<Kingdom> kingdoms = new ArrayList<>();
        for (Kingdom k : KingdomModule.getInstance().getKingdoms()) {
            if (k.getType() == KingdomType.GEEN || k.getType() == KingdomType.ERROR) continue;
            kingdoms.add(k);
        }
        Randomizer<Kingdom> random = new Randomizer<>(kingdoms);
        if (!random.hasResult()) {
            handleError(new KingdomException("No kingdoms available"));
            return null;
        }
        return random.result();
    }

    private void selectKingdom(KingdomFactionsPlayer p, Kingdom k) {
        try {
            p.getMembershipProfile().setKingdom(k);
            p.getPlayer().closeInventory();
            p.sendTitle(Title.TITLE, TitleDuration.SHORT, kingdomString(k));
            p.sendMessage(Messages.getInstance().getPrefix() + "Je hebt het kingdom " + kingdomString(k) + ChatColor.WHITE + " gekozen!");
            p.teleport(k.getSpawn());
            clearInventory(p);
        } catch (KingdomException e) {
            handleError(e);
        }
    }

    private void handleError(KingdomException e) {
        // Handle error: log and inform the player
        e.printStackTrace();
        // Optionally, send a message to the player
    }

    private void clearInventory(KingdomFactionsPlayer p) {
        p.getInventory().clear();
        p.getPlayer().getInventory().setHelmet(null);
        p.getPlayer().getInventory().setChestplate(null);
        p.getPlayer().getInventory().setLeggings(null);
        p.getPlayer().getInventory().setBoots(null);
    }
}
