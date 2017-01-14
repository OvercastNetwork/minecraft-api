package tc.oc.minecraft.api.server;

import java.util.Collection;
import java.util.UUID;

import tc.oc.minecraft.api.entity.Player;
import tc.oc.reference.Handle;
import tc.oc.reference.Handleable;

/**
 * A Minecraft server or proxy, local or remote
 */
public interface Server extends Handleable {

    @Override
    Handle<? extends Server> handle();

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
    Player getPlayerExact(String name);

    /**
     * Gets a connected player via their UUID
     *
     * @param id UUID of the player
     * @return their player instance, or null if the player was not found
     */
    Player getPlayer(UUID id);
}
