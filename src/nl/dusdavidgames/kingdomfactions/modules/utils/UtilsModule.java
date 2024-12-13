package nl.dusdavidgames.kingdomfactions.modules.utils;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.utils.yesnoconfirm.YesNoConfirmationModule;

public class UtilsModule {

    private static @Getter @Setter UtilsModule instance;

    public UtilsModule() {
        setInstance(this);

        // Initialize utility classes lazily if needed
        initializeUtilities();

        // Register listeners
        registerListeners();
    }

    private void initializeUtilities() {
        // Initialize utility classes when needed (could be optimized further)
        new NameHistory();
        new Messages();
        new Utils();
        new Item();
    }

    private void registerListeners() {
        // Register the Yes/No Confirmation listener
        KingdomFactionsPlugin.getInstance().registerListener(new YesNoConfirmationModule());
    }
}
