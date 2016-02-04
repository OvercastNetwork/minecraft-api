package tc.oc.minecraft.api.plugin;

import java.io.File;
import java.io.InputStream;

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
