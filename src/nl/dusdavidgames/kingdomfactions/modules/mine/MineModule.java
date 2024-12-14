package nl.dusdavidgames.kingdomfactions.modules.mine;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.mine.command.MineCommand;
import nl.dusdavidgames.kingdomfactions.modules.mine.listeners.PlayerTeleportEventListener;
import nl.dusdavidgames.kingdomfactions.modules.mine.listeners.PortalProtection;
import nl.dusdavidgames.kingdomfactions.modules.mine.listeners.PortalTravelEventListener;
import nl.dusdavidgames.kingdomfactions.modules.mine.listeners.WorldProtection;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class MineModule {

    private static @Getter @Setter MineModule instance; 

    public MineModule() {
        setInstance(this);
        new MineCommand("kingdomfactions.command.mine", "zet de mijn spawn", "[kingdom]", false, false).registerCommand();
        KingdomFactionsPlugin.getInstance().registerListener(new PortalTravelEventListener());
        KingdomFactionsPlugin.getInstance().registerListener(new PortalProtection());
        KingdomFactionsPlugin.getInstance().registerListener(new WorldProtection());
        KingdomFactionsPlugin.getInstance().registerListener(new PlayerTeleportEventListener());
    }

    public boolean isMinePortal(Location location) {
        return location.getBlock().getType() == Material.PORTAL;
    }

    public boolean containsMinePortal(List<Location> locations) {
        for (Location loc : locations) {
            if (isMinePortal(loc)) {
                return true; // Return immediately when a portal is found
            }
        }
        return false; // Return false if no portal is found
    }

    public void teleportToMineWorld(KingdomFactionsPlayer player) {
        // Example: Get the mine world spawn location and teleport the player there
        Location mineWorldSpawn = getMineWorldSpawnLocation();
        player.teleport(mineWorldSpawn);
    }

    private Location getMineWorldSpawnLocation() {
        // Define the spawn location for the mine world (this could be configurable)
        return new Location(Bukkit.getWorld("mine_world"), 0, 100, 0); // Example coordinates
    }
}
