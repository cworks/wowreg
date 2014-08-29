/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver
 * Class: EventsApi
 * Created: 8/25/14 1:23 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.json.JsonElement;
import net.cworks.json.JsonObject;
import net.cworks.wowconf.EventCoordinator;
import net.cworks.wowserver.ex.HttpServiceException;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import static spark.Spark.get;

public class EventsApi extends CoreApi {

    @Override
    public void start() {

        /**
         * Responds with attendee costs for an event.  Response schema looks like:
         *
         * {
         *    "httpStatus":200,
         *    "datetime":"2014-08-26T14:41:06+00:00",
         *    "response":{
         *        "id":272,
         *        "firstName":"Ronald",
         *        "lastName":"McDonald_13",
         *        "email":"ronald@mcdonalds_13.com",
         *        "items":[{
         *            "item":"XL Shirt","category":
         *            "merchandise",
         *            "price":1200,
         *            "desc":"Xtra Large Wow Conference 2015 shirt"}, {
         *            "item":"$10 donation",
         *            "category":"donation",
         *            "price":1000,
         *            "desc":"$10 donation"}]}}
         */
        get(new JsonResponseRoute(apiRoot() + "/events/:eventName/attendee/:attendeeId") {
            @Override
            public JsonElement handleRequest(Request request, Response response) {

                String eventName = request.params("eventName");
                String attendeeId = request.params("attendeeId");
                if(eventName == null || attendeeId == null) {
                    throw new HttpServiceException(400,
                        "eventName and attendeeId are required for this API");
                }

                JsonObject pendingAttendeeInfo = EventCoordinator.instance()
                    .pendingAttendee(eventName, attendeeId);
                if(pendingAttendeeInfo == null) {
                    return errorResponse(500, "Could not get attendee: " + attendeeId
                        + " for event: " + eventName);
                }
                return responseBody(pendingAttendeeInfo);
            }
        });

    }
}
