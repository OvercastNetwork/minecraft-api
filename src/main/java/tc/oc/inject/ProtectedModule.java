package tc.oc.inject;

import javax.annotation.Nullable;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * A {@link Module} that uses a {@link ProtectedBinder}.
 *
 * This module must be installed through a {@link ProtectedBinder}, which must be created explicitly
 * by calling {@link ProtectedBinder#newProtectedBinder(Binder)}. Attempting to install this module
 * into a normal {@link Binder} fails with an error.
 *
 * Unlike {@link com.google.inject.PrivateModule}, installing this never creates a new {@link ProtectedBinder}
 * automatically. This allows an entire tree of {@link ProtectedModule}s to share the same public binder.
 *
 * @see ProtectedBinder
 */
public abstract class ProtectedModule implements Module, ForwardingProtectedBinder {

    protected void configure() {}

    private final @Nullable Object moduleKey;
    private @Nullable ProtectedBinder binder;

    protected ProtectedModule(@Nullable Object moduleKey) {
        this.moduleKey = moduleKey;
    }

    protected ProtectedModule() {
        this(null);
    }

    @Override
    public int hashCode() {
        return moduleKey != null ? moduleKey.hashCode()
                                 : super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(moduleKey != null) {
            return obj != null &&
                   getClass().equals(obj.getClass()) &&
                   moduleKey.equals(((ProtectedModule) obj).moduleKey);
        } else {
            return super.equals(obj);
        }
    }

    @Override
    public final ProtectedBinder forwardedBinder() {
        return binder();
    }

    protected final ProtectedBinder binder() {
        if(binder == null) {
            throw new IllegalStateException("Binder is only usable during configuration");
        }
        return binder;
    }

    @Override
    public final void configure(Binder binder) {
        final ProtectedBinder old = this.binder;
        this.binder = ProtectedBinderImpl.current(binder);
        try {
            if(this.binder != null) {
                configure();
            } else {
                binder.skipSources(ProtectedModule.class).addError(
                    ProtectedModule.class.getSimpleName() +
                    " must be installed with a " +
                    ProtectedBinder.class.getSimpleName()
                );
            }
        } finally {
            this.binder = old;
        }
    }
}
