package nl.dusdavidgames.kingdomfactions.modules.wreckingball;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

public class WreckingBallModule {

    // Singleton instance
    public static @Getter @Setter WreckingBallModule instance;

    // Constant for Wrecking Ball item name
    private static final String WRECKINGBALL_NAME = ChatColor.RED + "Wrecking Ball";
    
    // Constructor registers listeners and commands
    public WreckingBallModule() {
        setInstance(this);
        KingdomFactionsPlugin.getInstance().registerListener(new WreckingBallListeners());
        new WreckingBallCommand("wreckingball", "kingdomfactions.command.wreckingball", "Get a wreckingball", "<>", false, false).registerCommand();
    }

    // Method to create and return the Wrecking Ball item
    public ItemStack getWreckingBall() {
        ItemStack wreckingBall = new ItemStack(Material.FIREBALL);
        ItemMeta meta = wreckingBall.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(WRECKINGBALL_NAME);
            meta.setLore(Utils.getInstance().getLore(ChatColor.RED + "Misbruik wordt bestraft! \n Absoluut NIET delen met spelers."));
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            wreckingBall.setItemMeta(meta);
        }
        
        return wreckingBall;
    }
}
