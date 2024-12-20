package nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.permission.PermissionModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.main.MainPlayerRunnable;
import nl.dusdavidgames.kingdomfactions.modules.player.runnables.main.ScheduledPlayerTask;
import nl.dusdavidgames.kingdomfactions.modules.pvplog.LogoutAction;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.Title;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.TitleDuration;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class CombatTracker extends Profile {

    private static int taskCounter = 0;

    public static void initCombatNotifier(MainPlayerRunnable r) {
        r.scheduleTask(new ScheduledPlayerTask() {
            @Override
            public void run(KingdomFactionsPlayer player) {
                if (taskCounter == 5) {
                    CombatTracker combatTracker = player.getCombatTracker();
                    taskCounter = 0;
                    if (combatTracker.isInCombat()) {
                        player.sendActionbar(ChatColor.RED + "Je bent momenteel in gevecht! Wacht " + combatTracker.getCombatSeconds() + " seconden voor je kan uitloggen!");
                    }
                } else {
                    taskCounter++;
                }
            }
        });
    }

    public CombatTracker(KingdomFactionsPlayer player) {
        super(player);
    }

    private @Getter @Setter boolean inCombat;
    private @Getter @Setter int combatSeconds;

    // Reset combat status, either apply combat or reset it
    public void resetCombat() {
        if (this.isInCombat()) {
            this.setCombatSeconds(40);
        } else {
            this.applyCombat();
        }
    }

    // Start combat and set timer
    public void applyCombat() {
        if (!this.inCombat) {
            this.setInCombat(true);
            this.setCombatSeconds(40);
            this.player.sendTitle(Title.SUBTITLE, TitleDuration.LONG, ChatColor.RED + "Je bent momenteel in gevecht!");
            this.player.sendMessage(Messages.getInstance().getPrefix() + "Je mag nu niet uitloggen! Als je toch uitlogt, verlies je je spullen.");
            this.player.sendMessage(Messages.getInstance().getPrefix() + "Gebruik /combat check om de huidige status te bekijken!");
        }
    }

    // Handle player disconnect during combat
    public void disconnect() {
        if (player.hasAction() && player.getAction() instanceof LogoutAction) {
            LogoutAction logoutAction = (LogoutAction) player.getAction();
            if (logoutAction.mayLogOut()) {
                this.player.kick(Messages.getInstance().getPrefix() + " \n " + ChatColor.RED + "Je bent veilig uitgelogd! \n" + ChatColor.RED + "Je bent geen spullen verloren!");
                return;
            }
        }
        try {
            throw new PlayerException("Unable to logout player!");
        } catch (PlayerException e) {
            e.printStackTrace();
        }
    }

    // Handle player disconnection with combat check
    public void handleDisconnect() {
        if (this.inCombat) {
            if (this.player.hasAction() && this.player.getAction() instanceof LogoutAction) {
                LogoutAction logoutAction = (LogoutAction) this.player.getAction();
                if (logoutAction.mayLogOut()) {
                    return;
                }
            }
            handlePvPLogger();
        }
    }

    // Log the player for PvP logging and handle punishment
    private void handlePvPLogger() {
        if (this.player.getPlayer().isDead()) {
            return;
        }
        this.player.kill();
        Logger.INFO.log("[PVPLOGGER] " + this.player.getName() + " was killed by The Kingdom Factions for PvP Logging!");
        PermissionModule.getInstance().getStaffMembers().broadcast(this.player.getFormattedName() + ChatColor.YELLOW + " is vermoord door de Server voor PvP Loggen!");
    }

    // Clear combat status after combat ends
    public void clearCombat() {
        this.setCombatSeconds(0);
        this.setInCombat(false);
    }
}
