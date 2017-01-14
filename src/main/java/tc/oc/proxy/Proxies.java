package tc.oc.proxy;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.function.Supplier;

public class Proxies {

    public static <T> T forwarding(Class<T> type, Supplier<? extends T> supplier) {
        return forwarding(type.getClassLoader(), type, supplier);
    }

    public static <T> T forwarding(ClassLoader loader, Class<T> type, Supplier<? extends T> supplier) {
        return (T) Proxy.newProxyInstance(
            loader,
            new Class<?>[]{type},
            (proxy, method, args) -> method.invoke(supplier.get(), args)
        );
    }

    public static <T> T forwarding(ClassLoader loader, Class<T> type, Supplier<? extends T> supplier, Map<Class<?>, ?> extensions) {
        final Class[] inters = new Class[extensions.size() + 1];
        int i = 0;
        for(Class inter : extensions.keySet()) {
            inters[i++] = inter;
        }
        inters[i] = type;

        return (T) Proxy.newProxyInstance(
            loader,
            inters,
            (proxy, method, args) -> {
                for(Class<?> inter : extensions.keySet()) {
                    if(method.getDeclaringClass().isAssignableFrom(inter)) {
                        return method.invoke(extensions.get(inter), args);
                    }
                }
                return method.invoke(supplier.get(), args);
            }
        );
    }
}
