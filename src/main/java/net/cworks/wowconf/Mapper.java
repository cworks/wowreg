package net.cworks.wowconf;

import cworks.json.Json;
import cworks.json.JsonArray;
import cworks.json.JsonElement;
import cworks.json.JsonObject;
import net.cworks.Wow;
import net.cworks.wowpay.PaymentResult;
import net.cworks.wowreg.domain.Attendee;

import java.util.ArrayList;
import java.util.List;

public class Mapper {


    public static List<Attendee> toAttendees(final JsonArray attendees) {
        String groupId = Wow.nextGroupId();
        List<Attendee> attendeeList = new ArrayList<>();
        for(int i = 0; i < attendees.size(); i++) {
            JsonObject object = attendees.get(i);
            Attendee attendee = Json.asObject(object.asString(), Attendee.class);
            attendee.setId(Wow.nextId());
            attendee.setGroupId(groupId);
            attendee.setPaymentStatus("Unpaid");
            attendee.setDateAdded(Wow.now());
            attendee.setEventId(Wow.eventId());
            attendee.setRegistrationId(Wow.nextRegistrationId());
            attendeeList.add(attendee);
        }

        return attendeeList;
    }

    public static JsonElement toJsonElement(final PaymentResult result) {

        return null;
    }
}
