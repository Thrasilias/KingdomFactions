package nl.dusdavidgames.kingdomfactions.modules.wreckingball;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Custom event triggered when a block is destroyed by the Wrecking Ball item.
 * This event ensures that blocks broken by the Wrecking Ball cannot be cancelled.
 * It extends the BlockBreakEvent to leverage the block and player details.
 */
public class BlockWreckedEvent extends BlockBreakEvent {

    /**
     * Constructor for the BlockWreckedEvent.
     * This is used to create a new event when a block is broken by the Wrecking Ball.
     *
     * @param block  The block being broken.
     * @param player The player who triggered the event (breaking the block).
     */
    public BlockWreckedEvent(Block block, Player player) {
        super(block, player);
    }

    /**
     * Override the isCancelled method to ensure that the block cannot be cancelled 
     * when broken by the Wrecking Ball.
     *
     * @return false, indicating that the block break event cannot be cancelled.
     */
    @Override
    public boolean isCancelled() {
        return false;
    }
}
