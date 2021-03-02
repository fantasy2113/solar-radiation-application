package de.jos.dwdcdc.app.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "users")
public final class User implements Serializable {

  @Column(name = "created")
  private Timestamp created;
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
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

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Timestamp getLastLogin() {
    return lastLogin;
  }

  public void setLastLogin(Timestamp lastLogin) {
    this.lastLogin = lastLogin;
  }

  public Timestamp getModified() {
    return modified;
  }

  public void setModified(Timestamp modified) {
    this.modified = modified;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public boolean isActive() {
    return isActive;
  }

  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }
}
