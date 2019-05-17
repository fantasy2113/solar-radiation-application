package de.josmer.solardb.utils;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class UserBCrypt {

    public String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public boolean isPassword(final String plainPassword, final String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.length() != 60) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
