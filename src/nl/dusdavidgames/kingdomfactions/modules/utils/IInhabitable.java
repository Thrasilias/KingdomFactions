package nl.dusdavidgames.kingdomfactions.modules.utils;

import nl.dusdavidgames.kingdomfactions.modules.utils.enums.InhabitableType;

public interface IInhabitable {

    // Returns the type of inhabitable object (e.g., city, village, etc.)
    InhabitableType getInhabitableType();

    // Additional method for checking if it's available for settlement
    default boolean isAvailableForSettlement() {
        return true;  // This can be overridden in implementing classes
    }

    // Optional method for getting the maximum population
    default int getMaxPopulation() {
        return 100;  // Default value, can be overridden
    }
}
