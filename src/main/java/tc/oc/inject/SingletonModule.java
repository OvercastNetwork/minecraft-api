package tc.oc.inject;

import com.google.inject.AbstractModule;

/**
 * A {@link com.google.inject.Module} that compares equal to any other
 * module of the exact same class. This ensures that only one instance
 * of the module is ever installed.
 */
public abstract class SingletonModule extends AbstractModule {
    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public final boolean equals(Object obj) {
        return obj != null && getClass().equals(obj.getClass());
    }
}
