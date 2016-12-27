package tc.oc.minecraft.api.scheduler;

/**
 * A handle returned from {@link Scheduler} methods that can be
 * used to cancel the task.
 *
 * TODO: {@link java.util.concurrent.ScheduledFuture} support
 */
public interface Task {
    void cancel();
}
