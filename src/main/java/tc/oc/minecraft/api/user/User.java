package tc.oc.minecraft.api.user;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import tc.oc.minecraft.api.entity.Player;

/**
 * A player who may or may not be online.
 *
 * Basic {@link User} instances can be looked up through {@link UserFinder},
 * or created through {@link UserFactory}.
 */
public interface User {

    /**
     * The player's unique ID
     *
     * This may be fake, but it is never null
     *
     * @see #hasValidId()
     */
    UUID getUniqueId();

    /**
     * If true, {@link #getUniqueId()} is the player's real ID, assigned by Mojang.
     *
     * If false, {@link #getUniqueId()} is a fake ID, derived from their username.
     *
     * Fake IDs can be generated from a login in offline mode, or from a non-existant
     * username, which can come from fake scoreboard entries.
     */
    default boolean hasValidId() {
        return UserUtils.isValidId(getUniqueId());
    }

    @Nullable String getName();

    default Optional<String> name() {
        return Optional.ofNullable(getName());
    }

    default Optional<Instant> updatedAt() {
        return Optional.empty();
    }

    default boolean isOnline() {
        return this instanceof Player;
    }

    default @Nullable Player getPlayer() {
        return this instanceof Player ? (Player) this : null;
    }

    default Optional<Player> onlinePlayer() {
        return Optional.ofNullable(getPlayer());
    }

    default Optional<Instant> lastPlayedAt() {
        return onlinePlayer().map(player -> Instant.now());
    }
}
