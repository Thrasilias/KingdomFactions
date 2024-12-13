package nl.dusdavidgames.kingdomfactions.modules.war;

import org.bukkit.Bukkit;

import lombok.Data;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.Nexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerList;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.war.event.WarStartEvent;
import nl.dusdavidgames.kingdomfactions.modules.war.event.WarStopEvent;

public @Data class War {

    private WarState warState = WarState.NOWAR;
    private final PlayerList totalSoldiers = new PlayerList();
    private final PlayerList malzanSoldiers = new PlayerList();
    private final PlayerList eredonSoldiers = new PlayerList();
    private final PlayerList adamantiumSoldiers = new PlayerList();
    private final PlayerList hyvarSoldiers = new PlayerList();
    private final PlayerList tilfiaSoldiers = new PlayerList();
    private final PlayerList dokSoldiers = new PlayerList();

    private long time;
    private long timeInMilliSeconds;

    public War(long time) {
        this.time = time;
        long currentTime = System.currentTimeMillis();
        this.timeInMilliSeconds = currentTime + ((time * 60) * 1000);
    }

    /**
     * Returns the remaining time in HH:mm:ss format.
     */
    public String getRemainingTime() {
        long timediff = timeInMilliSeconds - System.currentTimeMillis();
        long timeInSeconds = timediff / 1000;
        long seconds = timeInSeconds % 60;
        long minutes = (timeInSeconds / 60) % 60;
        long hours = (timeInSeconds / 3600) % 24;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public void start() {
        if (warState == WarState.WAR) {
            // War is already active
            return;
        }

        for (KingdomFactionsPlayer p : PlayerModule.getInstance().getPlayers()) {
            Kingdom kingdom = p.getKingdom();
            PlayerList soldiers = getSoldiers(kingdom);
            soldiers.add(p);
            p.sendTitle(ChatColor.DARK_RED + "Oorlog", ChatColor.RED + "Er is een oorlog begonnen!", 20, 20, 20);
        }

        setWarState(WarState.WAR);
        Bukkit.getPluginManager().callEvent(new WarStartEvent());
    }

    public void end() {
        clearSoldiers();
        notifyEndOfWar();
        WarModule.getInstance().setWar(null);
        unprotectNexuses();
        Bukkit.getPluginManager().callEvent(new WarStopEvent());
    }

    private void clearSoldiers() {
        getAllSoldiers().forEach(PlayerList::clear);
    }

    private void notifyEndOfWar() {
        for (KingdomFactionsPlayer p : PlayerModule.getInstance().getPlayers()) {
            p.sendTitle(ChatColor.RED + "Oorlog", ChatColor.RED + "De oorlog is afgelopen!", 20, 40, 20);
        }
    }

    private void unprotectNexuses() {
        for (INexus nexus : NexusModule.getInstance().getNexuses()) {
            if (nexus instanceof Nexus && ((Nexus) nexus).isProtected()) {
                ((Nexus) nexus).setProtected(false);
            }
        }
    }

    /**
     * Returns the list of soldiers for the given kingdom.
     * If no kingdom matches, returns the list of total soldiers.
     */
    public PlayerList getSoldiers(Kingdom kingdom) {
        switch (kingdom.getType()) {
            case ADAMANTIUM: return adamantiumSoldiers;
            case DOK: return dokSoldiers;
            case EREDON: return eredonSoldiers;
            case HYVAR: return hyvarSoldiers;
            case MALZAN: return malzanSoldiers;
            case TILIFIA: return tilfiaSoldiers;
            default: return totalSoldiers;
        }
    }

    /**
     * Returns all soldiers across all kingdoms.
     */
    public PlayerList getAllSoldiers() {
        PlayerList allSoldiers = new PlayerList();
        allSoldiers.addAll(totalSoldiers);
        allSoldiers.addAll(malzanSoldiers);
        allSoldiers.addAll(eredonSoldiers);
        allSoldiers.addAll(adamantiumSoldiers);
        allSoldiers.addAll(hyvarSoldiers);
        allSoldiers.addAll(tilfiaSoldiers);
        allSoldiers.addAll(dokSoldiers);
        return allSoldiers;
    }
}
