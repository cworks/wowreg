/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver
 * Class: ErrorCodes
 * Created: 11/19/14 12:52 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

public interface ErrorCodes {

    public static final Integer DEFAULT = 1000;

    public static final Integer WOWSERVER_ERROR = DEFAULT + 1;

    public static final Integer ALREADY_PAID_ERROR = DEFAULT + 2;

    public static final Integer REQUIRED_ARGUMENTS_ERROR = DEFAULT + 3;

    public static final Integer REGISTRATION_CANCELLED = DEFAULT + 4;
}
