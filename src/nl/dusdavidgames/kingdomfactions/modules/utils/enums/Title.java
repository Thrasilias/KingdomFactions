package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum Title {
    TITLE("Main Title"),
    SUBTITLE("Subtitle");

    private final String displayName;

    Title(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Check if the title is a main title.
     * @return true if the title is TITLE.
     */
    public boolean isMainTitle() {
        return this == TITLE;
    }

    /**
     * Check if the title is a subtitle.
     * @return true if the title is SUBTITLE.
     */
    public boolean isSubtitle() {
        return this == SUBTITLE;
    }
}
