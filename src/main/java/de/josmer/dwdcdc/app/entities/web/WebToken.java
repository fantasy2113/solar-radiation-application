package de.josmer.dwdcdc.app.entities.web;

public final class WebToken {

	private boolean authorized;
	private boolean error;
	private String secret;
	private String token;
	private boolean userError;

	public WebToken() {
		this.secret = "";
		this.token = "";
		this.authorized = false;
		this.error = false;
		this.userError = false;
	}

	public String getSecret() {
		return secret;
	}

	public String getToken() {
		return token;
	}

	public boolean isAuthorized() {
		return authorized;
	}

	public boolean isError() {
		return error;
	}

	public boolean isUserError() {
		return userError;
	}

	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setUserError(boolean userError) {
		this.userError = userError;
	}
}
