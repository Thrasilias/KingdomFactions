package nl.dusdavidgames.kingdomfactions.modules.utils;

import java.util.ArrayList;

import nl.dusdavidgames.kingdomfactions.modules.data.Data;

public class MetaData extends ArrayList<Data> {

    private static final long serialVersionUID = -7261348505790157311L;

    /**
     * Adds a Data object to the MetaData list with custom logic.
     * 
     * @param data The Data object to add.
     */
    @Override
    public boolean add(Data data) {
        // Custom logic before adding the data (if needed)
        // Example: Check if the data is valid, modify it, etc.
        if (data != null) {
            return super.add(data);  // Call ArrayList's add method
        }
        return false;  // Avoid adding null data, for example
    }

    // Optionally, you can leave the set() method or use it for additional custom behavior
    public void set(Data data) {
        this.add(data);  // Or add any other logic you need here
    }
}
