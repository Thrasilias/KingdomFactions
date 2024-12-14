package nl.dusdavidgames.kingdomfactions.modules.utils.rank;

import java.util.Objects;

public abstract class Rank {

    /**
     * Gets the name of the rank.
     * @return The name of the rank.
     */
    public abstract String getName();

    /**
     * Determines if this rank is a staff rank.
     * @return True if the rank is staff, false otherwise.
     */
    public abstract boolean isStaff();

    /**
     * Gets the permission node associated with this rank.
     * @return The permission string.
     */
    public String getPermission() {
        return "kingdomfactions.rank." + getName();
    }

    /**
     * Overrides the toString method to provide a string representation of the rank.
     * @return A string representation of the rank.
     */
    @Override
    public String toString() {
        return "Rank{name='" + getName() + "', isStaff=" + isStaff() + "}";
    }

    /**
     * Overrides equals to compare two Rank objects.
     * @param obj The object to compare with.
     * @return True if both ranks are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Rank rank = (Rank) obj;
        return isStaff() == rank.isStaff() && getName().equals(rank.getName());
    }

    /**
     * Overrides hashCode for proper ranking comparisons.
     * @return A hash code value for the rank.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getName(), isStaff());
    }
}
