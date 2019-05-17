package de.orbis.application.controller.v1.security;

public class WebToken {
    private String secret;
    private String token;

    public WebToken() {
        this.secret = "";
        this.token = "";
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
