/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowpay
 * Class: PayPalAccessToken
 * Created: 10/9/14 10:06 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowpay;

import com.paypal.core.ConfigManager;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

public class PayPalAccess {

    public static String getAccessToken() throws PayPalRESTException {

        String clientId = ConfigManager.getInstance().getValue("clientID");
        String clientSecret = ConfigManager.getInstance().getValue("clientSecret");

        OAuthTokenCredential oauthCreds = new OAuthTokenCredential(clientId, clientSecret);
        return oauthCreds.getAccessToken();
    }

    public static APIContext getAPIContext() throws PayPalRESTException {

        APIContext context = new APIContext(getAccessToken());
        return context;
    }

    public static APIContext getAPIContext(String requestId) throws PayPalRESTException {

        APIContext context = new APIContext(getAccessToken(), requestId);
        return context;
    }





}
