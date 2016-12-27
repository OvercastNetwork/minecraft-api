package tc.oc.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoggingExceptionHandler implements ExceptionHandler {

    private final Logger logger;

    public LoggingExceptionHandler(Logger logger) {
        this.logger = checkNotNull(logger);
    }

    @Override
    public void handleException(Throwable exception, String message) {
        logger.log(Level.SEVERE, message, exception);
    }

    public static LoggingExceptionHandler forGlobalLogger() {
        if(GLOBAL == null) {
            GLOBAL = new LoggingExceptionHandler(Logger.getGlobal());
        }
        return GLOBAL;
    }

    private static LoggingExceptionHandler GLOBAL;
}
