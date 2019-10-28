package de.josmer.dwdcdc.app.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_tab")
public final class User implements Serializable {

	@Column(name = "created")
	private Timestamp created;
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = IDENTITY)
	private int id;
	@Column(name = "is_active")
	private boolean isActive;
	@Column(name = "last_login")
	private Timestamp lastLogin;
	@Column(name = "modified")
	private Timestamp modified;
	@Column(name = "password")
	private String password;
	@Column(name = "username")
	private String username;

	public Timestamp getCreated() {
		return created;
	}

	public int getId() {
		return id;
	}

	public Timestamp getLastLogin() {
		return lastLogin;
	}

	public Timestamp getModified() {
		return modified;
	}

	public String getPassword() {
		return this.password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setModified(Timestamp modified) {
		this.modified = modified;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
