package tc.oc.inject;

import javax.annotation.Nullable;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;

class ProtectedBinderImpl implements ProtectedBinder {

    private static Class[] SKIP = new Class[]{
        ForwardingBinder.class,
        ForwardingPrivateBinder.class,
        ForwardingProtectedBinder.class,
        ProtectedBinder.class,
        ProtectedBinderImpl.class,
        ProtectedModule.class
    };

    private final Binder global;
    private final PrivateBinder local;

    ProtectedBinderImpl(Binder global, PrivateBinder local) {
        this.global = global.skipSources(SKIP);
        this.local = (local instanceof ProtectedBinder ? ((ProtectedBinder) local).forwardedBinder() : local).skipSources(SKIP);
    }

    @Override
    public PrivateBinder forwardedBinder() {
        return local;
    }

    @Override
    public Binder publicBinder() {
        return global;
    }

    /**
     * This hack is used to pass the current {@link ProtectedBinder} through Guice
     * when installing a {@link ProtectedModule}. Guice will pass its own {@link Binder}
     * to {@link ProtectedModule#configure(Binder)}, and we use this thread-local
     * to get the {@link ProtectedBinder} wrapping it.
     *
     * We could try and implement the install process ourselves, but then we would miss out
     * on Guice's module functionality, such as deduplication, error tracing, and so on.
     */

    private static final ThreadLocal<ProtectedBinderImpl> CURRENT = new ThreadLocal<>();

    @Override
    public void install(Module module) {
        final ProtectedBinderImpl prev = CURRENT.get();
        CURRENT.set(this);
        try {
            local.install(module);
        } finally {
            CURRENT.set(prev);
        }
    }

    static @Nullable ProtectedBinder current(Binder binder) {
        if(binder instanceof ProtectedBinder) {
            return (ProtectedBinder) binder;
        }

        final ProtectedBinderImpl current = ProtectedBinderImpl.CURRENT.get();
        if(current != null && current.local == binder) {
            return current;
        }

        return null;
    }
}
