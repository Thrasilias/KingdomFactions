public interface IRank {

    // Default implementation to check if the rank is associated with a kingdom
    default public boolean isKingdomRank() {
        return false;
    }

    // Method to get the name of the rank
    String getRankName();

    // Method to get the rank power/level (could be numeric)
    int getRankPower();

    // Method to check if the rank is promotable
    boolean canPromote();
}
