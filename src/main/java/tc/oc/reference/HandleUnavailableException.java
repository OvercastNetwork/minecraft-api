package tc.oc.reference;

public class HandleUnavailableException extends RuntimeException {

    public HandleUnavailableException() {
        this("Handle is not available");
    }

    public HandleUnavailableException(String message) {
        super(message);
    }
}
