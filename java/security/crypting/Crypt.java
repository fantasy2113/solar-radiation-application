package com.orbis.coreserver.api.security.crypting;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public final class Crypt {

    private static final Logger LOGGER = LogManager.getLogger(Crypt.class.getName());
    private Cipher deCryptCipher = null;
    private Cipher enCryptCipher = null;

    public Crypt() {
        init();
    }

    public String decrypt(final String code) {

        try {
            byte[] encrypted = deCryptCipher.doFinal(code.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException ex) {
            LOGGER.info(ex.getMessage());
            return "";
        }
    }

    public String encrypt(final String code) {
        try {
            byte[] clearText = enCryptCipher.doFinal(Hex.decodeHex(code.toCharArray()));
            return new String(clearText, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException | DecoderException ex) {
            LOGGER.info(ex.getMessage());
        }
        return "-undefined-";
    }

    private void init() {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Hex.decodeHex(GenKey.get().toCharArray()), "AES");
            deCryptCipher = Cipher.getInstance("AES"); //NOSONAR
            deCryptCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            enCryptCipher = Cipher.getInstance("AES"); //NOSONAR
            enCryptCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | DecoderException ex) {
            LOGGER.info(ex.getMessage());
        }
    }

}
