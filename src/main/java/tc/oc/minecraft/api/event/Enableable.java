package tc.oc.minecraft.api.event;

/**
 * A listener that receives lifecycle callbacks.
 */
public interface Enableable extends Listener {

    /**
     * Called when this listener starts listening
     */
    default void enable() {}

    /**
     * Called when this listener stops listening
     */
    default void disable() {}
}
