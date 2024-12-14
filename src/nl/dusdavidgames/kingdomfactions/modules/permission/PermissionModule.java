package nl.dusdavidgames.kingdomfactions.modules.permission;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

import java.util.HashSet;
import java.util.Set;

public class PermissionModule {

    private static @Getter @Setter PermissionModule instance;

    // Cached staff list
    private Set<KingdomFactionsPlayer> staffCache = null;

    public PermissionModule() {
        setInstance(this);
    }

    // Define roles as constants
    private static final Set<String> STAFF_ROLES = new HashSet<>();
    static {
        STAFF_ROLES.add("kingdomfactions.role.lead");
        STAFF_ROLES.add("kingdomfactions.role.pl");
        STAFF_ROLES.add("kingdomfactions.role.mod");
        STAFF_ROLES.add("kingdomfactions.role.support");
    }

    public boolean isStaff(KingdomFactionsPlayer p) {
        // Check if the player has any of the staff roles
        for (String role : STAFF_ROLES) {
            if (p.hasPermission(role)) {
                return true;
            }
        }
        return false;
    }

    public StaffList getStaffMembers() {
        // Use cached staff list if available
        if (staffCache == null) {
            staffCache = new HashSet<>();
            for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
                if (isStaff(player)) {
                    staffCache.add(player);
                }
            }
        }
        return new StaffList(staffCache);
    }
}
