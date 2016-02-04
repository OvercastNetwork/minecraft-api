package tc.oc.minecraft.api;

import java.util.Collection;
import java.util.UUID;

import tc.oc.minecraft.api.command.ConsoleCommandSender;
import tc.oc.minecraft.api.entity.Player;
import tc.oc.minecraft.api.logging.Loggable;
import tc.oc.minecraft.api.plugin.PluginManager;

public interface Server extends Loggable {

    PluginManager getPluginManager();

    ConsoleCommandSender getConsoleSender();

    /**
     * Return all players currently connected.
     *
     * @return all connected players
     */
    Collection<? extends Player> getOnlinePlayers();

    /**
     * Gets a connected player via their unique username.
     *
     * @param name of the player
     * @return their player instance
     */
    Player getPlayer(String name);

    /**
     * Gets a connected player via their UUID
     *
     * @param id UUID of the player
     * @return their player instance
     */
    Player getPlayer(UUID id);
}
