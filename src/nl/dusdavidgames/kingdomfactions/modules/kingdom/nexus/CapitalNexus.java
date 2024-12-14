package nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.KingdomDatabase;
import nl.dusdavidgames.kingdomfactions.modules.exception.DataException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.guardian.CapitalGuard;
import nl.dusdavidgames.kingdomfactions.modules.monster.GuardType;
import nl.dusdavidgames.kingdomfactions.modules.monster.IGuard;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.IInhabitable;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.NexusType;
import nl.dusdavidgames.kingdomfactions.modules.utils.particles.ParticleEffect;

public class CapitalNexus implements INexus {

    private @Getter Location location;
    private @Setter @Getter int health;
    private @Setter @Getter KingdomType kingdom;
    private @Setter @Getter String nexusId;
    private boolean destroy;

    private @Getter ArrayList<IGuard> guards = new ArrayList<>();

    private static final int DEFAULT_RADIUS = 400;
    private static final int GUARD_SPAWN_RADIUS = 4;
    private static final int GUARD_COUNT = 5;

    public CapitalNexus(Location location, KingdomType kingdom) {
        this.location = location;
        this.health = 800;
        this.kingdom = kingdom;
        this.nexusId = kingdom.toString();
    }

    @Override
    public Location getNexusLocation() {
        return location;
    }

    @Override
    public int getProtectedRadius() {
        try {
            return KingdomFactionsPlugin.getInstance().getDataManager().getInteger("kingdom.capital.protected_region");
        } catch (DataException e) {
            e.printStackTrace();
            return DEFAULT_RADIUS;  // Default radius if fetching fails
        }
    }

    public double getDistance(KingdomFactionsPlayer player) {
        return getDistance(player.getLocation());
    }

    public double getDistance(Location location) {
        Location loc = Utils.getInstance().getNewLocation(location);
        Location nexus = Utils.getInstance().getNewLocation(this.getNexusLocation());
        loc.setY(0);
        nexus.setY(0);
        return nexus.distance(loc);
    }

    @Override
    public NexusType getType() {
        return NexusType.CAPITAL;
    }

    public boolean isDestroyed() {
        return destroy;
    }

    public void setDestroyed(boolean destroy) {
        this.destroy = destroy;
    }

    public ArrayList<CapitalGuard> spawnGuards() {
        ArrayList<CapitalGuard> spawnedGuards = new ArrayList<>();
        for (int i = 0; i < GUARD_COUNT; i++) {
            CapitalGuard guard = spawnGuard();
            spawnedGuards.add(guard);
            this.guards.add(guard);
        }
        return spawnedGuards;
    }

    private CapitalGuard spawnGuard() {
        List<Location> spawnLocations = Utils.getInstance().drawCircle(this.location, GUARD_SPAWN_RADIUS, 1, true, false, 0);
        Location spawnLocation = spawnLocations.get(new Random().nextInt(spawnLocations.size()));
        Utils.getInstance().playParticle(spawnLocation, ParticleEffect.WITCH_MAGIC);
        
        CapitalGuard guard = new CapitalGuard(this, getRandomGuardType(), spawnLocation);
        guard.spawn();
        return guard;
    }

    private GuardType getRandomGuardType() {
        return new Random().nextInt(4) == 3 ? GuardType.SKELETON : GuardType.ZOMBIE;
    }

    public void save() {
        KingdomDatabase.getInstance().saveNexus(this);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public IInhabitable getOwner() {
        return KingdomModule.getInstance().getKingdom(this.getKingdom());
    }

    @Override
    public String toString() {
        return Utils.getInstance().getLocationString(this.location) + "/" + this.nexusId;
    }
}
