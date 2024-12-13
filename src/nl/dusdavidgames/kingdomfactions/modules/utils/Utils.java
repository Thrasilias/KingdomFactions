package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;

import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.configuration.ConfigModule;
import nl.dusdavidgames.kingdomfactions.modules.exception.DataException;
import nl.dusdavidgames.kingdomfactions.modules.exception.player.PlayerException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.buildings.Building;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildLevel;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildingType;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.MessagePrefix;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.Title;
import nl.dusdavidgames.kingdomfactions.modules.utils.enums.TitleDuration;
import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;
import nl.dusdavidgames.kingdomfactions.modules.utils.nms.NMSMethods;
import nl.dusdavidgames.kingdomfactions.modules.utils.particles.ParticleEffect;

public class Utils extends NMSMethods {

    private static @Getter @Setter Utils instance;

    public Utils() {
        setInstance(this);
    }

    public void playFirework(Player p, Location loc, Color color1, Color color2, FireworkEffect.Type type) {
        loc.add(0.5, 1, 0.5);
        Firework fw = p.getWorld().spawn(loc, Firework.class);
        FireworkMeta fwmeta = fw.getFireworkMeta();
        FireworkEffect.Builder builder = FireworkEffect.builder();

        builder.withFlicker();
        builder.withFade(color2);
        builder.withColor(color1);
        builder.with(type);
        fwmeta.clearEffects();
        setFireworkMetaPower(fwmeta);
        fwmeta.addEffect(builder.build());
        fw.setFireworkMeta(fwmeta);
    }

    private void setFireworkMetaPower(FireworkMeta fwmeta) {
        try {
            Field f = fwmeta.getClass().getDeclaredField("power");
            f.setAccessible(true);
            f.set(fwmeta, -1);
        } catch (Exception e) {
            Logger.ERROR.log("Error setting firework power.");
        }
    }

    public void pasteSchematic(KingdomFactionsPlayer p, String file, Location loc) {
        File schematic = new File("plugins/WorldEdit/schematics/" + file);
        EditSession es = new EditSession(new BukkitWorld(p.getPlayer().getWorld()), 999999999);
        
        try {
            p.sendMessage(Messages.getInstance().getPrefix() + "Bezig met het bouwen...");
            CuboidClipboard cc = CuboidClipboard.loadSchematic(schematic);
            cc.paste(es, new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), false);
        } catch (IOException | com.sk89q.worldedit.data.DataException | MaxChangedBlocksException e) {
            handleWorldEditException(e, p, loc, file);
        } catch (Exception e) {
            handleGeneralException(e, p, loc);
        }
    }

    private void handleWorldEditException(Exception e, KingdomFactionsPlayer p, Location loc, String file) {
        String errorMsg = "Er ging iets fout! Meld dit aub bij een staff lid!";
        Logger.ERROR.log("------------------------- WORLDEDIT PASTE PROBLEM -------------------------");
        Logger.ERROR.log("Exception: " + e.getClass().getSimpleName());
        Logger.ERROR.log("Expected Problem: Missing Schematic. /plugins/WorldEdit/schematics/" + file);
        Logger.ERROR.log("Location: " + getLocation(loc));
        Logger.ERROR.log("By player: " + p.getName() + " / " + p.getUuid());
        e.printStackTrace();
        Logger.ERROR.log("------------------------- WORLDEDIT PASTE PROBLEM -------------------------");
        p.sendMessage(MessagePrefix.KDFERROR, errorMsg);
        p.sendMessage(MessagePrefix.KDFERROR, "Vermeld deze informatie: " + e.getClass().getSimpleName());
    }

    private void handleGeneralException(Exception e, KingdomFactionsPlayer p, Location loc) {
        String errorMsg = "Er ging iets fout! Meld dit aub direct bij een staff lid!";
        Logger.ERROR.log("------------------------- WORLDEDIT PASTE PROBLEM -------------------------");
        Logger.ERROR.log("Exception: " + e);
        Logger.ERROR.log("Location: " + getLocation(loc));
        Logger.ERROR.log("By player: " + p.getName() + " / " + p.getUuid());
        e.printStackTrace();
        Logger.ERROR.log("------------------------- WORLDEDIT PASTE PROBLEM -------------------------");
        p.sendMessage(MessagePrefix.KDFERROR, errorMsg);
        p.sendMessage(MessagePrefix.KDFERROR, "Vermeld deze informatie: Exception");
    }

    public String getDate() {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        return ft.format(new Date());
    }

    public String getLocation(Location loc) {
        return loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ();
    }

    public String getSchematic(Kingdom k, BuildLevel l, BuildingType b) {
        return ConfigModule.getInstance().getFile(ConfigModule.SCHEMATICS).getConfig()
                .getString(k.getType().toString() + "." + b.toString() + "_" + l.getLevel());
    }

    public Location getNewLocation(Location oldLocation) {
        return new Location(oldLocation.getWorld(), oldLocation.getBlockX(), oldLocation.getBlockY(),
                oldLocation.getBlockZ());
    }

    public Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - radius % 16) / 16;
        HashSet<Entity> radiusEntities = new HashSet<>();
        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                for (Entity e : new Location(l.getWorld(), l.getBlockX() + chX * 16, l.getBlockY(), l.getBlockZ() + chZ * 16).getChunk().getEntities()) {
                    if ((e.getLocation().distance(l) <= radius) && (e.getLocation().getBlock() != l.getBlock())) {
                        radiusEntities.add(e);
                    }
                }
            }
        }
        return radiusEntities.toArray(new Entity[0]);
    }

    public void sendTitle(KingdomFactionsPlayer player, Title t, TitleDuration d, String message) {
        switch (t) {
            case SUBTITLE:
                sendSubtitle(player, d, message);
                break;
            case TITLE:
                sendTitle(player, d, message);
                break;
        }
    }

    private void sendSubtitle(KingdomFactionsPlayer player, TitleDuration d, String message) {
        switch (d) {
            case LONG:
                Utils.getInstance().sendSubTitle(player, message, 60, 160, 10);
                break;
            case MEDIUM:
                Utils.getInstance().sendSubTitle(player, message, 40, 80, 40);
                break;
            case SHORT:
                Utils.getInstance().sendSubTitle(player, message, 20, 40, 20);
                break;
        }
    }

    private void sendTitle(KingdomFactionsPlayer player, TitleDuration d, String message) {
        switch (d) {
            case LONG:
                Utils.getInstance().sendTitle(player, message, 60, 160, 10);
                break;
            case MEDIUM:
                Utils.getInstance().sendTitle(player, message, 40, 80, 40);
                break;
            case SHORT:
                Utils.getInstance().sendTitle(player, message, 20, 40, 20);
                break;
        }
    }

    public void sendMessage(MessagePrefix p, KingdomFactionsPlayer player, String message) {
        switch (p) {
            case DEBUG:
                player.sendMessage(ChatColor.YELLOW + "[DEBUG] " + ChatColor.GRAY + message);
                break;
            case KDFERROR:
                player.sendMessage(Messages.getInstance().getWarning() + message);
                break;
            case KINGDOMFACTIONS:
                player.sendMessage(Messages.getInstance().getPrefix() + message);
                break;
        }
    }

    private final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private SecureRandom rnd = new SecureRandom();

    public String generateRandomString(int size) {
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public boolean equalsLocation(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }

    public List<Location> drawCircle(Location loc, Integer radius, Integer height, Boolean hollow, Boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - radius; x <= cx + radius; x++) {
            for (int z = cz - radius; z <= cz + radius; z++) {
                for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < radius * radius && (!hollow || dist >= (radius - 1) * (radius - 1))) {
                        circleblocks.add(new Location(loc.getWorld(), x, y + plus_y, z));
                    }
                }
            }
        }
        return circleblocks;
    }

    public ArrayList<String> getLore(String lore) {
        ArrayList<String> newlore = new ArrayList<>();
        newlore.add(lore);
        return newlore;
    }

    public void playParticle(Location location, ParticleEffect effect) {
        for (Entity e : getNearbyEntities(location, 100)) {
            try {
                effect.sendToPlayer(PlayerModule.getInstance().getPlayer(e).getPlayer(), location, 0.4F, 0.4F, 0.4F, 1.0F, 20);
            } catch (PlayerException e1) {
                Logger.ERROR.log("Error sending particle effect to player.");
            } catch (Exception e1) {
                Logger.ERROR.log("Error with particle effect.");
            }
        }
    }

    public ArrayList<String> convert(ArrayList<Object> list) {
        ArrayList<String> temp = new ArrayList<>();
        for (Object o : list) {
            if (o instanceof String) {
                temp.add((String) o);
            }
        }
        return temp;
    }

    public String locationToDbString(Location loc) {
        return String.format("%d-%d-%d-%f-%f-%s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(),
                loc.getPitch(), loc.getYaw(), loc.getWorld().getName());
    }

    public Location DbStringToLocation(String s) {
        String[] parts = s.split("-");
        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int z = Integer.parseInt(parts[2]);
            float pitch = Float.parseFloat(parts[3]);
            float yaw = Float.parseFloat(parts[4]);
            World world = Bukkit.getWorld(parts[5]);
            return new Location(world, x, y, z, yaw, pitch);
        } catch (NumberFormatException e) {
            Logger.ERROR.log("Error parsing location string: " + s);
        }
        return null;
    }

    public World getOverWorld() {
        return getWorld("worlds.overworld");
    }

    private World getWorld(String key) {
        return Bukkit.getWorld(ConfigModule.getInstance().getFile(ConfigModule.CONFIG).getConfig().getString(key));
    }
}
