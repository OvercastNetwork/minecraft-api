package tc.oc.minecraft.api.event;

/**
 * If {@link #isActive()} returns false at startup,
 * this listener will not receive any events or callbacks.
 */
public interface Activatable extends Listener {
    default boolean isActive() {
        return true;
    };
}
