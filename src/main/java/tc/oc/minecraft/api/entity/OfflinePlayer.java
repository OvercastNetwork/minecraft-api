package tc.oc.minecraft.api.entity;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

public interface OfflinePlayer {

    /**
     * Get this player's UUID
     *
     * @return the UUID
     */
    UUID getUniqueId();

    default boolean isOnline() {
        return this instanceof Player;
    }

    default @Nullable Player getPlayer() {
        return this instanceof Player ? (Player) this : null;
    }

    default Optional<Player> onlinePlayer() {
        return Optional.ofNullable(getPlayer());
    }

    default Optional<String> getLastKnownName() {
        return onlinePlayer().map(Player::getName);
    }
}
