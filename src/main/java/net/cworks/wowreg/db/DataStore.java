package net.cworks.wowreg.db;

import com.mongodb.MongoClient;
import cworks.json.JsonArray;
import cworks.json.JsonObject;
import net.cworks.wowreg.domain.Attendee;
import net.cworks.wowreg.domain.PaymentAuth;
import net.cworks.wowreg.domain.PayPalInfo;
import net.cworks.wowreg.domain.PayPalTx;
import net.cworks.wowreg.domain.WowGroup;
import net.cworks.wowreg.mongo.JodaDateTimeConverter;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

public final class DataStore {

    private Datastore ds;

    private DataStore(Datastore datastore) {
        this.ds = datastore;
    }

    public static DataStore use(String dataStore) {
        Morphia morphia = new Morphia();
        morphia.mapPackage("net.cworks.wowreg.domain");
        morphia.getMapper().getConverters().addConverter(new JodaDateTimeConverter());
        Datastore ds = morphia.createDatastore(new MongoClient(), dataStore);
        return new DataStore(ds);
    }

    public void createAttendee(Attendee attendee) {
        ds.save(attendee);
    }

    public void createAttendees(final List<Attendee> attendees) {
        for(Attendee attendee : attendees) {
            createAttendee(attendee);
        }
    }

    public void retrieveAttendee(int id) {

    }

    public void retrieveAttendee(Attendee attendee) {

    }

    public JsonArray retrieveAttendees() {

        return null;
    }

    public void updateAttendee(Attendee attendee) {

    }

    public void updateAttendees(List<Attendee> attendees) {

    }

    public void deleteAttendee(int id) {

    }

    public void deleteAttendee(Attendee attendee) {

    }

    public void deleteAttendees(Integer...ids) {

    }

    public void deleteAttendees(List<Attendee> attendees) {

    }

    public void createGroup(WowGroup group) {

    }

    public void createGroup(String id) {

    }

    public void retrieveGroup(int id) {

    }

    public void retrieveGroup(WowGroup group) {

    }

    public void updateGroup(WowGroup group) {

    }

    public void deleteGroup(String id) {

    }

    public void deleteGroup(WowGroup group) {

    }

    public void createPayPalInfo(PayPalInfo info) {

    }

    public void retrievePayPalInfo(String id) {

    }

    public void createPaymentAuth(PaymentAuth auth) {
        ds.save(auth);
    }

    public void retrievePayPalAuth(String id) {

    }

    public void createPayPalTx(PayPalTx tx) {

    }

    public void retrievePayPalTx(String id) {

    }


    public JsonObject createAttendee(JsonObject jsonObject) {
        return null;
    }

    public void createAttendeeMeta(JsonObject attendeeMeta) {

    }

    public JsonArray createAttendees(JsonArray objects) {
        return null;
    }

    public JsonObject createAttendeeGroup(JsonObject attendeeGroup) {
        return null;
    }

    public void createAttendeeToAttendeeGroup(JsonObject attendeeToAttendeeGroup) {

    }

    public Integer updatePayPalPaymentInfoCancelled(String token) {
        return null;
    }

    public JsonArray retrieveGroupFromPaypalToken(String token) {
        return null;
    }

    public Integer updateAttendeeToCancelled(JsonObject attendee) {
        return null;
    }

    public void createAttendeeCost(JsonObject costRecord) {

    }

    public void createPayPalPaymentInfo(JsonObject info) {

    }

    public JsonArray retrieveGroupFromPayPalPaymentInfoId(Integer infoId) {
        return null;
    }

    public int createPayPalPaymentAuth(JsonObject authInfo) {
        return 0;
    }

    public String retrievePaymentId(String token, String payerId) {
        return null;
    }

    public int createPayPalPaymentTx(JsonObject txInfo) {
        return 0;
    }

    public JsonArray retrieveGroupFromPaymentId(String paymentId) {
        return null;
    }

    public void updatePayPalPaymentInfoComplete(String paymentId) {

    }

    public void updateAttendeeRegistration(String payPalTxId, String payPalTxUpdateTime, JsonArray attendees) {

    }

    public JsonObject isPayPalComplete(JsonObject authInfo) {
        return null;
    }

    public JsonArray retrievePendingAttendee(String eventName, Integer integer) {
        return null;
    }

    public JsonObject retrieveEventCoordinator() {
        return null;
    }


}
