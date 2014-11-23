/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.util
 * Class: AesTest
 * Created: 11/5/14 8:02 AM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.util;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class AesTest {

    @Test
    public void simpleAes() throws UnsupportedEncodingException {

        String clearText = RandomHelper.randomString("MY_DATA");
        String secretKey = String.valueOf(System.currentTimeMillis());
        String cipherText = Aes.encrypt(secretKey, clearText);
        String decryptedText = Aes.decrypt(secretKey, cipherText);

        System.out.println("Clear Text : " + clearText);

        System.out.println("URL Encoded: " + URLEncoder.encode(cipherText, "UTF-8"));
        System.out.println("URL Decoded: " + URLDecoder.decode(cipherText, "UTF-8"));

        System.out.println("Encrypted Text : " + cipherText);
        System.out.println("Decrypted Text : " + decryptedText);

    }
}
