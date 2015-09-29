/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowpay.ex
 * Class: AlreadyPaidException
 * Created: 11/18/14 1:16 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowpay.ex;

import cworks.json.JsonArray;
import cworks.json.JsonObject;

public class AlreadyPaidException extends PaymentException {

    private JsonObject paidInfo;
    private JsonArray attendees;

    public AlreadyPaidException(JsonObject paidInfo, JsonArray attendees) {
        super();
        this.paidInfo = paidInfo;
        this.attendees = attendees;
    }

    public JsonArray getAttendees() {
        return this.attendees;
    }

    public JsonObject getPaidInfo() {
        return this.paidInfo;
    }

}
