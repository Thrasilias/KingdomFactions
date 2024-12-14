package nl.dusdavidgames.kingdomfactions.modules.influence.runnable;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import org.bukkit.ChatColor;

public class InfluenceRunnable implements Runnable {

    @Override
    public void run() {
        for (KingdomFactionsPlayer p : PlayerModule.getInstance().getPlayers()) {
            IPlayerBase base = p;

            // Ensure thread safety and proper updating of influence
            if (base != null) {
                base.addInfluence(1);

                // Send a chat message to the player notifying them about the increase in influence
                p.getPlayer().sendMessage(ChatColor.GREEN + "Your influence has increased by 1!");
                
                // Optionally, you can customize the message further
                // p.getPlayer().sendMessage(ChatColor.YELLOW + "You now have " + base.getInfluence() + " influence!");
            }
        }
    }
}
