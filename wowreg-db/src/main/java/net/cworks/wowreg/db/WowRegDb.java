/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowreg.db
 * Class: WowRegDb
 * Created: 8/15/14 2:32 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowreg.db;

import net.cworks.json.JsonArray;
import net.cworks.json.JsonObject;
import net.cworks.json.builder.JsonArrayBuilder;
import net.cworks.wowreg.ISODateParser;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE;

public class WowRegDb {

    private DSLContext context;

    private Connection connection;

    WowRegDb(DSLContext context, Connection connection) {
        this.context = context;
        this.connection = connection;
    }

    public static WowRegDb db(String username, String password, String url) {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection connection = DriverManager.getConnection(url, username, password);
            DSLContext context = DSL.using(connection, SQLDialect.MYSQL);
            return new WowRegDb(context, connection);
        } catch (Exception ex) {
            throw new DbConnectException("Trouble connecting with: "
                + "username: " + username + " "
                + "password: " + blank(password, '*') + " "
                + "url: " + url, ex);
        }
    }

    /**
     * Return a string of the same length as text but fill it with the filler character
     * @param text
     * @param filler
     * @return
     */
    static String blank(String text, char filler) {
        if(text == null || text.trim().length() == 0) {
            return text;
        }

        char[] blank = new char[text.length()];
        for(int i = 0; i < text.length(); i++) {
            blank[i] = filler;
        }

        return new String(blank);
    }

    /**
     * Create multiple attendee records
     * @param attendees
     * @return
     */
    public int createAttendees(JsonArray attendees) {
        int n = 0;
        Iterator it = attendees.iterator();
        while(it.hasNext()) {
            JsonObject attendee = (JsonObject)it.next();
            n = n + createAttendee(attendee);
        }
        return n;
    }

    /**
     * Create one attendee record
     * @param attendee Attendee information in JSON format
     * @return the number of inserts, in this case 1
     */
    public int createAttendee(JsonObject attendee) {

        int status = 0;
        try {
            status = context.insertInto(ATTENDEE,
                ATTENDEE.REGISTRATION_ID,
                ATTENDEE.EVENT_ID,
                ATTENDEE.LAST_NAME,
                ATTENDEE.FIRST_NAME,
                ATTENDEE.ADDRESS,
                ATTENDEE.CITY,
                ATTENDEE.STATE,
                ATTENDEE.ZIP,
                ATTENDEE.COUNTRY,
                ATTENDEE.EMAIL,
                ATTENDEE.PHONE,
                ATTENDEE.PAYMENT_STATUS,
                ATTENDEE.TOTAL_PRICE,
                ATTENDEE.DATE_ADDED).values(
                attendee.getInteger("registrationId"),
                attendee.getInteger("eventId"),
                attendee.getString("lastName"),
                attendee.getString("firstName"),
                attendee.getString("address"),
                attendee.getString("city"),
                attendee.getString("state"),
                attendee.getString("zip"),
                attendee.getString("country", "US"),
                attendee.getString("email"),
                attendee.getString("phone"),
                attendee.getString("paymentStatus", PaymentStatus.NOT_PAID.toString()),
                attendee.getInteger("totalPrice"),
                _nowTimestamp()
            ).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating attendee record for: "
                        + attendee.asString());
            }
        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating attendee record for: "
                + attendee.asString(), ex);
        }

        return status;
    }

    public JsonArray retrieveAttendees() {

        Result<Record> result = context.select()
            .from(ATTENDEE)
            .orderBy(ATTENDEE.DATE_ADDED.desc().nullsFirst())
            .limit(1000).fetch();
        return attendees(result);
    }

    public void close() {
        if(connection != null) {
            try { connection.close(); } catch (SQLException ex) { }
        }
    }

    private Timestamp _nowTimestamp() {
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        return ts;
    }

    private JsonArray attendees(Result<Record> records) {
        JsonArrayBuilder builder  = Json().array();
        for(Record record : records) {

            record.getValue(ATTENDEE.REGISTRATION_ID);

            JsonObject attendee = Json().object()
                .number("id", record.getValue(ATTENDEE.ID))
                .number("registrationId", record.getValue(ATTENDEE.REGISTRATION_ID))
                .number("eventId", record.getValue(ATTENDEE.EVENT_ID))
                .string("lastName", record.getValue(ATTENDEE.LAST_NAME))
                .string("firstName", record.getValue(ATTENDEE.FIRST_NAME))
                .string("address", record.getValue(ATTENDEE.ADDRESS))
                .string("city", record.getValue(ATTENDEE.CITY))
                .string("state", record.getValue(ATTENDEE.STATE))
                .string("zip", record.getValue(ATTENDEE.ZIP))
                .string("country", record.getValue(ATTENDEE.COUNTRY))
                .string("email", record.getValue(ATTENDEE.EMAIL))
                .string("phone", record.getValue(ATTENDEE.PHONE))
                .string("paymentStatus", record.getValue(ATTENDEE.PAYMENT_STATUS))
                .number("amountPaid", record.getValue(ATTENDEE.AMOUNT_PAID))
                .number("totalPrice", record.getValue(ATTENDEE.TOTAL_PRICE))
                .string("paymentDate", ISODateParser.toString(
                    record.getValue(ATTENDEE.PAYMENT_DATE))).build();
            builder.add(attendee);
        }

        return builder.build();
    }
}
