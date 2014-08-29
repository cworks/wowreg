/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowconf
 * Class: Registrar
 * Created: 8/20/14 11:06 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowconf;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import net.cworks.wowreg.db.WowRegDb;

import java.util.Iterator;

import static net.cworks.json.Json.Json;

final public class Registrar {

    private static final Registrar INSTANCE = new Registrar();

    private Registrar() { }

    public static Registrar instance() {
        return INSTANCE;
    }

    /**
     * Register one attendee
     * @param attendee
     * @return the newly registered attendee as a JsonObject
     */
    public JsonObject register(JsonObject attendee) {
        WowRegDb db = db();

        // TODO validate attendee here...yeah right.
        validateAttendee(attendee);

        // create attendee records so we know who wants to attend
        attendee = db.createAttendee(attendee);
        if(attendee == null) {
            throw new RuntimeException("TODO Change this to custom exception");
        }

        // create attendee cost records so we know how much the attendee owes
        JsonArray items = attendee.getArray("items");
        if(items != null && items.size() > 0) {
            JsonArray eventPrices = db.retrieveEventPrices(items);
            for (int i = 0; i < eventPrices.size(); i++) {
                JsonObject eventPrice = eventPrices.get(i);
                JsonObject attendeeCost = Json().object()
                    .number("attendeeId", attendee.getNumber("id"))
                    .number("eventPricesId", eventPrice.getNumber("id")).build();
                db.createAttendeeCost(attendeeCost);
            }
        }
        // create attendeeMeta record to persist special info such as the ageClass of attendee
        String ageClass = attendee.getString("ageClass", "adult");
        JsonObject attendeeMeta = Json().object()
            .number("attendeeId", attendee.getNumber("id"))
            .string("metaKey", "age_class")
            .string("metaValue", ageClass)
            .string("metaType", "string").build();
        db.createAttendeeMeta(attendeeMeta);

        close(db);

        if(attendee.getInteger("id") == null) {
            return null;
        }
        return attendee;
    }

    /**
     * Register a group of attendees - TODO need to compute and insert ATTENDEE.total_price
     * @param attendees
     * @return
     */
    public int register(JsonArray attendees) {
        WowRegDb db = db();
        int registrationCount = 0;
        try {

            //
            // Handle registering the group and the group's point-of-contact
            //
            Iterator it = attendees.iterator();
            boolean hasPoc = false;
            JsonObject group = null;
            while (it.hasNext()) {
                JsonObject attendee = (JsonObject) it.next();
                if (attendee.getBoolean("poc")) {
                    // register this attendee and mark this attendee as a group point-of-contact
                    if(register(attendee) == null) {
                        throw new RuntimeException("Cannot create poc attendee");
                    }
                    registrationCount++;
                    JsonObject attendeeGroup = Json().object()
                        .number("pocId", attendee.getInteger("id"))
                        .string("groupName", attendee.getString("lastName") + "_"
                            + attendee.getString("firstName")).build();
                    // create the group and set group's point-of-contact
                    group = db.createAttendeeGroup(attendeeGroup);
                    hasPoc = true;
                    // actually place point-of-contact into group my creating this association
                    JsonObject attendeeToAttendeeGroup = Json().object()
                        .number("attendeeId", attendee.getInteger("id"))
                        .number("groupId", group.getInteger("id")).build();
                    // add registered attendee to the group
                    db.createAttendeeToAttendeeGroup(attendeeToAttendeeGroup);

                    break;
                }
            }

            // if the group does not have a point-of-contact then we don't
            // allow registration to continue.
            if (!hasPoc) {
                throw new RuntimeException("Group of Attendees does not " +
                    "have a group point-of-contact");
            }

            //
            // Now after group and point-of-contact has been created proceed with creating
            // other attendees and placing them into group
            //
            it = attendees.iterator();
            while(it.hasNext()) {
                JsonObject attendee = (JsonObject)it.next();
                if(attendee.getBoolean("poc", false)) {
                    continue; // we've already inserted the group point-of-contact above
                }
                if(register(attendee) != null) {
                    registrationCount++;
                    JsonObject attendeeToAttendeeGroup = Json().object()
                        .number("attendeeId", attendee.getInteger("id"))
                        .number("groupId", group.getInteger("id")).build();
                    // add registered attendee to the group
                    db.createAttendeeToAttendeeGroup(attendeeToAttendeeGroup);
                }
            }

        } finally {
            close(db);
        }

        return registrationCount;
    }


    public JsonObject registrationUpdate(JsonObject attendee) {

        // validate attendee
        validateAttendee(attendee);

        WowRegDb db = db();
        JsonObject targetAttendee = db.retrieveAttendee(attendee);
        JsonObject targetAttendeeCost = db.retrieveAttendeeCost(attendee);
        JsonArray targetAttendeeMeta = db.retrieveAttendeeMeta(attendee);
        db.updateAttendee(attendee);

        return null;

    }

    private void validateAttendee(JsonObject attendee) {

        attendee.getString("firstName");
        attendee.getString("lastName");
        attendee.getBoolean("poc"); // optional

    }

    int recordCosts(JsonObject attendee) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordCosts(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordAgeClass(JsonObject attendee) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    int recordAgeClass(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
    }

    private WowRegDb db() {
        return WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");
    }

    private void close(WowRegDb db) {
    }
}
