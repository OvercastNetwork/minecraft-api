package tc.oc.reference;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import javax.inject.Provider;

import tc.oc.proxy.ProxyBuilder;
import tc.oc.util.Lazy;

/**
 * A lightweight object that can quickly retrieve some other object of type {@link T},
 * without necessarily holding a strong reference to it. This is similar to a {@link WeakReference},
 * but more flexible, because the mechanism for (de)referencing can be implemented
 * by the creator of the handle. Various useful bells and whistles are also provided.
 *
 * There are several ways to retrieve the referent object:
 *
 *   {@link #getIfPresent()} is self-explanatory
 *
 *   {@link #get()} returns the referent if available, otherwise it throws a
 *   {@link HandleUnavailableException}. Any exception thrown by the dereferencing code is wrapped
 *   in a {@link HandleDereferenceException}, so that it can be reliably distinguished.
 *
 *   {@link #proxy()} tries to return a proxy for the actual referent that routes all
 *   method calls through the handle. This only works if the handle was constructed with
 *   an explicit interface type. Otherwise, {@link #proxy()} just calls {@link #get()}.
 */
public abstract class Handle<T> implements Supplier<T>, Provider<T> {

    private final @Nullable Class<T> type;
    private final @Nullable Object key;
    private final Lazy<T> proxy;

    private Handle(@Nullable Class<T> type, @Nullable Object key) {
        if(type != null && !type.isInterface()) {
            throw new IllegalArgumentException("Handle type " + type.getName() + " is not an interface");
        }

        this.type = type;
        this.key = key;

        proxy = Lazy.from(() -> (T) new ProxyBuilder()
            .loadFrom(type.getClassLoader())
            .delegateIdentity().toSupplier(this::key)
            .delegate(Handleable.class).toInstance(new Handleable() {
                @Override public Handle<?> handle() { return Handle.this; }
            })
            .delegate(type).toSupplier(this)
            .newProxyInstance()
        );
    }

    public boolean hasKey() {
        return key != null;
    }

    public Object key() throws HandleUnavailableException {
        return key != null ? key : get();
    }

    public boolean hasProxy() {
        return type != null;
    }

    public T proxy() {
        return type != null ? proxy.get() : get();
    }

    @Override
    public int hashCode() {
        return key().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || (
            obj instanceof Handle &&
            this.key().equals(((Handle) obj).key())
        );
    }

    public boolean isPresent() {
        return getIfPresent().isPresent();
    }

    public abstract Optional<T> getIfPresent();

    @Override
    public T get() throws HandleUnavailableException, HandleDereferenceException {
        final Optional<T> opt;
        try { opt = getIfPresent(); }
        catch(Throwable ex) { throw HandleDereferenceException.causedBy(ex); }
        return opt.orElseThrow(HandleUnavailableException::new);
    }

    public Handle<T> ifPresent(Consumer<? super T> consumer) {
        if(isPresent()) {
            consumer.accept(get());
        }
        return this;
    }

    public Handle<T> ifAbsent(Runnable runnable) {
        if(!isPresent()) {
            runnable.run();
        }
        return this;
    }

    public T orElse(T t) {
        return isPresent() ? get() : t;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return isPresent() ? get() : supplier.get();
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if(isPresent()) return get();
        throw exceptionSupplier.get();
    }

    public Handle<T> filter(Predicate<? super T> predicate) {
        return new Handle<T>(type, key) {
            @Override
            public Optional<T> getIfPresent() {
                return Handle.this.getIfPresent().filter(predicate);
            }
        };
    }

    public <U> Handle<U> map(Function<? super T, ? extends U> mapper) {
        return map(null, mapper);
    }

    public <U> Handle<U> map(@Nullable Object key, Function<? super T, ? extends U> mapper) {
        return map(null, key, mapper);
    }

    public <U> Handle<U> map(@Nullable Class<U> type, @Nullable Object key, Function<? super T, ? extends U> mapper) {
        return new Handle<U>(type, key) {
            @Override
            public Optional<U> getIfPresent() {
                return Handle.this.getIfPresent().flatMap(u -> {
                    try {
                        return Optional.of(mapper.apply(u));
                    } catch(HandleUnavailableException ex) {
                        return Optional.empty();
                    }
                });
            }

            @Override
            public U get() throws HandleUnavailableException {
                return mapper.apply(Handle.this.get());
            }
        };
    }

    public <U> Handle<U> flatMap(Function<? super T, Optional<U>> mapper) {
        return flatMap(null, mapper);
    }

    public <U> Handle<U> flatMap(@Nullable Object key, Function<? super T, Optional<U>> mapper) {
        return flatMap(null, key, mapper);
    }

    public <U> Handle<U> flatMap(@Nullable Class<U> type, @Nullable Object key, Function<? super T, Optional<U>> mapper) {
        return new Handle<U>(type, key) {
            @Override
            public Optional<U> getIfPresent() {
                return Handle.this.getIfPresent().flatMap(mapper);
            }
        };
    }

    public static <T> Handle<T> empty() {
        return empty(null);
    }

    public static <T> Handle<T> empty(@Nullable Object key) {
        return empty(null, key);
    }

    public static <T> Handle<T> empty(@Nullable Class<T> type, @Nullable Object key) {
        return new Handle<T>(type, key) {
            @Override
            public boolean isPresent() {
                return false;
            }

            @Override
            public Optional getIfPresent() {
                return Optional.empty();
            }

            @Override
            public T get() {
                throw new HandleUnavailableException();
            }
        };
    }

    public static <T> Handle<T> ofInstance(T instance) {
        return ofInstance(null, instance);
    }

    public static <T> Handle<T> ofInstance(@Nullable Class<T> type, T instance) {
        final Optional<T> opt = Optional.of(instance);
        return new Handle<T>(type, instance) {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public Optional<T> getIfPresent() {
                return opt;
            }

            @Override
            public T get() {
                return instance;
            }
        };
    }

    public static <T> Handle<T> ofWeakInstance(T instance) {
        return ofWeakInstance(null, instance);
    }

    public static <T> Handle<T> ofWeakInstance(@Nullable Object key, T instance) {
        return ofWeakInstance(null, key, instance);
    }

    public static <T> Handle<T> ofWeakInstance(@Nullable Class<T> type, @Nullable Object key, T instance) {
        return ofReference(type, key, new WeakReference<>(instance));
    }

    public static <T> Handle<T> ofReference(Reference<T> reference) {
        return ofReference(null, reference);
    }

    public static <T> Handle<T> ofReference(@Nullable Object key, Reference<T> reference) {
        return ofReference(null, key, reference);
    }

    public static <T> Handle<T> ofReference(@Nullable Class<T> type, @Nullable Object key, Reference<T> reference) {
        return ofNullableSupplier(type, key, reference::get);
    }

    public static <T> Handle<T> ofSupplier(Supplier<T> supplier) {
        return ofSupplier(null, supplier);
    }

    public static <T> Handle<T> ofSupplier(@Nullable Object key, Supplier<T> supplier) {
        return ofSupplier(null, key, supplier);
    }

    public static <T> Handle<T> ofSupplier(@Nullable Class<T> type, @Nullable Object key, Supplier<T> supplier) {
        return new Handle<T>(type, key) {
            @Override
            public Optional<T> getIfPresent() {
                try {
                    return Optional.of(get());
                } catch(HandleUnavailableException ex) {
                    return Optional.empty();
                }
            }

            @Override
            public T get() throws HandleUnavailableException {
                return supplier.get();
            }
        };
    }

    public static <T> Handle<T> ofOptionalSupplier(Supplier<Optional<T>> supplier) {
        return ofOptionalSupplier(null, supplier);
    }

    public static <T> Handle<T> ofOptionalSupplier(@Nullable Object key, Supplier<Optional<T>> supplier) {
        return ofOptionalSupplier(null, key, supplier);
    }

    public static <T> Handle<T> ofOptionalSupplier(@Nullable Class<T> type, @Nullable Object key, Supplier<Optional<T>> supplier) {
        return new Handle<T>(type, key) {
            @Override
            public Optional<T> getIfPresent() {
                return supplier.get();
            }
        };
    }

    public static <T> Handle<T> ofNullableSupplier(Supplier<T> supplier) {
        return ofNullableSupplier(null, supplier);
    }

    public static <T> Handle<T> ofNullableSupplier(@Nullable Object key, Supplier<T> supplier) {
        return ofNullableSupplier(null, key, supplier);
    }

    public static <T> Handle<T> ofNullableSupplier(@Nullable Class<T> type, @Nullable Object key, Supplier<T> supplier) {
        return new Handle<T>(type, key) {
            @Override
            public boolean isPresent() {
                return null != supplier.get();
            }

            @Override
            public Optional<T> getIfPresent() {
                return Optional.ofNullable(supplier.get());
            }

            @Override
            public T get() {
                final T instance;
                try { instance = supplier.get(); }
                catch(Throwable ex) { throw HandleDereferenceException.causedBy(ex); }
                if(instance == null) throw new HandleUnavailableException();
                return instance;
            }
        };
    }
}

