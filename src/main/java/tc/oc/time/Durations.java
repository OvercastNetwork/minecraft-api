package tc.oc.time;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public final class Durations {
    private Durations() {}

    /**
     * Parse a {@link Duration} in the same way as {@link Duration#parse(CharSequence)},
     * except that the "PT" prefix is optional.
     */
    public static Duration parse(CharSequence text) throws DateTimeParseException {
        if(text.length() > 0) {
            switch(text.charAt(0)) {
                case 'p':
                case 'P':
                case '-':
                    return Duration.parse(text);
            }
        }
        return Duration.parse("PT" + text);
    }
}
