/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowconf
 * Class: Events
 * Created: 8/25/14 1:29 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowconf;


import cworks.json.Json;
import cworks.json.JsonArray;
import cworks.json.JsonObject;
import cworks.json.builder.JsonObjectBuilder;
import net.cworks.wowreg.db.DataStore;

final public class EventCoordinator {

    private static final EventCoordinator INSTANCE = new EventCoordinator();

    private EventCoordinator() { }

    public static EventCoordinator instance() {
        return INSTANCE;
    }

    /**
     * Return costs associated with an attendee
     * @param eventName
     * @param attendeeId
     * @return
     */
    public JsonObject pendingAttendee(String eventName, String attendeeId) {
        DataStore db = DataStore.use("wowreg");

        JsonArray pendingInfo = db.retrievePendingAttendee(eventName, Integer.valueOf(attendeeId));
        JsonObjectBuilder builder = Json.object();
        // the array will contain duplicate information on the attendee so we grab it from
        // first record in the returned array
        if(pendingInfo.size() > 0) {
            builder.number("id", ((JsonObject)pendingInfo.get(0)).getInteger("id"))
                .string("firstName", ((JsonObject)pendingInfo.get(0)).getString("firstName"))
                .string("lastName", ((JsonObject)pendingInfo.get(0)).getString("lastName"))
                .string("email", ((JsonObject) pendingInfo.get(0)).getString("email"))
                .string("address", ((JsonObject) pendingInfo.get(0)).getString("address"))
                .string("zip", ((JsonObject) pendingInfo.get(0)).getString("zip"))
                .string("city", ((JsonObject) pendingInfo.get(0)).getString("city"))
                .string("state", ((JsonObject) pendingInfo.get(0)).getString("state"))
                .string("ageClass", ((JsonObject) pendingInfo.get(0)).getString("age_class"));
        }

        JsonArray items = Json.array().build();
        Integer total = 0;
        for(Object item : pendingInfo) {
            if(item instanceof JsonObject) {
                JsonObject itemRec = Json.object()
                    .string("item", ((JsonObject)item).getString("item"))
                    .string("category", ((JsonObject)item).getString("category"))
                    .number("price", ((JsonObject)item).getInteger("price"))
                    .string("desc", ((JsonObject)item).getString("desc")).build();
                total = total + ((JsonObject)item).getInteger("price");
                items.addObject(itemRec);
            }
        }

        builder.array("items", items);
        builder.number("total", total);

        return builder.build();
    }

    /**
     * Return the Wow Event Coordinator
     * @return
     */
    public JsonObject getWowCoordinator() {

        final DataStore db = DataStore.use("wowreg");

        JsonObject wowCoordinator = db.retrieveEventCoordinator();
        if(wowCoordinator == null) {
            wowCoordinator.setString("firstName", "Brooke")
                .setString("lastName", "Martin")
                .setString("email", "wowemail@gmail.com")
                .setString("phone", "817-111-2222")
                .setString("role", "Registration Coordinator");
        }

        return wowCoordinator;
    }
}
