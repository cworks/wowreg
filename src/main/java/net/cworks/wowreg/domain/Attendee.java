package net.cworks.wowreg.domain;

import org.joda.time.DateTime;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

@Entity("attendee")
public class Attendee implements Serializable {

    @Id
    private String   id;
    private String   groupId;
    private String   registrationId;
    private String   eventId;
    private Boolean  poc;       // keep
    private String   lastName;  // keep
    private String   firstName; // keep
    private String   address;   // keep
    private String   city;      // keep
    private String   state;     // keep
    private String   zip;       // keep
    private String   email;     // keep
    private String   phone;     // keep
    private String   ageGroup;  // keep, value will be 'Adult' or 'Teen'
    private String   shirtSize;    // keep
    private Integer  donationAmt;  // keep
    private Integer  roomAmt;      // keep
    private Integer  shirtAmt;     // keep
    private Integer  totalAmt;     // keep

    private String   paymentStatus;
    private DateTime paymentDate;
    private DateTime dateAdded;

    Attendee() {}

    Attendee(String id,
             String groupId,
             String registrationId,
             String eventId,
             Boolean poc,
             String lastName,
             String firstName,
             String address,
             String city,
             String state,
             String zip,
             String email,
             String phone,
             String ageGroup,
             String shirtSize,
             Integer donationAmt,
             Integer roomAmt,
             Integer shirtAmt,
             Integer totalAmt,
             String paymentStatus,
             DateTime paymentDate,
             DateTime dateAdded) {

        this.id = id;
        this.groupId = groupId;
        this.registrationId = registrationId;
        this.eventId = eventId;
        this.poc = poc;
        this.lastName = lastName;
        this.firstName = firstName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.email = email;
        this.phone = phone;
        this.ageGroup = ageGroup;
        this.shirtSize = shirtSize;
        this.donationAmt = donationAmt;
        this.roomAmt = roomAmt;
        this.shirtAmt = shirtAmt;
        this.totalAmt = totalAmt;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.dateAdded = dateAdded;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return this.registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getEventId() {
        return this.eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Boolean getPoc() {
        return poc;
    }

    public void setPoc(Boolean poc) {
        this.poc = poc;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public DateTime getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public DateTime getDateAdded() {
        return this.dateAdded;
    }

    public void setDateAdded(DateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getShirtSize() {
        return shirtSize;
    }

    public void setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
    }

    public Integer getDonationAmt() {
        return donationAmt;
    }

    public void setDonationAmt(Integer donationAmt) {
        this.donationAmt = donationAmt;
    }

    public Integer getRoomAmt() {
        return roomAmt;
    }

    public void setRoomAmt(Integer roomAmt) {
        this.roomAmt = roomAmt;
    }

    public Integer getShirtAmt() {
        return shirtAmt;
    }

    public void setShirtAmt(Integer shirtAmt) {
        this.shirtAmt = shirtAmt;
    }

    @Override
    public String toString() {
        return "WowAttendee{" +
            "id=" + id +
            ", groupId=" + groupId +
            ", registrationId='" + registrationId + '\'' +
            ", eventId=" + eventId +
            ", poc=" + poc +
            ", lastName='" + lastName + '\'' +
            ", firstName='" + firstName + '\'' +
            ", address='" + address + '\'' +
            ", city='" + city + '\'' +
            ", state='" + state + '\'' +
            ", zip='" + zip + '\'' +
            ", email='" + email + '\'' +
            ", phone='" + phone + '\'' +
            ", ageGroup='" + ageGroup + '\'' +
            ", shirtSize='" + shirtSize + '\'' +
            ", donationAmt=" + donationAmt +
            ", roomAmt=" + roomAmt +
            ", shirtAmt=" + shirtAmt +
            ", totalAmt=" + totalAmt +
            ", paymentStatus='" + paymentStatus + '\'' +
            ", paymentDate=" + paymentDate +
            ", dateAdded=" + dateAdded +
            '}';
    }

    public Integer getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
    }
}
