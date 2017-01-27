package tc.oc.minecraft.api.plugin;

import java.io.File;
import java.io.InputStream;

import com.google.inject.Injector;
import tc.oc.inject.ProtectedBinder;
import tc.oc.minecraft.api.event.EventRegistry;
import tc.oc.exception.ExceptionHandler;
import tc.oc.minecraft.api.server.Server;
import tc.oc.minecraft.api.logging.Loggable;

public interface Plugin extends Loggable {
    /**
     * Return metadata about this plugin
     */
    PluginDescription getDescription();

    /**
     * Return a folder where this plugin may store arbitrary data
     */
    File getDataFolder();

    /**
     * Open a resource embedded in this plugin.
     * The caller is responsible for closing the stream.
     */
    InputStream getResource(String name);

    /**
     * Return the Server instance hosting this plugin
     */
    Server getServer();
    /**
     * @return An {@link ExceptionHandler} that canbe used to report
     *         exceptions related to this plugin.
     */
    ExceptionHandler exceptionHandler();

    /**
     * @return The {@link EventRegistry} belonging to this plugin.
     */
    EventRegistry eventRegistry();

    /**
     * Return the private {@link Injector} for this plugin.
     *
     * Note that direct injector use is considered bad form,
     * and is only provided to assist in migrating legacy code.
     * Nice code should @Inject its dependencies.
     */
    Injector injector();

    /**
     * Called during {@link Injector} creation to configure this plugin. This happens
     * during server startup, before {@link #onLoad()} is called.
     *
     * The given {@link ProtectedBinder} belongs to this plugin's private environment,
     * and the binder returned from {@link ProtectedBinder#publicBinder()} is the global
     * environment shared by all plugins, and the server itself.
     *
     * If the plugin's main class is a {@link tc.oc.inject.ProtectedModule} rather than a
     * {@link Plugin}, then the server will provide a generic {@link Plugin} instance that
     * simply installs that module. The main class can also be a {@link com.google.inject.Module},
     * but then it will only have access to the private environment. This is the recommended
     * approach for purely injection-based plugins. This method is intended to assist in
     * migrating legacy code.
     */
    default void configure(ProtectedBinder binder) {}

    /**
     * Override and return false to prevent this plugin from being enabled by the server.
     * The plugin will still load and be injected, but the enable/disable lifecycle
     * callbacks will not be called.
     */
    default boolean isActive() {
        return true;
    }

    boolean isEnabled();

    /**
     * Called when this plugin is loaded.
     * All plugins are loaded before any of them are disabled.
     */
    void onLoad();

    /**
     * Called when this plugin is enabled.
     */
    void onEnable();

    /**
     * Called when this plugin is disabled.
     */
    void onDisable();
}
