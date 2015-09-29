/**
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * Baked with love by corbett
 * Project: wowreg
 * Package: net.cworks.wowserver.util
 * Class: Aes
 * Created: 11/4/14 8:09 PM
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 */
package net.cworks.wowserver.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

public class Aes {

    private static final String ALGORITHM = "AES";
    private static final Integer KEY_SIZE = 16;

    public static class AesException extends RuntimeException {
        public AesException(Throwable cause) {
            super(cause);
        }
        public AesException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String encrypt(String secretKey, String clearText) {
        String cipherText = null;
        try {
            Key key = generateKey(secretKey);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = c.doFinal(clearText.getBytes("UTF-8"));
            cipherText = new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            throw new AesException(ex);
        }

        return cipherText;
    }

    public static String decrypt(String secretKey, String cipherText) {
        String clearText = null;
        try {
            Key key = generateKey(secretKey);
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = new BASE64Decoder().decodeBuffer(cipherText);
            byte[] decrypted = c.doFinal(decoded);
            clearText = new String(decrypted);
        } catch(Exception ex) {
            throw new AesException(ex);
        }

        return clearText;
    }

    private static Key generateKey(String secretKey) throws Exception {
        byte[] keyBytes = secretKey.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyBytes = sha.digest(keyBytes);
        keyBytes = Arrays.copyOf(keyBytes, KEY_SIZE);
        Key key = new SecretKeySpec(keyBytes, ALGORITHM);
        return key;
    }
}
