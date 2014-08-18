/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db
 * Class: DbConnectException
 * Created: 8/15/14 2:36 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

public class DbConnectException extends RuntimeException {

    public DbConnectException(String message) {
        super(message);
    }

    public DbConnectException(String message, Throwable cause) {
       super(message, cause);
    }

    public DbConnectException(Throwable cause) {
        super(cause);
    }

}
