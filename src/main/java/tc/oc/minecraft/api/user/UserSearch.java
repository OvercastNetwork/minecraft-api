package tc.oc.minecraft.api.user;

import java.util.Locale;
import java.util.UUID;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Used by {@link UserFinder} to pass search criteria to {@link UserSource}s
 */
public class UserSearch {

    private final Object query;
    private final Predicate<User> filter;
    private final boolean sync;

    UserSearch(Object query, Predicate<User> filter, boolean sync) {
        checkArgument(query instanceof UUID || query instanceof String);
        this.query = query instanceof UUID ? query : ((String) query).toLowerCase(Locale.ROOT);
        this.filter = filter;
        this.sync = sync;
    }

    /**
     * Either a {@link UUID} or a {@link String} username
     */
    public Object query() {
        return query;
    }

    /**
     * Search result must pass this test
     */
    public Predicate<User> filter() {
        return filter;
    }

    /**
     * Is the search synchronized with the main server thread?
     *
     * If so, it must not perform any blocking operations.
     */
    public boolean sync() {
        return sync;
    }
}
