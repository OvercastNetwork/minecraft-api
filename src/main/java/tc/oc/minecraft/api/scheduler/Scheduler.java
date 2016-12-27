package tc.oc.minecraft.api.scheduler;

import java.time.Duration;
import javax.annotation.Nullable;

/**
 * A service for scheduling delayed/periodic tasks, optionally in sync with the server's main thread.
 *
 * Each plugin has its own scheduler that registers tasks on its behalf.
 */
public interface Scheduler {

    Task schedule(boolean sync, @Nullable Duration delay, @Nullable Duration period, Runnable task);

    default Task schedule(Tickable tickable) {
        return schedule(tickable.isSynchronous(), tickable.initialDelay(), tickable.tickPeriod(), tickable::tick);
    }

    default Task delay(boolean sync, @Nullable Duration delay, Runnable task) {
        return schedule(sync, delay, null, task);
    }

    default Task repeat(boolean sync, @Nullable Duration period, Runnable task) {
        return schedule(sync, null, period, task);
    }

    default Task run(boolean sync, Runnable task) {
        return schedule(sync, null, null, task);
    }

    default Task scheduleSync(@Nullable Duration delay, @Nullable Duration period, Runnable task) {
        return schedule(true, delay, period, task);
    }

    default Task scheduleAsync(@Nullable Duration delay, @Nullable Duration period, Runnable task) {
        return schedule(false, delay, period, task);
    }

    default Task delaySync(@Nullable Duration delay, Runnable task) {
        return delay(true, delay, task);
    }

    default Task delayAsync(@Nullable Duration delay, Runnable task) {
        return delay(false, delay, task);
    }

    default Task repeatSync(@Nullable Duration period, Runnable task) {
        return repeat(true, period, task);
    }

    default Task repeatAsync(@Nullable Duration period, Runnable task) {
        return repeat(false, period, task);
    }

    default Task runSync(Runnable task) {
        return run(true, task);
    }

    default Task runAsync(Runnable task) {
        return run(false, task);
    }
}
