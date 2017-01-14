package tc.oc.util;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;

public class Lazy<T> implements Supplier<T>, Provider<T> {

    public static <T> Lazy<T> from(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    private @Nullable Supplier<T> supplier;
    private @Nullable T instance;

    @Inject Lazy(Provider<T> provider) {
        this.supplier = provider::get;
    }

    Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if(instance != null) return instance;
        synchronized(this) {
            if(instance != null) return instance;
            if(supplier == null) {
                throw new IllegalStateException("Circular instantiation of lazy object");
            }
            final Supplier<T> temp = supplier;
            supplier = null;
            instance = temp.get();
            if(instance == null) {
                throw new NullPointerException("Lazy object cannot have a null value");
            }
            return instance;
        }
    }
}

