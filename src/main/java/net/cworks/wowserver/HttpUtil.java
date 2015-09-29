/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver
 * Class: Http
 * Created: 10/10/14 1:44 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public final class HttpUtil {

    /**
     * Common utility to get queryMap from a URL.  Does not support a key having more
     * than one value.
     * @param href
     * @return
     * @throws java.io.UnsupportedEncodingException
     */
    public static Map<String, String> queryMap(URL href) throws UnsupportedEncodingException {

        Map<String, String> queryMap = new LinkedHashMap<>();
        String queryString = href.getQuery();
        String[] pairs = queryString.split("&");
        for(String pair : pairs) {
            int i = pair.indexOf("=");
            queryMap.put(
                    URLDecoder.decode(pair.substring(0, i), "UTF-8"),
                    URLDecoder.decode(pair.substring(i + 1), "UTF-8"));
        }

        return queryMap;
    }
}
