package net.cworks.wowreg.domain;

import org.joda.time.DateTime;

public class AttendeeBuilder {
    private String id;
    private String groupId;
    private String registrationId;
    private String eventId;
    private Boolean poc;
    private String lastName;
    private String firstName;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String email;
    private String phone;
    private String ageGroup;
    private String shirtSize;
    private Integer donationAmt;
    private Integer roomAmt;
    private Integer shirtAmt;
    private Integer totalAmt;
    private String paymentStatus;
    private DateTime paymentDate;
    private DateTime dateAdded;

    public AttendeeBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public AttendeeBuilder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public AttendeeBuilder setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
        return this;
    }

    public AttendeeBuilder setEventId(String eventId) {
        this.eventId = eventId;
        return this;
    }

    public AttendeeBuilder setPoc(Boolean poc) {
        this.poc = poc;
        return this;
    }

    public AttendeeBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public AttendeeBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public AttendeeBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public AttendeeBuilder setCity(String city) {
        this.city = city;
        return this;
    }

    public AttendeeBuilder setState(String state) {
        this.state = state;
        return this;
    }

    public AttendeeBuilder setZip(String zip) {
        this.zip = zip;
        return this;
    }

    public AttendeeBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public AttendeeBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public AttendeeBuilder setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
        return this;
    }

    public AttendeeBuilder setShirtSize(String shirtSize) {
        this.shirtSize = shirtSize;
        return this;
    }

    public AttendeeBuilder setDonationAmt(Integer donationAmt) {
        this.donationAmt = donationAmt;
        return this;
    }

    public AttendeeBuilder setRoomAmt(Integer roomAmt) {
        this.roomAmt = roomAmt;
        return this;
    }

    public AttendeeBuilder setShirtAmt(Integer shirtAmt) {
        this.shirtAmt = shirtAmt;
        return this;
    }

    public AttendeeBuilder setTotalAmt(Integer totalAmt) {
        this.totalAmt = totalAmt;
        return this;
    }

    public AttendeeBuilder setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
        return this;
    }

    public AttendeeBuilder setPaymentDate(DateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public AttendeeBuilder setDateAdded(DateTime dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public Attendee create() {
        return new Attendee(id, groupId, registrationId, eventId, poc, lastName, firstName, address, city, state, zip, email, phone, ageGroup, shirtSize, donationAmt, roomAmt, shirtAmt, totalAmt, paymentStatus, paymentDate, dateAdded);
    }
}