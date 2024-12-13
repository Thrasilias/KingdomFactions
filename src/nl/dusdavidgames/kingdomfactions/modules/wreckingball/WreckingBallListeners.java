package nl.dusdavidgames.kingdomfactions.modules.wreckingball;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import nl.dusdavidgames.kingdomfactions.modules.nexus.NexusModule;
import nl.dusdavidgames.kingdomfactions.modules.nexus.build.BuildModule;
import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Messages;

public class WreckingBallListeners implements Listener {

    @EventHandler
    public void onUse(PlayerInteractEvent e) {
        try {
            // Early returns for conditions that should stop the event
            if (e.getAction() != Action.LEFT_CLICK_BLOCK || e.getItem() == null || !e.getItem().hasItemMeta()) {
                return;
            }
            
            // Check if the player is holding the WreckingBall item
            if (!e.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(WreckingBallModule.WRECKINGBALL_NAME)) {
                return;
            }

            // Check if the player is allowed to use the item
            KingdomFactionsPlayer player = PlayerModule.getInstance().getPlayer(e.getPlayer());
            if (player.isVanished() || !player.isStaff()) {
                e.getPlayer().getInventory().setItem(e.getPlayer().getInventory().getHeldItemSlot(), null);
                e.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Het is voor jou niet toegestaan om dit item te gebruiken.");
                return;
            }

            // Handle block interaction
            if (e.getClickedBlock() == null) return;
            Block clickedBlock = e.getClickedBlock();

            // Fire a custom event for the block being wrecked
            BlockWreckedEvent ev = new BlockWreckedEvent(clickedBlock, player.getPlayer());
            Bukkit.getPluginManager().callEvent(ev);

            // Break the block naturally (like the player would do manually)
            clickedBlock.breakNaturally();
        } catch (Exception ex) {
            // Log the exception for debugging purposes (optional)
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        try {
            ItemStack itemStack = e.getItemDrop().getItemStack();
            if (itemStack.getType() != Material.FIREBALL || !itemStack.hasItemMeta()) {
                return;
            }

            // Remove the WreckingBall item if dropped
            if (itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(WreckingBallModule.WRECKINGBALL_NAME)) {
                e.getItemDrop().remove();
            }
        } catch (Exception ex) {
            // Log the exception for debugging purposes (optional)
            ex.printStackTrace();
        }
    }

    @EventHandler
    public void onWreck(BlockWreckedEvent e) {
        // Prevent breaking of certain blocks
        Block block = e.getBlock();
        if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
            e.getPlayer().sendMessage(Messages.getInstance().getPrefix() + "Je mag geen kisten breken met een WreckingBall!");
            return;
        }

        // Prevent breaking blocks in restricted areas
        if (NexusModule.getInstance().isNexus(block) || BuildModule.getInstance().isBuilding(block)) {
            return;
        }
    }
}
