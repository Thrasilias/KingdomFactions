package nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles;

import java.util.ArrayList;

import org.bukkit.Bukkit;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.exception.kingdom.KingdomException;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionModule;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.faction.event.FactionSwitchEvent;
import nl.dusdavidgames.kingdomfactions.modules.faction.invite.Invite;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.event.KingdomSwitchEvent;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.event.RankChangeEvent;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.menu.KingdomMenu;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public class MembershipProfile extends Profile {

    private @Getter Faction faction;
    private @Getter Kingdom kingdom;
    private @Getter KingdomRank kingdomRank;
    private @Getter FactionRank factionRank;

    public MembershipProfile(KingdomFactionsPlayer player, Faction faction, Kingdom kingdom, KingdomRank kRank, FactionRank fRank) {
        super(player);
        this.faction = faction;
        this.kingdomRank = kRank;
        this.factionRank = fRank;
        this.kingdom = kingdom;
    }

    // Set kingdom and trigger kingdom switch event
    public void setKingdom(Kingdom k) throws KingdomException {
        this.kingdom = k;
        Bukkit.getPluginManager().callEvent(new KingdomSwitchEvent(this.player));
    }

    // Set faction and trigger faction switch event
    public void setFaction(Faction f) {
        Bukkit.getPluginManager().callEvent(new FactionSwitchEvent(this.player, f));
        this.player.getChatProfile().wipeChannels();
        this.faction = f;
        if (f != null) {
            f.addMember(this.player);
            this.player.getChatProfile().addChannel(f.getChannel());
        }
    }

    // Check if the player is a faction moderator
    public boolean isFactionMod() {
        return hasFaction() && (getFactionRank() == FactionRank.LEADER || getFactionRank() == FactionRank.OFFICER || player.isStaff());
    }

    // Check if the player is a faction officer
    public boolean isFactionOfficer() {
        return hasFaction() && getFactionRank() == FactionRank.OFFICER;
    }

    // Check if the player is a faction leader
    public boolean isFactionLeader() {
        return hasFaction() && getFactionRank() == FactionRank.LEADER;
    }

    // Open the Kingdom select menu
    public void openKingdomSelectMenu() {
        KingdomMenu.getInstance().setKindomMenu(this.player);
    }

    // Check if the player has a faction
    public boolean hasFaction() {
        return faction != null;
    }

    // Check if the player has pending invites
    public boolean hasPendingInvites() {
        return getPendingInvites().size() > 0;
    }

    // Get a list of pending invites
    public ArrayList<Invite> getPendingInvites() {
        ArrayList<Invite> temp = new ArrayList<>();
        for (Faction f : FactionModule.getInstance().getFactions()) {
            for (Invite i : f.getInvites()) {
                if (i.getPlayer().getName().equalsIgnoreCase(this.player.getName())) {
                    temp.add(i);
                }
            }
        }
        return temp;
    }

    // Get the pending invite for a specific faction
    public Invite getPendingInviteForFaction(Faction f) {
        for (Invite i : getPendingInvites()) {
            if (FactionModule.getInstance().compareFactions(f, i.getTargetFaction())) {
                return i;
            }
        }
        return null;
    }

    // Check if the player has a pending invite for a specific faction
    public boolean hasPendingInviteForFaction(Faction f) {
        return getPendingInviteForFaction(f) != null;
    }

    // Check if the player is a faction admin
    public boolean isFactionAdmin() {
        return isFactionLeader() || player.isStaff();
    }

    // Check if the player is the king of the kingdom
    public boolean isKoning() {
        return this.getKingdomRank() == KingdomRank.KONING;
    }

    // Check if the player is a guard in the kingdom
    public boolean isWachter() {
        return this.getKingdomRank() == KingdomRank.WACHTER;
    }

    @Override
    public String toString() {
        return this.player.getName();
    }

    // Set the kingdom rank and trigger a rank change event
    public void setKingdomRank(KingdomRank rank) {
        this.kingdomRank = rank;
        Bukkit.getPluginManager().callEvent(new RankChangeEvent(rank, this.player));
    }

    // Set the faction rank and trigger a rank change event
    public void setFactionRank(FactionRank rank) {
        this.factionRank = rank;
        Bukkit.getPluginManager().callEvent(new RankChangeEvent(rank, this.player));
    }
}
