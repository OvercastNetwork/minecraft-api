package tc.oc.minecraft.api.event;

public interface EventRegistry {
    /**
     * Register all event handler methods found in the given {@link Listener} to receive events.
     */
    void registerListener(Listener listener);

    /**
     * Unregister all event handlers created from this {@link EventRegistry}
     * that belong to the given {@link Listener}.
     */
    void unregisterListener(Listener listener);

    /**
     * Unregister all event handlers created from this {@link EventRegistry}.
     */
    void unregisterAll();
}
