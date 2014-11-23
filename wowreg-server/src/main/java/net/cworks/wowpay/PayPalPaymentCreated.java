/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowpay
 * Class: PayPalPaymentCreated
 * Created: 10/10/14 10:35 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowpay;

import net.cworks.json.JsonObject;

public interface PayPalPaymentCreated {

    public void paymentCreated(JsonObject info);
}
