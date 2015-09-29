package net.cworks.wowreg.domain;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class PaymentAuthBuilder {

    private String id;
    private DateTime createTime;
    private String paymentIntent;
    private List<PaymentAuth.Link> links;
    private String payerId;
    private String payerEmail;
    private String payerFirstName;
    private String payerLastName;
    private String payerPhone;
    private String paymentMethod;
    private String paymentState;
    private String paymentAmount;
    private String paymentDescription;

    public PaymentAuthBuilder() {
        this.links = new ArrayList<>();
    }

    public PaymentAuthBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PaymentAuthBuilder setCreateTime(DateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public PaymentAuthBuilder setPaymentIntent(String paymentIntent) {
        this.paymentIntent = paymentIntent;
        return this;
    }

    public PaymentAuthBuilder addLink(final PaymentAuth.Link link) {
        this.links.add(link);
        return this;
    }

    public PaymentAuthBuilder setPayerId(String payerId) {
        this.payerId = payerId;
        return this;
    }

    public PaymentAuthBuilder setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
        return this;
    }

    public PaymentAuthBuilder setPayerFirstName(String payerFirstName) {
        this.payerFirstName = payerFirstName;
        return this;
    }

    public PaymentAuthBuilder setPayerLastName(String payerLastName) {
        this.payerLastName = payerLastName;
        return this;
    }

    public PaymentAuthBuilder setPayerPhone(String payerPhone) {
        this.payerPhone = payerPhone;
        return this;
    }

    public PaymentAuthBuilder setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public PaymentAuthBuilder setPaymentState(String paymentState) {
        this.paymentState = paymentState;
        return this;
    }

    public PaymentAuthBuilder setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
        return this;
    }

    public PaymentAuthBuilder setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
        return this;
    }

    public PaymentAuth create() {
        return new PaymentAuth(id, createTime, paymentIntent, links,
            payerId, payerEmail, payerFirstName, payerLastName, payerPhone,
            paymentMethod, paymentState, paymentAmount, paymentDescription);
    }
}