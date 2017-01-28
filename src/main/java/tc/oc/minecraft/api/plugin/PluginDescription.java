package tc.oc.minecraft.api.plugin;

import java.util.List;

public interface PluginDescription {

    /**
     * Return the name of this plugin
     */
    String getName();

    /**
     * Return the version of the plugin
     */
    String getVersion();

    /**
     * Return a human-friendly description of the functionality the plugin provides
     */
    String getDescription();

    /**
     * Return the list of authors for the plugin
     */
    List<String> getAuthors();

    /**
     * Return a list of other plugins that the plugin requires
     */
    List<String> getDepend();

    /**
     * Return a list of other plugins that the plugin requires for full functionality
     */
    List<String> getSoftDepend();

    boolean isIsolated();
}
