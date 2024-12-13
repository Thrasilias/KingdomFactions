package nl.dusdavidgames.kingdomfactions.modules.utils;

import nl.dusdavidgames.kingdomfactions.modules.nexus.build.type.BuildingType;

public interface IBuildable {

    // Returns the type of the building (e.g., house, tower, etc.)
    BuildingType getBuildingType();

    // Determines if the building can be constructed (e.g., checks if resources are available)
    default boolean canBuild() {
        return true;  // Override in implementing classes if needed to add conditions
    }

    // Returns the cost to build the building (in resources)
    default int getConstructionCost() {
        return 100;  // Default cost, can be overridden
    }

    // Returns the time required to complete the construction
    default int getBuildTime() {
        return 60;  // Default build time in seconds, can be overridden
    }
}
