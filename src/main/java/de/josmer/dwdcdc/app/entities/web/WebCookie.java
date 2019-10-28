package de.josmer.dwdcdc.app.entities.web;

public class WebCookie {

	private String app;
	private String token;

	public WebCookie() {
		this.token = "";
		this.app = "";
	}

	public String getApp() {
		return app;
	}

	public String getToken() {
		return token;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
