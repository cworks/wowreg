/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowpay
 * Class: PayPal
 * Created: 10/6/14 10:36 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowpay;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.PayPalRESTException;
import cworks.json.Json;
import cworks.json.JsonArray;
import cworks.json.JsonObject;
import net.cworks.Log;
import net.cworks.wowpay.ex.AlreadyPaidException;
import net.cworks.wowpay.ex.PaymentException;
import net.cworks.wowreg.db.DataStore;
import net.cworks.wowreg.domain.Attendee;
import net.cworks.wowreg.domain.PaymentAuth;
import net.cworks.wowreg.domain.PaymentAuthBuilder;
import net.cworks.wowserver.HttpUtil;
import net.cworks.wowserver.util.Aes;
import org.joda.time.DateTime;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class PayPal {

    private static final Log LOG = Log.create(PayPal.class);

    private final DataStore db = DataStore.use("wowreg");

    /**
     * Force creation through this method so we can ensure paypal sdk_config.properties
     * file is read and loaded into memory before we create a PayPal instance.
     * @return
     */
    public static PayPal create() {
        PayPal payPal = new PayPal();
        return payPal;
    }

    /**
     * Hide public access, use create()
     */
    private PayPal() { }

    /**
     *
     * @param attendees
     * @return
     */
    public PaymentResult pay(final List<Attendee> attendees) {

        PaymentResult result = null;
        try {

            Amount amount = new Amount();
            amount.setCurrency("USD");
            amount.setTotal(toTotal(attendees));

            Transaction tx = new Transaction();
            tx.setDescription("2016 WoW Conference Registration");
            tx.setAmount(amount);

            List<Transaction> txList = new ArrayList<>();
            txList.add(tx);

            Payer payer = createPayer(attendees);
            Payment payment = new Payment();
            payment.setIntent("sale");
            payment.setPayer(payer);
            payment.setTransactions(txList);

            RedirectUrls redirectUrls = new RedirectUrls();
            String host = System.getProperty("wow.host", "localhost");
            String protocol = System.getProperty("wow.protocol", "http");
            String csrf = encodeCsrf();

            redirectUrls.setCancelUrl(protocol + "://" + host + "/cancelled.html?csrf=" + csrf);
            redirectUrls.setReturnUrl(protocol + "://" + host + "/thankyou.html?csrf="  + csrf);
            payment.setRedirectUrls(redirectUrls);

            Payment createdPayment = payment.create(PayPalAccess.getAPIContext());
            String response = Payment.getLastResponse();
            LOG.info("PayPal Response:" + response);

            DataStore.use("wowreg-test").createPaymentAuth(
                createPaymentAuth(getPointOfContact(attendees), createdPayment));

        } catch(Exception ex) {
            String errorMsg = "Error attempting paypal payment";
            LOG.error(errorMsg, ex);
            throw new PaymentException(errorMsg, ex);
        }

        return result;
    }

    private PaymentAuth createPaymentAuth(Attendee attendee, Payment payment) {

        PaymentAuthBuilder builder = new PaymentAuthBuilder();

        builder.setId(payment.getId());
        builder.setCreateTime(DateTime.parse(payment.getCreateTime()));
        builder.setPaymentIntent(payment.getIntent());

        List<Links> links = payment.getLinks();
        for(Links link : links) {
            builder.addLink(new PaymentAuth.Link(link.getHref(), link.getMethod(), link.getRel()));
        }

        builder.setPayerId(attendee.getId());
        builder.setPayerEmail(attendee.getEmail());
        builder.setPayerFirstName(attendee.getFirstName());
        builder.setPayerLastName(attendee.getLastName());
        builder.setPayerPhone(attendee.getPhone());

        builder.setPaymentMethod(payment.getPayer().getPaymentMethod());
        builder.setPaymentState(payment.getState());

        builder.setPaymentAmount(payment.getTransactions().get(0).getAmount().getTotal());
        builder.setPaymentDescription(payment.getTransactions().get(0).getDescription());

        return builder.create();
    }

    private String encodeCsrf() throws UnsupportedEncodingException {
        return URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8");
    }

    /**
     * Compute the total WoW Conference cost for all attendees in list
     * @param attendees
     * @return
     */
    private String toTotal(List<Attendee> attendees) {
        Integer total = 0;
        for(Attendee attendee : attendees) {
            Integer roomAmt = attendee.getRoomAmt();
            Integer shirtAmt = attendee.getShirtAmt();
            Integer donationAmt = attendee.getDonationAmt();
            Integer totalAmt = attendee.getTotalAmt();
            if(roomAmt + shirtAmt + donationAmt != totalAmt) {
                LOG.error("roomAmt+shirtAmt+donationAmt should equal totalAmt: "
                    + roomAmt + "+" + shirtAmt + "+" + donationAmt + "!=" + totalAmt);
            }
            total += roomAmt + shirtAmt + donationAmt;
        }

        return String.valueOf(total);
    }

    /**
     * Create a PayPal payment by using our PayPal access token.  PayPal will create
     * a payment resource on their side and send us back information about that payment
     * such as the paypal payment id (paypal_id), transaction amount (paypal_tx_amount),
     * the all important paypal approval URL (paypal_approval_url) which is used to redirect
     * user to PayPal so they can approve transaction, lastly the PayPal token (paypal_token)
     * which we need to persist because PayPal is gonna hand this token back to us when the
     * payment is executed by User.  We need to look it up and link the payment info with the
     * final approved PayPal transaction.
     *
     * In addition to persisting PayPal info we need to associate the 'intent' to pay with the
     * WoW group by creating a PayPalPaymentInfoToAttendeeGroup cross reference record.  This links
     * the one PayPalPaymentInfo record with one AttendeeGroup.  Note this association DOES not
     * mean the group has paid, it just means the group has done everything BUT execute the Payment
     * on PayPal. Once payment is executed a Registered record will be created.
     *
     * There may have been a few run-on sentences in the documentation above :)
     *
     * @param registrationInfo
     * @return
     */
    public JsonObject createPayment(JsonObject registrationInfo) {

        Amount amount = new Amount();
        amount.setCurrency("USD");

        // The cost will always come as whole dollars, for example 160 for $160.00
        // there will be no partial dollar amounts.
        amount.setTotal(String.valueOf(registrationInfo.getInteger("groupTotalCost")));

        Transaction transaction = new Transaction();
        transaction.setDescription("WoW conference registration");
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        JsonObject result = null;
        try {

            RedirectUrls redirectUrls = new RedirectUrls();
            // This will be called if users decides to cancel out of payment, paypal will hand us a token
            // see paypal_payment_info.paypal_token
            String host = System.getProperty("wow.host", "localhost");
            String protocol = System.getProperty("wow.protocol", "http");

            String csrf = encodeCsrf(registrationInfo);

            redirectUrls.setCancelUrl(protocol + "://" + host + "/cancelled.html?csrf=" + csrf);
            // This will be called if use goes through and authorizes payment
            // paypal will hand us a paymentId, see paypal_payment_info.paypal_id
            // Example return URL
            // https://wowconf.org/thankyou.html?success=true
            //  &paymentId=PAY-8RV460893L3735539KRMZ66Y
            //  &token=EC-4J403218R3236660U
            //  &PayerID=RSYY8JVJBG5NW
            redirectUrls.setReturnUrl(protocol + "://" + host + "/thankyou.html?csrf=" + csrf);
            payment.setRedirectUrls(redirectUrls);

            // create a payment by posting to the paypal API service using a valid accessToken
            Payment createdPayment = payment.create(PayPalAccess.getAPIContext());
            // get last response for posterity
            String response = Payment.getLastResponse();
            result = Json.asObject(response);
            System.out.println("Create Payment Resource Response:" + result.asString());

            JsonObject info = Json.object().string("payPalId", createdPayment.getId())
                .string("createTime", createdPayment.getCreateTime())
                .string("updatedTime", createdPayment.getUpdateTime())
                .string("state", createdPayment.getState()).build();

            // take the approvalUrl from JSON response and redirect
            // user to paypal so they can approve payment
            Iterator<Links> links = createdPayment.getLinks().iterator();
            while(links.hasNext()) {
                Links link = links.next();
                if("approval_url".equalsIgnoreCase(link.getRel())) {
                    // link to use for redirect
                    info.setString("approvalUrl", link.getHref());
                    // token is included as a URL param so we parse it out and save
                    info.setString("token", HttpUtil.queryMap(new URL(link.getHref())).get("token"));
                } else if("execute".equalsIgnoreCase(link.getRel())) {
                    info.setString("executeUrl", link.getHref());
                } else if("self".equalsIgnoreCase(link.getRel())) {
                    info.setString("selfUrl", link.getHref());
                }
            }

            Iterator<Transaction> trans = createdPayment.getTransactions().iterator();
            while(trans.hasNext()) {
                Transaction tx = trans.next();
                info.setNumber("txAmount", toCents(tx.getAmount().getTotal()))
                    .setString("txDesc", tx.getDescription());
                //
                // TODO for wowconf there will just be one Transaction so we break
                // TODO but if we want to reuse this code then we'd obviously need to fix this
                break;
            }

            if(info != null) {
                info.setNumber("groupId", registrationInfo.getInteger("groupId"));
                db.createPayPalPaymentInfo(info);
            }

        } catch(PayPalRESTException | MalformedURLException | UnsupportedEncodingException | ParseException ex) {
            // TODO propagate a custom exception
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * Convert dollars to cents
     * @param dollars
     * @return
     */
    public static Integer toCents(String dollars) throws ParseException {

        DecimalFormat df = new DecimalFormat("#.00");
        String value = df.format(Float.parseFloat(dollars)).toString();
        value = value.replace(".", "");
        value = value.replace(",", "");

        return Integer.parseInt(value);
    }

    /**
     * Execute the authorized payment and return transaction info upon success.
     *
     * When the response comes back from paypal we need to perform the following actions
     * in the WoW database so that attendees are properly marked as registered and paid.
     *
     * 1) create PAYPAL_PAYMENT_TX record that contains paypal transaction info
     * 2) update ATTENDEE record for each attendee in group to a paymentStatus of 'paid'
     * 3) insert record into REGISTERED table for each attendee in group
     *
     * @param authInfo
     * @return
     */
    public JsonObject executePayment(JsonObject authInfo) {

        JsonArray attendees = null;

        // First double check that payment hasn't already been given.
        JsonObject paid = hasPaid(authInfo);
        if(paid != null) {
            attendees = db.retrieveGroupFromPayPalPaymentInfoId(
                paid.getInteger("infoId"));

            throw new AlreadyPaidException(paid, attendees);
        }

        // create payment authorization
        if(db.createPayPalPaymentAuth(authInfo) != 1) {
            throw new PaymentException(
                "Could not create payment authorization record for: " + authInfo.asString());
        }

        // get associated paymentId
        String paymentId = db.retrievePaymentId(
            authInfo.getString("token"),
            authInfo.getString("payerId"));
        if(paymentId == null) {
            throw new PaymentException(
                "Could not retrieve paymentId for: " + authInfo.asString());
        }

        JsonObject txInfo = null;
        try {
            // This executes the payment in paypal
            txInfo = executePayment(paymentId, authInfo.getString("payerId"));
            if(db.createPayPalPaymentTx(txInfo) != 1) {
                throw new PaymentException(
                    "Could not create PayPalPaymentTx record for: " + authInfo.asString());
            }

            //
            // when we get here there's update work that needs to happen in wowregDb
            //

            // lookup group info and include attendees as part of response
            attendees = db.retrieveGroupFromPaymentId(paymentId);
            if(attendees != null) {
                txInfo.setArray("attendees", attendees);
            }

            // Need to update paypal_payment_info.paypal_state to 'completed'
            db.updatePayPalPaymentInfoComplete(paymentId);

            // Update registration_id (otherwise known as confirmation number)
            db.updateAttendeeRegistration(
                txInfo.getString("payPalTxId"),
                txInfo.getString("payPalTxUpdateTime"),
                txInfo.getArray("attendees"));

        } catch (Exception ex) {
            throw new PaymentException("Exception parsing txAmount for: " + authInfo.asString());
        }

        return txInfo;
    }

    /**
     * Checks to see if this authorization has already taken place.
     * @param authInfo
     * @return
     */
    private JsonObject hasPaid(JsonObject authInfo) {

        JsonObject completed = db.isPayPalComplete(authInfo);
        if(completed == null) {
            return null;
        }

        if (completed.getInteger("infoId", 0) != 0 &&
           completed.getInteger("authId", 0) != 0 &&
           completed.getInteger("txId", 0) != 0) {
            return completed;
        }

        return null;
    }

    /**
     * Execute a payment transaction given a token and a payerId, having both of
     * which implies this payment has been setup by calling createPayment.
     *
     * @param paymentId
     * @param payerId
     */
    private JsonObject executePayment(String paymentId, String payerId) throws ParseException {

        Payment completed = null;
        try {

            Payment payment = new Payment();
            payment.setId(paymentId);
            PaymentExecution paymentExecution = new PaymentExecution();
            paymentExecution.setPayerId(payerId);
            completed = payment.execute(PayPalAccess.getAPIContext(), paymentExecution);
        } catch(PayPalRESTException ex) {
            throw new PaymentException("Exception executing PayPal payment: "
                + " " + paymentId + " payerId: " + payerId, ex);
        }

        if(completed == null) {
            throw new PaymentException("Completed Payment cannot be null, " +
                "something when wrong with executing PayPal paymentId: " +
                    paymentId + " payerId: " + payerId);
        }

        JsonObject txInfo = Json.object().string("payPalId", completed.getId())
            .string("payPalPayerId", completed.getPayer().getPayerInfo().getPayerId())
            .string("payPalPayerEmail", completed.getPayer().getPayerInfo().getEmail())
            .string("payPalPayerFirstName", completed.getPayer().getPayerInfo().getFirstName())
            .string("payPalPayerLastName", completed.getPayer().getPayerInfo().getLastName())
            .number("payPalTxAmount", toCents(completed.getTransactions().get(0).getAmount().getTotal()))
            .string("payPalTxId", completed.getTransactions().get(0).getRelatedResources().get(0).getSale().getId())
            .string("payPalTxState", completed.getTransactions().get(0).getRelatedResources().get(0).getSale().getState())
            .string("payPalTxCreateTime", completed.getTransactions().get(0).getRelatedResources().get(0).getSale().getCreateTime())
            .string("payPalTxUpdateTime", completed.getTransactions().get(0).getRelatedResources().get(0).getSale().getUpdateTime())
            .build();

        Iterator<Links> links = completed.getTransactions().get(0)
            .getRelatedResources().get(0).getSale().getLinks().iterator();

        while(links.hasNext()) {
            Links link = links.next();
            if("parent_payment".equalsIgnoreCase(link.getRel())) {
                txInfo.setString("payPalTxParentPaymentUrl", link.getHref());
            } else if("refund".equalsIgnoreCase(link.getRel())) {
                txInfo.setString("payPalTxRefundUrl", link.getHref());
            } else if("self".equalsIgnoreCase(link.getRel())) {
                txInfo.setString("payPalTxSelfUrl", link.getHref());
            }
        }

        return txInfo;
    }

    /**
     * Create a csrf (cross site request forgery) token that we send to paypal
     * and they send back.  The csrf is an encryption of (groupId,pocId,groupName)
     * then URL encoded so that its web-safe.  The secretKey used for encryption
     * is set outside of this class in a system property called wow.instanceId.
     * @param registrationInfo
     * @return
     * @throws UnsupportedEncodingException
     */
    private String encodeCsrf(JsonObject registrationInfo) throws UnsupportedEncodingException {

        String instanceId = System.getProperty("wow.instanceId");
        String data = registrationInfo.getNumber("groupId") + ","
            + registrationInfo.getNumber("pocId") + ","
            + registrationInfo.getString("groupName");

        return URLEncoder.encode(Aes.encrypt(instanceId, data), "UTF-8");
    }

    public static PayPal newPayment() {

        return new PayPal();
    }

    /**
     * Create a PayPal Payer instance from the point-of-contact Attendee
     * @param attendees
     * @return
     */
    private static Payer createPayer(List<Attendee> attendees) {

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

//        Attendee attendee = getPointOfContact(attendees);
//        if(attendee != null) {
//            PayerInfo info = new PayerInfo();
//            info.setFirstName(attendee.getFirstName());
//            info.setLastName(attendee.getLastName());
//            info.setPayerId(attendee.getId());
//            payer.setPayerInfo(info);
//        }

        return payer;
    }

    private static Attendee getPointOfContact(List<Attendee> attendees) {
        for(Attendee attendee : attendees) {
            if(attendee.getPoc()) { // if group point-of-contact
                return attendee;
            }
        }
        return null;
    }


}
