package tc.oc.minecraft.api.server;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

import tc.oc.minecraft.api.command.ConsoleCommandSender;
import tc.oc.minecraft.api.logging.Loggable;
import tc.oc.minecraft.api.plugin.PluginFinder;
import tc.oc.minecraft.api.user.User;

/**
 * The local server i.e. the one hosting plugins
 */
public interface LocalServer extends Loggable, Server {

    String getName();

    String getVersion();

    PluginFinder getPluginFinder();

    ConsoleCommandSender getConsoleSender();

    /**
     * All Minecraft protocol versions supported by this server
     */
    Set<Integer> getProtocolVersions();

    Path getRootPath();

    void stop();

    boolean isStopping();

    default boolean hasWhitelist() {
        return false;
    }

    default void setWhitelist(boolean whitelist) {}

    default Set<? extends User> getSavedPlayers() {
        return Collections.emptySet();
    }

    default Set<? extends User> getWhitelistedPlayers() {
        return Collections.emptySet();
    }

    default Set<? extends User> getOperators() {
        return Collections.emptySet();
    }

    default int getMaxPlayers() {
        return 0;
    }

    default boolean isMainThread() {
        return false;
    }
}
