package de.josmer.springboot.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user_tab")
public final class User implements Serializable, de.josmer.springboot.base.interfaces.IUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = IDENTITY)
    private int id;
    @Column(name = "created")
    private Timestamp created;
    @Column(name = "modified")
    private Timestamp modified;
    @Column(name = "username")
    private String username;
    @Column(name = "is_active")
    private boolean isActive;
    @Column(name = "password")
    private String password;
    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(final String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    @Override
    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Timestamp getCreated() {
        return created;
    }

    @Override
    public void setCreated(Timestamp created) {
        this.created = created;
    }

    @Override
    public Timestamp getModified() {
        return modified;
    }

    @Override
    public void setModified(Timestamp modified) {
        this.modified = modified;
    }
}
