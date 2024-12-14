package nl.dusdavidgames.kingdomfactions.modules.utils.rank.framework.frame;

/**
 * Represents a development-related staff rank.
 * This rank is for members contributing to the technical or creative development of the server.
 */
public abstract class Development extends StaffRank {

    /**
     * Indicates whether this rank has access to development tools.
     * Subclasses can override this method to customize access levels.
     *
     * @return True, as development ranks typically have access to tools.
     */
    public boolean hasDevelopmentToolsAccess() {
        return true;
    }

    /**
     * Provides a description of the specific development role.
     * Subclasses should override this method to specify their responsibilities (e.g., "Plugin Developer", "Builder").
     *
     * @return A string describing the role.
     */
    public abstract String getDevelopmentRole();
}
