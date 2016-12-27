package tc.oc.minecraft.api.event;

import com.google.inject.Binder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.multibindings.Multibinder;

/**
 * Registers {@link Listener}s to receive events and callbacks whenever their owning plugin is enabled.
 *
 * This must be used in a plugin's private environment.
 *
 * @see Listener
 * @see Activatable
 * @see Enableable
 * @see tc.oc.minecraft.api.scheduler.Tickable
 */
public class ListenerBinder {

    private final Multibinder<Listener> listeners;

    public ListenerBinder(Binder binder) {
        this.listeners = Multibinder.newSetBinder(binder, Listener.class);
    }

    public LinkedBindingBuilder<Listener> bindListener() {
        return listeners.addBinding();
    }
}
