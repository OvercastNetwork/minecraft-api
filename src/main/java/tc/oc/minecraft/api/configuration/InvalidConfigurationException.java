package tc.oc.minecraft.api.configuration;

/**
 * Exception thrown when attempting to load an invalid {@link Configuration}
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }

    public InvalidConfigurationException(ConfigurationSection section, String message) {
        this(section, null, message);
    }

    public InvalidConfigurationException(ConfigurationSection section, String key, String message) {
        this(section, key, -1, message);
    }

    public InvalidConfigurationException(ConfigurationSection section, String key, int index, String message) {
        this("Configuration error at " + (key == null ? section.getCurrentPath() : section.resolvePath(key)) +
             (index < 0 ? "" : " element " + index) +
             ": " + message);
    }
}
