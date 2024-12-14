package nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.action.IAction;

public class SetCapitalAction implements IAction {

    private final Kingdom kingdom;
    private final KingdomFactionsPlayer player;

    @Getter @Setter
    private Location location;

    public SetCapitalAction(Kingdom kingdom, KingdomFactionsPlayer player) {
        this.kingdom = kingdom;
        this.player = player;
    }

    @Override
    public void execute() {
        if (kingdom == null || location == null) {
            // Handle error if kingdom or location is invalid
            player.sendMessage("Invalid kingdom or location.");
            return;
        }
        
        if (!player.hasPermission("kingdom.setcapital")) {
            // Handle lack of permission
            player.sendMessage("You do not have permission to set the capital.");
            return;
        }

        this.kingdom.getNexus().setLocation(location);
        this.kingdom.getNexus().save();
        player.sendMessage("Capital set successfully.");
        cancel();
    }

    @Override
    public void cancel() {
        this.player.setAction(null);
    }
}
