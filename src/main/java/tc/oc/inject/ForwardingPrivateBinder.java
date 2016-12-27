package tc.oc.inject;

import com.google.inject.Key;
import com.google.inject.PrivateBinder;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedElementBuilder;

public interface ForwardingPrivateBinder extends ForwardingBinder, PrivateBinder {

    PrivateBinder forwardedBinder();

    @Override
    default void expose(Key<?> key) {
        forwardedBinder().expose(key);
    }

    @Override
    default AnnotatedElementBuilder expose(Class<?> type) {
        return forwardedBinder().expose(type);
    }

    @Override
    default AnnotatedElementBuilder expose(TypeLiteral<?> type) {
        return forwardedBinder().expose(type);
    }

    @Override
    default PrivateBinder withSource(Object source) {
        return forwardedBinder().withSource(source);
    }

    @Override
    default PrivateBinder skipSources(Class... classesToSkip) {
        return forwardedBinder().skipSources(classesToSkip);
    }
}
