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
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.io.File;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Random;

import static net.cworks.json.Json.Json;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE;

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

        db.close();
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

        JsonObject nacho = Json().object().number("registrationId", 100)
            .number("eventId", 1000)
            .string("lastName", "Martin_" + key)
            .string("firstName", "Nacho_" + key)
            .string("address", "1234 Mexico Street")
            .string("city", "El Paso")
            .string("state", "TX")
            .string("zip", "79911")
            .string("country", "US")
            .string("email", "nacho_" + key + "@libre.com")
            .string("phone", "915-867-5309")
            .number("totalPrice", 500 * 100).build();
        db.createAttendee(nacho);
        db.close();

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
