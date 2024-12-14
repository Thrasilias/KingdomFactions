package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum TitleDuration {
    SHORT(20),  // 1 second
    MEDIUM(60), // 3 seconds
    LONG(100);  // 5 seconds

    private final int ticks;

    TitleDuration(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public String toString() {
        switch (this) {
            case SHORT: return "Short (1 second)";
            case MEDIUM: return "Medium (3 seconds)";
            case LONG: return "Long (5 seconds)";
            default: return super.toString();
        }
    }

    /**
     * Get the duration of the title in ticks.
     * @return the number of ticks corresponding to this duration.
     */
    public int getDurationInTicks() {
        return this.ticks;
    }
}
