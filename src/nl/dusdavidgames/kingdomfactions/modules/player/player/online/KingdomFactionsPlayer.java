package nl.dusdavidgames.kingdomfactions.modules.player.player.online;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.kitteh.vanish.staticaccess.VanishNoPacket;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.dusdavidgames.kingdomfactions.modules.chat.events.PrivateMessageEvent;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.MySQLModule;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.PlayerDatabase;
import nl.dusdavidgames.kingdomfactions.modules.empirewand.Spell;
import nl.dusdavidgames.kingdomfactions.modules.empirewand.SpellModule;
import nl.dusdavidgames.kingdomfactions.modules.exception.kingdom.KingdomException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.UnkownPlayerException;
import nl.dusdavidgames.kingdomfactions.modules.exception.shop.ShopException;
import nl.dusdavidgames.kingdomfactions.modules.exception.value.ValueException;
import nl.dusdavidgames.kingdomfactions.modules.faction.Faction;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionMember;
import nl.dusdavidgames.kingdomfactions.modules.faction.FactionRank;
import nl.dusdavidgames.kingdomfactions.modules.faction.invite.Invite;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.KingdomRank;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.BuildModule;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.buildings.BuildAction;
import nl.dusdavidgames.kingdomfactions.modules.nexus.protection.ProtectionModule;
import nl.dusdavidgames.kingdomfactions.modules.permission.PermissionModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBan;
import nl.dusdavidgames.kingdomfactions.modules.player.deathban.DeathBanModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.IPlayerBase;
import nl.dusdavidgames.kingdomfactions.modules.player.player.offline.OfflineKingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.ChatProfile;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.CombatTracker;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.MembershipProfile;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.SettingsProfile;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.profiles.StatisticsProfile;
import nl.dusdavidgames.kingdomfactions.modules.scoreboard.ScoreBoard;
import nl.dusdavidgames.kingdomfactions.modules.scoreboard.ScoreboardModule;
import nl.dusdavidgames.kingdomfactions.modules.time.TimeHelper;
import nl.dusdavidgames.kingdomfactions.modules.utils.Cooldown;
import nl.dusdavidgames.kingdomfactions.modules.utils.MetaData;
import nl.dusdavidgames.kingdomfactions.modules.utils.Randomizer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;
import nl.dusdavidgames.kingdomfactions.modules.utils.action.IAction;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.MessagePrefix;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.Title;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.TitleDuration;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;
import nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.YesNoConfirmation;
import nl.dusdavidgames.kingdomfactions.modules.viewdistance.ViewDistanceModule;

public class KingdomFactionsPlayer implements IPlayerBase {

    private @Getter UUID uuid;
    private @Getter Player player;
    private @Getter String name;
    private @Getter String ipAdres;
    private @Getter @Setter String territoryId;
    private @Getter @Setter KingdomType kingdomTerritory;
    private @Getter CombatTracker combatTracker;
    private @Getter @Setter Spell spell;
    private @Setter Spell activeSpell;
    private @Getter @Setter int activeSpellStrikes;
    private ScoreBoard scoreboard;
    private @Getter SettingsProfile settingsProfile;
    private @Getter @Setter StatisticsProfile statisticsProfile;
    private @Getter ChatProfile chatProfile;
    private @Getter MembershipProfile membershipProfile;
    private @Getter @Setter Location lastLocation;
    public @Getter MetaData metaData = new MetaData();
    public @Getter YesNoConfirmation yesNoConfirmation;

    private @Getter ArrayList<Cooldown> cooldowns = new ArrayList<Cooldown>();

    private @Getter @Setter IAction action;

    public KingdomFactionsPlayer(FactionRank factionRank, KingdomRank kingdomRank, Kingdom kingdom, Faction faction, String name) {
        this.player = Bukkit.getPlayer(name);
        this.name = name;
        this.uuid = player.getUniqueId();
        this.ipAdres = player.getAddress().getHostString();
        this.membershipProfile = new MembershipProfile(this, faction, kingdom, kingdomRank, factionRank);
        this.settingsProfile = new SettingsProfile(this);
        this.chatProfile = new ChatProfile(this);
        this.combatTracker = new CombatTracker(this);
    }

    public KingdomFactionsPlayer(FactionRank factionRank, KingdomRank kingdomRank, Kingdom kingdom, Faction faction, UUID uuid) {
        this.player = Bukkit.getPlayer(uuid);
        this.name = player.getName();
        this.uuid = player.getUniqueId();
        this.ipAdres = player.getAddress().getHostString();
        this.membershipProfile = new MembershipProfile(this, faction, kingdom, kingdomRank, factionRank);
        this.settingsProfile = new SettingsProfile(this);
        this.chatProfile = new ChatProfile(this);
        this.combatTracker = new CombatTracker(this);
    }

    public void setYesNoConfirmation(YesNoConfirmation confirm) {
        this.yesNoConfirmation = confirm;
    }

    public boolean hasYesNoConfirmation() {
        return this.yesNoConfirmation != null;
    }

    public void addCooldown(Cooldown cooldown) {
        this.cooldowns.add(cooldown);
    }

    public Cooldown getCooldown(String key) {
        return this.cooldowns.stream().filter(c -> c.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
    }

    public boolean hasCooldown(String key) {
        return getCooldown(key) != null;
    }

    public void removeCooldown(String key) {
        this.cooldowns.removeIf(c -> c.getKey().equalsIgnoreCase(key));
    }

    public boolean hasActiveSpell() {
        return this.activeSpell != null;
    }

    public Spell getActiveSpell() {
        return this.activeSpell;
    }

    public void executeCommand(String command) {
        Bukkit.dispatchCommand(this.getPlayer(), command);
    }

    public void executeCommands(String... command) {
        for (String s : command) {
            this.executeCommand(s);
        }
    }

    public void sendPacket(Packet<?> packet) {
        this.getEntityPlayer().playerConnection.sendPacket(packet);
    }

    public void sendTab(String header, String footer) {
        Utils.getInstance().sendTabTitle(this, header, footer);
    }

    public void sendMessage(MessagePrefix prefix, String message) {
        Utils.getInstance().sendMessage(prefix, this, message);
    }

    public String getFormattedName() {
        return getMembershipProfile().getKingdom().getType().getColor() + name;
    }

    public void setGameMode(GameMode g) {
        player.setGameMode(g);
    }

    public void setScoreBoard(ScoreBoard b) {
        this.scoreboard = b;
        player.setScoreboard(b.getBoard());
    }

    public void heal() {
        player.setHealth(20);
    }

    public void clearInventory() {
        this.getInventory().clear();
        this.getInventory().setBoots(null);
        this.getInventory().setHelmet(null);
        this.getInventory().setChestplate(null);
        this.getInventory().setLeggings(null);
    }

    public void feed() {
        player.setFoodLevel(20);
    }

    public void updateInventory() {
        player.updateInventory();
    }

    public void kill() {
        player.setHealth(0);
    }

    public boolean canSee(KingdomFactionsPlayer other) {
        return this.player.canSee(other.player);
    }

    public void addPotionEffect(PotionEffect effect) {
        player.addPotionEffect(effect);
    }

    public void removePotionEffect(PotionEffect effect) {
        player.removePotionEffect(effect);
    }

    public PlayerInventory getInventory() {
        return player.getInventory();
    }

    public int getCoins() {
        return this.getStatisticsProfile().getCoins();
    }

    public void setCoins(int coins) {
        this.getStatisticsProfile().setCoins(coins);
    }

    public void addCoins(int coins) {
        this.getStatisticsProfile().addCoins(coins);
    }

    public void removeCoins(int coins) {
        this.getStatisticsProfile().removeCoins(coins);
    }

    public boolean hasEnoughCoins(int coins) {
        return getCoins() >= coins;
    }

    public int getInfluence() {
        return this.getStatisticsProfile().getInfluence();
    }

    public void setInfluence(int influence) {
        this.getStatisticsProfile().setInfluence(influence);
    }

    public void addInfluence(int influence) {
        this.getStatisticsProfile().addInfluence(influence);
    }

    public void removeInfluence(int influence) {
        this.getStatisticsProfile().removeInfluence(influence);
    }

    public boolean hasEnoughInfluence(int influence) {
        return getInfluence() >= influence;
    }

    public void sendActionBar(String message) {
        Utils.getInstance().sendActionBar(this, message);
    }

    public void sendTitle(Title title, TitleDuration duration) {
        Utils.getInstance().sendTitle(this, title, duration);
    }

    public void sendHoverMessage(String message, String hover) {
        TextComponent msg = new TextComponent(message);
        msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
        player.spigot().sendMessage(msg);
    }

    public void sendClickMessage(String message, ClickEvent.Action action, String value) {
        TextComponent msg = new TextComponent(message);
        msg.setClickEvent(new ClickEvent(action, value));
        player.spigot().sendMessage(msg);
    }

    public void sendPrivateMessage(String playerName, String message) {
        Player recipient = Bukkit.getPlayer(playerName);
        if (recipient != null) {
            recipient.sendMessage(ChatColor.DARK_AQUA + "[PM] " + this.name + ": " + message);
        } else {
            this.sendMessage(MessagePrefix.ERROR, "Player not found.");
        }
    }

    public void receivePrivateMessage(String senderName, String message) {
        this.sendMessage(MessagePrefix.INFO, "Private message from " + senderName + ": " + message);
    }

    public void executeSpell() {
        if (this.hasActiveSpell()) {
            this.activeSpell.execute(this);
        }
    }

    public boolean isInCombat() {
        return combatTracker.isInCombat();
    }

    public void resetCombatTracker() {
        combatTracker.reset();
    }

    public void log(String message) {
        Logger.log(message);
    }
}
