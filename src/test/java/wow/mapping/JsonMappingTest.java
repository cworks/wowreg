package wow.mapping;

import cworks.json.Json;
import net.cworks.wowreg.domain.Attendee;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

public class JsonMappingTest {

    private Boolean  poc;
    private String   lastName;  //
    private String   firstName; //
    private String   address;   //
    private String   city;      //
    private String   state;     //
    private String   zip;       //
    private String   email;     //
    private String   phone;     //
    private String   ageGroup;     // Adult or Teen
    private String   shirtSize;        // shirtSize size
    private Integer  donationAmt;  // in cents
    private Integer  roomAmt;      // in cents
    private Integer  shirtAmt;     // in cents
    private Integer  totalAmt;

    private String        paymentStatus;
    private LocalDateTime paymentDate;
    private LocalDateTime dateAdded;

    private static final String ATTENDEE_JSON = "{\n" +
        "        \"firstName\": \"Nacho\",\n" +
        "        \"lastName\": \"Martin\",\n" +
        "        \"address\": \"641 Post Oak Dr\",\n" +
        "        \"city\": \"Hurst\",\n" +
        "        \"state\": \"Texas\",\n" +
        "        \"zip\": \"76053\",\n" +
        "        \"email\": \"cworkscode@gmail.com\",\n" +
        "        \"phone\": \"(817) 787 - 8888\",\n" +
        "        \"ageGroup\": \"Teen\",\n" +
        "        \"poc\": false,\n" +
        "        \"roomAmt\": 105,\n" +
        "        \"shirtSize\": \"XX-Large\",\n" +
        "        \"shirtAmt\": 15,\n" +
        "        \"donationAmt\": 20,\n" +
        "        \"totalAmt\": 140\n" +
        "    }";

    @Test
    public void testAttendeeMapping() {

        Attendee attendee = Json.asObject(ATTENDEE_JSON, Attendee.class);
        Assert.assertEquals("Nacho", attendee.getFirstName());
        Assert.assertEquals("Martin", attendee.getLastName());
        Assert.assertEquals("641 Post Oak Dr", attendee.getAddress());
        Assert.assertEquals("Hurst", attendee.getCity());
        Assert.assertEquals("Texas", attendee.getState());
        Assert.assertEquals("76053", attendee.getZip());
        Assert.assertEquals("cworkscode@gmail.com", attendee.getEmail());
        Assert.assertEquals("(817) 787 - 8888", attendee.getPhone());
        Assert.assertEquals("Teen", attendee.getAgeGroup());
        Assert.assertEquals(false, attendee.getPoc());
        Assert.assertEquals("XX-Large", attendee.getShirtSize());
        Assert.assertEquals(105L, (long)attendee.getRoomAmt());
        Assert.assertEquals(15L, (long) attendee.getShirtAmt());
        Assert.assertEquals(20L, (long)attendee.getDonationAmt());
        Assert.assertEquals(140L, (long)attendee.getTotalAmt());

        System.out.println(attendee);



    }

}
