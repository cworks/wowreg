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
import net.cworks.json.JsonObject;
import net.cworks.wowreg.ISODateParser;
import net.cworks.wowreg.db.WowRegDb;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import java.util.Date;

import static net.cworks.json.Json.Json;
import static spark.Spark.get;
import static spark.Spark.post;

public class AttendeesApi extends CoreApi {


    public void start() {

        final WowRegDb db = WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");

        post(new JsonResponseRoute(apiRoot() + "/attendees") {

            @Override
            public Object handle(Request request, Response response) {

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

                JsonObject responseData = Json().object()
                    .number("httpStatus", 200)
                    .string("datetime", ISODateParser.toString(new Date()))
                    .object("response", Json().object().string("message",
                         attendees.size() + " attendees created.").build())
                    .build();

                return responseData;
            }
        });

        get(new JsonResponseRoute(apiRoot() + "/attendees") {
            @Override
            public Object handle(Request request, Response response) {

                WowRegDb db = WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");
                JsonArray attendees = db.retrieveAttendees();

                return attendees;
            }
        });

    }

}
