/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowpay
 * Class: PaymentIdProvider
 * Created: 10/10/14 2:15 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowpay;

public interface PaymentProvider {
    public String paymentId();
    public String payerId();
}
