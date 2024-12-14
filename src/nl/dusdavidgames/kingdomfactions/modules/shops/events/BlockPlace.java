package nl.dusdavidgames.kingdomfactions.modules.shops.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import nl.dusdavidgames.kingdomfactions.modules.utils.logger.Logger;

public class BlockPlace implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        if (event.getItemInHand().getType() != Material.MOB_SPAWNER)
            return;

        ItemStack is = event.getItemInHand();
        if (!is.hasItemMeta())
            return;

        ItemMeta im = is.getItemMeta();
        if (!im.hasLore())
            return;

        // Get the lore and search for "Spawner:" keyword
        String lore = String.join("", im.getLore());

        if (!lore.contains("Spawner:"))
            return;

        EntityType entity = getEntityFromLore(lore);

        if (entity == EntityType.AREA_EFFECT_CLOUD) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("You cannot place this type of spawner.");
            return;
        }

        setSpawner(event.getBlock(), entity);
    }

    private EntityType getEntityFromLore(String lore) {
        String cleanedLore = lore.toUpperCase().replaceAll("[^A-Z]", "");  // Clean non-alphabet characters

        Logger.DEBUG.log("Cleaned LORE: " + cleanedLore);

        for (EntityType type : EntityType.values()) {
            String typeName = type.name();
            if (cleanedLore.equalsIgnoreCase(typeName)) {
                return type;
            }
        }

        return EntityType.AREA_EFFECT_CLOUD;  // Default invalid type if not found
    }

    public void setSpawner(Block block, EntityType ent) {
        BlockState blockState = block.getState();
        if (blockState instanceof CreatureSpawner) {
            CreatureSpawner spawner = (CreatureSpawner) blockState;
            spawner.setSpawnedType(ent);
            blockState.update();
        }
    }
}
