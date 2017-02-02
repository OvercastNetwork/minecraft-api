package tc.oc.minecraft.api.scheduler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;

import com.google.inject.BindingAnnotation;

/**
 * Binding annotation indicating something that is
 * NOT synchronized with the main server thread.
 */
@Qualifier
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface Async {}
