package nl.dusdavidgames.kingdomfactions.modules.utils.enums;

public enum ValueEditAction {
    ADD("Adding value"),
    REMOVE("Removing value");

    private final String description;

    ValueEditAction(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }

    /**
     * Checks if the action is an add action.
     * @return true if the action is ADD.
     */
    public boolean isAddAction() {
        return this == ADD;
    }

    /**
     * Checks if the action is a remove action.
     * @return true if the action is REMOVE.
     */
    public boolean isRemoveAction() {
        return this == REMOVE;
    }
}
