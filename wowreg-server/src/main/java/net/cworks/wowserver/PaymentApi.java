/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver
 * Class: PaymentApi
 * Created: 10/6/14 10:17 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.Log;
import net.cworks.json.JsonArray;
import net.cworks.json.JsonElement;
import net.cworks.json.JsonObject;
import net.cworks.wowconf.EventCoordinator;
import net.cworks.wowconf.Registrar;
import net.cworks.wowpay.PayPal;
import net.cworks.wowpay.ex.AlreadyPaidException;
import net.cworks.wowserver.ex.MalformedJsonRequest;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static spark.Spark.post;

public class PaymentApi extends CoreApi {

    private static final int ATTENDEE_GROUP_LIMIT = 6;

    @Override
    public void start() {

        /**
         * Create a payment resource which represents an attempt at
         * paying for Wow Conf via paypal.
         */
        post(new JsonResponseRoute(apiRoot() + "/pay") {

            @Override
            public JsonElement handleRequest(Request request, Response response) {

                // *** Handle inserting attendee info ***
                String body = request.body();
                JsonObject result = null;
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
                    JsonObject registrationInfo = Registrar.instance().register(attendees);
                    // create payment for group
                    if(registrationInfo != null) {
                        registrationInfo.setNumber("groupTotalCost",
                            Json().toObject(body).getInteger("groupTotalCost"));
                        result = PayPal.create().createPayment(registrationInfo);
                    }
                } else {
                    throw new MalformedJsonRequest(400,
                        "Request body is malformed JSON string " + n + " attendees created.");
                }

                return responseBody(result);
            }
        });

        /**
         * Handle authorizing and executing a payment from paypal.
         *
         */
        post(new JsonResponseRoute(apiRoot() + "/paypal") {

            @Override
            public JsonElement handleRequest(Request request, Response response) {

                JsonObject result = null;
                String body = request.body();
                int n = 0;
                if (body.startsWith("{")) {
                    JsonObject data = Json().toObject(body);
                    JsonObject payPalInfo = Json().object()
                            .string("paymentId", data.getString("paymentId"))
                            .string("payerId", data.getString("PayerID"))
                            .string("token", data.getString("token")).build();

                    // make sure required parameters are given in request
                    if(payPalInfo.getString("paymentId") == null ||
                       payPalInfo.getString("payerId") == null   ||
                       payPalInfo.getString("token") == null) {

                        Log.log.error(String.format(
                            "errorCode: %d /paypal request did not have required parameters: " +
                                "paymentId, payerId and token",
                                    ErrorCodes.REQUIRED_ARGUMENTS_ERROR));

                        JsonObject coordinator = EventCoordinator.instance().getWowCoordinator();
                        return errorResponse(400, ErrorCodes.WOWSERVER_ERROR, coordinator);
                    }

                    // execute the payment on paypal and fixup wow data
                    try {
                        result = PayPal.create().executePayment(payPalInfo);
                    } catch(AlreadyPaidException ex) {
                        result = Json().object().bool("alreadyPaid", true).build();
                        result.setArray("attendees", ex.getAttendees());
                        result.setString("completedOn", ex.getPaidInfo().getString("completedOn"));
                        result.setString("payPalTxId", ex.getPaidInfo().getString("payPalTxId"));

                        return errorResponse(400, ErrorCodes.ALREADY_PAID_ERROR, result);

                    } catch(Exception ex) {

                        JsonObject coordinator = EventCoordinator.instance().getWowCoordinator();
                        errorResponse(400, ErrorCodes.WOWSERVER_ERROR, coordinator);
                    }

                } else {
                    throw new MalformedJsonRequest(400,
                        "PayPal Request body is malformed JSON string.");
                }

                return responseBody(result);
            }
        });
    }
}
