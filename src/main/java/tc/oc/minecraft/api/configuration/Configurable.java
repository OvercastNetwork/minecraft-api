package tc.oc.minecraft.api.configuration;

public interface Configurable {

    /**
     * Return the most recently loaded configuration for this plugin
     */
    Configuration getConfig();

    /**
     * Reload this plugin's configuration from its source file or resource
     */
    void reloadConfig();

    /**
     * Save the currently loaded configuration to the filesystem
     */
    void saveConfig();
}
