package nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom;

import org.bukkit.ChatColor;

import lombok.Getter;

public enum KingdomType {

    HYVAR(ChatColor.GRAY + "[" + ChatColor.GOLD + "Hyvar" + ChatColor.GRAY + "] ", ChatColor.GOLD, 24),
    EREDON(ChatColor.GRAY + "[" + ChatColor.AQUA + "Eredon" + ChatColor.GRAY + "] ", ChatColor.AQUA, 2),
    TILIFIA(ChatColor.GRAY + "[" + ChatColor.DARK_GREEN + "Tilifia" + ChatColor.GRAY + "] ", ChatColor.DARK_GREEN, 6),
    MALZAN(ChatColor.GRAY + "[" + ChatColor.YELLOW + "Malzan" + ChatColor.GRAY + "] ", ChatColor.YELLOW, 22),
    ADAMANTIUM(ChatColor.GRAY + "[" + ChatColor.DARK_RED + "A" + ChatColor.DARK_GRAY + "damantium" + ChatColor.GRAY + "] ", ChatColor.DARK_GRAY, 20),
    DOK(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Dok" + ChatColor.GRAY + "] ", ChatColor.DARK_PURPLE, 4),
    GEEN(ChatColor.GRAY + "[" + ChatColor.WHITE + "Geen Kingdom" + ChatColor.GRAY + "] ", ChatColor.GRAY, 0),
    ERROR(ChatColor.RED + "ERROR>>", ChatColor.DARK_RED, 0);

    private @Getter String prefix;
    private @Getter ChatColor color;
    private @Getter int menuLocation;

    KingdomType(String prefix, ChatColor color, int menuLocation) {
        this.prefix = prefix != null ? prefix : ChatColor.GRAY + "[Unknown]";
        this.color = color != null ? color : ChatColor.GRAY;
        this.menuLocation = menuLocation;
    }

    public static KingdomType getKingdom(String kingdom) {
        if (kingdom == null) {
            return KingdomType.ERROR;
        }
        for (KingdomType k : KingdomType.values()) {
            if (k.name().equalsIgnoreCase(kingdom)) {
                return k;
            }
        }
        return KingdomType.ERROR;
    }

    public static String getKingdom(KingdomType kingdom) {
        return kingdom != null ? kingdom.name() : KingdomType.ERROR.name();
    }

    public static boolean isKingdom(String kingdom) {
        return getKingdom(kingdom) != KingdomType.ERROR;
    }
}
