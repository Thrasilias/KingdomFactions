package nl.dusdavidgames.kingdomfactions.modules.kingdom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;

import org.bukkit.Location;

import lombok.Getter;
import lombok.Setter;
import nl.dusdavidgames.kingdomfactions.KingdomFactionsPlugin;
import nl.dusdavidgames.kingdomfactions.modules.database.mysql.databases.KingdomDatabase;
import nl.dusdavidgames.kingdomfactions.modules.exception.kingdom.UnkownKingdomException;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.command.KingdomCommand;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.eventhandlers.KingdomSwitchListener;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.eventhandlers.capital.InteractEventListener;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.Kingdom;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.kingdom.KingdomType;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.menu.KingdomMenuModule;
import nl.dusdavidgames.kingdomfactions.modules.kingdom.nexus.SetCapitalAction;
import nl.dusdavidgames.kingdomfactions.modules.player.player.online.KingdomFactionsPlayer;
import nl.dusdavidgames.kingdomfactions.modules.utils.Utils;

public class KingdomModule {

    private static @Getter @Setter KingdomModule instance;

    @Getter
    private ArrayList<Kingdom> kingdoms = new ArrayList<>();

    public KingdomModule() {
        setInstance(this);
        loadKingdoms();
        new KingdomCommand("kingdom", "kingdomfactions.command.kingdom", "Main Kingdom Command", "", true, false).registerCommand();
        new KingdomMenuModule();
        
        // Register event listeners
        KingdomFactionsPlugin.getInstance().registerListener(new KingdomSwitchListener());
        KingdomFactionsPlugin.getInstance().registerListener(new InteractEventListener());
    }

    public Kingdom getSmallestKingdom() {
        HashMap<Kingdom, Integer> size = new HashMap<>();
        for (Kingdom k : kingdoms) {
            size.put(k, k.getMembers());
        }

        Optional<Entry<Kingdom, Integer>> smallest = size.entrySet()
                .stream()
                .min(Entry.comparingByValue()); // Finds the smallest kingdom by members

        return smallest.map(Entry::getKey).orElse(null); // Return smallest kingdom or null if none found
    }

    public void loadKingdoms() {
        for (KingdomType type : KingdomType.values()) {
            if (type == KingdomType.ERROR) continue;

            try {
                kingdoms.add(KingdomDatabase.getInstance().loadKingdom(type));
            } catch (UnkownKingdomException e) {
                KingdomDatabase.getInstance().prepareKingdom(type.toString());
                Kingdom kingdom = new Kingdom(
                        type,
                        Utils.getInstance().getMiningWorld().getSpawnLocation(),
                        new Location(Utils.getInstance().getOverWorld(), 0, 0, 0),
                        new Location(Utils.getInstance().getOverWorld(), 0, 0, 0)
                );
                kingdoms.add(kingdom);
            }
        }
    }

    public Kingdom getKingdom(KingdomType kingdomType) {
        return kingdoms.stream()
                .filter(k -> k.getType().equals(kingdomType))
                .findFirst()
                .orElse(null);
    }

    public Kingdom getKingdom(String kingdomName) {
        return kingdoms.stream()
                .filter(k -> k.getType().toString().equalsIgnoreCase(kingdomName))
                .findFirst()
                .orElse(null);
    }

    public void createAction(Kingdom kingdom, KingdomFactionsPlayer player) {
        SetCapitalAction action = new SetCapitalAction(kingdom, player);
        player.setAction(action);
    }
}
