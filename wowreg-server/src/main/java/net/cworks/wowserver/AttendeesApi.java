/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Baked with love by comartin
 * Package: net.cworks.wowserver
 * User: comartin
 * Created: 8/13/2014 2:24 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonElement;
import net.cworks.json.JsonObject;
import net.cworks.wowreg.db.WowRegDb;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import static net.cworks.json.Json.Json;
import static spark.Spark.get;
import static spark.Spark.post;

public class AttendeesApi extends CoreApi {

    public void start() {

        final WowRegDb db = WowRegDb.db();

        post(new JsonResponseRoute(apiRoot() + "/attendees") {

            @Override
            public JsonElement handleRequest(Request request, Response response) {

                String body = request.body();
                int n = 0;
                JsonArray attendees = Json().array().build();
                if(body.startsWith("{")) {
                    JsonObject attendee = db.createAttendee(Json().toObject(body));
                    attendees.add(attendee);
                } else if(body.startsWith("[")) {
                    attendees = db.createAttendees(Json().toArray(body));
                } else {
                    return errorResponse(400,
                        "Request body is malformed JSON string "
                            + attendees.size() + " attendees created.");
                }

                JsonObject responseData = responseBody(
                        Json().object().string("message", n + " attendees created.").build());

                return responseData;
            }
        });

        get(new JsonResponseRoute(apiRoot() + "/attendees") {
            @Override
            public JsonElement handleRequest(Request request, Response response) {

                WowRegDb db = WowRegDb.db();
                JsonArray attendees = db.retrieveAttendees();

                return attendees;
            }
        });

        get(new JsonResponseRoute(apiRoot() + "/attendees/:attendeeId") {
            @Override
            public JsonElement handleRequest(Request request, Response response) {



                return null;
            }
        });

        get(new JsonResponseRoute(apiRoot() + "/attendees/:eventId/:groupId") {
            @Override
            public JsonElement handleRequest(Request request, Response response) {
                return null;
            }
        });



    }

}
