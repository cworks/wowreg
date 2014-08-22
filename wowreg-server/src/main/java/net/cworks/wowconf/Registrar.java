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

import static net.cworks.json.Json.Json;

final public class Registrar {

    private static final Registrar INSTANCE = new Registrar();

    private Registrar() { }

    public static Registrar instance() {
        return INSTANCE;
    }

    public int register(JsonObject attendee) {
        WowRegDb db = db();

        // TODO validate attendee here...yeah right.

        // create attendee records so we know who wants to attend
        int n = db.createAttendee(attendee);
        if(n != 1) {
            throw new RuntimeException("TODO Change this to custom exception");
        }

        // create attendee cost records so we know how much the attendee owes
        //"items" : [
        //{ "item" : "shirt", "size" : "XL" },
        //{ "item" : "donation", "amount" : 10000 }]
        JsonArray items = attendee.getArray("items");
        if(items != null && items.size() > 0) {
            JsonArray eventPrices = db.retrieveEventPrices(items);
            for (int i = 0; i < eventPrices.size(); i++) {
                JsonObject eventPrice = eventPrices.get(i);
                JsonObject attendeeCost = Json().object()
                        .number("attendee_id", attendee.getNumber("id"))
                        .number("event_prices_id", eventPrice.getNumber("id")).build();
                db.createAttendeeCost(attendeeCost);
            }
        }
        // create attendeeMeta record to persist special info such as the ageClass of attendee
        String ageClass = attendee.getString("ageClass", "adult");
        JsonObject attendeeMeta = Json().object()
            .number("attendee_id", attendee.getNumber("id"))
            .string("meta_key", "age_class")
            .string("meta_value", ageClass)
            .string("meta_type", "string").build();
        db.createAttendeeMeta(attendeeMeta);

        close(db);
        return 0;
    }

    public int register(JsonArray attendees) {
        WowRegDb db = db();

        close(db);
        return 0;
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
        db.close();
    }
}
