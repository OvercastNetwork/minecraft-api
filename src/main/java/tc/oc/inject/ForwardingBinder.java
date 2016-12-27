package tc.oc.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Module;
import com.google.inject.PrivateBinder;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.AnnotatedConstantBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.Dependency;
import com.google.inject.spi.Message;
import com.google.inject.spi.ModuleAnnotatedMethodScanner;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeConverter;
import com.google.inject.spi.TypeListener;
import org.aopalliance.intercept.MethodInterceptor;

public interface ForwardingBinder extends Binder {

    Binder forwardedBinder();
    
    @Override
    default void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
        forwardedBinder().bindInterceptor(classMatcher, methodMatcher, interceptors);
    }

    @Override
    default void bindScope(Class<? extends Annotation> annotationType, Scope scope) {
        forwardedBinder().bindScope(annotationType, scope);
    }

    @Override
    default <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return forwardedBinder().bind(key);
    }

    @Override
    default <T> AnnotatedBindingBuilder<T> bind(TypeLiteral<T> typeLiteral) {
        return forwardedBinder().bind(typeLiteral);
    }

    @Override
    default <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
        return forwardedBinder().bind(type);
    }

    @Override
    default AnnotatedConstantBindingBuilder bindConstant() {
        return forwardedBinder().bindConstant();
    }

    @Override
    default <T> void requestInjection(TypeLiteral<T> type, T instance) {
        forwardedBinder().requestInjection(type, instance);
    }

    @Override
    default void requestInjection(Object instance) {
        forwardedBinder().requestInjection(instance);
    }

    @Override
    default void requestStaticInjection(Class<?>... types) {
        forwardedBinder().requestStaticInjection(types);
    }

    @Override
    default void install(Module module) {
        forwardedBinder().install(module);
    }

    @Override
    default Stage currentStage() {
        return forwardedBinder().currentStage();
    }

    @Override
    default void addError(String message, Object... arguments) {
        forwardedBinder().addError(message, arguments);
    }

    @Override
    default void addError(Throwable t) {
        forwardedBinder().addError(t);
    }

    @Override
    default void addError(Message message) {
        forwardedBinder().addError(message);
    }

    @Override
    default <T> Provider<T> getProvider(Key<T> key) {
        return forwardedBinder().getProvider(key);
    }

    @Override
    default <T> Provider<T> getProvider(Dependency<T> dependency) {
        return forwardedBinder().getProvider(dependency);
    }

    @Override
    default <T> Provider<T> getProvider(Class<T> type) {
        return forwardedBinder().getProvider(type);
    }

    @Override
    default <T> MembersInjector<T> getMembersInjector(TypeLiteral<T> typeLiteral) {
        return forwardedBinder().getMembersInjector(typeLiteral);
    }

    @Override
    default <T> MembersInjector<T> getMembersInjector(Class<T> type) {
        return forwardedBinder().getMembersInjector(type);
    }

    @Override
    default void convertToTypes(Matcher<? super TypeLiteral<?>> typeMatcher, TypeConverter converter) {
        forwardedBinder().convertToTypes(typeMatcher, converter);
    }

    @Override
    default void bindListener(Matcher<? super TypeLiteral<?>> typeMatcher, TypeListener listener) {
        forwardedBinder().bindListener(typeMatcher, listener);
    }

    @Override
    default void bindListener(Matcher<? super Binding<?>> bindingMatcher, ProvisionListener... listeners) {
        forwardedBinder().bindListener(bindingMatcher, listeners);
    }

    @Override
    default Binder withSource(Object source) {
        return forwardedBinder().withSource(source);
    }

    @Override
    default Binder skipSources(Class... classesToSkip) {
        return forwardedBinder().skipSources(classesToSkip);
    }

    @Override
    default PrivateBinder newPrivateBinder() {
        return forwardedBinder().newPrivateBinder();
    }

    @Override
    default void requireExplicitBindings() {
        forwardedBinder().requireExplicitBindings();
    }

    @Override
    default void disableCircularProxies() {
        forwardedBinder().disableCircularProxies();
    }

    @Override
    default void requireAtInjectOnConstructors() {
        forwardedBinder().requireAtInjectOnConstructors();
    }

    @Override
    default void requireExactBindingAnnotations() {
        forwardedBinder().requireExactBindingAnnotations();
    }

    @Override
    default void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {
        forwardedBinder().scanModulesForAnnotatedMethods(scanner);
    }
}
