package nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.GodMode;

public class SettingsProfile extends Profile {

    @Getter @Setter private boolean adminMode = false;
    @Getter @Setter private boolean ignoreInfluence = false;
    @Getter @Setter private GodMode godMode = GodMode.NOGOD;
    @Getter @Setter private boolean nightvision = false;
    @Getter @Setter private boolean spy = false;
    @Getter @Setter private boolean nexusIspect;

    public SettingsProfile(KingdomFactionsPlayer player) {
        super(player);
    }

    public boolean hasSpy() {
        return spy;
    }

    public boolean hasNightVision() {
        return nightvision;
    }

    public boolean hasIgnoreInfluence() {
        return ignoreInfluence;
    }

    public boolean hasAdminMode() {
        return adminMode;
    }

    public boolean hasNexusInspect() {
        return nexusIspect;
    }
}
