package tc.oc.collection;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public interface Optionals {

    static <T> Optional<T> first(Stream<Optional<? extends T>> options) {
        return (Optional<T>) options.filter(Optional::isPresent)
                                    .findFirst()
                                    .orElse(Optional.empty());
    }

    static <T> Optional<T> first(Collection<Optional<? extends T>> options) {
        return first(options.stream());
    }

    static <T> Optional<T> first(Optional<? extends T>... options) {
        return first(Stream.of(options));
    }
}
