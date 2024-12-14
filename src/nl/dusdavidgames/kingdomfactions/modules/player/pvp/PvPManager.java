package nl.dusdavidgames.kingdomfactions.modules.player.pvp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.CapitalNexus;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.settings.Setting;
import nl.dusdavidgames.kingdomfactions.modules.war.WarModule;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class PvPManager implements Listener {

    private boolean mayPvP = false;

    public PvPManager() {
        this.initTimeManager();
    }

    // Helper method to check if players are allies (same faction or kingdom)
    private boolean arePlayersAllies(KingdomFactionsPlayer player1, KingdomFactionsPlayer player2) {
        if (player1.hasFaction() && player2.hasFaction()) {
            return player1.getFaction() == player2.getFaction();
        } else if (player1.getKingdom() == player2.getKingdom()) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;

        if (e.getDamager() instanceof Player || e.getDamager() instanceof Projectile) {
            if (mayPvP) {
                if (e.getDamager() instanceof Projectile) {
                    handleShooting(e);
                } else {
                    handleMelee(e);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    private void handleMelee(EntityDamageByEntityEvent e) {
        try {
            KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getEntity());
            KingdomFactionsPlayer other = PlayerModule.getInstance().getPlayer(e.getDamager());

            if (!WarModule.getInstance().isWarActive()) {
                if (other.getCurrentNexus() instanceof CapitalNexus) {
                    Kingdom k = (Kingdom) other.getCurrentNexus().getOwner();
                    if (k.getType() != KingdomType.GEEN && k.getType() != KingdomType.ERROR) {
                        e.setCancelled(true);
                        return;
                    }
                }
            }

            if (Setting.BURGER_OORLOG.isEnabled()) {
                if (arePlayersAllies(player, other)) {
                    e.setCancelled(true);
                } else {
                    resetCombatTracker(player, other);
                }
            } else if (player.getKingdom() == other.getKingdom()) {
                e.setCancelled(true);
            } else {
                resetCombatTracker(player, other);
            }
        } catch (PlayerException ex) {
            Logger.ERROR.log("PlayerException encountered in melee combat: " + ex.getMessage());
        }
    }

    private void handleShooting(EntityDamageByEntityEvent e) {
        Projectile proj = (Projectile) e.getDamager();
        try {
            KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getEntity());
            KingdomFactionsPlayer other = PlayerModule.getInstance().getPlayer(proj.getShooter());

            if (!WarModule.getInstance().isWarActive()) {
                if (other.getCurrentNexus() instanceof CapitalNexus || player.getCurrentNexus() instanceof CapitalNexus) {
                    e.setCancelled(true);
                    return;
                }
            }

            if (Setting.BURGER_OORLOG.isEnabled()) {
                if (arePlayersAllies(player, other)) {
                    e.setCancelled(true);
                    e.getDamager().remove();
                } else {
                    resetCombatTracker(player, other);
                }
            } else if (player.getKingdom() == other.getKingdom()) {
                e.setCancelled(true);
            } else {
                resetCombatTracker(player, other);
            }
        } catch (PlayerException ex) {
            Logger.ERROR.log("PlayerException encountered in shooting combat: " + ex.getMessage());
        }
    }

    @EventHandler
    public void onCombust(EntityCombustByEntityEvent e) {
        if (!(e.getCombuster() instanceof Arrow)) return;

        Arrow proj = (Arrow) e.getCombuster();
        try {
            KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getEntity());
            KingdomFactionsPlayer other = PlayerModule.getInstance().getPlayer(proj.getShooter());

            if (!WarModule.getInstance().isWarActive()) {
                if (other.getCurrentNexus() instanceof CapitalNexus || player.getCurrentNexus() instanceof CapitalNexus) {
                    e.setCancelled(true);
                    return;
                }
            }

            if (arePlayersAllies(player, other)) {
                e.setCancelled(true);
                proj.remove();
            } else {
                resetCombatTracker(player, other);
            }
        } catch (PlayerException ex) {
            Logger.ERROR.log("PlayerException encountered in combust event: " + ex.getMessage());
        }
    }

    private void resetCombatTracker(KingdomFactionsPlayer player, KingdomFactionsPlayer other) {
        player.getCombatTracker().resetCombat();
        other.getCombatTracker().resetCombat();
    }

    // Time manager to handle PvP based on time of day
    @SuppressWarnings("deprecation")
    public void initTimeManager() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(KingdomFactionsPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                String hours = new SimpleDateFormat("HH").format(new Date());
                int currentHour = Integer.parseInt(hours);
                if (currentHour >= 12) {
                    if (!mayPvP) {
                        for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
                            player.sendTitle(ChatColor.DARK_RED + "PvP", ChatColor.RED + "De PvP staat nu aan!", 20, 80, 20);
                        }
                    }
                    mayPvP = true;
                } else {
                    if (mayPvP) {
                        for (KingdomFactionsPlayer player : PlayerModule.getInstance().getPlayers()) {
                            player.sendTitle(ChatColor.DARK_RED + "PvP", ChatColor.RED + "De PvP staat nu uit!", 20, 80, 20);
                            player.getCombatTracker().clearCombat();
                        }
                    }
                    mayPvP = false;
                }
            }
        }, 0L, 20 * 5);
    }
}
