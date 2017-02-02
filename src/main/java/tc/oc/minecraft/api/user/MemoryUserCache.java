package tc.oc.minecraft.api.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Caches {@link User}s in memory
 */
@Singleton
public class MemoryUserCache implements UserSource {

    private final UserFactory userFactory;

    private final Map<UUID, User> byId = new HashMap<>();
    private final Map<String, User> byName = new HashMap<>();

    @Inject MemoryUserCache(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    @Override
    public User search(UserSearch search, Supplier<User> next) {
        User user = search.query() instanceof UUID ? byId.get(search.query())
                                                   : byName.get((String) search.query());
        if(user == null || !search.filter().test(user)) {
            user = next.get();
            update(user);
        }
        return user;
    }

    @Override
    public void update(User user) {
        if(user.hasValidId()) {
            user = userFactory.copyUser(user);
            synchronized(byId) {
                byId.put(user.getUniqueId(), user);
                byName.put(user.name().get(), user);
            }
        }
    }
}
