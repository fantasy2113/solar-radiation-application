package com.orbis.coreserver.api.security.token;

import com.orbis.coreserver.api.security.crypting.Crypt;
import com.orbis.coreserver.base.Authentication;
import com.orbis.coreserver.base.Toolbox;
import com.orbis.coreserver.base.entities.UserRight;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public final class Token {

    private static final Logger LOGGER = LogManager.getLogger(Token.class.getName());
    private static String delimiter = null;
    private static String theToken = null;
    private static Crypt crypt = null;

    private Token() {
    }

    public static boolean check(final String token) {
        if (Toolbox.isNull(theToken) || Toolbox.isNull(token)) {
            return false;
        }
        return theToken.equals(token);
    }

    public static void init() {
        setDelimiter();
        crypt = new Crypt();
        theToken = Toolbox.getRandomString(12, true, true);
    }

    public static String get(final int userId) {
        return crypt.decrypt(getCode(userId));
    }

    public static String getCode(final int userId) {
        return theToken + delimiter + userId;
    }

    public static String getDelimiter() {
        return delimiter;
    }

    public static Authentication getAuthentication(final String encodedToken) {
        return new Authentication(crypt.encrypt(encodedToken), delimiter);
    }

    public static boolean isAccessDenied(final String token) {
        if (!Token.check(token)) {
            LOGGER.info("access denied");
            return true;
        }
        return false;
    }

    public static boolean isAccessDenied(final String token, final UserRight userRight) {
        if (!Token.check(token) || Toolbox.isNull(userRight)) {
            LOGGER.info("access denied");
            return true;
        }
        return false;
    }

    private static void setDelimiter() {
        final String delimiters = ",:;-_ ";
        delimiter = String.valueOf(delimiters.charAt(new Random().nextInt(delimiters.length())));
    }

}
