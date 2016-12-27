package tc.oc.inject;

import java.util.Objects;
import javax.annotation.Nullable;

import com.google.inject.AbstractModule;

/**
 * A {@link com.google.inject.Module} that implements equality testing
 * by comparing the module class, and an explicit key object. The key
 * can be passed to the constructor, or returned from an overridden
 * {@link #moduleKey()} method.
 */
public abstract class KeyedModule extends AbstractModule {

    private final @Nullable Object key;

    /**
     * If this constructor is used, then {@link #moduleKey()} must be overridden
     */
    protected KeyedModule() {
        this(null);
    }

    protected KeyedModule(Object key) {
        this.key = Objects.requireNonNull(key);
    }

    protected Object moduleKey() {
        return Objects.requireNonNull(key);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getClass(), moduleKey());
    }

    @Override
    public final boolean equals(Object obj) {
        return obj != null &&
               getClass().equals(obj.getClass()) &&
               moduleKey().equals(((KeyedModule) obj).moduleKey());
    }
}
