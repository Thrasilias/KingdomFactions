package nl.dusdavidgames.kingdomfactions.modules.kingdom.menu;

import java.util.ArrayList;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;

public class KingdomMenuModule {

    // Singleton instance
    private static @Getter KingdomMenuModule instance;

    private @Getter ArrayList<KingdomItem> items = new ArrayList<KingdomItem>();

    public KingdomMenuModule() {
        // Register the listener when the module is created
        KingdomFactionsPlugin.getInstance().registerListener(new KingdomMenu());
        instance = this;  // Set the singleton instance
    }

    // Initialize the kingdom items or other configuration
    public void init() {
        // Initialization for KingdomItems
        items.add(new KingdomItem(KingdomType.EREDON, 2));
        items.add(new KingdomItem(KingdomType.DOK, 4));
        items.add(new KingdomItem(KingdomType.TILIFIA, 6));
        items.add(new KingdomItem(KingdomType.ADAMANTIUM, 20));
        items.add(new KingdomItem(KingdomType.MALZAN, 22));
        items.add(new KingdomItem(KingdomType.HYVAR, 24));
        items.add(new KingdomItem(KingdomType.GEEN, 40));
    }
}
