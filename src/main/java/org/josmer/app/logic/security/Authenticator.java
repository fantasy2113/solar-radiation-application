package org.josmer.app.logic.security;

public final class Authenticator {

    private final String login;
    private final String pwd;

    public Authenticator() {
        this.login = "user";
        this.pwd = "abc123";
    }

    public boolean authenticate(final String login, final String pwd) {
        return this.login.equals(login) && this.pwd.equals(pwd);
    }
}
