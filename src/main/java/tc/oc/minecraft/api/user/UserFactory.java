package tc.oc.minecraft.api.user;

import java.time.Instant;
import java.util.UUID;

/**
 * Creates light-weight {@link User} objects that are safe to store indefinitely.
 */
public interface UserFactory {

    User createUser(UUID id);

    User createUser(String name);

    User createUser(UUID id, String name, Instant updatedAt);

    default User copyUser(User user) {
        final String name = user.getName();
        return name != null ? createUser(user.getUniqueId(), name, user.updatedAt().orElseGet(Instant::now))
                            : createUser(user.getUniqueId());
    }
}
