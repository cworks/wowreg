package net.cworks.wowreg.db;

import net.cworks.wowreg.domain.Attendee;
import net.cworks.wowreg.domain.AttendeeBuilder;
import net.cworks.wowreg.rand.Randomness;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataStoreTest {

    static DataStore dataStore;

    @BeforeClass
    public static void initClass() {
        dataStore = DataStore.use("wowreg-test");
    }

    @AfterClass
    public static void endClass() {
        System.out.println("Bye-Bye unit test friend");
    }

    public static Attendee createAttendee() {

        AttendeeBuilder builder = new AttendeeBuilder();
        String firstName = Randomness.getFemaleFirstName();
        String lastName = Randomness.getLastName();
        Attendee attendee = builder.setFirstName(firstName)
            .setLastName(lastName)
            .setAddress(Randomness.getStreetAddress())
            .setCity(Randomness.getCity())
            .setState(Randomness.getState())
            .setZip(Randomness.getZipCode())
            .setEmail(Randomness.getEmailAddress(firstName))
            .setId(lastName + "." + firstName)
            .setPhone(Randomness.getPhoneNumber())
            .setEventId("2016")
            .setGroupId("123ABC")
            .setDateAdded(DateTime.now(DateTimeZone.UTC))
            .setPaymentStatus("Unpaid")
            .setRegistrationId(String.valueOf(Randomness.getIntegerBetween(1, 1000)))
            .setShirtAmt(12)
            .setRoomAmt(300)
            .setAgeGroup(Randomness.getString(new String[]{"Adult", "Teen"}))
            .setShirtSize(Randomness.getString(new String[]{"None", "Small $12",
                    "Medium $12", "Large $12", "X-Large $12",
                    "XX-Large $15", "XXX-Large $15"}))
            .setDonationAmt(Randomness.getIntegerBetween(0, 100)).create();

        return attendee;
    }

    @Test
    public void testCreateAttendee() {
        Attendee attendee = createAttendee();
        dataStore.createAttendee(attendee);
    }
}
