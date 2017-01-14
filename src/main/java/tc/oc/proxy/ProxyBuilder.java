package tc.oc.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class ProxyBuilder {

    private static final List<Method> IDENTITY_METHODS;

    static {
        try {
            IDENTITY_METHODS = Arrays.asList(
                Object.class.getDeclaredMethod("equals", Object.class),
                Object.class.getDeclaredMethod("hashCode")
            );

        } catch(NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }

    private ClassLoader loader = null;
    private final Map<Method, InvocationHandler> methodHandlers = new LinkedHashMap<>();
    private final Map<Class<?>, InvocationHandler> typeHandlers = new LinkedHashMap<>();

    public ProxyBuilder loadFrom(ClassLoader loader) {
        this.loader = loader;
        return this;
    }

    public HandlerBuilder delegate(Method method) {
        return new HandlerBuilder<>(to -> methodHandlers.put(method, to));
    }

    public HandlerBuilder delegate(Iterable<Method> methods) {
        return new HandlerBuilder<>(to -> {
            for(Method method : methods) {
                methodHandlers.put(method, to);
            }
        });
    }

    public <T> HandlerBuilder<T> delegate(Class<T> type) {
        return new HandlerBuilder<>(to -> typeHandlers.put(type, to));
    }

    public HandlerBuilder delegateIdentity() {
        return delegate(IDENTITY_METHODS);
    }

    private void validate() {
        for(Class<?> type : typeHandlers.keySet()) {
            if(!type.isInterface() && !type.equals(Object.class)) {
                throw new IllegalArgumentException("Cannot proxy non-interface type " + type.getName());
            }
        }
        for(Method method : methodHandlers.keySet()) {
            if(typeHandlers.keySet().stream().noneMatch(method.getDeclaringClass()::isAssignableFrom)) {
                throw new IllegalArgumentException("Method " + method.getName() + " is not present in any implemented interfaces");
            }
        }
    }

    public Object newProxyInstance() {
        validate();
        return Proxy.newProxyInstance(
            loader != null ? loader : Thread.currentThread().getContextClassLoader(),
            typeHandlers.keySet().toArray(new Class<?>[typeHandlers.size()]),
            new Invoker(methodHandlers, typeHandlers)
        );
    }

    public class HandlerBuilder<T> {
        private Consumer<InvocationHandler> consumer;

        private HandlerBuilder(Consumer<InvocationHandler> consumer) {
            this.consumer = consumer;
        }

        public ProxyBuilder to(InvocationHandler handler) {
            if(consumer == null) {
                throw new IllegalStateException();
            }
            consumer.accept(handler);
            consumer = null;
            return ProxyBuilder.this;
        }

        public ProxyBuilder toInstance(T to) {
            return to((proxy, m, args) -> m.invoke(to, args));
        }

        public ProxyBuilder toSupplier(Supplier<T> to) {
            return to((proxy, method, args) -> method.invoke(to.get(), args));
        }
    }

    private static class Invoker implements InvocationHandler {

        private final LoadingCache<Method, InvocationHandler> handlers;

        private Invoker(Map<Method, InvocationHandler> methods, Map<Class<?>, InvocationHandler> types) {
            this.handlers = CacheBuilder.newBuilder().build(new CacheLoader<Method, InvocationHandler>() {
                @Override
                public InvocationHandler load(Method method) throws Exception {
                    // Look for an exact method
                    InvocationHandler handler = methods.get(method);
                    if(handler != null) return handler;

                    // Look for the exact declaring interface of the method
                    // If Object is delegated, that will be caught here
                    final Class<?> decl = method.getDeclaringClass();
                    handler = types.get(decl);
                    if(handler != null) return handler;

                    // Any Object methods not handled above are delegated to the Invoker itself
                    if(decl.equals(Object.class)) {
                        return (proxy, method1, args) -> method1.invoke(Invoker.this, args);
                    }

                    // If the method is not from Object, look for an interface that inherits it
                    for(Map.Entry<Class<?>, InvocationHandler> entry : types.entrySet()) {
                        if(decl.isAssignableFrom(entry.getKey())) {
                            return entry.getValue();
                        }
                    }

                    // Give up ¯\_(ツ)_/¯
                    throw new NoSuchMethodError(method.getName());
                }
            });
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return handlers.get(method).invoke(proxy, method, args);
        }
    }
}
