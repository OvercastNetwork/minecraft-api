package tc.oc.minecraft.api.server;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import tc.oc.minecraft.api.command.ConsoleCommandSender;
import tc.oc.minecraft.api.entity.OfflinePlayer;
import tc.oc.minecraft.api.logging.Loggable;
import tc.oc.minecraft.api.plugin.PluginFinder;

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

    default OfflinePlayer getOfflinePlayer(UUID id) {
        return () -> id;
    }

    default Optional<? extends OfflinePlayer> tryOfflinePlayer(UUID id) {
        return Optional.empty();
    }

    default Set<? extends OfflinePlayer> getSavedPlayers() {
        return Collections.emptySet();
    }

    default Set<? extends OfflinePlayer> getWhitelistedPlayers() {
        return Collections.emptySet();
    }

    default Set<? extends OfflinePlayer> getOperators() {
        return Collections.emptySet();
    }

    default int getMaxPlayers() {
        return 0;
    }
}
