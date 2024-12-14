package nl.dusdavidgames.kingdomfactions.modules.utils.action;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.modules.data.DataList;
import nl.dusdavidgames.kingdomfactions.modules.data.DataManager;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;

public abstract class SmallAction implements IAction {

    @Getter @Setter
    private String name;  // Made name mutable, allowing it to be changed if necessary.
    
    @Getter
    private KingdomFactionsPlayer player;
    
    // DataManager for additional data associated with the action
    @Getter
    private DataManager additionalData;

    // Constructor that initializes the player and name, and prepares the additional data manager
    public SmallAction(String name, KingdomFactionsPlayer player) {
        this.name = name;
        this.player = player;
        this.additionalData = new DataManager(new DataList());
    }

    // You can override the execute method in subclasses if needed
    @Override
    public void execute() throws KingdomFactionsException {
        // Default behavior for the execute method (if needed, can be overridden)
        // This may throw a KingdomFactionsException if necessary
    }

    // Cancel method is inherited from IAction interface
    @Override
    public void cancel() {
        super.cancel(); // Ensures player action is canceled properly
    }
}
