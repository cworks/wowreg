/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db
 * Class: DbCreateException
 * Created: 8/15/14 3:47 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

public class DbCreateException extends RuntimeException {

    public DbCreateException(String message) {
        super(message);
    }

    public DbCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbCreateException(Throwable cause) {
        super(cause);
    }
}
