package org.josmer.connector;

import org.springframework.stereotype.Component;

@Component
public abstract class Connector {
    private static String user = null;
    private static String password = null;
    private static String url = null;

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        Connector.user = user;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        Connector.password = password;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Connector.url = url;
    }
}
