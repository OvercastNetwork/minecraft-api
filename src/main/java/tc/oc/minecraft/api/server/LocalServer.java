package tc.oc.minecraft.api.server;

import java.util.Set;

import tc.oc.minecraft.api.command.ConsoleCommandSender;
import tc.oc.minecraft.api.logging.Loggable;
import tc.oc.minecraft.api.plugin.PluginFinder;
import tc.oc.reference.Handle;

/**
 * The local server i.e. the one hosting plugins
 */
public interface LocalServer extends Loggable, Server {

    @Override
    Handle<? extends LocalServer> handle();

    PluginFinder getPluginFinder();

    ConsoleCommandSender getConsoleSender();

    /**
     * All Minecraft protocol versions supported by this server
     */
    Set<Integer> getProtocolVersions();
}
