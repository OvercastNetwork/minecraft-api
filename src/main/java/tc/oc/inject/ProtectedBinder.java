package tc.oc.inject;

import com.google.inject.Binder;

/**
 * A {@link com.google.inject.PrivateBinder} that provides direct access to the enclosing
 * environment through {@link #publicBinder()}. This is the same environment that you create
 * bindings in by calling {@link #expose}, but access to the binder itself offers more flexibility.
 *
 * {@link ProtectedBinder}s are created by calling {@link #newProtectedBinder(Binder)}.
 *
 * @see ProtectedModule
 */
public interface ProtectedBinder extends ForwardingPrivateBinder {

    Binder publicBinder();

    default ProtectedBinder withSource(Object source) {
        return new ProtectedBinderImpl(publicBinder().withSource(source),
                                       forwardedBinder().withSource(source));
    }

    default ProtectedBinder skipSources(Class... classesToSkip) {
        return new ProtectedBinderImpl(publicBinder().skipSources(classesToSkip),
                                       forwardedBinder().skipSources(classesToSkip));
    }

    static ProtectedBinder newProtectedBinder(Binder parent) {
        return new ProtectedBinderImpl(parent, parent.newPrivateBinder());
    }
}
