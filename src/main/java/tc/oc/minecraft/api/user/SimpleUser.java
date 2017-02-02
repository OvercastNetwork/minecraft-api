package tc.oc.minecraft.api.user;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public final class SimpleUser implements User {

    private final UUID id;
    private final @Nullable String name;
    private final @Nullable Instant updatedAt;

    private SimpleUser(UUID id, @Nullable String name, @Nullable Instant updatedAt) {
        this.id = checkNotNull(id);
        this.name = name;
        this.updatedAt = updatedAt;
    }

    @Override
    public UUID getUniqueId() {
        return id;
    }

    @Override
    public @Nullable String getName() {
        return name;
    }

    @Override
    public Optional<String> name() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<Instant> updatedAt() {
        return Optional.ofNullable(updatedAt);
    }

    static class Factory implements UserFactory {

        @Override
        public User createUser(UUID id) {
            return new SimpleUser(id, null, null);
        }

        @Override
        public User createUser(String name) {
            return new SimpleUser(UserUtils.offlinePlayerId(name), name, null);
        }

        @Override
        public User createUser(UUID id, String name, Instant updatedAt) {
            return new SimpleUser(id, name, updatedAt);
        }

        @Override
        public User copyUser(User user) {
            return user instanceof SimpleUser
                   ? (SimpleUser) user
                   : UserFactory.super.copyUser(user);
        }
    }
}
