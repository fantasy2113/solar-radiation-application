package de.josmer.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public final class Token {

    private static final Logger LOGGER = LoggerFactory.getLogger(Token.class.getName());
    private static String delimiter = null;
    private static String theToken = null;
    private static Crypt crypt = null;

    private Token() {
    }

    public static boolean check(final String token) {
        if (theToken == null || token == null) {
            return false;
        }
        return theToken.equals(token);
    }

    public static void init() {
        setDelimiter();
        crypt = new Crypt();
        theToken = RandomStringUtils.random(12, true, true);
    }

    public static String get(final int userId) {
        return crypt.decrypt(getCode(userId));
    }

    private static String getCode(final int userId) {
        return theToken + delimiter + userId;
    }

    public static Authentication getAuthentication(final String encodedToken) {
        return new Authentication(crypt.encrypt(encodedToken), delimiter);
    }

    private static void setDelimiter() {
        final String delimiters = ",:;-_ ";
        delimiter = String.valueOf(delimiters.charAt(new Random().nextInt(delimiters.length())));
    }

}
