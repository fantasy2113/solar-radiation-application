package de.josmer.application.library.utils;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Toolbox {

    private Toolbox() {
    }

    public static String readFile(final String file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file)));
        } catch (IOException e) {
            return e.toString();
        }
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
}
