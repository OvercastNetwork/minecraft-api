package tc.oc.minecraft.api.entity;

import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Optional;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import tc.oc.minecraft.api.command.CommandSender;
import tc.oc.minecraft.api.user.User;

public interface Player extends User, CommandSender {

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
     * @param position the screen position:
     * @param message the message to send
     */
    void sendMessage(ChatMessageType position, BaseComponent... message);

    /**
     * Send a message to the specified screen position of this player.
     *
     * @param position the screen position
     * @param message the message to send
     */
    void sendMessage(ChatMessageType position, BaseComponent message);

    /**
     * Get the player's current locale
     */
    Locale getCurrentLocale();

    /**
     * Get the Minecraft protocol version in use by this player's connection
     */
    int getProtocolVersion();

    InetSocketAddress getAddress();

    @Override
    default Optional<String> name() {
        return Optional.of(getName());
    }
}
