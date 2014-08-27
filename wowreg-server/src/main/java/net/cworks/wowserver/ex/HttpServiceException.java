/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.ex
 * Class: HttpServiceException
 * Created: 8/25/14 9:05 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.ex;

public class HttpServiceException extends RuntimeException {

    int httpCode = 0;

    public HttpServiceException(int httpCode) {
        super();
        this.httpCode = httpCode;
    }

    public HttpServiceException(int httpCode, String message) {
        super(message);
        this.httpCode = httpCode;
    }

    public HttpServiceException(int httpCode, String message, Throwable cause) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public HttpServiceException(int httpCode, Throwable cause) {
        super(cause);
        this.httpCode = httpCode;
    }

    public int httpCode() {
        return httpCode;
    }

}
