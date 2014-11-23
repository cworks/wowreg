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

import net.cworks.Log;
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
        WowRegDb db = WowRegDb.db();

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

        if(attendee.getInteger("id") == null) {
            return null;
        }

        return attendee;
    }

    /**
     * Register a group of attendees - TODO need to compute and insert ATTENDEE.total_price
     * @param attendees
     * @return the registration group
     */
    public JsonObject register(JsonArray attendees) {
        WowRegDb db = WowRegDb.db();
        int registrationCount = 0;
        JsonObject groupInfo = null;
        try {

            //
            // Handle registering the group and the group's point-of-contact
            //
            Iterator it = attendees.iterator();
            boolean hasPoc = false;
            JsonObject group = null;
            JsonArray groupAttendees = Json().array().build();
            while (it.hasNext()) {
                JsonObject attendee = (JsonObject) it.next();
                if (attendee.getBoolean("poc")) {
                    // register this attendee and mark this attendee as a group point-of-contact
                    attendee = register(attendee);
                    if(attendee == null) {
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

                    JsonObject pocAttendee = Json().object().number("attendeeId", attendee.getInteger("id"))
                            .string("firstName", attendee.getString("firstName"))
                            .string("lastName", attendee.getString("lastName"))
                            .string("email", attendee.getString("email"))
                            .bool("poc", true).build();
                    groupAttendees.add(pocAttendee);

                    // construct groupInfo which is what we return from this method
                    groupInfo = Json().object().number("groupId", group.getInteger("id"))
                        .number("pocId", group.getInteger("pocId"))
                        .string("groupName", group.getString("groupName")).build();

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
                attendee = register(attendee);
                if(attendee != null) {
                    registrationCount++;
                    JsonObject attendeeToAttendeeGroup = Json().object()
                        .number("attendeeId", attendee.getInteger("id"))
                        .number("groupId", group.getInteger("id")).build();
                    // add registered attendee to the group
                    db.createAttendeeToAttendeeGroup(attendeeToAttendeeGroup);
                    JsonObject groupAttendee = Json().object().number("attendeeId", attendee.getInteger("id"))
                        .string("firstName", attendee.getString("firstName"))
                        .string("lastName", attendee.getString("lastName"))
                        .string("email", attendee.getString("email")).build();
                    groupAttendees.add(groupAttendee);
                }
            }
            // add group attendees to groupInfo
            groupInfo.setArray("groupAttendees", groupAttendees);
            groupInfo.setNumber("groupSize", registrationCount);
        } catch(Exception ex) {
            Log.log.error("exception in register() ", ex);
        }

        return groupInfo;
    }

    /**
     * Cancel a registration given a paypal token
     * 1. update paypal_payment_info.paypal_state = cancelled
     * 2. update each attendee in group to have a payment_status = cancelled
     * @param token
     * @return
     */
    public JsonArray cancel(String token) {

        WowRegDb db = WowRegDb.db();
        JsonArray cancelledAttendees = Json().array().build();

        Integer status = db.updatePayPalPaymentInfoCancelled(token);

        if(status == 1) {

            JsonArray attendees =db.retrieveGroupFromPaypalToken(token);
            Iterator it = attendees.iterator();
            while(it.hasNext()) {
                JsonObject attendee = (JsonObject)it.next();
                status = db.updateAttendeeToCancelled(attendee);
                if(status == 1) {
                    cancelledAttendees.addObject(attendee);
                }
            }
        }

        return cancelledAttendees;
    }
}
