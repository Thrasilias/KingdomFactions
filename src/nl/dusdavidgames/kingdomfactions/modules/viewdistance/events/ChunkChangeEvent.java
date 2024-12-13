package nl.dusdavidgames.kingdomfactions.modules.viewdistance.events;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.dusdavidgames.kingdomfactions.modules.player.PlayerModule;
import nl.dusdavidgames.kingdomfactions.modules.viewdistance.ViewDistanceModule;

public class ChunkChangeEvent implements Listener {

    @EventHandler
    public void onChunkChange(PlayerMoveEvent event) {
        // Get the player's current and previous location
        Location to = event.getTo();
        Location from = event.getFrom();
        
        // If the player has not changed chunk, do nothing
        if (to.getBlockX() == from.getBlockX() && to.getBlockZ() == from.getBlockZ()) {
            return;
        }
        
        // Get the current and previous chunks
        Chunk toChunk = to.getChunk();
        Chunk fromChunk = from.getChunk();
        
        // If the player is still in the same chunk, return
        if (toChunk.getX() == fromChunk.getX() && toChunk.getZ() == fromChunk.getZ()) {
            return;
        }

        // Delegate view distance update to the ViewDistanceModule
        PlayerModule playerModule = PlayerModule.getInstance();
        playerModule.getPlayer(event.getPlayer()).updateViewDistance();
    }
}
