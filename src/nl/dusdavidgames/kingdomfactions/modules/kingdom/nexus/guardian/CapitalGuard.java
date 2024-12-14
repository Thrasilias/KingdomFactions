package nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.guardian;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;

import net.md_5.bungee.api.ChatColor;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.CapitalNexus;
import nl.dusdavidgames.kingdomfactions.modules.monster.GuardType;
import nl.dusdavidgames.kingdomfactions.modules.monster.IGuard;
import nl.dusdavidgames.kingdomfactions.modules.nexus.INexus;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Item;

public class CapitalGuard implements IGuard {

    private Location location;
    private Entity entity;
    private boolean alive;
    private GuardType type;
    private CapitalNexus nexus;

    public CapitalGuard(CapitalNexus nexus, GuardType type, Location location) {
        this.nexus = nexus;
        this.type = type;
        this.location = location;
        this.alive = true;
        nexus.getGuards().add(this);
    }

    public Location getLocation() {
        return location;
    }

    public void spawn() {
        switch (type) {
            case SKELETON:
                this.entity = spawnSkeleton();
                break;
            case ZOMBIE:
                this.entity = spawnZombie();
                break;
        }

        equipGuard();
    }

    private Skeleton spawnSkeleton() {
        Skeleton skeleton = location.getWorld().spawn(location, Skeleton.class);
        skeleton.setCustomName(ChatColor.RED + "Nexus Wachter");
        skeleton.setCustomNameVisible(true);
        return skeleton;
    }

    private Zombie spawnZombie() {
        Zombie zombie = location.getWorld().spawn(location, Zombie.class);
        zombie.setCustomName(ChatColor.RED + "Nexus Wachter");
        zombie.setCustomNameVisible(true);
        return zombie;
    }

    private void equipGuard() {
        HashMap<Enchantment, Integer> armorE = new HashMap<>();
        armorE.put(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
        armorE.put(Enchantment.DURABILITY, 3);
        armorE.put(Enchantment.THORNS, 3);

        EntityEquipment equipment = ((LivingEntity) entity).getEquipment();
        
        if (entity instanceof Skeleton) {
            equipSkeleton(equipment);
        } else if (entity instanceof Zombie) {
            equipZombie(equipment);
        }

        // Equip armor
        equipment.setBoots(Item.getInstance().getItem(Material.DIAMOND_BOOTS, ChatColor.RED + "Wachter's Schoenen", 1, armorE, true));
        equipment.setLeggings(Item.getInstance().getItem(Material.DIAMOND_LEGGINGS, ChatColor.RED + "Wachter's Broek", 1, armorE, true));
        equipment.setChestplate(Item.getInstance().getItem(Material.DIAMOND_CHESTPLATE, ChatColor.RED + "Wachter's Kuras", 1, armorE, true));
        equipment.setHelmet(Item.getInstance().getItem(Material.DIAMOND_HELMET, ChatColor.RED + "Wachter's Helm", 1, armorE, true));
    }

    private void equipSkeleton(EntityEquipment equipment) {
        HashMap<Enchantment, Integer> bowE = new HashMap<>();
        bowE.put(Enchantment.ARROW_DAMAGE, 5);
        bowE.put(Enchantment.ARROW_FIRE, 1);
        bowE.put(Enchantment.ARROW_KNOCKBACK, 2);

        equipment.setItemInMainHand(Item.getInstance().getItem(Material.BOW, ChatColor.RED + "Nexus Wachter Boog", 1, bowE, true));
    }

    private void equipZombie(EntityEquipment equipment) {
        HashMap<Enchantment, Integer> swordE = new HashMap<>();
        swordE.put(Enchantment.DAMAGE_ALL, 5);
        swordE.put(Enchantment.FIRE_ASPECT, 2);
        swordE.put(Enchantment.KNOCKBACK, 3);

        equipment.setItemInMainHand(Item.getInstance().getItem(Material.DIAMOND_SWORD, ChatColor.RED + "Nexus Wachter Zwaard", 1, swordE, true));
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isAlive() {
        return alive;
    }

    public GuardType getType() {
        return type;
    }

    @Override
    public INexus getNexus() {
        return nexus;
    }

    public void kill() {
        setAlive(false);
        entity.remove();
        nexus.getGuards().remove(this);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setTarget(KingdomFactionsPlayer player) {
        setTarget(player.getPlayer().getPlayer());
    }

    public void setTarget(LivingEntity entity) {
        ((Monster) getEntity()).setTarget(entity);
    }

    @Override
    public void remove() {
        nexus.getGuards().remove(this);
    }
}
