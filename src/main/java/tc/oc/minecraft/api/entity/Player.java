package tc.oc.minecraft.api.entity;

import java.util.Locale;
import java.util.UUID;

import net.md_5.bungee.api.chat.BaseComponent;
import tc.oc.minecraft.api.command.CommandSender;

public interface Player extends CommandSender {

    /**
     * Gets this player's display name.
     *
     * @return the players current display name
     */
    String getDisplayName();

    /**
     * Sets this players display name to be used as their nametag and tab list
     * name.
     *
     * @param name the name to set
     */
    void setDisplayName(String name);

    /**
     * Send a message to the specified screen position of this player.
     *
     * @param position the screen position
     * @param message the message to send
     */
    void sendMessage(int position, BaseComponent... message);

    /**
     * Send a message to the specified screen position of this player.
     *
     * @param position the screen position
     * @param message the message to send
     */
    void sendMessage(int position, BaseComponent message);

    /**
     * Get this connection's UUID, if set.
     *
     * @return the UUID
     */
    UUID getUniqueId();

    /**
     * Get the player's current locale
     */
    Locale getCurrentLocale();
}
