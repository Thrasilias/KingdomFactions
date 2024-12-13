package nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.YesNoListener;

public class LeaderListener implements YesNoListener {

    private IPlayerBase other;

    public LeaderListener(IPlayerBase other) {
        this.other = other;
    }

    @Override
    public void onAgree(KingdomFactionsPlayer player) {
        if (player.getMembershipProfile().isFactionLeader()) {
            // Check if the player and the other player are in the same faction
            if (player.getFaction() == null || other.getFaction() == null || !player.getFaction().equals(other.getFaction())) {
                player.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je kunt het leiderschap alleen overdragen binnen dezelfde faction.");
                closeInventory(player);
                return;
            }

            // Transfer leadership
            player.setFactionRank(FactionRank.OFFICER);
            other.setFactionRank(FactionRank.LEADER);
            player.getFaction().broadcast(Messages.getInstance().getPrefix(),
                    player.getName() + " heeft het Faction leiderschap over gedragen aan " + other.getName());

            // Log leadership transfer (optional for debugging)
            Bukkit.getLogger().info(player.getName() + " has transferred faction leadership to " + other.getName());
        } else {
            player.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je bent geen faction leider.");
        }

        closeInventory(player);
    }

    @Override
    public void onDeny(KingdomFactionsPlayer player) {
        // Provide feedback to the player if they deny the action
        player.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Leiderschapsoverdracht geannuleerd.");
        closeInventory(player);
    }

    @Override
    public void onClose(KingdomFactionsPlayer player) {
        // Optional: Provide additional logic when the inventory is closed (e.g., canceling action or sending message)
    }

    private void closeInventory(KingdomFactionsPlayer player) {
        // Close the inventory after a short delay to ensure smooth UI behavior
        Bukkit.getScheduler().runTaskLater(KingdomFactionsPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                Player p = player.getPlayer();
                if (p != null) {
                    p.closeInventory();
                }
            }
        }, 5);
    }
}
