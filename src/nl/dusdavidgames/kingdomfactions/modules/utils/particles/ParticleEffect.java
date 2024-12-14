package nl.dusdavidgames.kingdomfactions.modules.utils.particles;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.world.level.material.Particle;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket.Data;

public enum ParticleEffect
{
    HUGE_EXPLOSION(Particle.EXPLOSION),
    LARGE_EXPLODE(Particle.EXPLOSION),
    FIREWORKS_SPARK(Particle.FIREWORK),
    BUBBLE(Particle.BUBBLE),
    SUSPEND(Particle.SUSPENDED),
    DEPTH_SUSPEND(Particle.SUSPENDED_DEPTH),
    TOWN_AURA(Particle.TOWN_AURA),
    CRIT(Particle.CRIT),
    MAGIC_CRIT(Particle.CRIT_MAGIC),
    MOB_SPELL(Particle.SPELL),
    MOB_SPELL_AMBIENT(Particle.SPELL_AMBIENT),
    SPELL(Particle.SPELL),
    INSTANT_SPELL(Particle.SPELL_INSTANT),
    WITCH_MAGIC(Particle.SPELL_WITCH),
    NOTE(Particle.NOTE),
    PORTAL(Particle.PORTAL),
    ENCHANTMENT_TABLE(Particle.ENCHANTMENT_TABLE),
    EXPLODE(Particle.EXPLOSION_NORMAL),
    FLAME(Particle.FLAME),
    LAVA(Particle.LAVA),
    FOOTSTEP(Particle.FOOTSTEP),
    SPLASH(Particle.SPLASH),
    LARGE_SMOKE(Particle.SMOKE),
    CLOUD(Particle.CLOUD),
    RED_DUST(Particle.REDSTONE),
    SNOWBALL_POOF(Particle.SNOWBALL),
    DRIP_WATER(Particle.DRIP_WATER),
    DRIP_LAVA(Particle.DRIP_LAVA),
    SNOW_SHOVEL(Particle.SNOW_SHOVEL),
    SLIME(Particle.SLIME),
    HEART(Particle.HEART),
    ANGRY_VILLAGER(Particle.VILLAGER_ANGRY),
    HAPPY_VILLAGER(Particle.VILLAGER_HAPPY),
    ICONCRACK(Particle.ITEM_CRACK),
    TILECRACK(Particle.ITEM_TAKE);

    private Particle nmsParticle;

    private ParticleEffect(Particle particle)
    {
        nmsParticle = particle;
    }

    public void sendToPlayer(Player player, Location location, float offsetX, float offsetY, float offsetZ, float speed, int count)
            throws Exception
    {
        ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket(
                nmsParticle,
                false,
                (float) location.getX(),
                (float) location.getY(),
                (float) location.getZ(),
                offsetX,
                offsetY,
                offsetZ,
                speed,
                count);        
        ((CraftPlayer)player).getHandle().connection.send(packet);
    }
}
