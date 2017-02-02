package tc.oc.minecraft.api.user;

import java.time.Instant;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import tc.oc.minecraft.api.entity.Player;
import tc.oc.minecraft.api.scheduler.Async;
import tc.oc.minecraft.api.server.LocalServer;

public class UserFinderImpl implements UserFinder {

    private final UserFactory userFactory;
    private final LocalServer localServer;
    private final Set<UserSource> sources;
    private final ListeningExecutorService executor;

    @Inject UserFinderImpl(UserFactory userFactory, LocalServer localServer, Set<UserSource> sources, @Async ListeningExecutorService executor) {
        this.userFactory = userFactory;
        this.localServer = localServer;
        this.sources = sources;
        this.executor = executor;
    }

    private class UserNotFound extends RuntimeException {}

    private User find(UserSearch search, Iterator<UserSource> sources) {
        if(sources.hasNext()) {
            return sources.next().search(search, () -> find(search, sources));
        }
        throw new UserNotFound();
    }

    private User find(Object query, boolean sync, Instant freshness) {
        try {
            return find(new UserSearch(query,
                                       user -> !(user.updatedAt().isPresent() &&
                                                 user.updatedAt().get().isBefore(freshness)),
                                       sync),
                        sources.iterator());
        } catch(UserNotFound e) {
            return query instanceof UUID ? userFactory.createUser((UUID) query)
                                         : userFactory.createUser(query.toString());
        }
    }

    private Player findOnline(Object query) {
        return query instanceof UUID ? localServer.getPlayer((UUID) query)
                                     : localServer.getPlayerExact(query.toString());
    }

    private User find(Object query, Instant freshness) {
        final boolean sync = localServer.isMainThread();
        if(sync) {
            final Player player = findOnline(query);
            if(player != null) return player;
        }
        return find(query, sync, freshness);
    }

    private ListenableFuture<User> findAsync(Object query, Instant freshness) {
        if(localServer.isMainThread()) {
            final Player player = findOnline(query);
            if(player != null) return Futures.immediateFuture(player);
        }
        return executor.submit(() -> find(query, false, freshness));
    }

    @Override
    public User findUser(UUID id, Instant freshness) {
        return find(id, freshness);
    }

    @Override
    public User findUser(String name, Instant freshness) {
        return find(name, freshness);
    }

    @Override
    public ListenableFuture<User> findUserAsync(UUID id, Instant freshness) {
        return findAsync(id, freshness);
    }

    @Override
    public ListenableFuture<User> findUserAsync(String name, Instant freshness) {
        return findAsync(name, freshness);
    }
}
