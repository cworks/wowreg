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
import java.security.SecureRandom;
import java.util.Random;

import static net.cworks.json.Json.Json;
import static net.cworks.wowserver.rand.RandomHelper.randomFirstName;
import static net.cworks.wowserver.rand.RandomHelper.randomIntegerBetween;
import static net.cworks.wowserver.rand.RandomHelper.randomLastName;
import static net.cworks.wowserver.rand.RandomHelper.randomPhoneNumber;

public class AttendeesApiTest {

    @BeforeClass
    public static void setupClass() {
        WowServer server = new WowServer();
        server.start();
    }

    @Test
    public void createAttendee() throws IOException {

        Random random = new SecureRandom();
        Integer key = random.nextInt((1000000 - 1) + 1) + 1;

        JsonObject hanSolo = Json().object().number("registrationId", 99)
            .number("eventId", 1001)
            .string("lastName", "Han_" + key)
            .string("firstName", "Solo_" + key)
            .string("address", "1 Millenium Falcon Way")
            .string("city", "Alderean")
            .string("state", "NM")
            .string("zip", "TK421")
            .string("country", "US")
            .string("email", "han_" + key + "@solo.com")
            .string("phone", "1-800-671-2311")
            .number("totalPrice", 300 * 100).build();
        String response = Http.post("http://localhost:4567/wow/attendees")
            .body(Json().toJson(hanSolo)).asString();
        System.out.println(response);
    }

    @Test
    public void createAttendees() throws IOException {

        int n = randomIntegerBetween(1, 10);
        JsonArray attendees = Json().array().build();
        for(int i = 0; i < n; i++) {
            String lastname = randomLastName();
            String firstname = randomFirstName();
            String email = null;
            switch(randomIntegerBetween(0, 2)) {
                case 0:  email = ".com"; break;
                case 1:  email = ".net"; break;
                case 2:  email = ".org"; break;
                default: email = ".edu";
            }
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
                .string("email", (firstname + "@" + lastname + email).toLowerCase())
                .string("phone", randomPhoneNumber())
                .number("totalPrice", randomIntegerBetween(10000, 1000000)).build();
            attendees.addObject(attendee);
        }

        String response = Http.post("http://localhost:4567/wow/attendees")
            .body(Json().toJson(attendees)).asString();
        System.out.println(response);

    }


    @Test
    public void retrieveAttendees() throws IOException {

        WowServer.main(null);
        String response = Http.get("http://localhost:4567/wow/attendees")
            .asString();
        System.out.println(response);
    }

}
