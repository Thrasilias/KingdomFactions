package nl.dusdavidgames.kingdomfactions.modules.utils.firework;

import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.world.entity.projectile.EntityFireworkRocket;
import net.minecraft.world.level.World;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;

public class FireworkEffectPlayer {

    public void playFirework(org.bukkit.World world, Location location, FireworkEffect fireworkEffect) {
        CustomFirework.spawn(location, fireworkEffect, world.getPlayers().toArray(new Player[0]));
    }

    public static class CustomFirework extends EntityFireworkRocket {
        Player[] players = null;

        public CustomFirework(World world, Player[] p) {
            super(world);
            players = p;
            this.setSize(0.25F, 0.25F); // Setting size of the firework rocket
        }

        boolean gone = false;

        @Override
        public void tick() {
            if (gone) {
                this.die();
                return;
            }
            if (!this.level.isClientSide) {
                gone = true;

                if (players != null) {
                    if (players.length > 0) {
                        for (Player player : players) {
                            PlayerModule.getInstance().getPlayer(player).getEntityPlayer().connection.send(new PacketPlayOutEntityStatus(this, (byte) 17));
                        }
                    } else {
                        this.level.broadcastEntityEvent(this, (byte) 17);
                    }
                }
                this.die();
            }
        }

        public static void spawn(Location location, FireworkEffect effect, Player[] players) {
            try {
                CustomFirework firework = new CustomFirework(((CraftWorld) location.getWorld()).getHandle(), players);
                FireworkMeta meta = ((Firework) firework.getBukkitEntity()).getFireworkMeta();
                meta.addEffect(effect);
                ((Firework) firework.getBukkitEntity()).setFireworkMeta(meta);
                firework.setPos(location.getX(), location.getY(), location.getZ());

                if (((CraftWorld) location.getWorld()).getHandle().addFreshEntity(firework)) {
                    firework.setInvisible(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
