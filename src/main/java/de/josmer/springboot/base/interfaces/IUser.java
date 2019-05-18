package de.josmer.springboot.base.interfaces;

import java.sql.Timestamp;

public interface IUser {
    boolean isActive();

    void setIsActive(boolean isActive);

    String getPassword();

    void setPassword(String password);

    String getUsername();

    void setUsername(String username);

    Timestamp getLastLogin();

    void setLastLogin(Timestamp lastLogin);

    int getId();

    void setId(int id);

    Timestamp getCreated();

    void setCreated(Timestamp created);

    Timestamp getModified();

    void setModified(Timestamp modified);
}
