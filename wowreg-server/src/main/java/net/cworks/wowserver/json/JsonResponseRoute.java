/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.json
 * Class: JsonResponseRoute
 * Created: 8/15/14 11:44 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.json;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonElement;
import net.cworks.json.JsonObject;
import net.cworks.json.builder.JsonObjectBuilder;
import net.cworks.wowreg.ISODateParser;
import net.cworks.wowserver.ex.HttpServiceException;
import spark.Request;
import spark.Response;
import spark.ResponseTransformerRoute;
import sun.misc.BASE64Encoder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.SecureRandom;
import java.util.Date;

import static net.cworks.json.Json.Json;

public abstract class JsonResponseRoute extends ResponseTransformerRoute {

    /**
     * By default send stacktrace in error response
     */
    protected boolean sendTrace = true;

    /**
     * Create for the given path
     * @param path
     */
    protected JsonResponseRoute(String path) {
        super(path, "application/json");
    }

    /**
     * Create for the given path and acceptType
     * @param path
     * @param acceptType
     */
    protected JsonResponseRoute(String path, String acceptType) {
        super(path, acceptType);
    }

    @Override
    public String render(Object model) {
        if(model instanceof JsonElement) {
            return Json().toJson((JsonElement)model);
        }
        return Json().toJson(model);
    }

    public String render(JsonElement model) {
        return Json().toJson(model);
    }

    /**
     * Overridden to handle exceptions that may come from sub-classes
     * @param request
     * @param response
     * @return
     */
    public Object handle(Request request, Response response) {

        JsonElement responseData = null;
        try {
            responseData = handleRequest(request, response);
            responseBody(responseData);
        } catch(HttpServiceException ex) {
            if(sendTrace) {
                responseData = errorResponse(ex.httpCode(), ex);
            } else {
                responseData = errorResponse(ex.httpCode(), ex.getMessage());
            }
        } catch(Exception ex) {
            if(sendTrace) {
                responseData = errorResponse(ex);
            } else {
                responseData = errorResponse(ex.getMessage());
            }
        }

        return responseData;
    }

    /**
     * Sub-classes should implement
     * @param request
     * @param response
     * @return
     */
    public abstract JsonElement handleRequest(Request request, Response response);

    /**
     * Build a general response body
     * @param content
     * @return
     */
    protected JsonObject responseBody(JsonElement content) {
        JsonObjectBuilder builder = Json().object()
            .number("httpStatus", 200)
            .string("datetime", ISODateParser.toString(new Date()))
            .string("requestId", requestId());
        if(content.isObject()) {
            builder.object("response", (JsonObject)content);
        } else if(content.isArray()) {
            builder.array("response", (JsonArray)content);
        }

        return builder.build();
    }

    /**
     * generate a general error response
     * @param httpStatus
     * @param message
     * @return
     */
    protected JsonObject errorResponse(Integer httpStatus, String message) {
        JsonObject error = Json().object()
            .string("error", message)
            .number("httpStatus", httpStatus).build();
        return error;
    }

    /**
     * generate a general error response
     * @param httpStatus
     * @param ex
     * @return
     */
    private JsonElement errorResponse(int httpStatus, HttpServiceException ex) {
        JsonObject error = Json().object()
            .string("error", ex.getMessage())
            .string("stacktrace", stackString(ex))
            .number("httpStatus", httpStatus).build();
        return error;
    }

    /**
     * generate a general error response
     * @param message
     * @return
     */
    private JsonElement errorResponse(String message) {
        JsonObject error = Json().object()
            .string("error", message).build();
        return error;
    }

    /**
     * generate a general error response
     * @param ex
     * @return
     */
    private JsonElement errorResponse(Exception ex) {
        JsonObject error = Json().object()
            .string("error", ex.getMessage())
            .string("stacktrace", stackString(ex)).build();
        return error;
    }

    String stackString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        ex.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    String requestId() {
        // 32 byte random number.
        SecureRandom generator = new SecureRandom();
        byte randomBytes[] = new byte[32];
        generator.nextBytes(randomBytes);
        BASE64Encoder encoder = new BASE64Encoder();
        String sessionID = encoder.encode(randomBytes);
        return sessionID;
    }

}
