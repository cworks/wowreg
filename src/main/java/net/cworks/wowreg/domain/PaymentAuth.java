package net.cworks.wowreg.domain;


import org.joda.time.DateTime;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;
import java.util.List;

@Entity("paymentauth")
public class PaymentAuth implements Serializable {

    @Entity("link")
    public static class Link {
        @Id
        private String href;
        private String method;
        private String rel;
        public Link(String href, String method, String rel) {
            this.href = href;
            this.method = method;
            this.rel = rel;
        }
        public String getHref() {
            return this.href;
        }
        public String getMethod() {
            return this.method;
        }
        public String getRel() {
            return this.rel;
        }
    }

    /**
     * Id returned from PayPal
     */
    @Id
    private String id;

    /**
     * DateTime PayPal authorization occurred
     */
    private DateTime createTime;

    /**
     * PayPal payment intent, typical will be sale
     */
    private String paymentIntent;

    /**
     * PayPal cancel and completion links
     */
    private List<Link> links;

    /**
     * Payer's id, should match Attendee.id
     */
    private String payerId;

    /**
     * Payer's email
     */
    private String payerEmail;

    /**
     * Payer's first name
     */
    private String payerFirstName;

    /**
     * Payer's last name
     */
    private String payerLastName;

    /**
     * Use PaymentAuthBuilder outside of package
     * @param id
     * @param createTime
     * @param paymentIntent
     * @param links
     * @param payerId
     * @param payerEmail
     * @param payerFirstName
     * @param payerLastName
     * @param payerPhone
     * @param paymentMethod
     * @param paymentState
     * @param paymentAmount
     * @param paymentDescription
     */
    PaymentAuth(String id, DateTime createTime, String paymentIntent, List<Link> links,
        String payerId, String payerEmail, String payerFirstName, String payerLastName,
        String payerPhone, String paymentMethod, String paymentState,
        String paymentAmount, String paymentDescription) {

        this.id = id;
        this.createTime = createTime;
        this.paymentIntent = paymentIntent;
        this.links = links;
        this.payerId = payerId;
        this.payerEmail = payerEmail;
        this.payerFirstName = payerFirstName;
        this.payerLastName = payerLastName;
        this.payerPhone = payerPhone;
        this.paymentMethod = paymentMethod;
        this.paymentState = paymentState;
        this.paymentAmount = paymentAmount;
        this.paymentDescription = paymentDescription;
    }

    /**

     * Payer's phone number
     */
    private String payerPhone;

    /**
     * PayPal payment method, will typically be "paypal"
     */
    private String paymentMethod;

    /**
     * State of the PayPal payment
     */
    private String paymentState;

    /**
     * Total amount of the PayPal payment
     */
    private String paymentAmount;

    /**
     * The payment description
     */
    private String paymentDescription;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public String getPaymentIntent() {
        return paymentIntent;
    }

    public void setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
    }

    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(String payerLastName) {
        this.payerLastName = payerLastName;
    }

    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }
}
