package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

import lombok.Getter;
import nl.dusdavidgames.kingdomfactions.modules.data.Data;
import nl.dusdavidgames.kingdomfactions.modules.data.types.ArrayData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.BooleanData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.DoubleData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.FloatData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.IntegerData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.LongData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.ObjectData;
import nl.dusdavidgames.kingdomfactions.modules.data.types.StringData;

public enum DataType {

    INT(IntegerData.class),
    STRING(StringData.class),
    BOOLEAN(BooleanData.class),
    FLOAT(FloatData.class),
    DOUBLE(DoubleData.class),
    OBJECT(ObjectData.class),
    LONG(LongData.class),
    LIST(ArrayData.class);

    @Getter
    private final Class<? extends Data> clazz;

    DataType(Class<? extends Data> clazz) {
        this.clazz = clazz;
    }

    /**
     * Returns an instance of the corresponding Data class.
     * @return A new instance of the corresponding Data class.
     */
    public Data getInstance() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if the DataType corresponds to a primitive type.
     * @return true if the DataType is a primitive type, false otherwise.
     */
    public boolean isPrimitive() {
        return this == INT || this == BOOLEAN || this == FLOAT || this == DOUBLE || this == LONG;
    }
}
