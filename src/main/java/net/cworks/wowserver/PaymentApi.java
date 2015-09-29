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

import cworks.json.Json;
import cworks.json.JsonArray;
import cworks.json.JsonElement;
import cworks.json.JsonObject;
import net.cworks.Log;
import net.cworks.wowconf.EventCoordinator;
import net.cworks.wowpay.PayPal;
import net.cworks.wowpay.PaymentResult;
import net.cworks.wowpay.ex.AlreadyPaidException;
import net.cworks.wowreg.db.DataStore;
import net.cworks.wowreg.domain.Attendee;
import net.cworks.wowserver.ex.MalformedJsonRequest;
import net.cworks.wowserver.json.JsonResponseRoute;
import spark.Request;
import spark.Response;

import java.util.List;

import static net.cworks.wowconf.Mapper.toAttendees;
import static net.cworks.wowconf.Mapper.toJsonElement;
import static spark.Spark.post;

public class PaymentApi extends CoreApi {

    private static final Log LOG = Log.create(PaymentApi.class);

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

                String body = request.body();
                PaymentResult result;

                if(body.startsWith("{")) {
                    JsonArray attendees = Json.asObject(body).getArray("attendees");
                    if(attendees.size() > ATTENDEE_GROUP_LIMIT) {
                        return errorResponse(400, String.format(
                            "Group cannot exceed %d attendees, this group has %d attendees.",
                                ATTENDEE_GROUP_LIMIT, attendees.size()));
                    }

                    List<Attendee> attendeeList = toAttendees(attendees);
                    DataStore.use("wowreg-test")
                        .createAttendees(attendeeList);

                    result = PayPal.newPayment().pay(attendeeList);

                } else {
                    throw new MalformedJsonRequest(400, "JSON request body is not a JSON object");
                }

                return responseBody(toJsonElement(result));
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
                    JsonObject data = Json.asObject(body);
                    JsonObject payPalInfo = Json.object()
                            .string("paymentId", data.getString("paymentId"))
                            .string("payerId", data.getString("PayerID"))
                            .string("token", data.getString("token")).build();

                    // make sure required parameters are given in request
                    if(payPalInfo.getString("paymentId") == null ||
                       payPalInfo.getString("payerId") == null   ||
                       payPalInfo.getString("token") == null) {

                        LOG.error(String.format(
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
                        result = Json.object().bool("alreadyPaid", true).build();
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
