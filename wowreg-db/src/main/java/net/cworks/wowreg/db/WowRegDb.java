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
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;

import static net.cworks.json.Json.Json;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_COST;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_GROUP;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_META;
import static net.cworks.wowreg.db.schema.Tables.ATTENDEE_TO_ATTENDEE_GROUP;
import static net.cworks.wowreg.db.schema.Tables.EVENTS_PERSONNEL;
import static net.cworks.wowreg.db.schema.Tables.EVENT_PRICES;
import static net.cworks.wowreg.db.schema.Tables.PAYPAL_PAYMENT_AUTH;
import static net.cworks.wowreg.db.schema.Tables.PAYPAL_PAYMENT_INFO;
import static net.cworks.wowreg.db.schema.Tables.PAYPAL_PAYMENT_TX;

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

    public static WowRegDb db() {

        String username = System.getProperty("db.un");
        String password = System.getProperty("db.pw");
        String url = System.getProperty("db.url");
        return db(username, password, url);
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

        JsonObject targetAtt = context.transactionResult(
            new TransactionalCallable<JsonObject>() {
                @Override
                public JsonObject run(Configuration config) throws Exception {

                    // TODO Commenting out this exception per meeting on 11/19/2014
                    // if(retrieveAttendee(attendee) != null) {
                        // attendee already exists
                        // throw new AttendeeExistsException("attendee exists " + attendee.asString());
                    //}

                    Record result = context.insertInto(ATTENDEE,
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
                        ATTENDEE.DATE_ADDED).values(
                        attendee.getString("registrationId"),
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
                        _nowTimestamp()
                    ).returning(ATTENDEE.ID).fetchOne();

                    // convert into Json Object
                    JsonObject freshAttendee = attendee(result);

                    if(freshAttendee == null) {
                        throw new DbCreateException("Error creating attendee record for: "
                            + attendee.asString());
                    }

                    return freshAttendee;
                }
            });

        // merge the input with the output which has the 'id' field
        targetAtt.merge(attendee);

        return targetAtt;
    }

    /**
     * Update certain attendee fields of an Attendee record.  This update will not
     * update id, registrationId, eventId, paymentStatus, amountPaid, totalPrice or
     * dateAdded.  All other fields however can be updated through this method.
     *
     * @param attendee
     * @throws net.cworks.wowreg.db.DbUpdateException if the update goes wrong
     * @return the updated attendee
     */
    public JsonObject updateAttendee(final JsonObject attendee) {

        if(attendee == null) {
            throw new IllegalArgumentException("Can't update a null attendee");
        }
        if(attendee.getInteger("id") == null) {
            throw new DbUpdateException("Can't update an attendee that doesn't have an id");
        }
        JsonObject att = context.transactionResult(
            new TransactionalCallable<JsonObject>() {
            @Override
            public JsonObject run(Configuration config) throws Exception {
                JsonObject targetAttendee = retrieveAttendee(attendee.getInteger("id"));
                if(targetAttendee == null) {
                    throw new DbUpdateException("Can't update a non-existing attendee: "
                        + attendee.getInteger("id"));
                }
                // remove fields we're not gonna update through this method
                attendee.removeField("id");
                attendee.removeField("registrationId");
                attendee.removeField("registration_id");
                attendee.removeField("eventId");
                attendee.removeField("event_id");
                attendee.removeField("paymentStatus");
                attendee.removeField("payment_status");
                attendee.removeField("amountPaid");
                attendee.removeField("amount_paid");
                attendee.removeField("totalPrice");
                attendee.removeField("total_price");
                attendee.removeField("dateAdded");
                attendee.removeField("date_added");
                targetAttendee.merge(attendee);
                int status = context.update(ATTENDEE)
                    .set(ATTENDEE.LAST_NAME, targetAttendee.getString("lastName"))
                    .set(ATTENDEE.FIRST_NAME, targetAttendee.getString("firstName"))
                    .set(ATTENDEE.ADDRESS, targetAttendee.getString("address"))
                    .set(ATTENDEE.CITY, targetAttendee.getString("city"))
                    .set(ATTENDEE.STATE, targetAttendee.getString("state"))
                    .set(ATTENDEE.ZIP, targetAttendee.getString("zip"))
                    .set(ATTENDEE.COUNTRY, targetAttendee.getString("country"))
                    .set(ATTENDEE.EMAIL, targetAttendee.getString("email"))
                    .set(ATTENDEE.PHONE, targetAttendee.getString("phone"))
                    .where(ATTENDEE.ID.eq(targetAttendee.getInteger("id"))).execute();
                if(status != 1) {
                    throw new DbUpdateException("Error updating attendee record for: "
                        + targetAttendee.asString());
                }

                return targetAttendee;
            }
        });

        return retrieveAttendee(att.getInteger("id"));
    }

    /**
     * Retrieve an attendee by id
     * @param id
     * @return
     */
    public JsonObject retrieveAttendee(Integer id) {
        Record record = context.select()
                .from(ATTENDEE)
                .where(ATTENDEE.ID.eq(id)).fetchOne();
        if(record == null) {
            return null;
        }
        return attendee(record);
    }

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
     * Pull back the set of attendeeCost records for an attendee
     * @param attendee
     * @return
     */
    public JsonObject retrieveAttendeeCost(JsonObject attendee) {

        return null;
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
     * Update attendeeMeta record give an existing meta record
     * @param meta
     * @return
     */
    public JsonObject updateAttendeeMeta(final JsonObject meta) {
        if(meta == null) {
            throw new IllegalArgumentException("Can't update a null attendeeMeta argument!");
        }
        if(meta.getInteger("id") == null) {
            throw new DbUpdateException(
                "Can't update an attendeeMeta record that doesn't have an id");
        }
        JsonObject metadata = context.transactionResult(
            new TransactionalCallable<JsonObject>() {
                @Override
                public JsonObject run(Configuration config) throws Exception {
                    JsonObject targetMeta = retrieveAttendeeMeta(meta.getInteger("id"));
                    if(targetMeta == null) {
                        throw new DbUpdateException("Can't update a non-existing " +
                            "attendeeMeta record: " + meta.getInteger("id"));
                    }
                    // remove fields we're not gonna update through this method
                    meta.removeField("id");
                    meta.removeField("attendeeId");
                    meta.removeField("attendee_id");
                    meta.removeField("dateAdded");
                    meta.removeField("date_added");
                    targetMeta.merge(meta);
                    int status = context.update(ATTENDEE_META)
                        .set(ATTENDEE_META.META_KEY, targetMeta.getString("metaKey"))
                        .set(ATTENDEE_META.META_VALUE, targetMeta.getString("metaValue"))
                        .set(ATTENDEE_META.META_TYPE, targetMeta.getString("metaType")).execute();
                    if(status != 1) {
                        throw new DbUpdateException("Error updating attendeeMeta record for: "
                            + targetMeta.asString());
                    }

                    return targetMeta;
                }
            });

        return retrieveAttendeeMeta(metadata.getInteger("id"));
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
     * Retrieve one attendeeMeta record given the record id
     * @param id
     * @return
     */
    public JsonObject retrieveAttendeeMeta(Integer id) {

        Result<Record> result = context.select()
                .from(ATTENDEE_META)
                .where(ATTENDEE_META.ID.eq(id))
                .fetch();
        return attendeeMetas(result).get(0);
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
            throw new DbQueryException("Error retrieving attendee: " + attendeeId
                + " for event: " + eventName, ex);
        }

        return items;
    }

    /**
     * Create a PAYPAL_PAYMENT_INFO record
     * @param paymentInfo
     * @return
     */
    public Integer createPayPalPaymentInfo(JsonObject paymentInfo) {

        Integer status = 0;
        try {
            status = context.insertInto(PAYPAL_PAYMENT_INFO,
                PAYPAL_PAYMENT_INFO.PAYPAL_ID,
                PAYPAL_PAYMENT_INFO.PAYPAL_CREATE_TIME,
                PAYPAL_PAYMENT_INFO.PAYPAL_UPDATE_TIME,
                PAYPAL_PAYMENT_INFO.PAYPAL_STATE,
                PAYPAL_PAYMENT_INFO.PAYPAL_TX_AMOUNT,
                PAYPAL_PAYMENT_INFO.PAYPAL_TX_DESC,
                PAYPAL_PAYMENT_INFO.PAYPAL_APPROVAL_URL,
                PAYPAL_PAYMENT_INFO.PAYPAL_EXECUTE_URL,
                PAYPAL_PAYMENT_INFO.PAYPAL_SELF_URL,
                PAYPAL_PAYMENT_INFO.PAYPAL_TOKEN,
                PAYPAL_PAYMENT_INFO.GROUP_ID
                ).values(
                    paymentInfo.getString("payPalId"),
                    _toTimestamp(paymentInfo.getString("createTime")),
                    _toTimestamp(paymentInfo.getString("updatedTime")),
                    paymentInfo.getString("state"),
                    paymentInfo.getInteger("txAmount"),
                    paymentInfo.getString("txDesc"),
                    paymentInfo.getString("approvalUrl"),
                    paymentInfo.getString("executeUrl"),
                    paymentInfo.getString("selfUrl"),
                    paymentInfo.getString("token"),
                    paymentInfo.getInteger("groupId")).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating payPalPaymentInfo record for: "
                    + paymentInfo.asString());
            }

        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating payPalPaymentInfo record for: "
                + paymentInfo.asString(), ex);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return status;
    }

    /**
     * Create PAYPAL_PAYMENT_AUTH record
     * @param authInfo
     * @return
     */
    public Integer createPayPalPaymentAuth(JsonObject authInfo) {

        Integer status = 0;
        try {
            status = context.insertInto(PAYPAL_PAYMENT_AUTH,
                PAYPAL_PAYMENT_AUTH.PAYPAL_TOKEN,
                PAYPAL_PAYMENT_AUTH.PAYPAL_PAYER_ID,
                PAYPAL_PAYMENT_AUTH.DATE_ADDED
            ).values(
                authInfo.getString("token"),
                authInfo.getString("payerId"),
                _nowTimestamp()).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating payPalPaymentAuth record for: "
                    + authInfo.asString());
            }

        } catch(DataAccessException ex) {
            throw new DbCreateException("Error creating payPalPaymentAuth record for: "
                + authInfo.asString(), ex);
        }

        return status;
    }

    /**
     * Create PAYPAL_PAYMENT_TX record
     * @param txInfo
     * @return
     */
    public int createPayPalPaymentTx(JsonObject txInfo) {

        Integer status = 0;
        try {
            status = context.insertInto(PAYPAL_PAYMENT_TX,
                PAYPAL_PAYMENT_TX.PAYPAL_ID,
                PAYPAL_PAYMENT_TX.PAYPAL_PAYER_ID,
                PAYPAL_PAYMENT_TX.PAYPAL_PAYER_EMAIL,
                PAYPAL_PAYMENT_TX.PAYPAL_PAYER_FIRSTNAME,
                PAYPAL_PAYMENT_TX.PAYPAL_PAYER_LASTNAME,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_AMOUNT,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_ID,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_STATE,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_SELF_URL,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_REFUND_URL,
                PAYPAL_PAYMENT_TX.PAYPAL_TX_PARENT_PAYMENT_URL,
                PAYPAL_PAYMENT_TX.PAYPAL_CREATE_TIME,
                PAYPAL_PAYMENT_TX.PAYPAL_UPDATE_TIME,
                PAYPAL_PAYMENT_TX.DATE_ADDED
            ).values(
                txInfo.getString("payPalId"),
                txInfo.getString("payPalPayerId"),
                txInfo.getString("payPalPayerEmail"),
                txInfo.getString("payPalPayerFirstName"),
                txInfo.getString("payPalPayerLastName"),
                txInfo.getInteger("payPalTxAmount"),
                txInfo.getString("payPalTxId"),
                txInfo.getString("payPalTxState"),
                txInfo.getString("payPalTxSelfUrl"),
                txInfo.getString("payPalTxRefundUrl"),
                txInfo.getString("payPalTxParentPaymentUrl"),
                _toTimestamp(txInfo.getString("payPalTxCreateTime")),
                _toTimestamp(txInfo.getString("payPalTxUpdateTime")),
                _nowTimestamp()).execute();
            if (status != 1) {
                throw new DbCreateException("Error creating payPalPaymentTx record for: "
                    + txInfo.asString());
            }

        } catch(Exception ex) {
            throw new DbCreateException("Error creating payPalPaymentTx record for: "
                + txInfo.asString(), ex);
        }

        return status;
    }

    /**
     * Given a token and payerId pull back the proper paymentId
     * @param token
     * @param payerId
     * @return
     */
    public String retrievePaymentId(String token, String payerId) {

        String paymentId = null;
        try {
            String SELECT_PAYMENT_ID = "SELECT pi.paypal_id " +
                "FROM paypal_payment_info pi, paypal_payment_auth pa " +
                "WHERE pa.paypal_payer_id ='" + payerId.trim() + "' " +
                "AND pa.paypal_token ='" + token.trim() + "' " +
                "AND pa.paypal_token = pi.paypal_token";

            Result<Record> result = context.fetch(SELECT_PAYMENT_ID);
            for(Record record : result) {
                paymentId = record.getValue("paypal_id", String.class);
                break;
            }
        } catch(DataAccessException ex) {
            throw new DbQueryException("Error retrieving paymentId for token:  " + token
                + " payerId: " + payerId, ex);
        }

        return paymentId;
    }

    /**
     * Return a group of attendees given the paypal payment id.
     *
     * @param paymentId
     * @return
     */
    public JsonArray retrieveGroupFromPaymentId(String paymentId) {

        JsonArray group = null;
        try {
            String SELECT_GROUP = String.format(
                "SELECT grp.id as group_id, grp.group_name, att.id, att.first_name, att.last_name " +
                "FROM attendee att, attendee_group grp, attendee_to_attendee_group a2g " +
                "WHERE grp.id = a2g.group_id " +
                 "AND a2g.attendee_id = att.id " +
                 "AND grp.id = (SELECT group_id " +
                                 "FROM paypal_payment_info info " +
                                "WHERE info.paypal_id = '%s')", paymentId.trim());

            group = Json().array().build();
            Result<Record> result = context.fetch(SELECT_GROUP);
            for(Record record : result) {
                JsonObject attendee = Json().object().build();
                attendee.setNumber("groupId", record.getValue("group_id", Integer.class));
                attendee.setNumber("id", record.getValue("id", Integer.class));
                attendee.setString("firstName", record.getValue("first_name", String.class));
                attendee.setString("lastName", record.getValue("last_name", String.class));
                group.addObject(attendee);
            }

        } catch(DataAccessException ex) {
            throw new DbQueryException(String.format(
                "Error retrieving group for paymentId: %s", paymentId), ex);
        }

        return group;
    }

    /**
     * Return a group of attendees from a paypal token.  The group may or may not
     * have paid.  If that needs to be determined use isPayPalComplete.
     * @param paypalToken
     * @return
     */
    public JsonArray retrieveGroupFromPaypalToken(String paypalToken) {

        JsonArray group = null;
        try {
            String SELECT_GROUP = String.format(
                "SELECT grp.id as group_id, grp.group_name, att.id, att.first_name, att.last_name " +
                    "FROM attendee att, attendee_group grp, attendee_to_attendee_group a2g " +
                    "WHERE grp.id = a2g.group_id " +
                    "  AND a2g.attendee_id = att.id " +
                    "  AND grp.id = (SELECT group_id " +
                    " FROM paypal_payment_info info " +
                    "WHERE info.paypal_token = '%s')", paypalToken.trim());

            group = Json().array().build();
            Result<Record> result = context.fetch(SELECT_GROUP);
            for(Record record : result) {
                JsonObject attendee = Json().object().build();
                attendee.setNumber("id", record.getValue("id", Integer.class));
                attendee.setNumber("groupId", record.getValue("group_id", Integer.class));
                attendee.setString("firstName", record.getValue("first_name", String.class));
                attendee.setString("lastName", record.getValue("last_name", String.class));
                group.addObject(attendee);
            }

        } catch(DataAccessException ex) {
            throw new DbQueryException(String.format(
                    "Error retrieving group from paypal token: %s", paypalToken), ex);
        }

        return group;
    }

    /**
     * Try and determine if payment is complete for a given paypal paymentId and paypal
     * payment token.
     * @param authInfo
     * @return
     */
    public JsonObject isPayPalComplete(JsonObject authInfo) {

        JsonObject paid = null;
        try {
            String HAS_PAID_SELECT = String.format(
                "SELECT info.id AS info_id, auth.id AS auth_id, tx.id as tx_id, " +
                        "tx.paypal_tx_id, tx.date_added as completed_on " +
                    "FROM paypal_payment_info info, paypal_payment_auth auth, paypal_payment_tx tx " +
                    "WHERE info.paypal_token = auth.paypal_token " +
                    "  AND info.paypal_id = tx.paypal_id " +
                    "  AND auth.paypal_payer_id = tx.paypal_payer_id " +
                    "  AND info.paypal_id = '%s' " +
                    "  AND info.paypal_token = '%s' " +
                    "  AND tx.paypal_tx_state = 'completed' " +
                    "  AND info.paypal_state = 'completed'",
                        authInfo.getString("paymentId").trim(),
                        authInfo.getString("token").trim());

            Result<Record> result = context.fetch(HAS_PAID_SELECT);
            for(Record record : result) {
                paid = Json().object().build();

                String completedOn = ISODateParser.toSimpleString(
                    record.getValue("completed_on", Date.class));

                paid.setNumber("infoId", record.getValue("info_id", Integer.class));
                paid.setNumber("authId", record.getValue("auth_id", Integer.class));
                paid.setNumber("txId", record.getValue("tx_id", Integer.class));
                paid.setString("completedOn", completedOn);
                paid.setString("payPalTxId", record .getValue("paypal_tx_id", String.class));

                return paid;
            }

        } catch(DataAccessException ex) {
            throw new DbQueryException(String.format(
                "Error determining if payment is complete from: %s", authInfo.asString()), ex);
        }

        return paid;
    }

    /**
     * Return the attendee group associated with the given paypal_payment_info.id
     * @param infoId
     * @return
     */
    public JsonArray retrieveGroupFromPayPalPaymentInfoId(Integer infoId) {

        JsonArray group = null;
        try {
            String SELECT_GROUP = String.format(
                "SELECT grp.id as group_id, grp.group_name, att.first_name, att.last_name " +
                    "FROM attendee att, attendee_group grp, attendee_to_attendee_group a2g " +
                    "WHERE grp.id = a2g.group_id " +
                    "  AND a2g.attendee_id = att.id " +
                    "  AND grp.id = (SELECT group_id " +
                    " FROM paypal_payment_info info " +
                    "WHERE info.id = %d)", infoId);

            group = Json().array().build();
            Result<Record> result = context.fetch(SELECT_GROUP);
            for(Record record : result) {
                JsonObject attendee = Json().object().build();
                attendee.setNumber("groupId", record.getValue("group_id", Integer.class));
                attendee.setString("firstName", record.getValue("first_name", String.class));
                attendee.setString("lastName", record.getValue("last_name", String.class));
                group.addObject(attendee);
            }

        } catch(DataAccessException ex) {
            throw new DbQueryException(String.format(
                "Error retrieving group from paypal_payment_info.id: %s", infoId), ex);
        }

        return group;
    }

    /**
     * Updates paypal_payment_info.paypal_state to 'completed'
     *
     * @param paymentId
     * @return
     */
    public Integer updatePayPalPaymentInfoComplete(String paymentId) {

        Integer status = context.update(PAYPAL_PAYMENT_INFO)
            .set(PAYPAL_PAYMENT_INFO.PAYPAL_STATE, "completed")
            .where(PAYPAL_PAYMENT_INFO.PAYPAL_ID.eq(paymentId.trim()))
                .execute();

        if(status != 1) {
            throw new DbUpdateException(String.format(
                "Error updating paypal_payment_info.paypal_state to 'completed' " +
                    "for paypal_id: %s", paymentId));
        }

        return status;
    }

    /**
     * Update registration fields for each attendee in the array.
     *
     * @param attendees this of attendees to update
     * @param registrationId the paypal confirmation number
     * @param paymentDate date that paypal payment was complete
     */
    public void updateAttendeeRegistration(
        String registrationId,
        String paymentDate,
        JsonArray attendees) {

        Iterator it = attendees.iterator();
        while(it.hasNext()) {
            JsonObject attendee = (JsonObject)it.next();
            Timestamp payDate = null;

            try {
                payDate = _toTimestamp(paymentDate);
            } catch (ParseException e) {
                payDate = _nowTimestamp();
            }

            int status = context.update(ATTENDEE)
                .set(ATTENDEE.REGISTRATION_ID, registrationId)
                .set(ATTENDEE.PAYMENT_DATE, payDate)
                .set(ATTENDEE.PAYMENT_STATUS, "completed")
                .where(ATTENDEE.ID.eq(attendee.getInteger("id"))).execute();
            if(status != 1) {
                throw new DbUpdateException("Error updating attendee record for: "
                    + attendee.asString());
            }
        }
    }

    /**
     * Retrieve the Wow Event Coordinator
     * @return
     */
    public JsonObject retrieveEventCoordinator() {

        Record record = context.select()
                .from(EVENTS_PERSONNEL)
                .where(EVENTS_PERSONNEL.ROLE.eq("Registration Coordinator"))
                    .fetchOne();
        if(record == null) {
            return null;
        }

        JsonObject coordinator = Json().object()
            .number("id", record.getValue(EVENTS_PERSONNEL.ID))
            .string("lastName", record.getValue(EVENTS_PERSONNEL.LAST_NAME))
            .string("firstName", record.getValue(EVENTS_PERSONNEL.FIRST_NAME))
            .string("email", record.getValue(EVENTS_PERSONNEL.EMAIL))
            .string("phone", record.getValue(EVENTS_PERSONNEL.PHONE))
            .string("role", record.getValue(EVENTS_PERSONNEL.ROLE)).build();

        return coordinator;
    }

    /**
     * Update the paypal_payment_info record associated with the given token to "cancelled"
     * @param token
     * @return
     */
    public Integer updatePayPalPaymentInfoCancelled(String token) {

        Integer status = context.update(PAYPAL_PAYMENT_INFO)
            .set(PAYPAL_PAYMENT_INFO.PAYPAL_STATE, "cancelled")
            .where(PAYPAL_PAYMENT_INFO.PAYPAL_TOKEN.eq(token.trim()))
            .execute();

        if(status != 1) {
            throw new DbUpdateException(String.format(
                "Error updating paypal_payment_info.paypal_state to 'cancelled' " +
                    "for paypal_token: %s", token));
        }

        return status;
    }

    /**
     * Update an attendee to a "cancelled" status.
     *
     * @param attendee
     * @return
     */
    public Integer updateAttendeeToCancelled(JsonObject attendee) {

        Integer status = context.update(ATTENDEE)
            .set(ATTENDEE.PAYMENT_STATUS, "cancelled")
            .where(ATTENDEE.ID.eq(attendee.getInteger("id"))).execute();

        if(status != 1) {
            throw new DbUpdateException("Error updating attendee to cancelled for: "
                + attendee.asString());
        }

        return status;
    }

// =================================================================================================
//
// Private scope
//
// =================================================================================================

    private Timestamp _nowTimestamp() {
        Date now = new Date();
        Timestamp ts = new Timestamp(now.getTime());
        return ts;
    }

    private Timestamp _toTimestamp(String isoDate) throws ParseException {
        Date d = ISODateParser.parse(isoDate);
        return new Timestamp(d.getTime());
    }

    private JsonObject attendee(Record record) {
        JsonObject attendee = Json().object()
            .number("id", record.getValue(ATTENDEE.ID))
            .string("registrationId", record.getValue(ATTENDEE.REGISTRATION_ID))
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
