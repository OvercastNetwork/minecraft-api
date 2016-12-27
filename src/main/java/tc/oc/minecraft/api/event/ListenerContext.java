package tc.oc.minecraft.api.event;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import tc.oc.minecraft.api.scheduler.Scheduler;
import tc.oc.minecraft.api.scheduler.Task;
import tc.oc.minecraft.api.scheduler.Tickable;

/**
 * Handles the lifecycle of a set of bound {@link Listener}s
 *
 * Each plugin creates one of these internally when it is enabled,
 * not when it is instantiated. This allows the listeners to inject
 * the plugin without creating a circular dependency.
 */
@Singleton
public class ListenerContext {

    private final EventRegistry eventRegistry;
    private final Scheduler scheduler;
    private final Set<Listener> listeners;

    private List<Record> records = Collections.emptyList();

    @Inject public ListenerContext(EventRegistry eventRegistry, Scheduler scheduler, Set<Listener> listeners) {
        this.eventRegistry = eventRegistry;
        this.scheduler = scheduler;
        this.listeners = listeners;
    }

    public void enable() {
        records = listeners.stream()
                           .map(Record::new)
                           .collect(Collectors.toList());
    }

    public void disable() {
        records.forEach(Record::disable);
        records = Collections.emptyList();
    }

    private class Record {
        final Listener listener;
        boolean enabled;
        @Nullable Task task;

        Record(Listener listener) {
            this.listener = listener;
            enable();
        }

        void enable() {
            if(listener instanceof Activatable && !((Activatable) listener).isActive()) return;
            enabled = true;

            if(listener instanceof Enableable) {
                ((Enableable) listener).enable();
            }

            eventRegistry.registerListener(listener);

            if(listener instanceof Tickable) {
                task = scheduler.schedule((Tickable) listener);
            }
        }

        void disable() {
            if(!enabled) return;
            enabled = false;

            if(task != null) {
                task.cancel();
                task = null;
            }

            eventRegistry.unregisterListener(listener);

            if(listener instanceof Enableable) {
                ((Enableable) listener).disable();
            }
        }
    }
}
