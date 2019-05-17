package de.josmer.application.library.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;

public class Toolbox {

    private Toolbox() {
    }


    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean isPassword(final String plainPassword, final String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.length() != 60) {
            return false;
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static double getRound(double val) {
        return Double.parseDouble(String.format(Locale.ENGLISH, "%.2f", val));
    }
}
