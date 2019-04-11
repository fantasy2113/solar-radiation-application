package de.josmer.app.lib.security;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class GenKey {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenKey.class.getName());

    private GenKey() {
    }

    public static String get() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] raw = secretKey.getEncoded();
            return Hex.encodeHexString(raw);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.info(ex.getMessage());
            return "714b03ce9b884dc7defc9a9043fdfa67";
        }
    }
}
