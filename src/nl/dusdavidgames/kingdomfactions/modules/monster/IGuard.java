package nl.dusdavidgames.kingdomfactions.modules.monster;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public interface IGuard {

    Location getLocation();
    
    void spawn();
    
    Entity getEntity();
    
    boolean isAlive();
    
    GuardType getType();
    
    INexus getNexus();
    
    void kill();
    
    void setAlive(boolean alive);
    
    void setTarget(LivingEntity entity);
    
    void setTarget(KingdomFactionsPlayer player);
    
    void remove();
}
