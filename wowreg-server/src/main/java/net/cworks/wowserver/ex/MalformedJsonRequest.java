/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.ex
 * Class: MalformedJsonRequest
 * Created: 8/25/14 9:04 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.ex;

public class MalformedJsonRequest extends HttpServiceException {

    public MalformedJsonRequest(int httpCode, String message) {
        super(httpCode, message);
    }
}
