package org.josmer.app.lib.security;

public class Key {

    private static final String SECRET = "app_key";

    private Key() {
    }

    public static boolean check(final String token) {
        return SECRET.equals(token);
    }

    public static String get() {
        return SECRET;
    }

    public static String undefined() {
        return "undefined";
    }
}
