/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db
 * Class: AttendeeExistsException
 * Created: 8/23/14 3:29 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

public class AttendeeExistsException extends RuntimeException {

    public AttendeeExistsException() {
        super();
    }

    public AttendeeExistsException(String message) {
        super(message);
    }

    public AttendeeExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttendeeExistsException(Throwable cause) {
        super(cause);
    }

}
