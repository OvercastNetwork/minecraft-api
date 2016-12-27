package tc.oc.exception;

/**
 * An object that encapsulates the handling of exceptions.
 *
 * Think of it as a catch block wrapper.
 */
public interface ExceptionHandler {

    void handleException(Throwable exception, String message);

    default void handleException(Throwable exception) {
        handleException(exception, "Uncaught exception");
    }
}
