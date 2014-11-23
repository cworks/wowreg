/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver
 * Class: RegistrationApi
 * Created: 8/18/14 10:43 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.Log;
import net.cworks.json.JsonArray;
import net.cworks.json.JsonElement;
import net.cworks.json.JsonObject;
import net.cworks.wowconf.Registrar;
import net.cworks.wowserver.ex.MalformedJsonRequest;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static spark.Spark.post;

public class RegistrationApi extends CoreApi {

    private static final int ATTENDEE_GROUP_LIMIT = 6;

    @Override
    public void start() {

        post(new JsonResponseRoute(apiRoot() + "/register") {

            @Override
            public JsonElement handleRequest(Request request, Response response) {

                String body = request.body();
                int n = 0;
                JsonObject registrationInfo = null;
                if(body.startsWith("{")) {
                    JsonArray attendees = Json().toObject(body).getArray("attendees");
                    if(attendees.size() > ATTENDEE_GROUP_LIMIT) {
                        return errorResponse(400, "attendee group cannot exceed "
                            + ATTENDEE_GROUP_LIMIT
                            + " attendees and this group has "
                            + attendees.size() + " attendees.");
                    }
                    // add the eventId for the event to each attendee
                    Iterator it = attendees.iterator();
                    JsonObject event = Json().object().number("eventId", 1).build();
                    while(it.hasNext()) {
                        JsonObject attendee = (JsonObject)it.next();
                        attendee.merge(event);
                    }
                    // register several attendees
                    registrationInfo = Registrar.instance().register(attendees);
                } else {
                    throw new MalformedJsonRequest(400,
                        "Request body is malformed JSON string " + n + " attendees created.");
                }

                return this.responseBody(registrationInfo);
            }
        });


        /**
         * Example request, we need the token to perform the cancellation.
         * https://www.wowconf.org/cancelled.html?
         * csrf=Xk7f%2Fzg5uLV76yne43YJh3YsQhQMQGcXvxBsoll%2B0UA%3D&
         * token=EC-4E248859BT1001115
         */
        post(new JsonResponseRoute(apiRoot() + "/cancel") {

            @Override
            public JsonElement handleRequest(Request request, Response response) {

                String body = request.body();
                if(body == null || body.trim().length() == 0) {
                    // return generic cancel response
                    return errorResponse(400, ErrorCodes.REGISTRATION_CANCELLED,
                        "Awe bummer your registration was cancelled.");
                }

                JsonObject data = Json().toObject(body);
                JsonObject cancelInfo = Json().object()
                        .string("csrf", data.getString("csrf"))
                        .string("token", data.getString("token")).build();

                // make sure required parameters are given in request
                if(cancelInfo.getString("csrf") == null ||
                   cancelInfo.getString("token") == null) {

                    Log.log.error(String.format(
                        "errorCode: %d /cancel request did not have required parameters: " +
                            "csrf and token", ErrorCodes.REQUIRED_ARGUMENTS_ERROR));

                    return errorResponse(400, ErrorCodes.REGISTRATION_CANCELLED,
                        "Awe bummer your registration was cancelled.");
                }

                try {
                    JsonArray cancelled = Registrar.instance().cancel(
                        cancelInfo.getString("token"));

                    // return the array of cancelled attendees
                    return errorResponse(400, ErrorCodes.REGISTRATION_CANCELLED, cancelled);
                } catch(Exception ex) {
                    return errorResponse(400, ErrorCodes.REGISTRATION_CANCELLED,
                        "Awe bummer your registration was cancelled.");
                }

            }
        });
    }
}
