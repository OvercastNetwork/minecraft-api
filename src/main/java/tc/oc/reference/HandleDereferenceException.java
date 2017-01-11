package tc.oc.reference;

public class HandleDereferenceException extends RuntimeException {

    public HandleDereferenceException(Throwable cause) {
        super("Exception while dereferencing", cause);
    }

    public static HandleDereferenceException causedBy(Throwable cause) {
        return cause instanceof HandleDereferenceException ? (HandleDereferenceException) cause
                                                           : new HandleDereferenceException(cause);
    }
}
