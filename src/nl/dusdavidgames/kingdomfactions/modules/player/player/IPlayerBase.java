package nl.dusdavidgames.kingdomfactions.modules.player.player;

import java.util.UUID;

import nl.dusdavidgames.kingdomfactions.modules.exception.faction.FactionException;
import nl.dusdavidgames.kingdomfactions.modules.exception.value.ValueException;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionMember;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBan;

/**
 * Interface representing a player's base information and actions in the game.
 */
public interface IPlayerBase {

    /**
     * @return true if the player is online, false otherwise.
     */
    boolean isOnline();

    /**
     * @return true if the player is part of a faction, false otherwise.
     */
    boolean hasFaction();

    /**
     * @return the player's name.
     */
    String getName();

    /**
     * @return the unique identifier (UUID) of the player.
     */
    UUID getUuid();

    /**
     * @return the player's current rank in the kingdom.
     */
    KingdomRank getKingdomRank();

    /**
     * @return the player's current rank in the faction.
     */
    FactionRank getFactionRank();

    /**
     * @return the faction the player is a part of, or null if none.
     */
    Faction getFaction();

    /**
     * @return the kingdom the player is a part of, or null if none.
     */
    Kingdom getKingdom();

    /**
     * Sets the kingdom of the player.
     * 
     * @param kingdom the kingdom to set.
     */
    void setKingdom(Kingdom kingdom);

    /**
     * Sets the faction of the player.
     * 
     * @param faction the faction to set.
     */
    void setFaction(Faction faction);

    /**
     * Sets the player's kingdom rank.
     * 
     * @param kingdomRank the rank to set.
     */
    void setKingdomRank(KingdomRank kingdomRank);

    /**
     * Sets the player's faction rank.
     * 
     * @param factionRank the rank to set.
     */
    void setFactionRank(FactionRank factionRank);

    /**
     * Saves the player's data.
     */
    void save();

    /**
     * @return the player's death ban information.
     */
    DeathBan getDeathBan();

    /**
     * @return true if the player has an active death ban, false otherwise.
     */
    boolean hasDeathBan();

    /**
     * Purges the player's data from the system.
     */
    void purge();

    /**
     * @return the player's formatted name, typically for display purposes.
     */
    String getFormattedName();

    /**
     * Adds the specified number of coins to the player's balance.
     * 
     * @param coins the number of coins to add.
     */
    void addCoins(int coins);

    /**
     * Adds the specified number of influence points to the player.
     * 
     * @param influence the number of influence points to add.
     */
    void addInfluence(int influence);

    /**
     * Removes the specified number of influence points from the player.
     * 
     * @param influence      the number of influence points to remove.
     * @param mayBeNegative whether influence points can go negative.
     * @throws ValueException if the value cannot be removed.
     */
    void removeInfluence(int influence, boolean mayBeNegative) throws ValueException;

    /**
     * Removes the specified number of coins from the player's balance.
     * 
     * @param coins          the number of coins to remove.
     * @param mayBeNegative  whether coins can go negative.
     * @throws ValueException if the value cannot be removed.
     */
    void removeCoins(int coins, boolean mayBeNegative) throws ValueException;

    /**
     * @return the player's current influence points.
     */
    int getInfluence();

    /**
     * @return the player's current coin balance.
     */
    int getCoins();

    /**
     * Converts the current player to a faction member.
     * 
     * @return the faction member representation of the player.
     */
    FactionMember toFactionMember();

    /**
     * Converts an old player object to this new player object.
     * 
     * @param old the old player object.
     * @return the new player object.
     */
    IPlayerBase convert(IPlayerBase old);

    /**
     * @return the player's total playtime in the form of an array (could represent hours, minutes, seconds).
     */
    int[] getPlaytime();

    /**
     * Makes the player leave their faction, if applicable.
     * 
     * @throws FactionException if the player is not a member of a faction.
     */
    default void leaveFaction() throws FactionException {
        if (this.hasFaction()) {
            Faction f = this.getFaction();
            f.removeMember(this);
            this.setFaction(null);
        } else {
            throw new FactionException("Can't leave a faction, when someone is not a member of one!");
        }
    }
}
