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

public class AttendeesApi {

    public static void attendeesApi() {

        post(new JsonResponseRoute("/wow/attendees") {

            @Override
            public Object handle(Request request, Response response) {

                String body = request.body();
                JsonObject attendeeData = Json().toObject(body);
                attendeeData.getString("firstName");

                JsonObject responseData = Json().object()
                    .object("request", attendeeData)
                    .string("datetime", ISODateParser.toString(new Date()))
                    .build();

                return responseData;
            }
        });

        get(new JsonResponseRoute("/wow/attendees") {
            @Override
            public Object handle(Request request, Response response) {

                WowRegDb db = WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");
                JsonArray attendees = db.retrieveAttendees();

                return attendees;
            }
        });

    }

}
