package tc.oc.inject;

import javax.inject.Provider;

import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;

public interface Providers {

    /**
     * Return a {@link Provider} that always throws a {@link ProvisionException}
     * with the given message;
     */
    static <T> Provider<T> failing(String message) {
        return () -> {
            throw new ProvisionException(message);
        };
    }

    /**
     * Return a {@link Provider} that always fails, saying that the name is "unavailable".
     *
     * This is useful to catch premature access of injected fields, e.g.
     *
     * <code>
     *     @Inject Provider<Thing> thingProvider = Providers.unavailable(Thing.class);
     * </code>
     *
     * Injection will replace the failing provider with the working one.
     */
    static <T> Provider<T> unavailable(String name) {
        return failing(name + " is not available yet");
    }

    static <T> Provider<T> unavailable(TypeLiteral<T> type) {
        return unavailable(type.toString());
    }

    static <T> Provider<T> unavailable(Class<T> type) {
        return unavailable(type.getName());
    }
}
