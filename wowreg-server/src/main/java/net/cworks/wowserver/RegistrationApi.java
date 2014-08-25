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

import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import net.cworks.wowconf.Registrar;
import net.cworks.wowreg.ISODateParser;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import java.util.Date;
import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static spark.Spark.post;

public class RegistrationApi extends CoreApi {

    private static final int ATTENDEE_GROUP_LIMIT = 6;

    @Override
    public void start() {

        post(new JsonResponseRoute(apiRoot() + "/register") {

            @Override
            public Object handle(Request request, Response response) {

                String body = request.body();
                int n = 0;
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
                    n = Registrar.instance().register(attendees);
                } else {
                    return errorResponse(400,
                        "Request body is malformed JSON string " + n + " attendees created.");
                }

                JsonObject responseData = Json().object()
                    .number("httpStatus", 200)
                    .string("datetime", ISODateParser.toString(new Date()))
                    .object("response", Json().object().string("message", n + " attendees created.")
                            .build())
                    .build();

                return responseData;
            }
        });
    }
}
