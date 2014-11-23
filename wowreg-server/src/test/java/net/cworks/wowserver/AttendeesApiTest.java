/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 
 * Baked with love by comartin
 * Package: net.cworks.wowserver
 * User: comartin
 * Created: 8/18/2014 2:56 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import net.cworks.http.Http;
import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static net.cworks.json.Json.Json;
import static net.cworks.wowserver.util.RandomHelper.randomFirstName;
import static net.cworks.wowserver.util.RandomHelper.randomIntegerBetween;
import static net.cworks.wowserver.util.RandomHelper.randomLastName;
import static net.cworks.wowserver.util.RandomHelper.randomPhoneNumber;

public class AttendeesApiTest {

    @BeforeClass
    public static void setupClass() {
        System.setProperty("db.un", "root");
        System.setProperty("db.pw", "");
        System.setProperty("db.url", "jdbc:mysql://localhost:3306/wowreg");
        WowServer server = new WowServer();
        server.main(null);
    }

    @Test
    public void createAttendee() throws IOException {

        String lastname = randomLastName();
        String firstname = randomFirstName();
        JsonObject attendee = Json().object()
            .number("registrationId", randomIntegerBetween(1, 50))
            .number("eventId", randomIntegerBetween(1000, 1005))
            .string("lastName", lastname)
            .string("firstName", firstname)
            .string("address", randomIntegerBetween(1, 9999) + " " + randomLastName() + " Lane")
            .string("city", randomLastName() + "wark")
            .string("state", "TX")
            .string("zip", String.valueOf(randomIntegerBetween(70000, 99999)))
            .string("country", "US")
            .string("email", (firstname + "@" + lastname + emailSuffix()).toLowerCase())
            .string("phone", randomPhoneNumber())
            .number("totalPrice", randomIntegerBetween(10000, 1000000)).build();

        String response = Http.post("http://localhost:4040/wow/attendees")
            .body(Json().toJson(attendee)).asString();
        System.out.println(response);
    }

    @Test
    public void createAttendees() throws IOException {

        int n = randomIntegerBetween(1, 10);
        JsonArray attendees = Json().array().build();
        for(int i = 0; i < n; i++) {
            String lastname = randomLastName();
            String firstname = randomFirstName();
            JsonObject attendee = Json().object()
                .number("registrationId", randomIntegerBetween(1, 50))
                .number("eventId", randomIntegerBetween(1000, 1005))
                .string("lastName", lastname)
                .string("firstName", firstname)
                .string("address", randomIntegerBetween(1, 9999) + " " + randomLastName() + " Street")
                .string("city", randomLastName() + "ville")
                .string("state", "TX")
                .string("zip", String.valueOf(randomIntegerBetween(70000, 99999)))
                .string("country", "US")
                .string("email", (firstname + "@" + lastname + emailSuffix()).toLowerCase())
                .string("phone", randomPhoneNumber())
                .number("totalPrice", randomIntegerBetween(10000, 1000000)).build();
            attendees.addObject(attendee);
        }

        String response = Http.post("http://localhost:4040/wow/attendees")
            .body(Json().toJson(attendees)).asString();
        System.out.println(response);

    }

    @Test
    public void retrieveAttendees() throws IOException {

        String response = Http.get("http://localhost:4040/wow/attendees")
            .asString();
        System.out.println(response);
    }

    static String emailSuffix() {
        String email = null;
        switch(randomIntegerBetween(0, 2)) {
            case 0:  email = ".com"; break;
            case 1:  email = ".net"; break;
            case 2:  email = ".org"; break;
            default: email = ".edu";
        }
        return email;
    }

}
