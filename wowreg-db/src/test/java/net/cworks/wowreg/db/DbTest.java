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

import net.cworks.json.JsonObject;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.Test;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;

import static net.cworks.json.Json.Json;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE;

public class DbTest {

    @Test
    public void use() {

        Connection connection = null;
        String username = "root";
        String password = "";
        String url = "jdbc:mysql://localhost:3306/wowreg";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(url, username, password);
            DSLContext create = DSL.using(connection, SQLDialect.MYSQL);
            Result<Record> result = create.select().from(ATTENDEE).fetch();
            for (Record r : result) {
                Integer id = r.getValue(ATTENDEE.ID);
                String firstName = r.getValue(ATTENDEE.FIRST_NAME);
                String lastName = r.getValue(ATTENDEE.LAST_NAME);
                String email = r.getValue(ATTENDEE.EMAIL);
                System.out.println("ID: " + id
                    + " first name: " + firstName
                    + " last name: " + lastName
                    + " email: " + email);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        } finally {
            if(connection != null) {
                try { connection.close(); } catch(SQLException ignore) { }
            }
        }
    }

    @Test
    public void insertAttendee() {

        Random random = new SecureRandom();
        Integer key = random.nextInt((1000000 - 1) + 1) + 1;

        WowRegDb db = WowRegDb.db("root", "", "jdbc:mysql://localhost:3306/wowreg");

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
}
