package nl.dusdavidgames.kingdomfactions.modules.utils.particles;

import java.lang.reflect.Field;

/**
 * Utility class for performing reflection-based operations on fields.
 * Allows setting and retrieving values of private or protected fields.
 */
public class ReflectionUtilities {

    /**
     * Sets the value of a field in a given instance.
     *
     * @param instance  The instance whose field is to be modified.
     * @param fieldName The name of the field to set.
     * @param value     The value to set.
     * @throws NoSuchFieldException   If the field does not exist.
     * @throws IllegalAccessException If the field cannot be accessed.
     */
    public static void setValue(Object instance, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(instance, fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }

    /**
     * Retrieves the value of a field from a given instance.
     *
     * @param instance  The instance whose field is to be retrieved.
     * @param fieldName The name of the field to retrieve.
     * @return The value of the field.
     * @throws NoSuchFieldException   If the field does not exist.
     * @throws IllegalAccessException If the field cannot be accessed.
     */
    public static Object getValue(Object instance, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = getField(instance, fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    /**
     * Finds a field in the class hierarchy of the given instance.
     *
     * @param instance  The instance whose class hierarchy is to be searched.
     * @param fieldName The name of the field to find.
     * @return The Field object corresponding to the given field name.
     * @throws NoSuchFieldException If the field does not exist.
     */
    private static Field getField(Object instance, String fieldName) throws NoSuchFieldException {
        Class<?> clazz = instance.getClass();

        while (clazz != null) {
            try {
                return clazz.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field \"" + fieldName + "\" not found in class hierarchy of " + instance.getClass().getName());
    }
}
