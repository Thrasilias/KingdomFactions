package nl.dusdavidgames.kingdomfactions.modules.war.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.vanish.event.VanishStatusChangeEvent;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.war.War;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;

public class VanishSwitchEventListener implements Listener {

    @EventHandler
    public void onVanishSwitch(VanishStatusChangeEvent e) {
        if (!WarModule.getInstance().isWarActive()) return;

        KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());
        if (player == null) return; // Check if player is valid

        War war = WarModule.getInstance().getWar();
        boolean isVanishing = e.isVanishing();

        // Remove or add the player to the appropriate kingdom's soldier list
        modifySoldierList(war, player, isVanishing);
    }

    private void modifySoldierList(War war, KingdomFactionsPlayer player, boolean isVanishing) {
        // Based on the player's kingdom, add or remove them from the war soldiers list
        switch (player.getKingdom().getType()) {
            case ADAMANTIUM:
                if (isVanishing) war.getAdamantiumSoldiers().remove(player);
                else war.getAdamantiumSoldiers().add(player);
                break;
            case DOK:
                if (isVanishing) war.getDokSoldiers().remove(player);
                else war.getDokSoldiers().add(player);
                break;
            case EREDON:
                if (isVanishing) war.getEredonSoldiers().remove(player);
                else war.getEredonSoldiers().add(player);
                break;
            case GEEN:
                break; // No action needed for 'GEEN'
            case HYVAR:
                if (isVanishing) war.getHyvarSoldiers().remove(player);
                else war.getHyvarSoldiers().add(player);
                break;
            case MALZAN:
                if (isVanishing) war.getMalzanSoldiers().remove(player);
                else war.getMalzanSoldiers().add(player);
                break;
            case TILIFIA:
                if (isVanishing) war.getTilfiaSoldiers().remove(player);
                else war.getTilfiaSoldiers().add(player);
                break;
            default:
                break;
        }

        // Always remove or add the player to the total soldiers list
        if (isVanishing) {
            war.getTotalSoldiers().remove(player);
        } else {
            war.getTotalSoldiers().add(player);
        }
    }
}
