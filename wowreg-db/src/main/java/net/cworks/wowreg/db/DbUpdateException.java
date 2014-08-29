/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg
 * Class: DbUpdateException
 * Created: 8/29/14 9:36 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

public class DbUpdateException extends RuntimeException {

    public DbUpdateException(String message) {
        super(message);
    }

    public DbUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbUpdateException(Throwable cause) {
        super(cause);
    }
}
