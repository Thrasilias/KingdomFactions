package nl.dusdavidgames.kingdomfactions.modules.monster;

import org.bukkit.entity.LivingEntity;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;

public class MonsterModule {

    public MonsterModule() {
        setInstance(this);
    }

    private static @Getter @Setter MonsterModule instance;

    /**
     * Get the guard associated with a given entity.
     * 
     * @param e the LivingEntity to check
     * @return the IGuard instance if found, otherwise null
     */
    public IGuard getGuard(LivingEntity e) {
        // Loop through each Nexus and its guards to find the one corresponding to the entity
        for (INexus nexus : NexusModule.getInstance().getNexuses()) {
            for (IGuard guard : nexus.getGuards()) {
                if (guard.getEntity().equals(e)) {
                    return guard;
                }
            }
        }
        return null;
    }

    /**
     * Check if the provided LivingEntity is a guard.
     * 
     * @param e the LivingEntity to check
     * @return true if the entity is a guard, false otherwise
     */
    public boolean isGuard(LivingEntity e) {
        return getGuard(e) != null;
    }

}
