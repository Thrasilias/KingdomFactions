package nl.dusdavidgames.kingdomfactions.modules.kingdom.menu;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;

import java.util.HashMap;
import java.util.Map;

public class KingdomItem {

    private @Getter KingdomType type;
    private @Getter ItemStack item;
    private @Getter int slot;

    private static final Map<KingdomType, String> kingdomNames = new HashMap<>();
    private static final Map<KingdomType, Integer> kingdomColors = new HashMap<>();

    static {
        kingdomNames.put(KingdomType.ADAMANTIUM, ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "damantium");
        kingdomColors.put(KingdomType.ADAMANTIUM, 15);

        kingdomNames.put(KingdomType.DOK, ChatColor.DARK_PURPLE + "Dok");
        kingdomColors.put(KingdomType.DOK, 10);

        kingdomNames.put(KingdomType.EREDON, ChatColor.AQUA + "Eredon");
        kingdomColors.put(KingdomType.EREDON, 3);

        kingdomNames.put(KingdomType.HYVAR, ChatColor.GOLD + "Hyvar");
        kingdomColors.put(KingdomType.HYVAR, 1);

        kingdomNames.put(KingdomType.MALZAN, ChatColor.YELLOW + "Malzan");
        kingdomColors.put(KingdomType.MALZAN, 4);

        kingdomNames.put(KingdomType.TILIFIA, ChatColor.DARK_GREEN + "Tilifia");
        kingdomColors.put(KingdomType.TILIFIA, 13);

        // Default values for unknown or unimplemented kingdoms
        kingdomNames.put(KingdomType.GEEN, ChatColor.GRAY + "Geen Kingdom");
        kingdomColors.put(KingdomType.GEEN, 0);
    }

    public KingdomItem(KingdomType type, int slot) {
        this.type = type;
        this.slot = slot;
        this.item = createItem(type);
    }

    private ItemStack createItem(KingdomType type) {
        String name = kingdomNames.getOrDefault(type, ChatColor.GRAY + "Unknown Kingdom");
        int color = kingdomColors.getOrDefault(type, 0);
        return Item.getInstance().getItem(Material.STAINED_GLASS_PANE, name, 1, color);
    }
}
