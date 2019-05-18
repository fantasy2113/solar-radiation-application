package de.josmer.dwdcdc.springboot.base.web;

public class WebCookie {
    private String token;
    private String app;

    public WebCookie() {
        this.token = "";
        this.app = "";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
