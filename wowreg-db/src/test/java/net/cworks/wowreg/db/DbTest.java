/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db
 * Class: DbTest
 * Created: 8/14/14 9:24 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Random;

import static net.cworks.json.Json.Json;

public class DbTest {

    @Test
    public void selectAttendees() {

        if(!isDbReady()) {
            return;
        }

        JsonObject config = dbConfig();
        String username = config.getString("db.username");
        String password = config.getString("db.password");
        String url = config.getString("db.url");

        WowRegDb db = WowRegDb.db(username, password, url);
        JsonArray list = db.retrieveAttendees();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            JsonObject attendee = (JsonObject)it.next();
            System.out.println(Json().toJson(attendee));
        }

    }

    @Test
    public void insertAttendee() {

        if(!isDbReady()) {
            return;
        }

        JsonObject config = dbConfig();
        String username = config.getString("db.username");
        String password = config.getString("db.password");
        String url = config.getString("db.url");

        Random random = new SecureRandom();
        Integer key = random.nextInt((1000000 - 1) + 1) + 1;

        WowRegDb db = WowRegDb.db(username, password, url);

        JsonObject nacho = Json().object().string("registrationId", "100")
            .number("eventId", 1000)
            .string("lastName", "Martin_" + key)
            .string("firstName", "Nacho_" + key)
            .string("address", "1234 Mexico Street")
            .string("city", "El Paso")
            .string("state", "TX")
            .string("zip", "79911")
            .string("country", "US")
            .string("email", "nacho_" + key + "@libre.com")
            .string("phone", "915-867-5309").build();
        db.createAttendee(nacho);

    }

    @Test
    public void updateAttendee() {
        if(!isDbReady()) {
            return;
        }

        JsonObject config = dbConfig();
        String username = config.getString("db.username");
        String password = config.getString("db.password");
        String url = config.getString("db.url");

        Random random = new SecureRandom();
        Integer key = random.nextInt((1000000 - 1) + 1) + 1;

        WowRegDb db = WowRegDb.db(username, password, url);

        JsonObject attendee = Json().object().string("registrationId", "100")
                .number("eventId", 1000)
                .string("lastName", "Man_" + key)
                .string("firstName", "Update_" + key)
                .string("address", "123 Sesame St.")
                .string("city", "New York")
                .string("state", "NY")
                .string("zip", "10001")
                .string("country", "US")
                .string("email", "update_" + key + "@man.com")
                .string("phone", "646-123-4567")
                .number("totalPrice", 500 * 100).build();
        attendee = db.createAttendee(attendee);
        attendee.setString("firstName", "John_" + key)
            .setString("lastName", "Big_" + key)
            .setString("address", "456 Sesame St.")
            .setString("city", "Brooklyn")
            .setString("zip", "10005")
            .setString("email", "john_" + key + "@big.com");
        JsonObject updatedAttendee = db.updateAttendee(attendee);
        JsonObject attendee2 = db.retrieveAttendee(updatedAttendee);
        Assert.assertEquals("John_" + key, attendee2.getString("firstName"));
        Assert.assertEquals("Big_" + key, attendee2.getString("lastName"));
        Assert.assertEquals("456 Sesame St.", attendee2.getString("address"));
        Assert.assertEquals("Brooklyn", attendee2.getString("city"));
        Assert.assertEquals("10005", attendee2.getString("zip"));
        Assert.assertEquals("john_" + key + "@big.com", attendee2.getString("email"));
        Assert.assertEquals("646-123-4567", attendee2.getString("phone"));
        Assert.assertEquals(1000, attendee2.getNumber("eventId"));
        Assert.assertEquals("100", attendee2.getString("registrationId"));

    }

    /**
     * Return true if database is up and running, otherwise false.
     * used in this unit test because some of these tests require a database
     * to be up and running the wowreg schema.
     * @return
     */
    boolean isDbReady() {

        try {
            JsonObject config = dbConfig();
            String username = config.getString("db.username");
            String password = config.getString("db.password");
            String url = config.getString("db.url");

            WowRegDb db = WowRegDb.db(username,
                password,
                url);

        } catch(DbConnectException ex) {
            // DbConnectException means database isn't ready
            return false;
        }

        return true;
    }

    /**
     * Return database config, username, password and url
     * @return
     */
    JsonObject dbConfig() {
        JsonObject config = Json().toObject(
            new File(System.getProperty("wowreg.propfile",
                "src/test/resources/wowreg.json")));
        return config;
    }
}
