package tc.oc.minecraft.api.user;

import java.time.Instant;
import java.util.UUID;

import com.google.common.util.concurrent.ListenableFuture;

/**
 * Lookup {@link User}s by their {@link UUID} or username.
 *
 * If the returned {@link User} has a {@link User#name()}, then it
 * will also have an {@link User#updatedAt()} timestamp, indicating the most
 * recent time that the name, and any other properties, were verified with
 * some authoritative source.
 *
 * The freshness parameter constrains the result to {@link User}s who have
 * been verified since the given {@link Instant}.
 *
 * A failed {@link UUID} lookup will return a {@link User} with no name or,
 * update timestamp. A failed username lookup will return a {@link User} with
 * a fake {@link UUID}, i.e. {@link User#hasValidId()} will be false.
 *
 * The methods returning a {@link User} are synchronous, and may block on I/O
 * operations. If these are called on the main server thread, they will return
 * an empty result rather than block. The asynchronous methods always return
 * immediately, and perform any blocking operations on worker threads.
 *
 * Any of these methods may return a {@link tc.oc.minecraft.api.entity.Player},
 * if the respective player is currently online.
 */
public interface UserFinder {

    User findUser(UUID id, Instant freshness);

    User findUser(String name, Instant freshness);

    ListenableFuture<User> findUserAsync(UUID id, Instant freshness);

    ListenableFuture<User> findUserAsync(String name, Instant freshness);

    default User findUser(UUID id) {
        return findUser(id, Instant.EPOCH);
    }

    default User findUser(String name) {
        return findUser(name, Instant.EPOCH);
    }

    default ListenableFuture<User> findUserAsync(UUID id) {
        return findUserAsync(id, Instant.EPOCH);
    }

    default ListenableFuture<User> findUserAsync(String name) {
        return findUserAsync(name, Instant.EPOCH);
    }
}
