package tc.oc.minecraft.api.server;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.UUID;
import javax.annotation.Nullable;

import tc.oc.minecraft.api.entity.Player;

/**
 * A Minecraft server or proxy, local or remote
 */
public interface Server {

    InetSocketAddress getAddress();

    default int getPort() {
        return getAddress().getPort();
    }

    /**
     * Return all players currently connected.
     *
     * @return all connected players
     */
    Collection<? extends Player> getOnlinePlayers();

    /**
     * Gets a connected player via their unique username (case insensitive).
     *
     * @param name of the player
     * @return their player instance, or null if the player was not found
     */
    @Nullable Player getPlayerExact(String name);

    /**
     * Gets a connected player via their UUID
     *
     * @param id UUID of the player
     * @return their player instance, or null if the player was not found
     */
    @Nullable Player getPlayer(UUID id);
}
