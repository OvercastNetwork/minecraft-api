package tc.oc.inject;

import com.google.inject.Binder;

public interface ForwardingProtectedBinder extends ProtectedBinder, ForwardingPrivateBinder {

    @Override
    ProtectedBinder forwardedBinder();

    @Override
    default Binder publicBinder() {
        return forwardedBinder().publicBinder();
    }

    @Override
    default ProtectedBinder withSource(Object source) {
        return forwardedBinder().withSource(source);
    }

    @Override
    default ProtectedBinder skipSources(Class... classesToSkip) {
        return forwardedBinder().skipSources(classesToSkip);
    }
}
