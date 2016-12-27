package tc.oc.minecraft.api.scheduler;

import java.time.Duration;

import tc.oc.minecraft.api.event.Listener;

/**
 * An object that is ticked at regular time intervals.
 */
public interface Tickable extends Listener {

    /**
     * If true, call {@link #tick()} on the main thread.
     * If false, call {@link #tick()} on a background thread.
     */
    default boolean isSynchronous() {
        return true;
    }

    /**
     * Time to wait before the first call to {@link #tick()}.
     *
     * Default is no delay.
     */
    default Duration initialDelay() {
        return Duration.ZERO;
    }

    /**
     * Time between calls to {@link #tick()}.
     *
     * The actual period may be longer than this, but it will never be shorter.
     * A period of zero (which is the default) will be rounded up to one tick
     * in a Bukkit environment, but is not allowed in Bungee.
     */
    default Duration tickPeriod() {
        return Duration.ZERO;
    }

    void tick();
}
