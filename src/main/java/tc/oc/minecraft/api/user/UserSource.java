package tc.oc.minecraft.api.user;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * A service that can lookup {@link User}s by {@link UUID} or username.
 *
 * This service deals only with real data, verified by Mojang's API.
 * It never returns fake {@link UUID}s or usernames.
 *
 * Several of these can be registered through {@link UserSourceBinder}.
 * The {@link UserFinder} will try them all, in the order they are registred.
 *
 * Each source may cache results from later sources for any amount of time,
 * but it must preserve the {@link User#updatedAt()} timestamp. When a
 * cached user does not pass {@link UserSearch#filter()}, and the next
 * source returns a result (which will pass the filter), the cache should
 * be updated with that result.
 */
public interface UserSource {

    /**
     * Try to find a {@link User} matching the given {@link UserSearch}.
     *
     * If no such user is available, call the given {@link Supplier} and
     * return the result, which can be assumed to match all search criteria.
     * This result must NOT be retained directly, but it can be copied using
     * {@link UserFactory#copyUser(User)}, and cached indefinitely for future searches.
     * If the next supplier throws an exception, it must be propagated.
     *
     * This method can be called concurrently, so it must be thread-safe.
     * If {@link UserSearch#sync()} is true, then the main server thread is waiting
     * on the result of this search, and so it must not block, or try to synchronize
     * with other threads that might block. Care must be taken to prevent synchronous
     * searches from waiting on async searches that block.
     */
    User search(UserSearch search, Supplier<User> next);

    /**
     * Update any cached data for the given {@link User}, which will always have
     * a {@link User#name()} and an {@link User#updatedAt()} timestamp. The given
     * object must NOT be retained, but a copy can be made with
     * {@link UserFactory#copyUser(User)} that can be retained indefinitely.
     *
     * The user should NOT be cached if {@link User#hasValidId()} returns false.
     *
     * This method is NEVER called on the main server thread, so it can always block.
     */
    default void update(User user) {}
}
