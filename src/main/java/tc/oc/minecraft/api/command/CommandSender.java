package tc.oc.minecraft.api.command;

import net.md_5.bungee.api.chat.BaseComponent;
import tc.oc.minecraft.api.permissions.Permissible;

public interface CommandSender extends Permissible {

    /**
     * Get the unique name of this command sender.
     *
     * @return the senders username
     */
    String getName();

    default String getName(CommandSender viewer) {
        return getName();
    }

    /**
     * Send a message to this sender.
     *
     * @param message the message to send
     * @deprecated use components
     */
    @Deprecated
    void sendMessage(String message);

    default void sendMessage(String[] message) {
        for(String line : message) sendMessage(line);
    }

    /**
     * Send a message to this sender.
     *
     * @param message the message to send
     */
    void sendMessage(BaseComponent... message);

    /**
     * Send a message to this sender.
     *
     * @param message the message to send
     */
    void sendMessage(BaseComponent message);
}
