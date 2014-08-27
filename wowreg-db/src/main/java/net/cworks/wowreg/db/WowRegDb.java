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
import net.cworks.json.builder.JsonObjectBuilder;
import net.cworks.wowreg.ISODateParser;
import net.cworks.wowreg.db.conn.ConnPool;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.TransactionalCallable;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_COST;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_GROUP;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_META;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_TO_ATTENDEE_GROUP;
import static net.cworks.wowreg.db.schema.Tables.EVENT_PRICES;

public final class WowRegDb {

    private ConnPool pool = null;

    private DSLContext context = null;

    private WowRegDb(ConnPool pool) {
        this.pool = pool;
    }

    private void initContext() {
        try {
            Connection connection = this.pool.connection();
            context = DSL.using(connection, SQLDialect.MYSQL);
        } catch(SQLException ex) {
            throw new DbCreateException("could not initContext");
        }
    }

    /**
     * Initialize and create a WowRegDb instance
     * @param username
     * @param password
     * @param url
     * @return
     */
    public static WowRegDb db(String username, String password, String url) {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            ConnPool pool = new ConnPool(url, username, password);
            WowRegDb db = new WowRegDb(pool);
            db.initContext();
            return db;
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
     * @return a JsonArray containing JsonObjects representing all the newly inserted attendees
     */
    public JsonArray createAttendees(JsonArray attendees) {
        int n = 0;
        JsonArray list = Json().array().build();
        Iterator it = attendees.iterator();
        while(it.hasNext()) {
            JsonObject attendee = (JsonObject)it.next();
            list.add(createAttendee(attendee));
        }
        return list;
    }

    private String INSERT_ATTENDEE_SQL = "INSERT INTO attendee (" +
        "registration_id," +
        "event_id," +
        "last_name," +
        "first_name," +
        "address," +
        "city," +
        "state," +
        "zip," +
        "country," +
        "email," +
        "phone," +
        "payment_status," +
        "amount_paid," +
        "total_price," +
        "payment_date) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    /**
     * Create one attendee record
     * @param attendee Attendee information in JSON format
     * @return the newly created attendee record as a JsonObject
     */
    public JsonObject createAttendee(final JsonObject attendee) {

        JsonObject insertedAttendee = context.transactionResult(
            new TransactionalCallable<JsonObject>() {
                @Override
                public JsonObject run(Configuration config) throws Exception {

                    if(retrieveAttendee(attendee) != null) {
                        // attendee already exists
                        throw new AttendeeExistsException("attendee exists " + attendee.asString());
                    }

                    int status = context.insertInto(ATTENDEE,
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
                        ATTENDEE.AMOUNT_PAID,
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
                        attendee.getInteger("totalPrice", 0),
                        attendee.getInteger("amountPaid", 0),
                        _nowTimestamp()
                    ).execute();
                    if (status != 1) {
                        throw new DbCreateException("Error creating attendee record for: "
                                + attendee.asString());
                    }

                    return retrieveAttendee(attendee);
                }
            });

        attendee.merge(insertedAttendee);

        return attendee;
    }

    private static final String SELECT_ATTENDEE_SQL = "SELECT id," +
            "registration_id," +
            "event_id," +
            "last_name," +
            "first_name," +
            "address," +
            "city," +
            "state," +
            "zip," +
            "country," +
            "email," +
            "phone," +
            "payment_status," +
            "amount_paid," +
            "total_price," +
            "payment_date," +
            "date_added" +
        "FROM attendee" +
        "WHERE event_id = ?" +
        "  AND last_name = ?" +
        "  AND first_name = ?" +
        "  AND email = ?";
    /**
     * Retrieve one attendee
     * @return
     */
    public JsonObject retrieveAttendee(JsonObject attendee) {

        Record record = context.select()
                .from(ATTENDEE)
                .where(ATTENDEE.EVENT_ID.eq(attendee.getInteger("eventId")))
                .and(ATTENDEE.LAST_NAME.eq(attendee.getString("lastName")))
                .and(ATTENDEE.FIRST_NAME.eq(attendee.getString("firstName")))
                .and(ATTENDEE.EMAIL.eq(attendee.getString("email"))).fetchOne();
        if(record == null) {
            return null;
        }
        return attendee(record);
    }

    /**
     * Return a pending (i.e. not paid) attendee if one exists given an eventName and attendeeId
     * @param eventName
     * @param attendeeId
     * @return
     */
    public JsonObject retrieveNotPaidAttendee(String eventName, Integer attendeeId) {

        JsonObject attendee = null;
        try {
            String SELECT_NOTPAID_ATTENDEE_INFO = "SELECT" +
                "    att.id," +
                "    att.first_name," +
                "    att.last_name," +
                "    att.email," +
                "    att.address," +
                "    att.zip," +
                "    att.city," +
                "    att.state," +
                "    att.zip," +
                "    meta.meta_value AS age_class" +
                "FROM attendee att," +
                "    events evt," +
                "    attendee_meta meta" +
                "WHERE att.id = " + Integer.valueOf(attendeeId) +
                "    AND evt.event_name = '" + eventName + "'" +
                "    AND att.event_id = evt.id" +
                "    AND att.id = meta.attendee_id" +
                "    AND att.payment_status = 'NOT_PAID'";

            Result<Record> result = context.fetch(SELECT_NOTPAID_ATTENDEE_INFO);
            for(Record record : result) {
                attendee = Json().object()
                    .number("id", record.getValue("id", Integer.class))
                    .string("firstName", record.getValue("first_name", String.class))
                    .string("lastName", record.getValue("last_name", String.class))
                    .string("email", record.getValue("email", String.class))
                    .string("address", record.getValue("address", String.class))
                    .string("zip", record.getValue("zip", String.class))
                    .string("city", record.getValue("city", String.class))
                    .string("state", record.getValue("state", String.class))
                    .string("ageClass", record.getValue("age_class", String.class)).build();
                break;
            }
        } catch(DataAccessException ex) {
            throw new DbQueryException("Error retrieving pending attendee: " + attendeeId
                + " for event: " + eventName, ex);
        }

        return attendee;
    }

    private static final String SELECT_ATTENDEES_SQL = "SELECT id" +
            "registration_id," +
            "event_id," +
            "last_name," +
            "first_name," +
            "address," +
            "city," +
            "state," +
            "zip," +
            "country," +
            "email," +
            "phone," +
            "payment_status," +
            "amount_paid," +
            "total_price," +
            "payment_date," +
            "date_added" +
        "FROM attendee" +
        "ORDER BY date_added DESC LIMIT ?";
    /**
     * Retrieve the first 1000 attendees in descending dateAdded order
     * @return
     */
    public JsonArray retrieveAttendees() {

        Result<Record> result = context.select()
                .from(ATTENDEE)
                .orderBy(ATTENDEE.DATE_ADDED.desc().nullsFirst())
                .limit(1000).fetch();
        return attendees(result);
    }

    /**
     * Retrieve eventPrices that match the given items
     * @param items
     * @return
     */
    public JsonArray retrieveEventPrices(JsonArray items) {

        JsonArray eventPrices = Json().array().build();
        Iterator it = items.iterator();
        while(it.hasNext()) {
            JsonObject item = (JsonObject)it.next();
            Result<Record> record = context.select()
                .from(EVENT_PRICES)
                .where(EVENT_PRICES.ITEM.eq(item.getString("item"))).fetch();
            JsonObject eventPrice = eventPrice(record);
            eventPrices.addObject(eventPrice);
        }

        return eventPrices;
    }

    /**
     * Create a new attendeeCost record
     * @param attendeeCost
     */
    public void createAttendeeCost(JsonObject attendeeCost) {
        int status = 0;
        try {
            status = context.insertInto(ATTENDEE_COST,
                ATTENDEE_COST.ATTENDEE_ID,
                ATTENDEE_COST.EVENT_PRICES_ID).values(
                attendeeCost.getInteger("attendeeId"),
                attendeeCost.getInteger("eventPricesId")
            ).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating attendeeCost record for: "
                    + attendeeCost.asString());
            }
        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating attendeeCost record for: "
                + attendeeCost.asString(), ex);
        }
    }

    /**
     * Create a new attendeeMeta record and return the new attendeeMeta.id
     * @param attendeeMeta
     * @return attendee metadata as a JsonObject
     */
    public JsonObject createAttendeeMeta(JsonObject attendeeMeta) {
        JsonObject meta = null;
        try {
            int status = context.insertInto(ATTENDEE_META,
                ATTENDEE_META.ATTENDEE_ID,
                ATTENDEE_META.META_KEY,
                ATTENDEE_META.META_VALUE,
                ATTENDEE_META.META_TYPE).values(
                attendeeMeta.getInteger("attendeeId"),
                attendeeMeta.getString("metaKey"),
                attendeeMeta.getString("metaValue"),
                attendeeMeta.getString("metaType")).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating attendeeMeta record for: "
                    + attendeeMeta.asString());
            }

            JsonArray array = retrieveAttendeeMeta(attendeeMeta);
            meta = array.get(0);
        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating attendeeMeta record for: "
                + attendeeMeta.asString(), ex);
        }

        return meta;
    }

    /**
     * Retrieve an attendees list of associated attendeeMeta
     * @param attendee
     * @return
     */
    public JsonArray retrieveAttendeeMeta(JsonObject attendee) {

        Result<Record> result = context.select()
            .from(ATTENDEE_META)
            .where(ATTENDEE_META.ATTENDEE_ID.eq(attendee.getInteger("attendeeId")))
            .orderBy(ATTENDEE_META.ID.desc())
            .fetch();
        return attendeeMetas(result);
    }

    /**
     * Create an attendeeGroup record and return the attendeeGroup id
     * @param attendeeGroup
     * @return attendeeGroup as a JsonObject
     */
    public JsonObject createAttendeeGroup(JsonObject attendeeGroup) {
        JsonObject group;
        try {
            int status = context.insertInto(ATTENDEE_GROUP,
                ATTENDEE_GROUP.POC_ID,
                ATTENDEE_GROUP.GROUP_NAME).values(
                attendeeGroup.getInteger("pocId"),
                attendeeGroup.getString("groupName")).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating attendeeGroup record for: "
                    + attendeeGroup.asString());
            }

            group = retrieveAttendeeGroup(attendeeGroup);

        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating attendeeGroup record for: "
                + attendeeGroup.asString(), ex);
        }

        return group;
    }

    public JsonObject retrieveAttendeeGroup(JsonObject attendeeGroup) {

        Result<Record> records = context.select()
            .from(ATTENDEE_GROUP)
            .where(ATTENDEE_GROUP.POC_ID.eq(attendeeGroup.getInteger("pocId")))
            .orderBy(ATTENDEE_GROUP.ID.desc())
            .fetch();
        JsonObject group = null;
        for(Record record : records) {
            group = Json().object()
                .number("id", record.getValue(ATTENDEE_GROUP.ID))
                .number("pocId", record.getValue(ATTENDEE_GROUP.POC_ID))
                .string("groupName", record.getValue(ATTENDEE_GROUP.GROUP_NAME)).build();
            break;
        }

        return group;
    }

    /**
     * Create attendeeToAttendeeGroup relationship and return the id
     * @param attendeeToAttendeeGroup
     */
    public void createAttendeeToAttendeeGroup(JsonObject attendeeToAttendeeGroup) {

        int status = -1;
        try {
            status = context.insertInto(ATTENDEE_TO_ATTENDEE_GROUP,
                ATTENDEE_TO_ATTENDEE_GROUP.ATTENDEE_ID,
                ATTENDEE_TO_ATTENDEE_GROUP.GROUP_ID).values(
                    attendeeToAttendeeGroup.getInteger("attendeeId"),
                    attendeeToAttendeeGroup.getInteger("groupId")).execute();
        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating attendeeToAttendeeGroup record for: "
                + attendeeToAttendeeGroup.asString(), ex);
        }

        if(status != 1) {
            throw new DbCreateException("Error creating attendeeToAttendeeGroup record for: "
                + attendeeToAttendeeGroup.asString());
        }
    }

    public JsonArray retrievePendingAttendee(String eventName, Integer attendeeId) {
        JsonArray items = Json().array().build();
        try {
            String SELECT_ATTENDEE_COST = "SELECT " +
                "att.id, " +
                "att.first_name, " +
                "att.last_name, " +
                "att.email, " +
                "att.address, " +
                "att.zip, " +
                "att.city, " +
                "att.state, " +
                "meta.meta_value AS age_class, " +
                "prices.item, " +
                "prices.category, " +
                "prices.price, " +
                "prices.desc " +
                "FROM attendee att, attendee_cost cost, " +
                    "events evt, event_prices prices, attendee_meta meta " +
                "WHERE att.id =" + attendeeId +
                "  AND evt.event_name ='" + eventName + "'" +
                "  AND att.event_id = evt.id " +
                "  AND att.id = cost.attendee_id " +
                "  AND att.id = meta.attendee_id " +
                "  AND cost.event_prices_id = prices.id " +
                "ORDER BY prices.price DESC";

            Result<Record> result = context.fetch(SELECT_ATTENDEE_COST);
            for(Record record : result) {
                System.out.printf(record.toString());
                JsonObject item = Json().object()
                    .number("id", record.getValue("id", Integer.class))
                    .string("firstName", record.getValue("first_name", String.class))
                    .string("lastName", record.getValue("last_name", String.class))
                    .string("email", record.getValue("email", String.class))
                    .string("address", record.getValue("address", String.class))
                    .string("zip", record.getValue("zip", String.class))
                    .string("city", record.getValue("city", String.class))
                    .string("state", record.getValue("state", String.class))
                    .string("ageClass", record.getValue("age_class", String.class))
                    .string("item", record.getValue("item", String.class))
                    .string("category", record.getValue("category", String.class))
                    .number("price", record.getValue("price", Integer.class))
                    .string("desc", record.getValue("desc", String.class)).build();
                items.addObject(item);
            }
        } catch(DataAccessException ex) {
            throw new DbCreateException("Error retrieving attendee: " + attendeeId
                + " for event: " + eventName, ex);
        }

        return items;
    }

    private Timestamp _nowTimestamp() {
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        return ts;
    }

    private JsonObject attendee(Record record) {
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
        return attendee;
    }

    private JsonArray attendees(Result<Record> records) {
        JsonArrayBuilder builder  = Json().array();
        for(Record record : records) {
            JsonObject attendee = attendee(record);
            builder.add(attendee);
        }
        return builder.build();
    }

    private JsonArray attendeeMetas(Result<Record> records) {
        JsonArrayBuilder builder = Json().array();
        for(Record record : records) {
            JsonObject attendeeMeta = Json().object()
                .number("id", record.getValue(ATTENDEE_META.ID))
                .number("attendeeId", record.getValue(ATTENDEE_META.ATTENDEE_ID))
                .string("metaKey", record.getValue(ATTENDEE_META.META_KEY))
                .string("metaValue", record.getValue(ATTENDEE_META.META_VALUE))
                .string("metaType", record.getValue(ATTENDEE_META.META_TYPE))
                .string("dateAdded", ISODateParser.toString(
                    record.getValue(ATTENDEE_META.DATE_ADDED))).build();
            builder.add(attendeeMeta);
        }
        return builder.build();
    }

    private JsonObject eventPrice(Result<Record> records) {
        JsonObjectBuilder builder = Json().object();
        JsonObject eventPrice = null;
        for(Record record : records) {

            eventPrice = builder.number("id", record.getValue(EVENT_PRICES.ID))
                .string("item", record.getValue(EVENT_PRICES.ITEM))
                .string("category", record.getValue(EVENT_PRICES.CATEGORY))
                .string("description", record.getValue(EVENT_PRICES.DESC))
                .number("price", record.getValue(EVENT_PRICES.PRICE)).build();

            break; // just first eventPrice
        }

        return eventPrice;
    }

    private JsonObject rsToAttendee(ResultSet record) throws SQLException {
        JsonObject attendee = Json().object()
            .number("id", record.getInt("id"))
            .number("registrationId", record.getInt("registration_id"))
            .number("eventId", record.getInt("event_id"))
            .string("lastName", record.getString("last_name"))
            .string("firstName", record.getString("first_name"))
            .string("address", record.getString("address"))
            .string("city", record.getString("city"))
            .string("state", record.getString("state"))
            .string("zip", record.getString("zip"))
            .string("country", record.getString("country"))
            .string("email", record.getString("email"))
            .string("phone", record.getString("phone"))
            .string("paymentStatus", record.getString("payment_status"))
            .number("amountPaid", record.getInt("amount_paid"))
            .number("totalPrice", record.getInt("total_price"))
            .string("paymentDate", ISODateParser.toString(
                    record.getTimestamp("payment_date"))).build();
        return attendee;
    }
}
